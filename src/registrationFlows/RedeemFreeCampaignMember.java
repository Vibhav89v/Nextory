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
import generics.Excel;
import generics.Property;
import pages.AdminPage;
import pages.CampaignPage;
import pages.CustomerFormPage;
import pages.HomePage;
import pages.LoginPage;
import pages.MyAccountPage;
import pages.NewSubscriptionPage;
import pages.PaymentCardDetailsPage;

public class RedeemFreeCampaignMember extends SuperTestScript
{
	public static String campCode;
	public static String newId;
	public static String confirm;
	public static String newPswd;
	public static String cardNumber;
	public static String cvc;
	public static String fn;
	public static String ln;
	public static String cellNum;
	public static String existingId;
	public static String existingPswd;
	public static String month;
	public static String year;
	public static String adminUn;
	public static String adminPwd;
	public static String adminUrl;
	
	
	public RedeemFreeCampaignMember()
	{
	loginRequired=false;
	logoutRequired=false;
	}
	
	
	
	
	//-------------------------------------------------REDEEM CAMPAIGN FOR BAS----------------------------------------------------------------// 
	
	@Test(enabled=true, priority=20, groups={"Registrations" , "All"})
	public void freeCampaignMemberNewUserBas() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		log.info("REDEEMING CAMPAIGN AS A NEW USER FOR BAS SUBSCRIPTION");
		
		campCode=Excel.getCellValue(INPUT_PATH,"CampaignCodes", 1, 0);
		newId=RandomEmail.email();
		confirm=newId;
		newPswd=Excel.getCellValue(INPUT_PATH, "NewEmail", 1, 2);
		cardNumber=Excel.getCellValue(INPUT_PATH, "NewEmail", 1, 3);
		cvc=Excel.getCellValue(INPUT_PATH, "NewEmail", 1, 4);
		month=Excel.getCellValue(INPUT_PATH,"NewEmail", 1, 5);
		year=Excel.getCellValue(INPUT_PATH,"NewEmail", 1, 6);
		adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
		adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
		adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
		
		fn="automation";
		ln="test";
		cellNum="1234567890";
		
		
		log.info("Clicking on Kampanjkod link in home page");
		HomePage home=new HomePage(driver);
		home.clickCampaign();  
		log.info("Navigating to the Campaign Page");
		
		log.info("Filling the campaign code and user details");
		log.info("USERNAME: '" +newId+ "' PASSWORD: '" +newPswd+ "' CampCode: '" +campCode+ "'");
		CampaignPage camp=new CampaignPage(driver);
		camp.enterCampaignCode(campCode);
		camp.enterEmailId(newId);
		camp.enterConfirmMailId(confirm);
		camp.enterPassword(newPswd);
		camp.clickToContinue();
		log.info("Navigating to Subscription Page");
		
		log.info("Choosing the Subscription");
		NewSubscriptionPage chose=new NewSubscriptionPage(driver);
		chose.clickBasSub();
		chose.clickContinue();
		log.info("Navigating to Payment page");
		
		log.info("Entering Payment Details");
		PaymentCardDetailsPage card=new PaymentCardDetailsPage(driver);
		
	
			String act1 = driver.findElement(By.xpath("//div[@class='col-xs-12 col-sm-6 col-lg-5 usp__headers']//h3[1]//span[@class='samewidth']")).getText();
			String actTrim1= act1.replaceAll("\\s+", "");
			Assert.assertEquals(actTrim1, "30dagar");
			
			String act2= driver.findElement(By.xpath("//div[@class='col-xs-12 col-sm-6 col-lg-5 usp__headers']//h3[2]//span[@class='samewidth']")).getText();
			String actTrim2= act2.replaceAll("\\s+", "");
			Assert.assertEquals(actTrim2, "99kr");
			log.info("Making payment where Your Offer / Ditt erbjudande: Gratis i " +act1+ " and Pris/mån efter gratisperioden: " +act2);
	
		
		card.enterCardNumber(cardNumber);
		card.clickExpiryMonthDropdown();
		card.selectExpiryMonth(month);
		card.clickExpiryYearDropdown();
		card.selectExpiryYear(year);
		card.enterCvcNumber(cvc);
		card.clickPaymentSubmit();
		
		log.info("Filling the customer details");
		CustomerFormPage cust= new CustomerFormPage(driver);
		cust.enterFirstName(fn);
		cust.enterLastName(ln);
		cust.enterMobileNumber(cellNum);
		cust.clickContinue();
		log.info("Campaign code Successfully redeemed for BAS");
		
