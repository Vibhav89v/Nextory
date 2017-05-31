package confirmationsRuleMailer;
																	//Flow works only for Member Paying and Free trial Member

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
import pages.ChangingSubscriptionPage;
import pages.HomePage;
import pages.LoginPage;
import pages.MyAccountPage;

public class SubscriptionDowngrade extends SuperTestScript
{
	public static String un;
	public static String pwd;
	public static String sub;
	public static String member;
	public static String adminUn;
	public static String adminPwd;
	public static String adminUrl;
	
	public SubscriptionDowngrade()
	{
		loginRequired=false;
		logoutRequired=false;
	}
	
//----------------------------------------------Subscription Downgrade for Member Paying----------------------------------------------//
	

	
	@Test(enabled=true, priority=7, groups={"ConfirmationsRuleMailerPositive" , "All"})
	public void subscriptionDowngrade()
	{
		log.info("DOWNGRADE FOR MEMBER PAYING");
		
		un=Excel.getCellValue(INPUT_PATH, "SubsDowngrade", 1, 0);
		pwd=PasswordFromAdmin.gettingPasswordFromAdmin(un);
		sub=Excel.getCellValue(INPUT_PATH, "SubsDowngrade", 1, 1);
		member=Excel.getCellValue(INPUT_PATH, "SubsDowngrade", 1, 2);
		adminUn=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINUN");
		adminPwd=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINPWD");
		adminUrl=Property.getPropertyValue(CONFIG_PATH+CONFIG_FILE, "ADMINURL");
		
		log.info("Clicking on Login Button");
		HomePage home=new HomePage(driver);
		home.clickLoginLink();
		log.info("Navigating to Login Page");
		
		//------------------------------------LOGIN AS A MEMBER_PAYING EXISTING USER--------------------------------------------//		
		
		log.info("Entering login details with username as : '" +un+ "' and password as : '" +pwd+ "'" );
		LoginPage login=new LoginPage(driver);
		login.setEmailId(un);
		login.setPassword(pwd);
		login.clickLoginButton();
		log.info("Navigating to My Account Page");
		
		String initSubs = driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//h3[@class='category-h1 dynamic']")).getText();
		String initSubDate = driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
		
		String customerid= Database.executeQuery("select customerid from customerinfo where email='" +un+ "'");
		String runDate= Database.executeQuery("select next_subscription_run_date from customer2subscriptionmap where customerid=" +customerid);
		String memTypeCode= Database.executeQuery("select member_type_code from customerinfo  where email='" +un+ "'");
		
		String initText = driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='right']")).getText();
		if(initText.contains("PREMIUM")|| initText.contains("STANDARD"))
		{
			log.info("PRESENT SUBSCRIPTION TYPE AND PRICE PER MONTH IS : "+initText);
			
			log.info("Clicking on Subscription Downgrade Button");
			MyAccountPage account=new MyAccountPage(driver);
			account.clickToChangeSubscription();
			log.info("Navigating to Subscription Page");
			
			log.info("Downgrading to lower subscriptions");
			ChangingSubscriptionPage change=new ChangingSubscriptionPage(driver);
			change.changeToBas();
			change.clickToContinue();
			change.clickToAccept();
			log.info("Navigating back to My Account page and Validating");
			
			Excel.shiftingRowsUp(INPUT_PATH, "SubsDowngrade", 1);
			
			String expNotice= "Dittabonnemangkommerändras";
			String actNotice= driver.findElement(By.xpath("//div[@class='noticebox text-center']//span")).getText();
			String actNoticTrim=actNotice.replaceAll("\\s+", "");
			Assert.assertEquals(actNoticTrim, expNotice);
				
			log.info(actNotice);
			
//			log.info("Validating the Subscription Run Date from Database");
//			String exp= runDate;
//			String act=driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
//			Assert.assertEquals(act, exp);	
//			log.info("Previous to downgrading, Subscription Run Date was :" +exp);
//			log.info("Post-Downgrading, Subscription Run Date is : "+act);

			log.info("Validating the Subscription Run Date from WebSite");	
			String finalSubDate=driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='left']")).getText();
			Assert.assertEquals(finalSubDate, initSubDate);
			log.info("Previous to downgrading, Subscription Run Date was :" +initSubDate);
			log.info("Post-Downgrading, Subscription Run Date is : "+finalSubDate);
			
			String finalText= driver.findElement(By.xpath("//div[@class='my-account-wrapper clearfix']//li[@class='right']")).getText();
			String finalTextTrim= finalText.replaceAll("\\s+","");
			Assert.assertEquals(finalTextTrim, "BAS:99kr/månad");
			
		
			log.info("Subscription will be downgraded to : '" +finalText+ "' after the '" +initSubDate+ "'");
			
			String finalSubs=driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
			String subs=finalSubs.substring(22);
			
			
			
			//-------------------------------------------------- For Member Paying ------------------------------------------------------//
			
			
			if(memTypeCode.equalsIgnoreCase("304001"))                      
			{
				log.info("MEMBER_TYPE= 'MEMBER_PAYING'");
				
				Assert.assertEquals(finalSubs, initSubs);
			
				log.info("Subscription till Next Subscription Run Date is : " +finalSubs);
				
				WebElement ele=driver.findElement(By.xpath("//button[@class='blueButton flex nedgradering popupLink']"));
				
				if(ele.isEnabled())
				{
					log.info("Avbryt nedgradering button is clickable");
				}
				
				else
				{
					log.info("Avbryt nedgradering is not clickable");
				}
			
			}
			
			
			
			//-------------------------------------------------- For Free Trial --------------------------------------------------------------//
			
			else if(memTypeCode.equalsIgnoreCase("203002"))
			{
				log.info("MEMBER_TYPE= 'FREE_TRIAL_MEMBER'");
				
				String expSub="Duharabonnemanget:BAS";
				String actSub= driver.findElement(By.xpath("//h3[@class='category-h1 dynamic']")).getText();
				String actSubTrim= actSub.replaceAll("\\s+", "");
				
				Assert.assertEquals(actSubTrim, expSub);
				log.info("After Downgrading: " +actSub);
				
				WebElement ele=driver.findElement(By.xpath("//button[@class='blueButton flex upgrade']"));
				
				if(ele.isEnabled())
				{
					log.info("Uppgradera / Ändra button is clickable");
				}
				
				else
				{
					log.info("Uppgradera / Ändra button is not clickable");
				}
			}
			
				
			
				
			
		home.clickNextoryLogo();
		home.clickAccountLink();
		
		log.info("logging out");
		MyAccountPage acc=new MyAccountPage(driver);
		acc.clickLogOut();
		
		new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudböcker & E-böcker - Ladda ner Gratis Ljudbok/E-bok Online"));
		
		
		//---------------------------------------------------------VALIDATION IN ADMIN------------------------------------------------------------------//
		
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
		
		if(memTypeCode.equalsIgnoreCase("203002"))
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
		
		else if(memTypeCode.equalsIgnoreCase("304001"))
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

	}
		
		else
		{
			log.info("Member is already in the lowest i.e. BAS Subscription, looking for the STANDARD or PREMIUM member to Downgrade. ");
			Excel.shiftingRowsUp(INPUT_PATH, "SubsDowngrade", 1);
			
			log.info("logging out");
			MyAccountPage acc=new MyAccountPage(driver);
			acc.clickLogOut();
			
			subscriptionDowngrade();
			
		}
	
	}
	
}
