package postRegistrationRuleMailer;

import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import common.RandomEmail;
import common.SuperTestScript;
import generics.AddDate;
import generics.Excel;
import generics.Property;
import pages.AdminPage;
import pages.CustomerFormPage;
import pages.HomePage;
import pages.MyAccountPage;
import pages.NewSubscriptionPage;
import pages.PaymentCardDetailsPage;
import pages.RegistrationPage;

public class Welcome extends SuperTestScript
{
	public static String newEmail;
	public static String confirm;
	public static String newPwd;
	public static String cardNumber;
	public static String cvc;
	public static String fn;
	public static String ln;
	public static String cellNum;
	public static String month;
	public static String year;
	public static String adminUn;
	public static String adminPwd;
	public static String adminUrl;
	
	
	public Welcome()
	{
		loginRequired=false;
		logoutRequired=false;
	}
	
//-------------------------------------------BAS SUBSCRIPTION--------------------------------------------------------//
	@Test(enabled=true, priority=1, groups={"PostRegistrationRuleMailer" , "All"})	
	public void welcomeBasSubscription() throws InterruptedException, EncryptedDocumentException, InvalidFormatException, IOException
	{
		log.info("REGISTRATION FLOW FOR BAS SUBSCRIPTION");
		log.info("in welcome script");
		
//		Batch.batchExecution();
//		Thread.sleep(20000);
		
		newEmail=RandomEmail.email();
		confirm=newEmail;
		newPwd=Excel.getCellValue(INPUT_PATH, "NewEmail", 1, 2);
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
		
		log.info("Clicking on Prova gratis i 14 dagar button");
		HomePage home=new HomePage(driver);
		home.clickToRegister();
		
		log.info("Choosing Subscription");
		NewSubscriptionPage subs=new NewSubscriptionPage(driver);
		subs.clickBasSub();
		subs.clickContinue();
		
		log.info("Registering by email id with email as '" +newEmail+ "' Password as '" +newPwd+ "'");
		RegistrationPage reg = new RegistrationPage(driver);
		reg.setNewEmail(newEmail);
		reg.confirmNewEmail(confirm);
		reg.setNewPassword(newPwd);
		reg.clickToContinue();
		
		log.info("Entering Payment Card Details");
		PaymentCardDetailsPage card=new PaymentCardDetailsPage(driver);
		
			String act1 = driver.findElement(By.xpath("//div[@class='col-xs-12 col-sm-6 col-lg-5 usp__headers']//h3[1]//span")).getText();
			Assert.assertEquals(act1, "14 dagar");
			
			String act2= driver.findElement(By.xpath("//div[@class='col-xs-12 col-sm-6 col-lg-5 usp__headers']//h3[2]//span")).getText();
			Assert.assertEquals(act2, "99 kr");
			
			log.info("Making payment where Gratis i: " +act1+ " and Pris per månad efter gratisperioden: " +act2);
		
		
		
		card.enterCardNumber(cardNumber);
		card.clickExpiryMonthDropdown();
		card.selectExpiryMonth(month);
		card.clickExpiryYearDropdown();
		card.selectExpiryYear(year);
		card.enterCvcNumber(cvc);
		card.clickPaymentSubmit();

		
		
		log.info("Entering customer details");
		CustomerFormPage cust= new CustomerFormPage(driver);
		cust.enterFirstName(fn);
		cust.enterLastName(ln);
		cust.enterMobileNumber(cellNum);
		cust.clickContinue();
		
		Excel.shiftingRowsDown(INPUT_PATH, "ExistingEmail", 2);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 1, newEmail);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 2, newPwd);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 3, "BAS");
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 4, "FREE TRIAL");
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 6, cardNumber);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 7, month);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 8, year);
		Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 9, cvc);
		
		//Excel.shiftingRowsUp(INPUT_PATH, "newEmail", 1);
		
		log.info("Registration has completed");
		
		home.clickNextoryLogo();
		home.clickAccountLink();
		


			String text = driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='right']")).getText();
			String finalText= text.replaceAll("\\s+","");
			Assert.assertEquals(finalText, "BAS:99kr/månad");
			log.info("Order is " +text);
			
			String currentDate= AddDate.currentDate();
			log.info("Current Date is : " +currentDate);
			
			String expected= AddDate.addingDays(14);
			String actual= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