		log.info("Navigating to customer info page");
		Excel.shiftingRowsDown(INPUT_PATH, "ExistingEmail", 2);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 1, newId);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 2, newPswd);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 3, "BAS");
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 4, "FREE CAMPAIGN MEMBER");
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 5, campCode);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 6, cardNumber);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 7, month);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 8, year);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 9, cvc);
		
		//Excel.ShiftingRowsUp(INPUT_PATH, "Email", 2);
	//	Excel.shiftingRowsUp(INPUT_PATH, "CampaignCodes", 1);
	//	Excel.shiftingRowsUp(INPUT_PATH, "NewEmail", 1);
		
		home.clickNextoryLogo();
		home.clickAccountLink();
		
		String text=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
		String subs=text.substring(22);
		
		
			String actualSub = driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='right']")).getText();
			String actualSubtrim= actualSub.replaceAll("\\s+", "");
			String exp1 = "BAS:99kr/månad";
			Assert.assertEquals(actualSubtrim, exp1);
			log.info("Order is " +actualSub);
			
			String currentDate= AddDate.currentDate();
			log.info("Current Date is : " +currentDate);
			
			String expected= AddDate.addingDays(30);
			String actual= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
			
			Assert.assertEquals(actual, expected.trim());
			log.info("Next Payment Date is: " +actual);
			
			String act=driver.findElement(By.xpath("//div[@class='my-account-wrapper']//li[@class='right']")).getText();
			String actTrim= act.replaceAll("\\s+", "");
			Assert.assertTrue(actTrim.contains("0.0kr"));	
			
			log.info("Recent Payment: " +act);
		
		
		MyAccountPage acc=new MyAccountPage(driver);
		acc.clickLogOut();
		
		new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudböcker & E-böcker - Ladda ner Gratis Ljudbok/E-bok Online"));
		
		log.info("VALIDATING INTO ADMIN SITE");

		
		driver.manage().deleteAllCookies();
		driver.get(adminUrl);
		
		AdminPage admin=new AdminPage(driver);
		admin.setUserName(adminUn);
		admin.setPassword(adminPwd);
		admin.clickLogin();
		admin.clickCustMgmt();
		admin.setEPost(newId);
		admin.clickSearch();
		String memberStatus = admin.getMemberType();
		String subsType = admin.getSubsType();
		
	
			Assert.assertEquals(memberStatus, "FREE_CAMPAIGN_MEMBER");
			log.info("Membership Status is: " +memberStatus + " in Admin Site");
			
			if(subs.equalsIgnoreCase("BAS"))
			{
				subs= "BASE";
			}
			Assert.assertEquals(subsType, subs);
			log.info("Subscription Type is: " +subsType+ " in Admin Site");
		
		
		

		admin.clickLogout();
		driver.get(url);
	
	}


	//-------------------------------------------------REDEEM CAMPAIGN FOR STANDARD----------------------------------------------------------------//
	
	@Test(enabled=true, priority=21, groups={"Registrations" , "All"})
	public void freeCampaignMemberNewUserStandard() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		campCode=Excel.getCellValue(INPUT_PATH,"CampaignCodes", 1, 0);
		newId=RandomEmail.email();
		confirm=newId;
		newPswd=Excel.getCellValue(INPUT_PATH, "NewEmail", 1, 2);
		cardNumber=Excel.getCellValue(INPUT_PATH, "NewEmail", 1, 3);
		cvc=Excel.getCellValue(INPUT_PATH, "NewEmail", 1, 4);
		month=Excel.getCellValue(INPUT_PATH,"NewEmail", 1, 5);
		year=Excel.getCellValue(INPUT_PATH,"NewEmail", 1, 6);
		adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
		adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
		adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
		
		fn="automation";
		ln="test";
		cellNum="1234567890";
		
		log.info("USERNAME: '" +newId+ "' PASSWORD: '" +newPswd+ "' CampCode: '" +campCode+ "'");
		log.info("Clicking on Kampanjkod link in home page");
		HomePage home=new HomePage(driver);
		home.clickCampaign();  
		log.info("Navigating to the Campaign Page");
		
		log.info("Filling the campaign code and user details");
		CampaignPage camp=new CampaignPage(driver);
		camp.enterCampaignCode(campCode);
		camp.enterEmailId(newId);
		camp.enterConfirmMailId(confirm);
		camp.enterPassword(newPswd);
		camp.clickToContinue();
		log.info("Navigating to Subscription Page");
		
		log.info("Choosing the Subscription");
		NewSubscriptionPage chose=new NewSubscriptionPage(driver);
		chose.clickStandardSub();
		chose.clickContinue();
		log.info("Navigating to Payment page");
		
		log.info("Entering Payment Details");
		PaymentCardDetailsPage card=new PaymentCardDetailsPage(driver);
		
			String act1 = driver.findElement(By.xpath("//div[@class='col-xs-12 col-sm-6 col-lg-5 usp__headers']//h3[1]//span[@class='samewidth']")).getText();
			String actTrim1= act1.replaceAll("\\s+", "");
			Assert.assertEquals(actTrim1, "30dagar");
		
			String act2= driver.findElement(By.xpath("//div[@class='col-xs-12 col-sm-6 col-lg-5 usp__headers']//h3[2]//span[@class='samewidth']")).getText();
			String actTrim2= act2.replaceAll("\\s+", "");
			Assert.assertEquals(actTrim2, "199kr");
			log.info("Making payment where Your Offer / Ditt erbjudande: Gratis i " +act1+ " and Pris/mån efter gratisperioden: " +act2);
		
		card.enterCardNumber(cardNumber);
		card.clickExpiryMonthDropdown();
		card.selectExpiryMonth(month);
		card.clickExpiryYearDropdown();
		card.selectExpiryYear(year);
		card.enterCvcNumber(cvc);
		card.clickPaymentSubmit();
		
		log.info("Filling the customer details");
		CustomerFormPage cust= new CustomerFormPage(driver);
		cust.enterFirstName(fn);
		cust.enterLastName(ln);
		cust.enterMobileNumber(cellNum);
		cust.clickContinue();
		log.info("Campaign code Successfully redeemed for STANDARD");
		
		log.info("Navigating to customer info page");
		Excel.shiftingRowsDown(INPUT_PATH, "ExistingEmail", 2);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 1, newId);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 2, newPswd);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 3, "STANDARD");
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 4, "FREE CAMPAIGN MEMBER");
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 5, campCode);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 6, cardNumber);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 7, month);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 8, year);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 9, cvc);
		
		//Excel.ShiftingRowsUp(INPUT_PATH, "Email", 2);
		//Excel.shiftingRowsUp(INPUT_PATH, "CampaignCodes", 1);
		//Excel.shiftingRowsUp(INPUT_PATH, "NewEmail", 1);
		
		home.clickNextoryLogo();
		home.clickAccountLink();
		
		String text=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
		String subs=text.substring(22);
		
		
			String actualSub = driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='right']")).getText();
			String actualSubtrim= actualSub.replaceAll("\\s+", "");
			String exp1 = "STANDARD:199kr/månad";
			Assert.assertEquals(actualSubtrim, exp1);
			log.info("Order is " +actualSub);
		
			String currentDate= AddDate.currentDate();
			log.info("Current Date is : " +currentDate);
		
			String expected= AddDate.addingDays(30);
			String actual= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
		
			Assert.assertEquals(actual, expected.trim());
			log.info("Next Payment Date is: " +actual);
		
			String act=driver.findElement(By.xpath("//div[@class='my-account-wrapper']//li[@class='right']")).getText();
			String actTrim= act.replaceAll("\\s+", "");
			Assert.assertTrue(actTrim.contains("0.0kr"));	
			log.info("Recent Payment: " +act);
		
		MyAccountPage acc=new MyAccountPage(driver);
		acc.clickLogOut();
		
		new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudböcker & E-böcker - Ladda ner Gratis Ljudbok/E-bok Online"));
		
		log.info("VALIDATING INTO ADMIN SITE");

		driver.manage().deleteAllCookies();
		driver.get(adminUrl);
		
		AdminPage admin=new AdminPage(driver);
		admin.setUserName(adminUn);
		admin.setPassword(adminPwd);
		admin.clickLogin();
		admin.clickCustMgmt();
		admin.setEPost(newId);
		admin.clickSearch();
		String memberStatus = admin.getMemberType();
		String subsType = admin.getSubsType();
		
	
			Assert.assertEquals(memberStatus, "FREE_CAMPAIGN_MEMBER");
			log.info("Membership Status is: " +memberStatus + " in Admin Site");
			
			if(subs.equalsIgnoreCase("BAS"))
			{
				subs= "BASE";
			}
			Assert.assertEquals(subsType, subs);
			log.info("Subscription Type is: " +subsType+ " in Admin Site");
		
		
		
			admin.clickLogout();
			driver.get(url);
	
	}
	
	
	//-------------------------------------------------REDEEM CAMPAIGN FOR PREMIUM----------------------------------------------------------------//
	
	@Test(enabled=true, priority=22 , groups={"Registrations" , "All"})
	public void freeCampaignMemberNewUserPremium() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
	campCode=Excel.getCellValue(INPUT_PATH,"CampaignCodes", 1, 0);
	newId=RandomEmail.email();
	confirm=newId;
	newPswd=Excel.getCellValue(INPUT_PATH, "NewEmail", 1, 2);
	cardNumber=Excel.getCellValue(INPUT_PATH, "NewEmail", 1, 3);
	cvc=Excel.getCellValue(INPUT_PATH, "NewEmail", 1, 4);
	month=Excel.getCellValue(INPUT_PATH,"NewEmail", 1, 5);
	year=Excel.getCellValue(INPUT_PATH,"NewEmail", 1, 6);
	adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
	adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
	adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
	
	fn="automation";
	ln="test";
	cellNum="1234567890";
	
	log.info("USERNAME: '" +newId+ "' PASSWORD: '" +newPswd+ "' CampCode: '" +campCode+ "'");
	log.info("Clicking on Kampanjkod link in home page");
	HomePage home=new HomePage(driver);
	home.clickCampaign();  
	log.info("Navigating to the Campaign Page");
	
	log.info("Filling the campaign code and user details");
	CampaignPage camp=new CampaignPage(driver);
	camp.enterCampaignCode(campCode);
	camp.enterEmailId(newId);
	camp.enterConfirmMailId(confirm);
	camp.enterPassword(newPswd);
	camp.clickToContinue();
	log.info("Navigating to Subscription Page");
	
	log.info("Choosing the Subscription");
	NewSubscriptionPage chose=new NewSubscriptionPage(driver);
	chose.clickPremiumSub();
	chose.clickContinue();
	log.info("Navigating to Payment page");
	
	log.info("Entering Payment Details");
	PaymentCardDetailsPage card=new PaymentCardDetailsPage(driver);
	
		String act1 = driver.findElement(By.xpath("//div[@class='col-xs-12 col-sm-6 col-lg-5 usp__headers']//h3[1]//span[@class='samewidth']")).getText();
		String actTrim1= act1.replaceAll("\\s+", "");
		Assert.assertEquals(actTrim1, "30dagar");

		String act2= driver.findElement(By.xpath("//div[@class='col-xs-12 col-sm-6 col-lg-5 usp__headers']//h3[2]//span[@class='samewidth']")).getText();
		String actTrim2= act2.replaceAll("\\s+", "");
		Assert.assertEquals(actTrim2, "249kr");
		log.info("Making payment where Your Offer / Ditt erbjudande: Gratis i " +act1+ " and Pris/mån efter gratisperioden: " +act2);
	
	card.enterCardNumber(cardNumber);
	card.clickExpiryMonthDropdown();
	card.selectExpiryMonth(month);
	card.clickExpiryYearDropdown();
	card.selectExpiryYear(year);
	card.enterCvcNumber(cvc);
	card.clickPaymentSubmit();
	
	log.info("Filling the customer details");
	CustomerFormPage cust= new CustomerFormPage(driver);
	cust.enterFirstName(fn);
	cust.enterLastName(ln);
	cust.enterMobileNumber(cellNum);
	cust.clickContinue();
	log.info("Campaign code Successfully redeemed for PREMIUM");
	
	log.info("Navigating to customer info page");
	Excel.shiftingRowsDown(INPUT_PATH, "ExistingEmail", 2);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 1, newId);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 2, newPswd);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 3, "PREMIUM");
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 4, "FREE CAMPAIGN MEMBER");
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 5, campCode);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 6, cardNumber);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 7, month);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 8, year);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 9, cvc);
	
	//Excel.ShiftingRowsUp(INPUT_PATH, "Email", 2);
	//Excel.shiftingRowsUp(INPUT_PATH, "CampaignCodes", 1);
	//Excel.shiftingRowsUp(INPUT_PATH, "NewEmail", 1);
	
	home.clickNextoryLogo();
	home.clickAccountLink();
	
	String text=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
	String subs=text.substring(22);
	
		String actualSub = driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='right']")).getText();
		String actualSubtrim= actualSub.replaceAll("\\s+", "");
		String exp1 = "PREMIUM:249kr/månad";
		Assert.assertEquals(actualSubtrim, exp1);
		log.info("Order is " +actualSub);

		String currentDate= AddDate.currentDate();
		log.info("Current Date is : " +currentDate);

		String expected= AddDate.addingDays(30);
		String actual= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
		Assert.assertEquals(actual, expected.trim());
		log.info("Next Payment Date is: " +actual);

		String act=driver.findElement(By.xpath("//div[@class='my-account-wrapper']//li[@class='right']")).getText();
		String actTrim= act.replaceAll("\\s+", "");
		Assert.assertTrue(actTrim.contains("0.0kr"));	
		log.info("Recent Payment: " +act);
	
	MyAccountPage acc=new MyAccountPage(driver);
	acc.clickLogOut();
	
	new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudböcker & E-böcker - Ladda ner Gratis Ljudbok/E-bok Online"));
	
	log.info("VALIDATING INTO ADMIN SITE");

	driver.manage().deleteAllCookies();
	driver.get(adminUrl);
	
	AdminPage admin=new AdminPage(driver);
	admin.setUserName(adminUn);
	admin.setPassword(adminPwd);
	admin.clickLogin();
	admin.clickCustMgmt();
	admin.setEPost(newId);
	admin.clickSearch();
	String memberStatus = admin.getMemberType();
	String subsType = admin.getSubsType();
	

		Assert.assertEquals(memberStatus, "FREE_CAMPAIGN_MEMBER");
		log.info("Membership Status is: " +memberStatus + " in Admin Site");
		
		if(subs.equalsIgnoreCase("BAS"))
		{
			subs= "BASE";
		}
		
		Assert.assertEquals(subsType, subs);
		log.info("Subscription Type is: " +subsType+ " in Admin Site");
	
	
		admin.clickLogout();
		driver.get(url);
	}
	
	
	/*----------------------------------REDEEM CAMPAIGN FOR EXISTING USER ----------------------------------------*/
	
	
	@Test(enabled=true, priority=23, groups={"Registrations" , "All"})
	public void freeCampaignMemberExistingUser() throws EncryptedDocumentException, InvalidFormatException, IOException
	{
		
		
		log.info("REDEEMING AS A EXISTING USER FOR CURRENT SUBSCRIPTION");
		
		campCode=Excel.getCellValue(INPUT_PATH,"CampaignCodes", 1, 0);
		existingId=Excel.getCellValue(INPUT_PATH, "MemberPaying", 1, 0);
		existingPswd=PasswordFromAdmin.gettingPasswordFromAdmin(existingId);
		adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
		adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
		adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
		
		log.info("USERNAME: '" +existingId+ "' PASSWORD: '" +existingPswd+ "' CampCode: '" +campCode+ "'");
		log.info("clicking on login");
		HomePage home=new HomePage(driver);
		home.clickLoginLink();
		
		log.info("Entering login details");
		LoginPage login=new LoginPage(driver);
		login.setEmailId(existingId);
		login.setPassword(existingPswd);
		login.clickLoginButton();
		
		String initText = driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='right']")).getText();
		log.info("Current Subscription is  " +initText);
		
		String text=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
		String subs=text.substring(22);
		
		
		log.info("Clicking on Campaign");
		home.clickCampaign();
		
		log.info("Entering campaign code");
		CampaignPage camp=new CampaignPage(driver);
		camp.enterCampaignCode(campCode);
		camp.clickToContinue();
		
		log.info("Entering customer details");
		CustomerFormPage cust=new CustomerFormPage(driver);
		cust.clickContinue();
		
		
