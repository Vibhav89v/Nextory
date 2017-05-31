package giftCardsRuleMailer;

import org.testng.annotations.Test;

import common.PasswordFromAdmin;
import common.RandomEmail;
import common.SuperTestScript;
import generics.Excel;
import pages.GiftcardPage;
import pages.HomePage;
import pages.LoginPage;
import pages.MyAccountPage;
import pages.PaymentCardDetailsPage;

public class OrderConfirmation extends SuperTestScript
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
	
	public OrderConfirmation()
	{
	loginRequired=false;
	logoutRequired=false;
	}
	
	@Test(enabled=true, priority=1, groups={"GiftCardRuleMailer" , "All"})
	public void GiftCardOrderConfirmationNewUser() throws InterruptedException
	{
		
		log.info("PURCHASING GIFTCARD AS A NEW USER/VISITOR");
		
		fornamn=Excel.getCellValue(INPUT_PATH, "GiftcardOrderConfirmation", 1, 0);
		efternamn=Excel.getCellValue(INPUT_PATH, "GiftcardOrderConfirmation", 1, 1);
		email=RandomEmail.email();
		cardNumber=email;
		cvc=Excel.getCellValue(INPUT_PATH, "NewEmail", 1, 4);
		month=Excel.getCellValue(INPUT_PATH,"NewEmail", 1, 5);
		year=Excel.getCellValue(INPUT_PATH,"NewEmail", 1, 6);
		
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
		
		log.info("Thankyou For Your Purchase / Tack för ditt köp");
		
		home.clickNextoryLogo();
		
		//Excel.shiftingRowsUp(INPUT_PATH, "GiftcardOrderConfirmation", 1);
		
	}
	
	@Test(enabled=true, priority=2 , groups={"GiftCardRuleMailer" , "All"})
	public void GiftCardOrderConfirmationExistingUser() throws InterruptedException
	{
		
		log.info("PURCHASING GIFT CARD AS A EXISTING USER");
		
		un=Excel.getCellValue(INPUT_PATH, "ExistingEmail", 2, 1);
		pwd=PasswordFromAdmin.gettingPasswordFromAdmin(un);
		cardNumber=Excel.getCellValue(INPUT_PATH, "ExistingEmail", 2, 6);
		month=Excel.getCellValue(INPUT_PATH, "ExistingEmail", 2, 7);
		year=Excel.getCellValue(INPUT_PATH, "ExistingEmail", 2, 8);
		cvc=Excel.getCellValue(INPUT_PATH, "ExistingEmail", 2, 9);
		
		log.info("Navigating to Login Page");
		HomePage home=new HomePage(driver);
		home.clickLoginLink();
		
		log.info("Entering Login Details");
		LoginPage login=new LoginPage(driver);
		login.setEmailId(un);
		login.setPassword(pwd);
		login.clickLoginButton();
		
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
		
		log.info("Thankyou For Your Purchase / Tack för ditt köp");
		
		home.clickNextoryLogo();
		home.clickAccountLink();
		
		log.info("logging out");
		MyAccountPage account=new MyAccountPage(driver);
		account.clickLogOut();
		
	}

}