//			
			Assert.assertEquals(actual, expected.trim());
			log.info("Next Payment Date is: " +actual);
			
			String act=driver.findElement(By.xpath("//div[@class='my-account-wrapper']//li[@class='right']")).getText();
			String trimAct=act.replaceAll("\\s+", "");
			String exp="ProvaNextoryGratisi14dagar:0.0kr";	
			
			Assert.assertEquals(trimAct, exp);
			log.info("Recent Payment: " +act);


		
		MyAccountPage account=new MyAccountPage(driver);
		account.clickLogOut();
		
		new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudböcker & E-böcker - Ladda ner Gratis Ljudbok/E-bok Online"));

		driver.manage().deleteAllCookies();
		driver.get(adminUrl);
		
		AdminPage admin=new AdminPage(driver);
		admin.setUserName(adminUn);
		admin.setPassword(adminPwd);
		admin.clickLogin();
		admin.clickCustMgmt();
		admin.setEPost(newEmail);
		admin.clickSearch();
		String memberStatus = admin.getMemberType();
		String subsType = admin.getSubsType();
		
			Assert.assertEquals(memberStatus, "FREE_TRIAL_MEMBER");
			log.info("Membership Status is: " +memberStatus + " in Admin Site");
			
			Assert.assertEquals(subsType, "BASE");
			log.info("Subscription Type is: " +subsType+ " in Admin Site");

		
		admin.clickLogout();
		driver.get(url);
		
		
		
		
		//		Thread.sleep(6000);
//		CheckEmail.subject = Excel.getCellValue(INPUT_PATH, "SubjectLine", 1, 1);
//		CheckEmail.validatingSubjectLine();
		}

	
	
//-------------------------------------------STANDARD SUBSCRIPTION--------------------------------------------------------//	
	
	
	
@Test(enabled=true, priority=2, groups={"PostRegistrationRuleMailer" , "All"})	
public void welcomeStandardSubscription() throws InterruptedException, EncryptedDocumentException, InvalidFormatException, IOException
{
	log.info("REGISTRATION FLOW FOR STANDARD SUBSCRIPTION");
	log.info("in welcome script");
	
//	Batch.batchExecution();
//	Thread.sleep(20000);
	
	newEmail=RandomEmail.email();
	confirm=newEmail;
	newPwd=Excel.getCellValue(INPUT_PATH, "NewEmail", 1, 2);
	cardNumber=Excel.getCellValue(INPUT_PATH, "NewEmail", 1, 3);
	cvc=Excel.getCellValue(INPUT_PATH, "NewEmail", 1, 4);
	month=Excel.getCellValue(INPUT_PATH, "NewEmail", 1, 5);
	year=Excel.getCellValue(INPUT_PATH, "NewEmail", 1, 6);
	adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
	adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
	adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
	
	fn="automation";
	ln="test";
	cellNum="1234567890";
	
	log.info("Clicking on Prova gratis i 14 dagar button");
	HomePage home=new HomePage(driver);
	home.clickToRegister();
	
	log.info("Choosing Subscription");
	NewSubscriptionPage subs=new NewSubscriptionPage(driver);
	subs.clickStandardSub();
	subs.clickContinue();
	
	log.info("Registering by email id with email as '" +newEmail+ "' Password as '" +newPwd+ "'");
	RegistrationPage reg = new RegistrationPage(driver);
	reg.setNewEmail(newEmail);
	reg.confirmNewEmail(confirm);
	reg.setNewPassword(newPwd);
	reg.clickToContinue();
	
	log.info("Entering Payment Card Details");
	PaymentCardDetailsPage card=new PaymentCardDetailsPage(driver);


		String act1 = driver.findElement(By.xpath("//div[@class='col-xs-12 col-sm-6 col-lg-5 usp__headers']//h3[1]//span")).getText();
		Assert.assertEquals(act1, "14 dagar");
		String act2= driver.findElement(By.xpath("//div[@class='col-xs-12 col-sm-6 col-lg-5 usp__headers']//h3[2]//span")).getText();
		Assert.assertEquals(act2, "199 kr");
		log.info("Making payment where Gratis i: " +act1+ " and Pris per månad efter gratisperioden: " +act2);

	
	card.enterCardNumber(cardNumber);
	card.clickExpiryMonthDropdown();
	card.selectExpiryMonth(month);
	card.clickExpiryYearDropdown();
	card.selectExpiryYear(year);
	card.enterCvcNumber(cvc);
	card.clickPaymentSubmit();
	

	Excel.shiftingRowsDown(INPUT_PATH, "ExistingEmail", 2);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 1, newEmail);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 2, newPwd);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 3, "STANDARD");
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 4, "FREE TRIAL");
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 6, cardNumber);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 7, month);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 8, year);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 9, cvc);
	
	//Excel.shiftingRowsUp(INPUT_PATH, "newEmail", 1);
	
	log.info("Entering customer details");
	CustomerFormPage cust= new CustomerFormPage(driver);
	cust.enterFirstName(fn);
	cust.enterLastName(ln);
	cust.enterMobileNumber(cellNum);
	cust.clickContinue();
	
	log.info("Registration has completed");
	
	home.clickNextoryLogo();
	home.clickAccountLink();

		String text = driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='right']")).getText();
		String finalText= text.replaceAll("\\s+","");
		Assert.assertEquals(finalText, "STANDARD:199kr/månad");
		log.info("Order is " +text);
		
		String currentDate= AddDate.currentDate();
		log.info("Current Date is : " +currentDate);
		
		String expected= AddDate.addingDays(14);
		String actual= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