//		Excel.shiftingRowsDown(INPUT_PATH, "ExistingEmail", 2);
//		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 1, existingId);
//		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 2, existingPswd);
//		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 3, subs);
//		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 4, "MEMBER_CAMPAIGN_EXISTING");
//		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 5, campCode);
	
		//Excel.shiftingRowsUp(INPUT_PATH, "CampaignCodes", 1);
		Excel.shiftingRowsUp(INPUT_PATH, "MemberPaying", 1);
		
		home.clickNextoryLogo();
		home.clickAccountLink();
		
		
			String finalText = driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='right']")).getText();
			log.info("Order is " +finalText);
		
		
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
		admin.setEPost(existingId);
		admin.clickSearch();
		String memberStatus = admin.getMemberType();
		String subsType = admin.getSubsType();
		
	
			Assert.assertEquals(memberStatus, "MEMBER_CAMPAIGN_EXISTING");
			log.info("Membership Status is: " +memberStatus + " in Admin Site");
			
			if(subs.equalsIgnoreCase("BAS"))
			{
				subs= "BASE";
			}
			Assert.assertEquals(subsType, subs);
			log.info("Subscription Type is: " +subsType+ " in Admin Site");
		
		
			admin.clickLogout();
			driver.get(url);
	
		
		
	}


	
	
}
