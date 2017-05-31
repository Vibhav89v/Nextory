package registrationFlows;

import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import common.PasswordFromAdmin;
import common.RandomEmail;
import common.SuperTestScript;
import generics.AddDate;
import generics.Database;
import generics.Excel;
import generics.Property;
import pages.AdminPage;
import pages.GiftcardPage;
import pages.HomePage;
import pages.LoginPage;
import pages.MyAccountPage;
import pages.NewSubscriptionPage;

public class RedeemGiftCards extends SuperTestScript
{
	public static String un;
	public static String pwd;
	public static String kod;
	public static String codePrice;
	public static String adminUn;
	public static String adminPwd;
	public static String adminUrl;
	public static double pricePerDay;
	
	
	public RedeemGiftCards()
	{
		loginRequired=false;
		logoutRequired=false;
	}
	
	
	//-----------------------------------Redeeming GiftCard For Bas Subscription---------------------------------------//
	
	
	@Test(enabled=true, priority=24, groups={"Registrations" , "All"})
	public void redeemGiftCardBasNewUser() throws InterruptedException, EncryptedDocumentException, InvalidFormatException, IOException
	{
		adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
		adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
		adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
		un=RandomEmail.email();
		pwd=Excel.getCellValue(INPUT_PATH, "NewEmail" , 1, 2);
		kod=Database.executeQuery("select a.voucher from giftcard_pur_details a left outer join giftcard b on a.giftcardid=b.id where giftcardname='PresenkortNew' and status='ACTIVE' and redeemedby is null and a.validupto>curdate() order by rand() limit 1");
		codePrice= Database.executeQuery("select a.price from giftcard_pur_details a left outer join giftcard b on a.giftcardid=b.id where voucher='" +kod+ "'");
		 
		pricePerDay= 3.3;                      //<---------------- 99 kr/30 days = 3.3kr/day
		 
		
		log.info("REDEEMING GIFTCARD FOR BAS SUBSCRIPTION");
		 
		log.info("Clicking on Presentkort Link");
		HomePage home=new HomePage(driver);
		home.clickGiftCard();
		
		log.info("Clicking on 'Ny Kund' button");
		GiftcardPage gc=new GiftcardPage(driver);
		gc.clickNyKund();
		Thread.sleep(2000);
		
		log.info("Entering the Giftcard code and the Email as '" +un+ "' and password as '" +pwd+ "'");
		gc.enterPresentkodNewUser(kod);
		gc.enterNewEmail(un);
		gc.verifyNewEmail(un);
		gc.enterNewPassword(pwd);
		gc.clickToRedeemAsNew();
		
		
		
		log.info("Choosing Subscriptions");
		NewSubscriptionPage subs= new NewSubscriptionPage(driver);
		subs.clickBasSub();
		subs.clickContinue();
		
		
		String act1=driver.findElement(By.xpath("//div[@class='presentKortPopUp']/h2")).getText();
		String actTrim1=act1.replaceAll("\\s+", "");
		String exp1="BAS";
		
		Assert.assertEquals(actTrim1, exp1);
		log.info("PresentKort Subscription :" +act1);
		
		String act2=driver.findElement(By.xpath("//div[@class='presentKortPopUp']/h3")).getText();
		String actTrim2=act2.replaceAll("\\s+", "");
		String exp2="99kr/mån";
		
		Assert.assertEquals(actTrim2,exp2);
		log.info("Presentkort Price : " +act2);
		
		gc.clickPopUpFortsatt();
		
		home.clickNextoryLogo();
		home.clickAccountLink();
		
		Excel.shiftingRowsDown(INPUT_PATH, "ExistingEmail", 2);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 1, un);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 2, pwd);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 3, "BAS");
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 4, "FREE_GIFTCARD_NOCARDINFO");
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 5, kod);
		
		//Excel.shiftingRowsUp(INPUT_PATH, "NewEmail", 1);
		
		String actSubs=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
		String actSubsTrim=actSubs.replaceAll("\\s+", "");
		String expSubs="Duharabonnemanget:BAS";
		
			Assert.assertEquals(actSubsTrim, expSubs);
			log.info(actSubs);
		
		String actual= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
		
		double result=Double.parseDouble(codePrice);
		int days = (int) Math.round(result / pricePerDay);
		
		 
		String currentDate= AddDate.currentDate();
		log.info("Current Date is : " +currentDate);
			
		String expected= AddDate.addingDays(days);

		
			Assert.assertEquals(actual, expected.trim());
			log.info("Next Payment Date is: " +actual);
		
		log.info("logging out");
		MyAccountPage acc=new MyAccountPage(driver);
		acc.clickLogOut();
		
		new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudböcker & E-böcker - Ladda ner Gratis Ljudbok/E-bok Online"));
		
		log.info("Validating into Admin Site");
		
		driver.manage().deleteAllCookies();
		driver.get(adminUrl);
		
		AdminPage admin=new AdminPage(driver);
		admin.setUserName(adminUn);
		admin.setPassword(adminPwd);
		admin.clickLogin();
		admin.clickCustMgmt();
		admin.setEPost(un);
		admin.clickSearch();
		String memberStatus = admin.getMemberType();
		String subsType = admin.getSubsType();
		
	
			Assert.assertEquals(memberStatus, "FREE_GIFTCARD_NOCARDINFO");
			log.info("Membership Status is: " +memberStatus + " in Admin Site");
			
		
			Assert.assertEquals(subsType, "BASE");
			log.info("Subscription Type is: " +subsType+ " in Admin Site");
		
		
			admin.clickLogout();
			driver.get(url);
	
		
	}
	
	
	
	//---------------------------------Redeeming GiftCard For Standard Subscription-----------------------------------//
	
	
	@Test(enabled=true, priority=25, groups={"Registrations" , "All"})
	public void redeemGiftCardStandardNewUser() throws InterruptedException, EncryptedDocumentException, InvalidFormatException, IOException
	{
		adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
		adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
		adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
		un=RandomEmail.email();
		pwd=Excel.getCellValue(INPUT_PATH, "NewEmail" , 1, 2);
		kod=Database.executeQuery("select a.voucher from giftcard_pur_details a left outer join giftcard b on a.giftcardid=b.id where giftcardname='PresenkortNew' and status='ACTIVE' and redeemedby is null and a.validupto>curdate() order by rand() limit 1");
		codePrice= Database.executeQuery("select a.price from giftcard_pur_details a left outer join giftcard b on a.giftcardid=b.id where voucher='" +kod+ "'");
		 
		pricePerDay= 6.633333333333333;                      //<---------------- 199 kr/30 days = 6.633333333333333 kr/day
		 
		 
		 
			 log.info("REDEEMING GIFTCARD FOR STANDARD SUBSCRIPTION");
			 
			 log.info("Clicking on Presentkort Link");
			HomePage home=new HomePage(driver);
			home.clickGiftCard();
			
			log.info("Clicking on 'Ny Kund' button");
			GiftcardPage gc=new GiftcardPage(driver);
			gc.clickNyKund();
			Thread.sleep(2000);
			
			log.info("Entering the Giftcard code and the Email as '" +un+ "' and password as '" +pwd+ "'");
			gc.enterPresentkodNewUser(kod);
			gc.enterNewEmail(un);
			gc.verifyNewEmail(un);
			gc.enterNewPassword(pwd);
			gc.clickToRedeemAsNew();
			
			log.info("Choosing Subscriptions");
			NewSubscriptionPage subs= new NewSubscriptionPage(driver);
			subs.clickStandardSub();
			subs.clickContinue();
			
			
			String act1=driver.findElement(By.xpath("//div[@class='presentKortPopUp']/h2")).getText();
			String actTrim1=act1.replaceAll("\\s+", "");
			String exp1="STANDARD";
			
			Assert.assertEquals(actTrim1, exp1);
			log.info("PresentKort Subscription :" +act1);
			
			String act2=driver.findElement(By.xpath("//div[@class='presentKortPopUp']/h3")).getText();
			String actTrim2=act2.replaceAll("\\s+", "");
			String exp2="199kr/mån";
			
			Assert.assertEquals(actTrim2,exp2);
			log.info("Presentkort Price : " +act2);
			
			gc.clickPopUpFortsatt();
			
			home.clickNextoryLogo();
			home.clickAccountLink();
			
			Excel.shiftingRowsDown(INPUT_PATH, "ExistingEmail", 2);
			Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 1, un);
			Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 2, pwd);
			Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 3, "STANDARD");
			Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 4, "FREE_GIFTCARD_NOCARDINFO");
			Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 5, kod);
			
			//Excel.shiftingRowsUp(INPUT_PATH, "NewEmail", 1);
			
			String actSubs=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
			String actSubsTrim=actSubs.replaceAll("\\s+", "");
			String expSubs="Duharabonnemanget:STANDARD";
			
				Assert.assertEquals(actSubsTrim, expSubs);
				log.info(actSubs);
			
			String actual= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
			
			double result=Double.parseDouble(codePrice);
			int days = (int) Math.round(result / pricePerDay);
			 
			String currentDate= AddDate.currentDate();
			log.info("Current Date is : " +currentDate);
				
			String expected= AddDate.addingDays(days);
	
			
				Assert.assertEquals(actual, expected.trim());
				log.info("Next Payment Date is: " +actual);
			
			log.info("logging out");
			MyAccountPage acc=new MyAccountPage(driver);
			acc.clickLogOut();
			
			new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudböcker & E-böcker - Ladda ner Gratis Ljudbok/E-bok Online"));
			
			log.info("Validating into Admin Site");
			
			driver.manage().deleteAllCookies();
			driver.get(adminUrl);
			
			AdminPage admin=new AdminPage(driver);
			admin.setUserName(adminUn);
			admin.setPassword(adminPwd);
			admin.clickLogin();
			admin.clickCustMgmt();
			admin.setEPost(un);
			admin.clickSearch();
			String memberStatus = admin.getMemberType();
			String subsType = admin.getSubsType();
			
		
				Assert.assertEquals(memberStatus, "FREE_GIFTCARD_NOCARDINFO");
				log.info("Membership Status is: " +memberStatus + " in Admin Site");
				
			
				Assert.assertEquals(subsType, "STANDARD");
				log.info("Subscription Type is: " +subsType+ " in Admin Site");
			
			
				admin.clickLogout();
				driver.get(url);
		
	}
	
	
	//-----------------------------Redeem GiftCard for Premium Subscription---------------------------------//
	
	@Test(enabled=true, priority=26, groups={"Registrations" , "All"})
	public void redeemGiftCardPremiumNewUser() throws InterruptedException, EncryptedDocumentException, InvalidFormatException, IOException
	{
		adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
		adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
		adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
		un=RandomEmail.email();
		pwd=Excel.getCellValue(INPUT_PATH, "NewEmail" , 1, 2);
		kod=Database.executeQuery("select a.voucher from giftcard_pur_details a left outer join giftcard b on a.giftcardid=b.id where giftcardname='PresenkortNew' and status='ACTIVE' and redeemedby is null and a.validupto>curdate() order by rand() limit 1");
		codePrice= Database.executeQuery("select a.price from giftcard_pur_details a left outer join giftcard b on a.giftcardid=b.id where voucher='" +kod+ "'");
		 
		pricePerDay= 8.3;                      //<---------------- 249 kr/30 days = 8.3 kr/day
		 
		log.info("REDEEMING GIFTCARD FOR PREMIUM SUBSCRIPTION");
		 
		 log.info("Clicking on Presentkort Link");
		HomePage home=new HomePage(driver);
		home.clickGiftCard();
		
		log.info("Clicking on 'Ny Kund' button");
		GiftcardPage gc=new GiftcardPage(driver);
		gc.clickNyKund();
		Thread.sleep(2000);
		
		log.info("Entering the Giftcard code and the Email as '" +un+ "' and password as '" +pwd+ "'");
		gc.enterPresentkodNewUser(kod);
		gc.enterNewEmail(un);
		gc.verifyNewEmail(un);
		gc.enterNewPassword(pwd);
		gc.clickToRedeemAsNew();
		
		log.info("Choosing Subscriptions");
		NewSubscriptionPage subs= new NewSubscriptionPage(driver);
		subs.clickPremiumSub();
		subs.clickContinue();
		
		
		String act1=driver.findElement(By.xpath("//div[@class='presentKortPopUp']/h2")).getText();
		String actTrim1=act1.replaceAll("\\s+", "");
		String exp1="PREMIUM";
		
		Assert.assertEquals(actTrim1, exp1);
		log.info("PresentKort Subscription :" +act1);
		
		String act2=driver.findElement(By.xpath("//div[@class='presentKortPopUp']/h3")).getText();
		String actTrim2=act2.replaceAll("\\s+", "");
		String exp2="249kr/mån";
		
		Assert.assertEquals(actTrim2,exp2);
		log.info("Presentkort Price : " +act2);
		
		gc.clickPopUpFortsatt();
		
		home.clickNextoryLogo();
		home.clickAccountLink();
		
		Excel.shiftingRowsDown(INPUT_PATH, "ExistingEmail", 2);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 1, un);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 2, pwd);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 3, "PREMIUM");
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 4, "FREE_GIFTCARD_NOCARDINFO");
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 5, kod);
		
		//Excel.shiftingRowsUp(INPUT_PATH, "NewEmail", 1);
		
		String actSubs=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
		String actSubsTrim=actSubs.replaceAll("\\s+", "");
		String expSubs="Duharabonnemanget:PREMIUM";
		
			Assert.assertEquals(actSubsTrim, expSubs);
			log.info(actSubs);
		
		String actual= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
		
		double result=Double.parseDouble(codePrice);
		int days = (int) Math.round(result / pricePerDay);
		 
		String currentDate= AddDate.currentDate();
		log.info("Current Date is : " +currentDate);
			
		String expected= AddDate.addingDays(days);
	
		
			Assert.assertEquals(actual, expected.trim());
			log.info("Next Payment Date is: " +actual);
		
		log.info("logging out");
		MyAccountPage acc=new MyAccountPage(driver);
		acc.clickLogOut();
		
		new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudböcker & E-böcker - Ladda ner Gratis Ljudbok/E-bok Online"));
		
		log.info("Validating into Admin Site");
		
		driver.manage().deleteAllCookies();
		driver.get(adminUrl);
		
		AdminPage admin=new AdminPage(driver);
		admin.setUserName(adminUn);
		admin.setPassword(adminPwd);
		admin.clickLogin();
		admin.clickCustMgmt();
		admin.setEPost(un);
		admin.clickSearch();
		String memberStatus = admin.getMemberType();
		String subsType = admin.getSubsType();
		
	
			Assert.assertEquals(memberStatus, "FREE_GIFTCARD_NOCARDINFO");
			log.info("Membership Status is: " +memberStatus + " in Admin Site");
			
		
			Assert.assertEquals(subsType, "PREMIUM");
			log.info("Subscription Type is: " +subsType+ " in Admin Site");
		
		
			admin.clickLogout();
			driver.get(url);
	
	}

	
	
	//----------------------------Redeeming GiftCard As Existing User Without Login----------------------------------//
	
	@Test(enabled=true, priority=27, groups={"Registrations" , "All"})
	public void redeemGiftCardExistingUserWithoutLogin() throws EncryptedDocumentException, InvalidFormatException, IOException, InterruptedException
	{
		adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
		adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
		adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
		un=Excel.getCellValue(INPUT_PATH, "MemberPaying" , 1, 0);
		pwd=PasswordFromAdmin.gettingPasswordFromAdmin(un);
		kod=Database.executeQuery("select a.voucher from giftcard_pur_details a left outer join giftcard b on a.giftcardid=b.id where giftcardname='PresenkortNew' and status='ACTIVE' and redeemedby is null and a.validupto>curdate() order by rand() limit 1");
		codePrice= Database.executeQuery("select a.price from giftcard_pur_details a left outer join giftcard b on a.giftcardid=b.id where voucher='" +kod+ "'");
		
		log.info("Clicking on Presentkort Link");
		HomePage home=new HomePage(driver);
		home.clickGiftCard();
			
		log.info("Clicking on 'Ny Kund' button");
		GiftcardPage gc=new GiftcardPage(driver);
		gc.clickRedanKund();
		Thread.sleep(2000);
		
		log.info("Email as '" +un+ "' and password as '" +pwd+ "'");
		gc.enterPresentKodExistingUser(kod);
		gc.enterExistingEmail(un);
		gc.enterExistingPassword(pwd);
		gc.clickToRedeemAsExisting();
		
		home.clickNextoryLogo();
		home.clickAccountLink();
		
		String initSubs=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
		String subs=initSubs.substring(22);
		
		Excel.shiftingRowsUp(INPUT_PATH, "MemberPaying", 1);
		
		if(initSubs.contains("BAS"))
		{
			pricePerDay= 3.3;                      //<---------------- 99 kr/30 days = 3.3kr/day
			
			String actSubs=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
			String actSubsTrim=actSubs.replaceAll("\\s+", "");
			String expSubs="Duharabonnemanget:BAS";
			
				Assert.assertEquals(actSubsTrim, expSubs);
				log.info(actSubs);
			
			String actual= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
			
			double result=Double.parseDouble(codePrice);
			int days = (int) Math.round(result / pricePerDay);
			 
			String currentDate= AddDate.currentDate();
			log.info("Current Date is : " +currentDate);
				
			String expected= AddDate.addingDays(days);
			
			
				Assert.assertEquals(actual, expected.trim());
				log.info("Next Payment Date is: " +actual);
		}
		
		else if(initSubs.contains("STANDARD"))
		{
			pricePerDay= 6.633333333333333;                      //<---------------- 199 kr/30 days = 6.633333333333333 kr/day			
			String actSubs=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
			String actSubsTrim=actSubs.replaceAll("\\s+", "");
			String expSubs="Duharabonnemanget:STANDARD";
			
				Assert.assertEquals(actSubsTrim, expSubs);
				log.info(actSubs);
			
			String actual= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
			
			double result=Double.parseDouble(codePrice);
			int days = (int) Math.round(result / pricePerDay);
			 
			String currentDate= AddDate.currentDate();
			log.info("Current Date is : " +currentDate);
				
			String expected= AddDate.addingDays(days);
			
			
				Assert.assertEquals(actual, expected.trim());
				log.info("Next Payment Date is: " +actual);
		}
		
		else if(initSubs.contains("PREMIUM PLUS"))
		{
			pricePerDay	= 6.6;                      //<---------------- 198 kr/30 days = 6.6kr/day
			
			String actSubs=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
			String actSubsTrim=actSubs.replaceAll("\\s+", "");
			String expSubs="Duharabonnemanget:PREMIUMPLUS";
			
				Assert.assertEquals(actSubsTrim, expSubs);
				log.info(actSubs);
			
			String actual= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
			
			double result=Double.parseDouble(codePrice);
			int days = (int) Math.round(result / pricePerDay);
			 
			String currentDate= AddDate.currentDate();
			log.info("Current Date is : " +currentDate);
				
			String expected= AddDate.addingDays(days);
			
			
				Assert.assertEquals(actual, expected.trim());
				log.info("Next Payment Date is: " +actual);
		}
		
		else if(initSubs.contains("PREMIUM"))
		{
			pricePerDay	= 8.3;                      //<---------------- 249 kr/30 days = 8.3kr/day
			
			String actSubs=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
			String actSubsTrim=actSubs.replaceAll("\\s+", "");
			String expSubs="Duharabonnemanget:PREMIUM";
			
				Assert.assertEquals(actSubsTrim, expSubs);
				log.info(actSubs);
			
			String actual= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
			
			double result=Double.parseDouble(codePrice);
			int days = (int) Math.round(result / pricePerDay);
			 
			String currentDate= AddDate.currentDate();
			log.info("Current Date is : " +currentDate);
				
			String expected= AddDate.addingDays(days);
			
			
				Assert.assertEquals(actual, expected.trim());
				log.info("Next Payment Date is: " +actual);
		}
		
		log.info("logging out");
		MyAccountPage acc=new MyAccountPage(driver);
		acc.clickLogOut();
		
		new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudböcker & E-böcker - Ladda ner Gratis Ljudbok/E-bok Online"));
		
		log.info("Validating into Admin Site");
		
		driver.manage().deleteAllCookies();
		driver.get(adminUrl);
		
		AdminPage admin=new AdminPage(driver);
		admin.setUserName(adminUn);
		admin.setPassword(adminPwd);
		admin.clickLogin();
		admin.clickCustMgmt();
		admin.setEPost(un);
		admin.clickSearch();
		String memberStatus = admin.getMemberType();
		String subsType = admin.getSubsType();
		
	
			Assert.assertEquals(memberStatus, "MEMBER_GIFTCARD_EXISTING");
			log.info("Membership Status is: " +memberStatus + " in Admin Site");
			
			if(subs.contains("BAS"))
			{
				subs= "BASE";
			}
			else if(subs.contains("PREMIUM PLUS"))
			{
				subs= "PREMIUM";
			}
		
			Assert.assertEquals(subsType, subs);
			log.info("Subscription Type is: " +subsType+ " in Admin Site");
		
		
			admin.clickLogout();
			driver.get(url);
		
	}
	
	
	//----------------------------Redeeming GiftCard As Existing User With Login----------------------------------//
	
	@Test(enabled=true, priority=28, groups={"Registrations" , "All"})
	public void redeemGiftCardExistingUserWithLogin() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
		adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
		adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
		un=Excel.getCellValue(INPUT_PATH, "MemberPaying" , 1, 0);
		pwd=PasswordFromAdmin.gettingPasswordFromAdmin(un);
		kod=Database.executeQuery("select a.voucher from giftcard_pur_details a left outer join giftcard b on a.giftcardid=b.id where giftcardname='PresenkortNew' and status='ACTIVE' and redeemedby is null and a.validupto>curdate() order by rand() limit 1");
		codePrice= Database.executeQuery("select a.price from giftcard_pur_details a left outer join giftcard b on a.giftcardid=b.id where voucher='" +kod+ "'");
		 
		log.info("Clicking on Login Link");
		HomePage home=new HomePage(driver);
		home.clickLoginLink();
		
		log.info("Login with the details Email as '" +un+ "' and password as '" +pwd+ "'");
		LoginPage login = new LoginPage(driver);
		login.setEmailId(un);
		login.setPassword(pwd);
		login.clickLoginButton();
		
		String initSubs=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
		String subs=initSubs.substring(22);
		
		home.clickGiftCard();
		
		log.info("Entering the giftcard Code");
		GiftcardPage gc=new GiftcardPage(driver);
		gc.clickRedanKund();
		gc.enterPresentKodExistingUser(kod);
		gc.clickToRedeemAsExisting();
		
		home.clickNextoryLogo();
		home.clickAccountLink();
		
		Excel.shiftingRowsUp(INPUT_PATH, "MemberPaying", 1);
		
		if(initSubs.contains("BAS"))
		{
			pricePerDay= 3.3;                      //<---------------- 99 kr/30 days = 3.3kr/day
			
			String actSubs=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
			String actSubsTrim=actSubs.replaceAll("\\s+", "");
			String expSubs="Duharabonnemanget:BAS";
			
				Assert.assertEquals(actSubsTrim, expSubs);
				log.info(actSubs);
			
			String actual= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
			
			double result=Double.parseDouble(codePrice);
			int days = (int) Math.round(result / pricePerDay);
			 
			String currentDate= AddDate.currentDate();
			log.info("Current Date is : " +currentDate);
				
			String expected= AddDate.addingDays(days);
			
			
				Assert.assertEquals(actual, expected.trim());
				log.info("Next Payment Date is: " +actual);
		}
		
		else if(initSubs.contains("STANDARD"))
		{
			pricePerDay= 6.633333333333333;                      //<---------------- 199 kr/30 days = 6.633333333333333 kr/day			
			String actSubs=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
			String actSubsTrim=actSubs.replaceAll("\\s+", "");
			String expSubs="Duharabonnemanget:STANDARD";
			
				Assert.assertEquals(actSubsTrim, expSubs);
				log.info(actSubs);
			
			String actual= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
			
			double result=Double.parseDouble(codePrice);
			int days = (int) Math.round(result / pricePerDay);
			 
			String currentDate= AddDate.currentDate();
			log.info("Current Date is : " +currentDate);
				
			String expected= AddDate.addingDays(days);
			
			
				Assert.assertEquals(actual, expected.trim());
				log.info("Next Payment Date is: " +actual);
		}
		
		else if(initSubs.contains("PREMIUM PLUS"))
		{
			pricePerDay	= 6.6;                      //<---------------- 198 kr/30 days = 6.6kr/day
			
			String actSubs=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
			String actSubsTrim=actSubs.replaceAll("\\s+", "");
			String expSubs="Duharabonnemanget:PREMIUMPLUS";
			
				Assert.assertEquals(actSubsTrim, expSubs);
				log.info(actSubs);
			
			String actual= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
			
			double result=Double.parseDouble(codePrice);
			int days = (int) Math.round(result / pricePerDay);
			 
			String currentDate= AddDate.currentDate();
			log.info("Current Date is : " +currentDate);
				
			String expected= AddDate.addingDays(days);
			
			
				Assert.assertEquals(actual, expected.trim());
				log.info("Next Payment Date is: " +actual);
		}
		
		else if(initSubs.contains("PREMIUM"))
		{
			pricePerDay	= 8.3;                      //<---------------- 249 kr/30 days = 8.3kr/day
			
			String actSubs=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
			String actSubsTrim=actSubs.replaceAll("\\s+", "");
			String expSubs="Duharabonnemanget:PREMIUM";
			
				Assert.assertEquals(actSubsTrim, expSubs);
				log.info(actSubs);
			
			String actual= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
			
			double result=Double.parseDouble(codePrice);
			int days = (int) Math.round(result / pricePerDay);
			 
			String currentDate= AddDate.currentDate();
			log.info("Current Date is : " +currentDate);
				
			String expected= AddDate.addingDays(days);
			
			
				Assert.assertEquals(actual, expected.trim());
				log.info("Next Payment Date is: " +actual);
		}
		
		log.info("logging out");
		MyAccountPage acc=new MyAccountPage(driver);
		acc.clickLogOut();
		
		new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudböcker & E-böcker - Ladda ner Gratis Ljudbok/E-bok Online"));
		
		log.info("Validating into Admin Site");
		
		driver.manage().deleteAllCookies();
		driver.get(adminUrl);
		
		AdminPage admin=new AdminPage(driver);
		admin.setUserName(adminUn);
		admin.setPassword(adminPwd);
		admin.clickLogin();
		admin.clickCustMgmt();
		admin.setEPost(un);
		admin.clickSearch();
		String memberStatus = admin.getMemberType();
		String subsType = admin.getSubsType();
		
	
			Assert.assertEquals(memberStatus, "MEMBER_GIFTCARD_EXISTING");
			log.info("Membership Status is: " +memberStatus + " in Admin Site");
			
			
			if(subs.contains("BAS"))
			{
				subs= "BASE";
			}
			else if(subs.contains("PREMIUM PLUS"))
			{
				subs= "PREMIUM";
			}
		
			Assert.assertEquals(subsType, subs);
			log.info("Subscription Type is: " +subsType+ " in Admin Site");
		
		
			admin.clickLogout();
			driver.get(url);
	
	}
	
}
	


