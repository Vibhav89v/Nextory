package confirmationsRuleMailer;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
import pages.EndSubsFeedbackPage;
import pages.EndSubscriptionPage;
import pages.HomePage;
import pages.LoginPage;
import pages.MyAccountPage;

public class ChurnFreeTrialCampaignMember extends SuperTestScript
{
	public static String un;
	public static String pwd;
	public static String adminUn;
	public static String adminPwd;
	public static String adminUrl;
	
	public ChurnFreeTrialCampaignMember()
	{
		loginRequired=false;
		logoutRequired=false;
	}
	
	
	@Test(enabled=true, priority=9, groups={"ConfirmationsRuleMailerPositive" , "All"})
	public void churnFreeTrialCampaignMember()
	{
		log.info("ENDING THE SUBSCRIPTION");
		
		un=Excel.getCellValue(INPUT_PATH, "ChurnFreeTrialCampaign", 1, 0);    //using the Free Trial or Free Campaign Member email		
		pwd=PasswordFromAdmin.gettingPasswordFromAdmin(un);
		adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
		adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
		adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
		
		log.info("Clicking on Login Button");
		HomePage home=new HomePage(driver);
		home.clickLoginLink();
		log.info("Navigating to Login Page");
		
		log.info("Entering login details with username as : '" +un+ "' and password as : '" +pwd+ "'" );
		LoginPage login=new LoginPage(driver);
		login.setEmailId(un);
		login.setPassword(pwd);
		login.clickLoginButton();
		log.info("Navigating to My Account Page");
		
		String text=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
		log.info(text);
		String subs=text.substring(22);
		String memTypeCode= Database.executeQuery("select member_type_code from customerinfo where email='" +un+ "'");
		
		if(memTypeCode.equalsIgnoreCase("203002") || memTypeCode.equalsIgnoreCase("202002"))
		{
		
			log.info("Subscription is " +subs);
			log.info("Clicking on End Subscription button");
		  
			MyAccountPage acc=new MyAccountPage(driver);
			acc.clickToEndSubscription();
		
				if(subs.contains("PREMIUM") || subs.contains("STANDARD"))
				{
					EndSubscriptionPage end=new EndSubscriptionPage(driver);
					end.clickToEnd();
				}
		
			log.info("Feedback Page");
			EndSubsFeedbackPage feed=new EndSubsFeedbackPage(driver);
			feed.clickFeedbackDropdown();
			feed.selectNoTimeToUse();
			feed.clickToEndButton();
			feed.clickClearButton();
			
			Excel.shiftingRowsUp(INPUT_PATH, "ChurnFreeTrialCampaign", 1);
		
			log.info("Subscription Ended");
			home.clickNextoryLogo();
			home.clickAccountLink();
		
			WebElement elem1 = driver.findElement(By.xpath("//button[@class='btn button big greyButton']"));
			WebElement elem2 = driver.findElement(By.xpath("//div[@class='ctaButton-fix screenMe']//a[text()='Aktivera Nextory nu!']"));
			Assert.assertTrue(elem1.isEnabled() && elem2.isEnabled());
			{
				log.info("'Aktivera' button is enabled and clickable");
				log.info("'Aktivera Nextory nu!' button is Enabled and Clickable");
			}
		
		
			log.info("logging out");
			MyAccountPage account=new MyAccountPage(driver);
			account.clickLogOut();
		
			new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudb�cker & E-b�cker - Ladda ner Gratis Ljudbok/E-bok Online"));
		
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
//			driver.findElement(By.id("e-mail")).sendKeys(adminUn);
//			driver.findElement(By.id("password")).sendKeys(adminPwd);
//			driver.findElement(By.xpath("//input[@value='Logga in']")).click();
//			driver.findElement(By.xpath("//b[text()='Customer Mgmt']")).click();
//			driver.findElement(By.xpath("//input[@id='email']")).sendKeys(un);;
//			driver.findElement(By.xpath("//input[@class='CssButton searchCustomer']")).click();
//			String memberStatus = driver.findElement(By.xpath("//input[@id='membertype']")).getAttribute("value");
//			String subsType = driver.findElement(By.xpath("//input[@id='subscriptionType']")).getAttribute("value");
		
			if(memTypeCode.equalsIgnoreCase("203002"))
			{
				Assert.assertEquals(memberStatus, "NONMEMBER_PREVIOUS_TRIAL");
				log.info("Membership Status is: " +memberStatus + " in Admin Site");
			
				if(subs.equalsIgnoreCase("BAS"))
				{
					subs= "BASE";
				}
				
				else if(subs.equalsIgnoreCase("PREMIUM PLUS"))
				{
					subs="PREMIUM";
				}
			
				Assert.assertEquals(subsType, subs);
				log.info("Subscription Type is: " +subsType+ " in Admin Site");
				
				admin.clickLogout();
				driver.get(url);
			}
		
			else if(memTypeCode.equalsIgnoreCase("202002"))
			{
				Assert.assertEquals(memberStatus, "NONMEMBER_PREVIOUS_CAMPAIGN");
				log.info("Membership Status is: " +memberStatus + " in Admin Site");
			
				if(subs.equalsIgnoreCase("BAS"))
				{
					subs= "BASE";
				}
				
				else if(subs.equalsIgnoreCase("PREMIUM PLUS"))
				{
					subs="PREMIUM";
				}
			
				Assert.assertEquals(subsType, subs);
				log.info("Subscription Type is: " +subsType+ " in Admin Site");
				
				admin.clickLogout();
				driver.get(url);
			}
		
		
		
		else
		{
			log.info("Member type is neither Free Trial Member nor Free Campaign Member");
			Excel.shiftingRowsUp(INPUT_PATH, "ChurnFreeTrialCampaign", 1);
			home.clickNextoryLogo();
			home.clickAccountLink();
			churnFreeTrialCampaignMember();
		}
	}
	}
	
	
	
//--------------------------------------------------- Negative Flows ----------------------------------------------------------------------//	
	
