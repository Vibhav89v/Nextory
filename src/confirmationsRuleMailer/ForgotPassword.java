package confirmationsRuleMailer;

import org.testng.annotations.Test;

import common.SuperTestScript;
import generics.Excel;
import pages.ForgotPasswordPage;
import pages.HomePage;
import pages.LoginPage;

public class ForgotPassword extends SuperTestScript
{
	public static String un;
	
	public ForgotPassword()
	{
		loginRequired=false;
		logoutRequired=false;
	}
	
	@Test(enabled=true, priority=13,  groups={"ConfirmationsRuleMailerPositive" , "All"})
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
		forgot.clickSkickaButton();
		
		log.info("Password Link sent on the Email Id entered");
		
		//log.info("Refreshing the Excel Sheet");
		//Excel.shiftingRowsUp(INPUT_PATH, "ForgotPassword", 1);
		//log.info("Excel sheet refreshed");
		
		home.clickNextoryLogo();
		
	}

}
