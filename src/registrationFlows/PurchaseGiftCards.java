package registrationFlows;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import common.PasswordFromAdmin;
import common.RandomEmail;
import common.SuperTestScript;
import generics.Database;
import generics.Excel;
import generics.Property;
import pages.AdminPage;
import pages.GiftcardPage;
import pages.HomePage;
import pages.LoginPage;
import pages.MyAccountPage;
import pages.PaymentCardDetailsPage;

public class PurchaseGiftCards extends SuperTestScript
{
	
	public static String fornamn;
	public static String efternamn;
	public static String email;
	public static String cardNumber;
	public static String cvc;
	public static String un;
	public static String pwd;
	public static String month;
	public static String year;
	public static String adminUn;
	public static String adminPwd;
	public static String adminUrl;

	public PurchaseGiftCards()
	{
		loginRequired=false;
		logoutRequired=false;
	}
	
	
//------------------------------------------PURCHASING GIFTCARD AS A NEW USER For One Month------------------------------------//	

@Test(enabled=true, priority=14, groups={"Registrations" , "All"})
public void giftCardPurchaseNewUserOneManad() throws InterruptedException
{
	
	log.info("PURCHASING GIFTCARD AS A NEW USER/VISITOR");
	
	fornamn=Excel.getCellValue(INPUT_PATH, "GiftcardOrderConfirmation", 1, 0);
	efternamn=Excel.getCellValue(INPUT_PATH, "GiftcardOrderConfirmation", 1, 1);
	email=RandomEmail.email();
	cardNumber=Excel.getCellValue(INPUT_PATH, "GiftcardOrderConfirmation", 1, 3);
	cvc=Excel.getCellValue(INPUT_PATH, "GiftcardOrderConfirmation", 1, 4);
	month=Excel.getCellValue(INPUT_PATH,"GiftcardOrderConfirmation", 1, 5);
	year=Excel.getCellValue(INPUT_PATH,"GiftcardOrderConfirmation", 1, 6);
	adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
	adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
	adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
	
	log.info("Clicking on Presentkort Link at HomePage");		
	HomePage home=new HomePage(driver);
	home.clickGiftCard();
	
	log.info("Choosing the Gift card to purchase & Entering the Buyer details in customer form");
	GiftcardPage gift=new GiftcardPage(driver);
	gift.clickKopForOneMonth();
	Thread.sleep(2000);
	gift.enterFirstName(fornamn);
	gift.enterLastName(efternamn);
	gift.enterEmailToReceiveCode(email);
	gift.clickFortsattToGetCode();
	
	log.info("Entering the card details as final step");
	
	PaymentCardDetailsPage card=new PaymentCardDetailsPage(driver);
	card.enterCardNumber(cardNumber);
	card.clickExpiryMonthDropdown();
	card.selectExpiryMonth(month);
	card.clickExpiryYearDropdown();
	card.selectExpiryYear(year);
	card.enterCvcNumber(cvc);
	card.clickToGetGiftCard();
	
	String act1= driver.findElement(By.xpath("//h1[@class='category-h1']")).getText();
	String actTrim1= act1.replaceAll("\\s+", "");
	String exp1= "Tackfördittköp";
	Assert.assertEquals(actTrim1, exp1);
	log.info(act1);
	
	String act2= driver.findElement(By.xpath("//h5[@class='orderNumber']")).getText();
	Assert.assertTrue(act2.contains("Ditt ordernummer"));
	log.info(act2);
	
	log.info("Thankyou For Your Purchase / Tack för ditt köp");
	
	home.clickNextoryLogo();
	
	//Excel.shiftingRowsUp(INPUT_PATH, "GiftcardOrderConfirmation", 1);
	
	log.info("VALIDATING INTO ADMIN SITE");

	
	driver.manage().deleteAllCookies();
	driver.get(adminUrl);
	
	AdminPage admin=new AdminPage(driver);
	admin.setUserName(adminUn);
	admin.setPassword(adminPwd);
	admin.clickLogin();
	admin.clickCustMgmt();
	admin.setEPost(email);
	admin.clickSearch();
	String memberStatus = admin.getMemberType();
	

		Assert.assertEquals(memberStatus, "VISITOR_GIFTCARD_BUYER");
		log.info("Membership Status is: " +memberStatus + " in Admin Site");

	admin.clickLogout();
	driver.get(url);
	
}


//------------------------------------------PURCHASING GIFTCARD AS A NEW USER For Three Month------------------------------------//


@Test(enabled=true, priority=15, groups={"Registrations" , "All"})
public void giftCardPurchaseNewUserThreeManad() throws InterruptedException
{
	
	log.info("PURCHASING GIFTCARD AS A NEW USER/VISITOR");
	
	fornamn=Excel.getCellValue(INPUT_PATH, "GiftcardOrderConfirmation", 1, 0);
	efternamn=Excel.getCellValue(INPUT_PATH, "GiftcardOrderConfirmation", 1, 1);
	email=RandomEmail.email();
	cardNumber=Excel.getCellValue(INPUT_PATH, "GiftcardOrderConfirmation", 1, 3);
	cvc=Excel.getCellValue(INPUT_PATH, "GiftcardOrderConfirmation", 1, 4);
	month=Excel.getCellValue(INPUT_PATH,"GiftcardOrderConfirmation", 1, 5);
	year=Excel.getCellValue(INPUT_PATH,"GiftcardOrderConfirmation", 1, 6);
	adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
	adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
	adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
	
	log.info("Clicking on Presentkort Link at HomePage");		
	HomePage home=new HomePage(driver);
	home.clickGiftCard();
	
	log.info("Choosing the Gift card to purchase & Entering the Buyer details in customer form");
	GiftcardPage gift=new GiftcardPage(driver);
	gift.clickKopForThreeMonth();
	Thread.sleep(2000);
	gift.enterFirstName(fornamn);
	gift.enterLastName(efternamn);
	gift.enterEmailToReceiveCode(email);
	gift.clickFortsattToGetCode();
	
	log.info("Entering the card details as final step");
	
	PaymentCardDetailsPage card=new PaymentCardDetailsPage(driver);
	card.enterCardNumber(cardNumber);
	card.clickExpiryMonthDropdown();
	card.selectExpiryMonth(month);
	card.clickExpiryYearDropdown();
	card.selectExpiryYear(year);
	card.enterCvcNumber(cvc);
	card.clickToGetGiftCard();
	
	String act1= driver.findElement(By.xpath("//h1[@class='category-h1']")).getText();
	String actTrim1= act1.replaceAll("\\s+", "");
	String exp1= "Tackfördittköp";
	Assert.assertEquals(actTrim1, exp1);
	log.info(act1);
	
	String act2= driver.findElement(By.xpath("//h5[@class='orderNumber']")).getText();
	Assert.assertTrue(act2.contains("Ditt ordernummer"));
	log.info(act2);
	
	log.info("Thankyou For Your Purchase / Tack för ditt köp");
	
	home.clickNextoryLogo();
	
	//Excel.shiftingRowsUp(INPUT_PATH, "GiftcardOrderConfirmation", 1);
	
	log.info("VALIDATING INTO ADMIN SITE");

	
	driver.manage().deleteAllCookies();
	driver.get(adminUrl);
	
	AdminPage admin=new AdminPage(driver);
	admin.setUserName(adminUn);
	admin.setPassword(adminPwd);
	admin.clickLogin();
	admin.clickCustMgmt();
	admin.setEPost(email);
	admin.clickSearch();
	String memberStatus = admin.getMemberType();
	

		Assert.assertEquals(memberStatus, "VISITOR_GIFTCARD_BUYER");
		log.info("Membership Status is: " +memberStatus + " in Admin Site");

	admin.clickLogout();
	driver.get(url);
	
}


//------------------------------------------PURCHASING GIFTCARD AS A NEW USER For Six Month------------------------------------//

@Test(enabled=true, priority=16, groups={"Registrations" , "All"})
public void giftCardPurchaseNewUserSixManad() throws InterruptedException
{
	
	log.info("PURCHASING GIFTCARD AS A NEW USER/VISITOR");
	
	fornamn=Excel.getCellValue(INPUT_PATH, "GiftcardOrderConfirmation", 1, 0);
	efternamn=Excel.getCellValue(INPUT_PATH, "GiftcardOrderConfirmation", 1, 1);
	email=RandomEmail.email();
	cardNumber=Excel.getCellValue(INPUT_PATH, "GiftcardOrderConfirmation", 1, 3);
	cvc=Excel.getCellValue(INPUT_PATH, "GiftcardOrderConfirmation", 1, 4);
	month=Excel.getCellValue(INPUT_PATH,"GiftcardOrderConfirmation", 1, 5);
	year=Excel.getCellValue(INPUT_PATH,"GiftcardOrderConfirmation", 1, 6);
	adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
	adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
	adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
	
	log.info("Clicking on Presentkort Link at HomePage");		
	HomePage home=new HomePage(driver);
	home.clickGiftCard();
	
	log.info("Choosing the Gift card to purchase & Entering the Buyer details in customer form");
	GiftcardPage gift=new GiftcardPage(driver);
	gift.clickKopForSixMonth();
	Thread.sleep(2000);
	gift.enterFirstName(fornamn);
	gift.enterLastName(efternamn);
	gift.enterEmailToReceiveCode(email);
	gift.clickFortsattToGetCode();
	
	log.info("Entering the card details as final step");
	
	PaymentCardDetailsPage card=new PaymentCardDetailsPage(driver);
	card.enterCardNumber(cardNumber);
	card.clickExpiryMonthDropdown();
	card.selectExpiryMonth(month);
	card.clickExpiryYearDropdown();
	card.selectExpiryYear(year);
	card.enterCvcNumber(cvc);
	card.clickToGetGiftCard();
	
	String act1= driver.findElement(By.xpath("//h1[@class='category-h1']")).getText();
	String actTrim1= act1.replaceAll("\\s+", "");
	String exp1= "Tackfördittköp";
	Assert.assertEquals(actTrim1, exp1);
	log.info(act1);
	
	String act2= driver.findElement(By.xpath("//h5[@class='orderNumber']")).getText();
	Assert.assertTrue(act2.contains("Ditt ordernummer"));
	log.info(act2);
	
	log.info("Thankyou For Your Purchase / Tack för ditt köp");
	
	home.clickNextoryLogo();
	
	//Excel.shiftingRowsUp(INPUT_PATH, "GiftcardOrderConfirmation", 1);
	
	log.info("VALIDATING INTO ADMIN SITE");

	
	driver.manage().deleteAllCookies();
	driver.get(adminUrl);
	
	AdminPage admin=new AdminPage(driver);
	admin.setUserName(adminUn);
	admin.setPassword(adminPwd);
	admin.clickLogin();
	admin.clickCustMgmt();
	admin.setEPost(email);
	admin.clickSearch();
	String memberStatus = admin.getMemberType();
	

		Assert.assertEquals(memberStatus, "VISITOR_GIFTCARD_BUYER");
		log.info("Membership Status is: " +memberStatus + " in Admin Site");

	admin.clickLogout();
	driver.get(url);
	
}


//----------------------------------PURCHASING GIFTCARD OF ONE MONTH AS A EXISTING USER------------------------------------------//


@Test(enabled=true, priority=17 , groups={"Registrations" , "All"})
public void oneMonthGiftCardPurchaseExistingUser() throws InterruptedException
{
	
	log.info("PURCHASING GIFT CARD AS A EXISTING USER");
	
	un=Excel.getCellValue(INPUT_PATH, "PurchaseGiftCard", 1, 0);
	//Database.executeUpdate("update customerinfo set password='76C3123DBEDEC1E13F920DB346B9C5BC' where email='" +un+ "'");
	pwd=PasswordFromAdmin.gettingPasswordFromAdmin(un);
	cardNumber=Excel.getCellValue(INPUT_PATH, "PurchaseGiftCard", 1, 1);
	cvc=Excel.getCellValue(INPUT_PATH, "PurchaseGiftCard", 1, 2);
	month=Excel.getCellValue(INPUT_PATH, "PurchaseGiftCard", 1, 3);
	year=Excel.getCellValue(INPUT_PATH, "PurchaseGiftCard", 1, 4);
	adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
	adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
	adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
	
	log.info("Navigating to Login Page");
	HomePage home=new HomePage(driver);
	home.clickLoginLink();
	
	log.info("Entering Login Details");
	LoginPage login=new LoginPage(driver);
	login.setEmailId(un);
	login.setPassword(pwd);
	login.clickLoginButton();
	
	String initSubs = driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//h3[@class='category-h1 dynamic']")).getText();
	String subs=initSubs.substring(22);
	
	log.info("Clicking on Presentkort Link");
	home.clickGiftCard();
	
	log.info("Choosing the gift card to purchase");
	GiftcardPage gift=new GiftcardPage(driver);
	gift.clickKopForOneMonth();
	Thread.sleep(2000);
	gift.clickFortsattToGetCode();
	
	log.info("Filling Payment Card Details as Final Step");
	PaymentCardDetailsPage card=new PaymentCardDetailsPage(driver);
	card.enterCardNumber(cardNumber);
	card.clickExpiryMonthDropdown();
	card.selectExpiryMonth(month);
	card.clickExpiryYearDropdown();
	card.selectExpiryYear(year);
	card.enterCvcNumber(cvc);
	card.clickToGetGiftCard();
	
	String act1= driver.findElement(By.xpath("//h1[@class='category-h1']")).getText();
	String actTrim1= act1.replaceAll("\\s+", "");
	String exp1= "Tackfördittköp";
	Assert.assertEquals(actTrim1, exp1);
	log.info(act1);
	
	String act2= driver.findElement(By.xpath("//h5[@class='orderNumber']")).getText();
	Assert.assertTrue(act2.contains("Ditt ordernummer"));
	log.info(act2);
	
	
	log.info("Thankyou For Your Purchase / Tack för ditt köp");
	
	home.clickNextoryLogo();
	home.clickAccountLink();
	
	log.info("logging out");
	MyAccountPage account=new MyAccountPage(driver);
	account.clickLogOut();
	
	Excel.shiftingRowsUp(INPUT_PATH, "PurchaseGiftCard", 1);
	
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
	
	String memTypeCode= Database.executeQuery("select member_type_code from customerinfo  where email='" +un+ "'");
	
	if(memTypeCode.equalsIgnoreCase("304001"))                 //<------------------------------- Purchased giftcard by Member Paying     
	{
		
		Assert.assertEquals(memberStatus, "MEMBER_PAYING");
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
		
	}
	
	else if(memTypeCode.equalsIgnoreCase("203002"))				//<------------------------------- Purchased giftcard by Free Trial Member     
	{
		Assert.assertEquals(memberStatus, "FREE_TRIAL_MEMBER");
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
		
	}
	
	else if(memTypeCode.equalsIgnoreCase("202002"))				//<------------------------------- Purchased giftcard by Free Campaign Member     
	{
		Assert.assertEquals(memberStatus, "FREE_CAMPAIGN_MEMBER");
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
		
	}
	

	admin.clickLogout();
	driver.get(url);
	
}


//----------------------------------PURCHASING GIFTCARD OF THREE MONTH AS A EXISTING USER------------------------------------------//

@Test(enabled=true, priority=18 , groups={"Registrations" , "All"})
public void threeMonthGiftCardPurchaseExistingUser() throws InterruptedException
{
	
	log.info("PURCHASING GIFT CARD AS A EXISTING USER");
	
	un=Excel.getCellValue(INPUT_PATH, "PurchaseGiftCard", 1, 0);
	//Database.executeUpdate("update customerinfo set password='76C3123DBEDEC1E13F920DB346B9C5BC' where email='" +un+ "'");
	pwd=PasswordFromAdmin.gettingPasswordFromAdmin(un);
	cardNumber=Excel.getCellValue(INPUT_PATH, "PurchaseGiftCard", 1, 1);
	cvc=Excel.getCellValue(INPUT_PATH, "PurchaseGiftCard", 1, 2);
	month=Excel.getCellValue(INPUT_PATH, "PurchaseGiftCard", 1, 3);
	year=Excel.getCellValue(INPUT_PATH, "PurchaseGiftCard", 1, 4);
	adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
	adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
	adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
	
	log.info("Navigating to Login Page");
	HomePage home=new HomePage(driver);
	home.clickLoginLink();
	
	log.info("Entering Login Details");
	LoginPage login=new LoginPage(driver);
	login.setEmailId(un);
	login.setPassword(pwd);
	login.clickLoginButton();
	
	String initSubs = driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//h3[@class='category-h1 dynamic']")).getText();
	String subs=initSubs.substring(22);
	
	log.info("Clicking on Presentkort Link");
	home.clickGiftCard();
	
	log.info("Choosing the gift card to purchase");
	GiftcardPage gift=new GiftcardPage(driver);
	gift.clickKopForThreeMonth();
	Thread.sleep(2000);
	gift.clickFortsattToGetCode();
	
	log.info("Filling Payment Card Details as Final Step");
	PaymentCardDetailsPage card=new PaymentCardDetailsPage(driver);
	card.enterCardNumber(cardNumber);
	card.clickExpiryMonthDropdown();
	card.selectExpiryMonth(month);
	card.clickExpiryYearDropdown();
	card.selectExpiryYear(year);
	card.enterCvcNumber(cvc);
	card.clickToGetGiftCard();
	
	String act1= driver.findElement(By.xpath("//h1[@class='category-h1']")).getText();
	String actTrim1= act1.replaceAll("\\s+", "");
	String exp1= "Tackfördittköp";
	Assert.assertEquals(actTrim1, exp1);
	log.info(act1);
	
	String act2= driver.findElement(By.xpath("//h5[@class='orderNumber']")).getText();
	Assert.assertTrue(act2.contains("Ditt ordernummer"));
	log.info(act2);
	
	
	log.info("Thankyou For Your Purchase / Tack för ditt köp");
	
	home.clickNextoryLogo();
	home.clickAccountLink();
	
	log.info("logging out");
	MyAccountPage account=new MyAccountPage(driver);
	account.clickLogOut();
	
	Excel.shiftingRowsUp(INPUT_PATH, "PurchaseGiftCard", 1);
	
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
	
	String memTypeCode= Database.executeQuery("select member_type_code from customerinfo  where email='" +un+ "'");
	
	if(memTypeCode.equalsIgnoreCase("304001"))                 //<------------------------------- Purchased giftcard by Member Paying     
	{
		
		Assert.assertEquals(memberStatus, "MEMBER_PAYING");
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
		
	}
	
	else if(memTypeCode.equalsIgnoreCase("203002"))				//<------------------------------- Purchased giftcard by Free Trial Member     
	{
		Assert.assertEquals(memberStatus, "FREE_TRIAL_MEMBER");
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
		
	}
	
	else if(memTypeCode.equalsIgnoreCase("202002"))				//<------------------------------- Purchased giftcard by Free Campaign Member     
	{
		Assert.assertEquals(memberStatus, "FREE_CAMPAIGN_MEMBER");
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
		
	}
	

	admin.clickLogout();
	driver.get(url);
	
}


//----------------------------------PURCHASING GIFTCARD OF SIX MONTH AS A EXISTING USER------------------------------------------//

@Test(enabled=true, priority=19 , groups={"Registrations" , "All"})
public void sixMonthGiftCardPurchaseExistingUser() throws InterruptedException
{
	
	log.info("PURCHASING GIFT CARD AS A EXISTING USER");
	
	un=Excel.getCellValue(INPUT_PATH, "PurchaseGiftCard", 1, 0);
	//Database.executeUpdate("update customerinfo set password='76C3123DBEDEC1E13F920DB346B9C5BC' where email='" +un+ "'");
	pwd=PasswordFromAdmin.gettingPasswordFromAdmin(un);
	cardNumber=Excel.getCellValue(INPUT_PATH, "PurchaseGiftCard", 1, 1);
	cvc=Excel.getCellValue(INPUT_PATH, "PurchaseGiftCard", 1, 2);
	month=Excel.getCellValue(INPUT_PATH, "PurchaseGiftCard", 1, 3);
	year=Excel.getCellValue(INPUT_PATH, "PurchaseGiftCard", 1, 4);
	adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
	adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
	adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
	
	log.info("Navigating to Login Page");
	HomePage home=new HomePage(driver);
	home.clickLoginLink();
	
	log.info("Entering Login Details");
	LoginPage login=new LoginPage(driver);
	login.setEmailId(un);
	login.setPassword(pwd);
	login.clickLoginButton();
	
	String initSubs = driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//h3[@class='category-h1 dynamic']")).getText();
	String subs=initSubs.substring(22);
	
	log.info("Clicking on Presentkort Link");
	home.clickGiftCard();
	
	log.info("Choosing the gift card to purchase");
	GiftcardPage gift=new GiftcardPage(driver);
	gift.clickKopForSixMonth();
	Thread.sleep(2000);
	gift.clickFortsattToGetCode();
	
	log.info("Filling Payment Card Details as Final Step");
	PaymentCardDetailsPage card=new PaymentCardDetailsPage(driver);
	card.enterCardNumber(cardNumber);
	card.clickExpiryMonthDropdown();
	card.selectExpiryMonth(month);
	card.clickExpiryYearDropdown();
	card.selectExpiryYear(year);
	card.enterCvcNumber(cvc);
	card.clickToGetGiftCard();
	
	String act1= driver.findElement(By.xpath("//h1[@class='category-h1']")).getText();
	String actTrim1= act1.replaceAll("\\s+", "");
	String exp1= "Tackfördittköp";
	Assert.assertEquals(actTrim1, exp1);
	log.info(act1);
	
	String act2= driver.findElement(By.xpath("//h5[@class='orderNumber']")).getText();
	Assert.assertTrue(act2.contains("Ditt ordernummer"));
	log.info(act2);
	
	
	log.info("Thankyou For Your Purchase / Tack för ditt köp");
	
	home.clickNextoryLogo();
	home.clickAccountLink();
	
	log.info("logging out");
	MyAccountPage account=new MyAccountPage(driver);
	account.clickLogOut();
	
	Excel.shiftingRowsUp(INPUT_PATH, "PurchaseGiftCard", 1);
	
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
	
	String memTypeCode= Database.executeQuery("select member_type_code from customerinfo  where email='" +un+ "'");
	
	if(memTypeCode.equalsIgnoreCase("304001"))                 //<------------------------------- Purchased giftcard by Member Paying     
	{
		
		Assert.assertEquals(memberStatus, "MEMBER_PAYING");
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
		
	}
	
	else if(memTypeCode.equalsIgnoreCase("203002"))				//<------------------------------- Purchased giftcard by Free Trial Member     
	{
		Assert.assertEquals(memberStatus, "FREE_TRIAL_MEMBER");
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
		
	}
	
	else if(memTypeCode.equalsIgnoreCase("202002"))				//<------------------------------- Purchased giftcard by Free Campaign Member     
	{
		Assert.assertEquals(memberStatus, "FREE_CAMPAIGN_MEMBER");
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
		
	}
	

	admin.clickLogout();
	driver.get(url);
	
}

}