//		
		Assert.assertEquals(actual, expected.trim());
		log.info("Next Payment Date is: " +actual);
		
		String act=driver.findElement(By.xpath("//div[@class='my-account-wrapper']//li[@class='right']")).getText();
		String trimAct=act.replaceAll("\\s+", "");
		String exp="ProvaNextoryGratisi14dagar:0.0kr";
		
		Assert.assertEquals(trimAct, exp);
		log.info("Recent Payment: " +act);
		
	
	MyAccountPage account=new MyAccountPage(driver);
	account.clickLogOut();
	
	new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudböcker & E-böcker - Ladda ner Gratis Ljudbok/E-bok Online"));
	
	driver.manage().deleteAllCookies();
	driver.get(adminUrl);
	
	AdminPage admin=new AdminPage(driver);
	admin.setUserName(adminUn);
	admin.setPassword(adminPwd);
	admin.clickLogin();
	admin.clickCustMgmt();
	admin.setEPost(newEmail);
	admin.clickSearch();
	String memberStatus = admin.getMemberType();
	String subsType = admin.getSubsType();
	
	
		Assert.assertEquals(memberStatus, "FREE_TRIAL_MEMBER");
		log.info("Membership Status is: " +memberStatus + " in Admin Site");
		
		Assert.assertEquals(subsType, "STANDARD");
		log.info("Subscription Type is: " +subsType+ " in Admin Site");
	
	
	admin.clickLogout();
	driver.get(url);
	
	
//	Thread.sleep(6000);
//	CheckEmail.subject = Excel.getCellValue(INPUT_PATH, "SubjectLine", 1, 1);
//	CheckEmail.validatingSubjectLine();
	}




//-------------------------------------------PREMIUM SUBSCRIPTION--------------------------------------------------------//



