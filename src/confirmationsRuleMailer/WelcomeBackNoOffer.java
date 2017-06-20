package confirmationsRuleMailer;

import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import common.PasswordFromAdmin;
import common.SuperTestScript;
import generics.AddDate;
import generics.Excel;
import generics.Property;
import pages.AdminPage;
import pages.CustomerFormPage;
import pages.HomePage;
import pages.LoginPage;
import pages.MyAccountPage;
import pages.NewSubscriptionPage;
import pages.PaymentCardDetailsPage;

public class WelcomeBackNoOffer extends SuperTestScript 
{

	public static String un;
	public static String pwd;
	public static String cardNumber;
	public static String cvc;
	public static String month;
	public static String year;
	public static String adminUn;
	public static String adminPwd;
	public static String adminUrl;
	
	
	public WelcomeBackNoOffer()
	{
		loginRequired=false;
		logoutRequired=false;
		
	}
	
//----------------------------------------- WELCOME BACK FOR BAS ----------------------------------------------------//	
@Test(enabled=true, priority=11,  groups={"ConfirmationsRuleMailerPositive" , "All"})
	
	public void welcomeBackNoOfferBas() throws InterruptedException, EncryptedDocumentException, InvalidFormatException, IOException
	{
		
	un=Excel.getCellValue(INPUT_PATH, "NonMembers", 1, 0);
	pwd=PasswordFromAdmin.gettingPasswordFromAdmin(un);
	cardNumber=Excel.getCellValue(INPUT_PATH, "NonMembers", 1, 1);
	cvc=Excel.getCellValue(INPUT_PATH, "NonMembers", 1, 2);
	month=Excel.getCellValue(INPUT_PATH,"NonMembers", 1, 3);
	year=Excel.getCellValue(INPUT_PATH,"NonMembers", 1, 4);
	adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
	adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
	adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
		
	log.info("Clicking on Login Button");
	HomePage home=new HomePage(driver);
	home.clickLoginLink();
	log.info("On Login Page");
	
	log.info("Entering login details with username as '" +un+ "' and password as '" +pwd+ "'");
	LoginPage login=new LoginPage(driver);
	login.setEmailId(un);
	login.setPassword(pwd);
	login.clickLoginButton();
	
	
	MyAccountPage account=new MyAccountPage(driver);
	account.clickOnActivate();
	log.info("Clicked on activate button");
	
	log.info("choosing Subscription");
	NewSubscriptionPage sub=new NewSubscriptionPage(driver);
	sub.clickBasSub();
	sub.clickContinue();
	log.info("navigating to Payment Page");
	
	log.info("Entering Payment Details");
	PaymentCardDetailsPage card=new PaymentCardDetailsPage(driver);
	card.clickToOpenPaymentForm();
	card.typeCardNumber(cardNumber);
	card.clickExpiryMonthDropdown();
	card.selectExpiryMonth(month);
	card.clickExpiryYearDropdown();
	card.selectExpiryYear(year);
	card.enterCvcNumber(cvc);
	card.clickReactivateSubscription();
	log.info("navigating to customerinfo page");
	
//	Excel.shiftingRowsDown(INPUT_PATH, "ExistingEmail", 2);
//	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 1, un);
//	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 2, pwd);
//	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 3, "PREMIUM");
//	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 4, "MEMBER_PAYING");
//	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 6, cardNumber);
//	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 7, month);
//	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 8, year);
//	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 9, cvc);
	
	log.info("Verifying the customer info");
	CustomerFormPage cust=new CustomerFormPage(driver);
	cust.clickContinue();
	log.info("Nonmember successfully converted to Active Member");
	
	Excel.shiftingRowsUp(INPUT_PATH, "NonMembers", 1);
	
	home.clickNextoryLogo();
	home.clickAccountLink();
	
	String actual= account.getRunDate();
	
	String currentDate= AddDate.currentDate();
	log.info("Current Date is : " +currentDate);
	
	String expected= AddDate.addingDays(30);
	
			Assert.assertEquals(actual, expected.trim());
			log.info("Next Payment Date is: " +actual);
	
	log.info("logging out");
	account.clickLogOut();
	
	new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudb�cker & E-b�cker - Lyssna & l�s gratis i mobilen"));
	
	log.info("VALIDATING INTO ADMIN SITE");
	
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
	
		Assert.assertEquals(memberStatus, "MEMBER_PAYING");
		log.info("Membership Status is: " +memberStatus + " in Admin Site");
		
	
			
		Assert.assertEquals(subsType, "BASE");
		log.info("Subscription Type is: " +subsType+ " in Admin Site");

		
		
		
	admin.clickLogout();
	driver.get(url);

	}


//---------------------------------------- Welcome Back for Standard ----------------------------------------------//

@Test(enabled=true, priority=12,  groups={"ConfirmationsRuleMailerPositive" , "All"})
public void welcomeBackNoOfferStandard() throws InterruptedException, EncryptedDocumentException, InvalidFormatException, IOException
{
	un=Excel.getCellValue(INPUT_PATH, "NonMembers", 1, 0);
	pwd=PasswordFromAdmin.gettingPasswordFromAdmin(un);
	cardNumber=Excel.getCellValue(INPUT_PATH, "NonMembers", 1, 1);
	cvc=Excel.getCellValue(INPUT_PATH, "NonMembers", 1, 2);
	month=Excel.getCellValue(INPUT_PATH,"NonMembers", 1, 3);
	year=Excel.getCellValue(INPUT_PATH,"NonMembers", 1, 4);
	adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
	adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
	adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
	
	log.info("Clicking on Login Button");
	HomePage home=new HomePage(driver);
	home.clickLoginLink();
	log.info("On Login Page");

	log.info("Entering login details with username as '" +un+ "' and password as '" +pwd+ "'");
	LoginPage login=new LoginPage(driver);
	login.setEmailId(un);
	login.setPassword(pwd);
	login.clickLoginButton();


	MyAccountPage account=new MyAccountPage(driver);
	account.clickOnActivate();
	log.info("Clicked on activate button");

	log.info("choosing Subscription");
	NewSubscriptionPage sub=new NewSubscriptionPage(driver);
	sub.clickStandardSub();
	sub.clickContinue();
	log.info("navigating to Payment Page");

	log.info("Entering Payment Details");
	PaymentCardDetailsPage card=new PaymentCardDetailsPage(driver);
	card.clickToOpenPaymentForm();
	card.typeCardNumber(cardNumber);
	card.clickExpiryMonthDropdown();
	card.selectExpiryMonth(month);
	card.clickExpiryYearDropdown();
	card.selectExpiryYear(year);
	card.enterCvcNumber(cvc);
	card.clickReactivateSubscription();
	log.info("navigating to customerinfo page");

//Excel.shiftingRowsDown(INPUT_PATH, "ExistingEmail", 2);
//Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 1, un);
//Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 2, pwd);
//Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 3, "PREMIUM");
//Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 4, "MEMBER_PAYING");
//Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 6, cardNumber);
//Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 7, month);
//Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 8, year);
//Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 9, cvc);


	log.info("Verifying the customer info");
	CustomerFormPage cust=new CustomerFormPage(driver);
	cust.clickContinue();
	log.info("Nonmember successfully converted to Active Member");

	Excel.shiftingRowsUp(INPUT_PATH, "NonMembers", 1);


	home.clickNextoryLogo();
	home.clickAccountLink();

	String actual= account.getRunDate();

	String currentDate= AddDate.currentDate();
	log.info("Current Date is : " +currentDate);

	String expected= AddDate.addingDays(30);

		Assert.assertEquals(actual, expected.trim());
		log.info("Next Payment Date is: " +actual);

	log.info("logging out");
	account.clickLogOut();

	new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudb�cker & E-b�cker - Lyssna & l�s gratis i mobilen"));

	log.info("VALIDATING INTO ADMIN SITE");

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

		Assert.assertEquals(memberStatus, "MEMBER_PAYING");
		log.info("Membership Status is: " +memberStatus + " in Admin Site");
		
	
		Assert.assertEquals(subsType, "STANDARD");
		log.info("Subscription Type is: " +subsType+ " in Admin Site");

	
	admin.clickLogout();
	driver.get(url);

}
	


//-------------------------------------------------- Welcome Back for Premium Subscription -----------------------------------//
	
@Test(enabled=true, priority=13,  groups={"ConfirmationsRuleMailerPositive" , "All"})
public void welcomeBackNoOfferPremium() throws InterruptedException, EncryptedDocumentException, InvalidFormatException, IOException
	{
		
	un=Excel.getCellValue(INPUT_PATH, "NonMembers", 1, 0);
	pwd=PasswordFromAdmin.gettingPasswordFromAdmin(un);
	cardNumber=Excel.getCellValue(INPUT_PATH, "NonMembers", 1, 1);
	cvc=Excel.getCellValue(INPUT_PATH, "NonMembers", 1, 2);
	month=Excel.getCellValue(INPUT_PATH,"NonMembers", 1, 3);
	year=Excel.getCellValue(INPUT_PATH,"NonMembers", 1, 4);
	adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
	adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
	adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
		
	log.info("Clicking on Login Button");
	HomePage home=new HomePage(driver);
	home.clickLoginLink();
	log.info("On Login Page");
	
	log.info("Entering login details with username as '" +un+ "' and password as '" +pwd+ "'");
	LoginPage login=new LoginPage(driver);
	login.setEmailId(un);
	login.setPassword(pwd);
	login.clickLoginButton();
	
	
	MyAccountPage account=new MyAccountPage(driver);
	account.clickOnActivate();
	log.info("Clicked on activate button");
	
	log.info("choosing Subscription");
	NewSubscriptionPage sub=new NewSubscriptionPage(driver);
	sub.clickPremiumSub();
	sub.clickContinue();
	log.info("navigating to Payment Page");
	
	log.info("Entering Payment Details");
	PaymentCardDetailsPage card=new PaymentCardDetailsPage(driver);
	card.clickToOpenPaymentForm();
	card.typeCardNumber(cardNumber);
	card.clickExpiryMonthDropdown();
	card.selectExpiryMonth(month);
	card.clickExpiryYearDropdown();
	card.selectExpiryYear(year);
	card.enterCvcNumber(cvc);
	card.clickReactivateSubscription();
	log.info("navigating to customerinfo page");
	

	log.info("Verifying the customer info");
	CustomerFormPage cust=new CustomerFormPage(driver);
	cust.clickContinue();
	log.info("Nonmember successfully converted to Active Member");

	Excel.shiftingRowsUp(INPUT_PATH, "NonMembers", 1);


	home.clickNextoryLogo();
	home.clickAccountLink();
	
	String actual= account.getRunDate();
	
	String currentDate= AddDate.currentDate();
	log.info("Current Date is : " +currentDate);
	
	String expected= AddDate.addingDays(30);
	
		Assert.assertEquals(actual, expected.trim());
		log.info("Next Payment Date is: " +actual);
	
	log.info("logging out");
	account.clickLogOut();
	
	new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudb�cker & E-b�cker - Lyssna & l�s gratis i mobilen"));
	
	log.info("VALIDATING INTO ADMIN SITE");
	
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
	
		Assert.assertEquals(memberStatus, "MEMBER_PAYING");
		log.info("Membership Status is: " +memberStatus + " in Admin Site");
		
		/*if(subs.equalsIgnoreCase("BAS"))
		{
			subs= "BASE";
		}
			*/
		Assert.assertEquals(subsType, "PREMIUM");
		log.info("Subscription Type is: " +subsType+ " in Admin Site");

		
		
		
	admin.clickLogout();
	driver.get(url);

	}





//-------------------------------------------- NEGATIVE FLOWS -----------------------------------------------//

@Test(enabled=true, priority=14,  groups={"ConfirmationsRuleMailerNegative" , "All"})
public void negativeFlows()
{
	un=Excel.getCellValue(INPUT_PATH, "NonMembers", 1, 0);
	pwd=PasswordFromAdmin.gettingPasswordFromAdmin(un);
	cardNumber=Excel.getCellValue(INPUT_PATH, "NonMembers", 1, 1);
	cvc=Excel.getCellValue(INPUT_PATH, "NonMembers", 1, 2);
	month=Excel.getCellValue(INPUT_PATH,"NonMembers", 1, 3);
	year=Excel.getCellValue(INPUT_PATH,"NonMembers", 1, 4);
	adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
	adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
	adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
		
	log.info("Clicking on Login Button");
	HomePage home=new HomePage(driver);
	home.clickLoginLink();
	log.info("On Login Page");
	
	log.info("Entering login details with username as '" +un+ "' and password as '" +pwd+ "'");
	LoginPage login=new LoginPage(driver);
	login.setEmailId(un);
	login.setPassword(pwd);
	login.clickLoginButton();
	
	
	MyAccountPage account=new MyAccountPage(driver);
	account.clickOnActivate();
	log.info("Clicked on activate button");
	
	log.info("choosing Subscription");
	NewSubscriptionPage sub=new NewSubscriptionPage(driver);
	sub.clickBasSub();
	sub.clickContinue();
	log.info("navigating to Payment Page");
	
	log.info("Entering Payment Details");
	PaymentCardDetailsPage card=new PaymentCardDetailsPage(driver);
	card.clickToOpenPaymentForm();
	card.typeCardNumber("1234567887654321");
	card.clickExpiryMonthDropdown();
	card.selectExpiryMonth("12");
	card.clickExpiryYearDropdown();
	card.selectExpiryYear("28");
	card.enterCvcNumber("888");
	card.clickReactivateSubscription();
	log.info("navigating to customerinfo page");
	
	card.clickProvaIgen();
	
	
	sub.clickBasSub();
	sub.clickContinue();
	
	card.clickToOpenPaymentForm();
	card.typeCardNumber(cardNumber);
	card.clickExpiryMonthDropdown();
	card.selectExpiryMonth(month);
	card.clickExpiryYearDropdown();
	card.selectExpiryYear(year);
	card.enterCvcNumber(cvc);
	card.clickReactivateSubscription();
	
	
	log.info("Verifying the customer info");
	CustomerFormPage cust=new CustomerFormPage(driver);
	cust.clickContinue();
	log.info("Nonmember successfully converted to Active Member");
	
	Excel.shiftingRowsUp(INPUT_PATH, "NonMembers", 1);
	
	home.clickNextoryLogo();
	home.clickAccountLink();
	
	String actual= account.getRunDate();
	
	String currentDate= AddDate.currentDate();
	log.info("Current Date is : " +currentDate);
	
	String expected= AddDate.addingDays(30);
	
			Assert.assertEquals(actual, expected.trim());
			log.info("Next Payment Date is: " +actual);
	
	log.info("logging out");
	account.clickLogOut();
	
	new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudb�cker & E-b�cker - Lyssna & l�s gratis i mobilen"));
	
	log.info("VALIDATING INTO ADMIN SITE");
	
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
	
		Assert.assertEquals(memberStatus, "MEMBER_PAYING");
		log.info("Membership Status is: " +memberStatus + " in Admin Site");
		
	
			
		Assert.assertEquals(subsType, "BASE");
		log.info("Subscription Type is: " +subsType+ " in Admin Site");

		
		
		
	admin.clickLogout();
	driver.get(url);
}
	
}
