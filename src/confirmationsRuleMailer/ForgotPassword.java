package confirmationsRuleMailer;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import common.SuperTestScript;
import generics.Excel;
import pages.ForgotPasswordPage;
import pages.HomePage;
import pages.LoginPage;

public class ForgotPassword extends SuperTestScript
{
	public static String un;
	
	SoftAssert soft= new SoftAssert();
	
	public ForgotPassword()
	{
		loginRequired=false;
		logoutRequired=false;
	}
	
	@Test(enabled=true, priority=41,  groups={"ConfirmationsRuleMailerPositive" , "All"})
	public void forgotPasswordTC101001()
	{
		un=Excel.getCellValue(INPUT_PATH, "ForgotPassword", 1, 0);
		
		log.info("Clicking Login on HomePage");
		HomePage home=new HomePage(driver);
		home.clickLoginLink();
		
		log.info("Clicking on Forgot Password");
		LoginPage login=new LoginPage(driver);
		login.setEmailId(un);
		login.clickForgotPassword();
		
		log.info("Entering email id '" +un+ "' to send the new password link");
		ForgotPasswordPage forgot=new ForgotPasswordPage(driver);
		forgot.enterEmailId(un);
		Assert.assertTrue(forgot.skickaButtonClickable(), "SKICKA BUTTON IS NOT CLICKABLE");
		forgot.clickSkickaButton();
		
		boolean elem= driver.findElement(By.xpath("//h1[contains(text(),'Lösenordsåterställning')]")).isDisplayed();
		
		Assert.assertTrue(elem, "FORGOT PASSWORD LINK NOT SENT and FAILED");
		
		log.info("Password Link sent on the Email Id entered");
		
		//log.info("Refreshing the Excel Sheet");
		//Excel.shiftingRowsUp(INPUT_PATH, "ForgotPassword", 1);
		//log.info("Excel sheet refreshed");
		
		home.clickNextoryLogo();
		
	}
	
	
	//---------------------------------------------------------NEGATIVE FLOWS------------------------------------------------------------------//
	
	@Test(enabled=true, priority=42,  groups={"ConfirmationsRuleMailerNegative" , "All"})
	public void forgotPasswordBlankEmail()
	{
		
		log.info("LEAVING THE EMAIL BOX EMPTY: NEGATIVE FLOW");
		un=Excel.getCellValue(INPUT_PATH, "ForgotPassword", 1, 0);
		
		log.info("Clicking Login on HomePage");
		HomePage home=new HomePage(driver);
		home.clickLoginLink();
		
		log.info("Clicking on Forgot Password");
		LoginPage login=new LoginPage(driver);
		login.setEmailId(un);
		login.clickForgotPassword();
		
		log.info("Leaving email id to get the Error Message");
		ForgotPasswordPage forgot=new ForgotPasswordPage(driver);
		forgot.clickSkickaButton();
		
		String actErrMsg = driver.findElement(By.xpath("//label[@class='error']")).getText();
		String actErrMsgTrim = actErrMsg.replaceAll("\\s+", "");
		String expErrMsg = "Vänligenfylliengiltige-postadress.";
		
		Assert.assertEquals(actErrMsgTrim, expErrMsg, "ERROR MESSAGE AT FORGET PASSWORD PAGE DOES NOT MATCH");
		
		forgot.enterEmailId(un);
		Assert.assertTrue(forgot.skickaButtonClickable(), "SKICKA BUTTON IS NOT CLICKABLE");
		forgot.clickSkickaButton();
	
		boolean elem= driver.findElement(By.xpath("//h1[contains(text(),'Lösenordsåterställning')]")).isDisplayed();
		
		soft.assertTrue(driver.findElement(By.xpath("//h1[contains(text(),'Lösenordsåterställning')]")).isDisplayed(), "FORGOT PASSWORD LINK NOT SENT and FAILED");
		
		log.info("Password Link sent on the Email Id entered");
		
		home.clickNextoryLogo();
	}
	
	
	@Test(enabled=true, priority=43,  groups={"ConfirmationsRuleMailerNegative" , "All"})
	public void forgotPasswordWrongEmail()
	{
		
		log.info("ENTERING THE EMAIL BOX WITH NON_EXISTING EMAIL: NEGATIVE FLOW");
		un=Excel.getCellValue(INPUT_PATH, "ForgotPassword", 1, 0);
		
		log.info("Clicking Login on HomePage");
		HomePage home=new HomePage(driver);
		home.clickLoginLink();
		
		log.info("Clicking on Forgot Password");
		LoginPage login=new LoginPage(driver);
		login.clickForgotPassword();
		
		log.info("Entering Wrong Email Id to get the Error Message");
		ForgotPasswordPage forgot=new ForgotPasswordPage(driver);
		forgot.enterEmailId("non_existing_email@frescano.se");
		forgot.clickSkickaButton();
		
		String actErrMsg = driver.findElement(By.xpath("//span[@id='email.errors']")).getText();
		String actErrMsgTrim = actErrMsg.replaceAll("\\s+", "");
		String expErrMsg = "Denangivnae-postadressenärfelaktig";
		
		Assert.assertEquals(actErrMsgTrim, expErrMsg, "ERROR MESSAGE AT FORGET PASSWORD PAGE DOES NOT MATCH");
		
		forgot.clearEmailTextbox();
		forgot.enterEmailId(un);
		forgot.clickSkickaButton();
		
		boolean elem= driver.findElement(By.xpath("//h1[contains(text(),'Lösenordsåterställning')]")).isDisplayed();
		
		Assert.assertTrue(elem, "FORGOT PASSWORD LINK NOT SENT and FAILED");
		
		log.info("Password Link sent on the Email Id entered");
			
		home.clickNextoryLogo();
		
	}

}