@Test(enabled=true, priority=3, groups={"PostRegistrationRuleMailer" , "All"})	
public void welcomePremiumSubscription() throws InterruptedException, EncryptedDocumentException, InvalidFormatException, IOException
{
	log.info("REGISTRATION FLOW FOR PREMIUM SUBSCRIPTION");
	log.info("in welcome script");
	
//	Batch.batchExecution();
//	Thread.sleep(20000);
	
	newEmail=RandomEmail.email();
	confirm=newEmail;
	newPwd=Excel.getCellValue(INPUT_PATH, "NewEmail", 1, 2);
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
	
	log.info("Clicking on Prova gratis i 14 dagar button");
	HomePage home=new HomePage(driver);
	home.clickToRegister();
	
	log.info("Choosing Subscription");
	NewSubscriptionPage subs=new NewSubscriptionPage(driver);
	subs.clickPremiumSub();
	subs.clickContinue();
	
	log.info("Registering by email id with email as '" +newEmail+ "' Password as '" +newPwd+ "'");
	RegistrationPage reg = new RegistrationPage(driver);
	reg.setNewEmail(newEmail);
	reg.confirmNewEmail(confirm);
	reg.setNewPassword(newPwd);
	reg.clickToContinue();
	
	log.info("Entering Payment Card Details");
	PaymentCardDetailsPage card=new PaymentCardDetailsPage(driver);
	
		String act1 = driver.findElement(By.xpath("//div[@class='col-xs-12 col-sm-6 col-lg-5 usp__headers']//h3[1]//span")).getText();
		Assert.assertEquals(act1, "14 dagar");
		String act2= driver.findElement(By.xpath("//div[@class='col-xs-12 col-sm-6 col-lg-5 usp__headers']//h3[2]//span")).getText();
		Assert.assertEquals(act2, "249 kr");
		log.info("Making payment where Gratis i: " +act1+ " and Pris per månad efter gratisperioden: " +act2);
	
	
	
	
	card.enterCardNumber(cardNumber);
	card.clickExpiryMonthDropdown();
	card.selectExpiryMonth(month);
	card.clickExpiryYearDropdown();
	card.selectExpiryYear(year);
	card.enterCvcNumber(cvc);
	card.clickPaymentSubmit();
	
	Excel.shiftingRowsDown(INPUT_PATH, "ExistingEmail", 2);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 1, newEmail);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 2, newPwd);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 3, "PREMIUM");
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 4, "FREE TRIAL");
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 6, cardNumber);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 7, month);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 8, year);
	Excel.setExcelData(INPUT_PATH, "ExistingEmail", 2, 9, cvc);
	
	//Excel.shiftingRowsUp(INPUT_PATH, "newEmail", 1);
	
	log.info("Entering customer details");
	CustomerFormPage cust= new CustomerFormPage(driver);
	cust.enterFirstName(fn);
	cust.enterLastName(ln);
	cust.enterMobileNumber(cellNum);
	cust.clickContinue();
	
	log.info("Registration has completed");
	
	home.clickNextoryLogo();
	home.clickAccountLink();
	
		String text = driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='right']")).getText();
		String finalText= text.replaceAll("\\s+","");
		Assert.assertEquals(finalText, "PREMIUM:249kr/månad");
		log.info("Order is " +text);
		
		String currentDate= AddDate.currentDate();
		log.info("Current Date is : " +currentDate);
		
		String expected= AddDate.addingDays(14);
		String actual= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
//		
		Assert.assertEquals(actual, expected.trim());
		log.info("Next Payment Date is: " +actual);
		
		String act=driver.findElement(By.xpath("//div[@class='my-account-wrapper']//li[@class='right']")).getText();
		String trimAct=act.replaceAll("\\s+", "");
		String exp="ProvaNextoryGratisi14dagar:0.0kr";	
		
		Assert.assertEquals(trimAct, exp);
		log.info("Recent Payment: " +act);
	
	
	MyAccountPage account=new MyAccountPage(driver);
	account.clickLogOut();
	
	new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudböcker & E-böcker - Ladda ner Gratis Ljudbok/E-bok Online"));
	
	driver.manage().deleteAllCookies();
	driver.get(adminUrl);
	
	AdminPage admin=new AdminPage(driver);
	admin.setUserName(adminUn);
	admin.setPassword(adminPwd);
	admin.clickLogin();
	admin.clickCustMgmt();
	admin.setEPost(newEmail);
	admin.clickSearch();
	String memberStatus = admin.getMemberType();
	String subsType = admin.getSubsType();
	
	Assert.assertEquals(memberStatus, "FREE_TRIAL_MEMBER");
		log.info("Membership Status is: " +memberStatus + " in Admin Site");
		
		Assert.assertEquals(subsType, "PREMIUM");
		log.info("Subscription Type is: " +subsType+ " in Admin Site");
	
	
	admin.clickLogout();
	driver.get(url);
	
//	Thread.sleep(6000);
//	CheckEmail.subject = Excel.getCellValue(INPUT_PATH, "SubjectLine", 1, 1);
//	CheckEmail.validatingSubjectLine();
	}
}