	@Test(enabled=true, priority=9, groups={"ConfirmationsRuleMailerNegative" , "All"})
	public void churnFreeTrialCampaignMemberNoSelection()
	{
		log.info("ENDING THE SUBSCRIPTION");
		
		un=Excel.getCellValue(INPUT_PATH, "ChurnFreeTrialCampaign", 1, 0);    //using the Free Trial or Free Campaign Member email		
		pwd=PasswordFromAdmin.gettingPasswordFromAdmin(un);
		adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
		adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
		adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
		
		log.info("Clicking on Login Button");
		HomePage home=new HomePage(driver);
		home.clickLoginLink();
		log.info("Navigating to Login Page");
		
		log.info("Entering login details with username as : '" +un+ "' and password as : '" +pwd+ "'" );
		LoginPage login=new LoginPage(driver);
		login.setEmailId(un);
		login.setPassword(pwd);
		login.clickLoginButton();
		log.info("Navigating to My Account Page");
		
		String text=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
		log.info(text);
		String subs=text.substring(22);
		String memTypeCode= Database.executeQuery("select member_type_code from customerinfo where email='" +un+ "'");
		
		if(memTypeCode.equalsIgnoreCase("203002") || memTypeCode.equalsIgnoreCase("202002"))
		{
		
			log.info("Subscription is " +subs);
			log.info("Clicking on End Subscription button");
		  
			MyAccountPage acc=new MyAccountPage(driver);
			acc.clickToEndSubscription();
		
				if(subs.contains("PREMIUM") || subs.contains("STANDARD"))
				{
					EndSubscriptionPage end=new EndSubscriptionPage(driver);
					end.clickToEnd();
				}
		
			log.info("Feedback Page");
			EndSubsFeedbackPage feed=new EndSubsFeedbackPage(driver);
			
			feed.clickToEndButton();                                             // Clicking on End Button without Input from Dropdown
			
			Alert a1 = driver.switchTo().alert();                                              
			String alertText=a1.getText();
			if(alertText.equalsIgnoreCase("V�lj ett alternativ"))				// Validating for the Alert Box with the Suggestion message.
			{
				log.info("Alert box: " + alertText);
			}
			
			else 
			{
				log.info("No Alert Box found for no selection");
			}
			
			a1.accept();
			
			
			feed.clickFeedbackDropdown();										// Continuing with Churning Free Trial/Campaign Member
			feed.selectNoTimeToUse();
			feed.clickToEndButton();
			feed.clickClearButton();
			
			Excel.shiftingRowsUp(INPUT_PATH, "ChurnFreeTrialCampaign", 1);
		
			log.info("Subscription Ended");
			home.clickNextoryLogo();
			home.clickAccountLink();
		
			WebElement elem1 = driver.findElement(By.xpath("//button[@class='btn button big greyButton']"));
			WebElement elem2 = driver.findElement(By.xpath("//div[@class='ctaButton-fix screenMe']//a[text()='Aktivera Nextory nu!']"));
			Assert.assertTrue(elem1.isEnabled() && elem2.isEnabled());
			{
				log.info("'Aktivera' button is enabled and clickable");
				log.info("'Aktivera Nextory nu!' button is Enabled and Clickable");
			}
		
		
			log.info("logging out");
			MyAccountPage account=new MyAccountPage(driver);
			account.clickLogOut();
		
			new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudb�cker & E-b�cker - Ladda ner Gratis Ljudbok/E-bok Online"));
		
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
//			driver.findElement(By.id("e-mail")).sendKeys(adminUn);
//			driver.findElement(By.id("password")).sendKeys(adminPwd);
//			driver.findElement(By.xpath("//input[@value='Logga in']")).click();
//			driver.findElement(By.xpath("//b[text()='Customer Mgmt']")).click();
//			driver.findElement(By.xpath("//input[@id='email']")).sendKeys(un);;
//			driver.findElement(By.xpath("//input[@class='CssButton searchCustomer']")).click();
//			String memberStatus = driver.findElement(By.xpath("//input[@id='membertype']")).getAttribute("value");
//			String subsType = driver.findElement(By.xpath("//input[@id='subscriptionType']")).getAttribute("value");
		
			if(memTypeCode.equalsIgnoreCase("203002"))
			{
				Assert.assertEquals(memberStatus, "NONMEMBER_PREVIOUS_TRIAL");
				log.info("Membership Status is: " +memberStatus + " in Admin Site");
			
				if(subs.equalsIgnoreCase("BAS"))
				{
					subs= "BASE";
				}
				
				else if(subs.equalsIgnoreCase("PREMIUM PLUS"))
				{
					subs="PREMIUM";
				}
			
				Assert.assertEquals(subsType, subs);
				log.info("Subscription Type is: " +subsType+ " in Admin Site");
				
				admin.clickLogout();
				driver.get(url);
			}
		
			else if(memTypeCode.equalsIgnoreCase("202002"))
			{
				Assert.assertEquals(memberStatus, "NONMEMBER_PREVIOUS_CAMPAIGN");
				log.info("Membership Status is: " +memberStatus + " in Admin Site");
			
				if(subs.equalsIgnoreCase("BAS"))
				{
					subs= "BASE";
				}
				
				else if(subs.equalsIgnoreCase("PREMIUM PLUS"))
				{
					subs="PREMIUM";
				}
			
				Assert.assertEquals(subsType, subs);
				log.info("Subscription Type is: " +subsType+ " in Admin Site");
				
				admin.clickLogout();
				driver.get(url);
			}
		
		
		
		else
		{
			log.info("Member type is neither Free Trial Member nor Free Campaign Member");
			Excel.shiftingRowsUp(INPUT_PATH, "ChurnFreeTrialCampaign", 1);
			home.clickNextoryLogo();
			home.clickAccountLink();
			churnFreeTrialCampaignMember();
		}
}
}
}