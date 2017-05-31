package confirmationsRuleMailer;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import common.PasswordFromAdmin;
import common.SuperTestScript;
import generics.Database;
import generics.Excel;
import generics.Property;
import pages.AdminPage;
import pages.HomePage;
import pages.LoginPage;
import pages.MyAccountPage;
import pages.PaymentCardDetailsPage;

public class CardUpdatedExpiry extends SuperTestScript
{
	public static String un;
	public static String pwd;
	public static String cardNumber;
	public static String cvc;
	public static String month;
	public static String year;
	public static String memTypeCode;
	public static String adminUn;
	public static String adminPwd;
	public static String adminUrl;
	public static String initSubs;
	public static String finalSubs;
	
	public CardUpdatedExpiry()
	{
		loginRequired=false;	
		logoutRequired=false;
	}
	
	
	@Test(enabled=true, priority=12,  groups={"ConfirmationsRuleMailerPositive" , "All"})
	public void cardUpdatedExpiry()
	{
		
		un=Excel.getCellValue(INPUT_PATH, "CardUpdated", 1, 0);
		pwd=PasswordFromAdmin.gettingPasswordFromAdmin(un);
		cardNumber=Excel.getCellValue(INPUT_PATH, "CardUpdated", 1, 1);
		cvc=Excel.getCellValue(INPUT_PATH, "CardUpdated", 1, 2);
		month=Excel.getCellValue(INPUT_PATH,"CardUpdated", 1, 3);
		year=Excel.getCellValue(INPUT_PATH,"CardUpdated", 1, 4);
		adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
		adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
		adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
		
		memTypeCode= Database.executeQuery("select member_type_code from customerinfo where email='" +un+ "'");
		
		log.info("Navigating To Login Page");
		HomePage home=new HomePage(driver);
		home.clickLoginLink();
		
		log.info("Entering login details with username as : '" +un+ "' and password as : '" +pwd+ "'" );
		LoginPage login=new LoginPage(driver);
		login.setEmailId(un);
		login.setPassword(pwd);
		login.clickLoginButton();
		
		if(!"201002".equalsIgnoreCase(memTypeCode))
		{
		initSubs=driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='right']")).getText();
		}
		
		String initRunDate= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
		
		
		log.info("Clicking on button to change Credit Card");
		MyAccountPage account=new MyAccountPage(driver);
		account.clickToChangeCreditCard();
		
		log.info("Filling the New Credit Card Details");
		PaymentCardDetailsPage card=new PaymentCardDetailsPage(driver);
		card.enterCardNumber(cardNumber);
		card.clickExpiryMonthDropdown();
		card.selectExpiryMonth(month);
		card.clickExpiryYearDropdown();
		card.selectExpiryYear(year);
		card.enterCvcNumber(cvc);
		card.clickToSaveCreditCard();
		
		log.info("Your Credit Card Details are Updated / Dina kreditkortsuppgifter har blivit uppdaterade.");
		
		//Excel.shiftingRowsUp(INPUT_PATH, "CardUpdated", 1);
		
		home.clickNextoryLogo();
		home.clickAccountLink();
		
		try
		{
		finalSubs= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='right']")).getText();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.info("Subscription not found");
		}
		
		String finalRunDate= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
		
		if(!"201002".equalsIgnoreCase(memTypeCode))
		{
		Assert.assertEquals(finalSubs, initSubs);
		log.info("Subscription before and after the card changes are unaffected: " +initSubs);
		}
		
		Assert.assertEquals(initRunDate, finalRunDate);
		log.info("Subscription Run Date is unaffected after and before the card changes: " +initRunDate);
		
		log.info("logging out");
		account.clickLogOut();
		
		new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudböcker & E-böcker - Ladda ner Gratis Ljudbok/E-bok Online"));
		
		log.info("VALIDATING IN ADMIN SITE");
		
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
		
		if(memTypeCode.equalsIgnoreCase("201002"))									//<---------- card update for FREE_GIFTCARD_NOCARDINFO 
		{
			Assert.assertEquals(memberStatus, "FREE_GIFTCARD_MEMBER");
			log.info("FREE_GIFTCARD_NOCARDINFO changed to FREE_GIFTCARD_MEMBER");
		}
		
		else if(memTypeCode.equalsIgnoreCase("305006"))							//<---------- card update for MEMBER_CARD_EXPIRYDUE  
		{
			Assert.assertEquals(memberStatus, "MEMBER_PAYING");
			log.info("MEMBER_CARD_EXPIRYDUE changed to MEMBER_PAYING");
		}
		
		else if(memTypeCode.equalsIgnoreCase("205006"))							//<---------- card update for FREE_CARD_EXPRIYDUE 
		{
			Assert.assertEquals(memberStatus, "FREE_CAMPAIGN_MEMBER");
			log.info("FREE_CARD_EXPRIYDUE changed to FREE_CAMPAIGN_MEMBER");
		}
		
		else if(memTypeCode.equalsIgnoreCase("203002"))							//<---------- card update for FREE_TRIAL_MEMBER 
		{
			Assert.assertEquals(memberStatus, "FREE_TRIAL_MEMBER");
			log.info("FREE_TRIAL_MEMBER remains as FREE_TRIAL_MEMBER after card updated");
		}
		
		else if(memTypeCode.equalsIgnoreCase("304001"))							//<---------- card update for MEMBER_PAYING 
		{
			Assert.assertEquals(memberStatus, "MEMBER_PAYING");
			log.info("MEMBER_PAYING remains as MEMBER_PAYING after card updated");
		}
		
		else
		{
			log.info("card updated for member Type Code: ' " +memTypeCode+ " ' and after the card update the MemberType is: '" +memberStatus+"'");
		}
		
		
		
	}

}
