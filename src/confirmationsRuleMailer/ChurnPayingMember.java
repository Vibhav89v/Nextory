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

public class ChurnPayingMember extends SuperTestScript
{
	public static String un;
	public static String pwd;
	public static String adminUn;
	public static String adminPwd;
	public static String adminUrl;
	
	public ChurnPayingMember()
	{
		loginRequired=false;
		logoutRequired=false;
	}
	
	//SoftAssert soft=new SoftAssert();
	//-----------------------------------------ENDING THE SUBSCRIPTION---------------------------------------------//
	

	
	@Test(enabled=true, priority=10, groups={"ConfirmationsRuleMailerPositive" , "All"})
	public void churnPayingMemberTC100601()
	{
		log.info("ENDING THE SUBSCRIPTION");
		
		un=Excel.getCellValue(INPUT_PATH, "ChurnPayingMember", 1, 0);    //  <-------------- using the member_paying email		
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
		
	   try
	   {
		   
		   if(text.contains("PREMIUM") || text.contains("STANDARD"))
		   {
		   
		   log.info("Subscription is " +subs);
		   
		   log.info("Clicking on End Subscription button");
		   MyAccountPage acc=new MyAccountPage(driver);
		   acc.clickToEndSubscription();

		   log.info("Confirming");
		   String actual=driver.findElement(By.xpath("//p[@class='OmUnlimited-p']")).getText();
		   String expected="betala bara 99 kr/mån?";
		   
		   Assert.assertTrue(actual.contains(expected));
		   log.info("TIP: " +expected);
		   
		   EndSubscriptionPage end=new EndSubscriptionPage(driver);
		   end.clickToConfirmEndSubscription();
		   }
		   
		   else
		   {
			   log.info("Subscription is BAS");
			   
			   log.info("Clicking on End Subscription button");
			   MyAccountPage acc=new MyAccountPage(driver);
			   acc.clickToEndSubscription();
		   }
		   
	   }
	  
	   catch(Exception e)
	   {
		   e.printStackTrace();
		  
	   }
	
	   
		log.info("Feedback Page");
		EndSubsFeedbackPage feed=new EndSubsFeedbackPage(driver);
		feed.clickFeedbackDropdown();
		feed.selectNoTimeToUse();
		feed.clickToEndButton();
		feed.clickClearButton();
		
		log.info("Subscription Ended");
		home.clickNextoryLogo();
		home.clickAccountLink();
		
		Excel.shiftingRowsUp(INPUT_PATH, "ChurnPayingMember", 1);
		
		if(text.contains("BAS"))
		{
			String act1=driver.findElement(By.xpath("//h3[contains(text(),'konto är avslutat')]")).getText();
			String actTrim1 = act1.replaceAll("\\s+", "");
			String exp1= "BASEkontoäravslutat";
			
			Assert.assertEquals(actTrim1, exp1);
			log.info(act1);
		}
		
		else if(text.contains("STANDARD"))
		{
			String act1=driver.findElement(By.xpath("//h3[contains(text(),'konto är avslutat')]")).getText();
			String actTrim1 = act1.replaceAll("\\s+", "");
			String exp1= "STANDARDkontoäravslutat";
			
			Assert.assertEquals(actTrim1, exp1);
			log.info(act1);
		}
		
		else if(text.contains("PREMIUM"))
		{
			String act1=driver.findElement(By.xpath("//h3[contains(text(),'konto är avslutat')]")).getText();
			String actTrim1 = act1.replaceAll("\\s+", "");
			String exp1= "PREMIUMkontoäravslutat";
			
			Assert.assertEquals(actTrim1, exp1);
			log.info(act1);
		}
		
		String customerid= Database.executeQuery("select customerid from customerinfo where email='" +un+ "'"); 
		String runDate= Database.executeQuery("select next_subscription_run_date from customer2subscriptionmap where customerid=" +customerid);
		
		
			String exp= runDate;
			String act=driver.findElement(By.xpath("//h5[contains(text(),'konto är giltigt')]/../..//li[@class='left']")).getText();
			Assert.assertEquals(act, exp);	
			log.info("Expected date for Account access is until : " +exp);
			log.info("Your Default account is valid until : "+act);
	
		
		WebElement elem = driver.findElement(By.xpath("//button[contains(text(),'Aktivera')]"));
		if(elem.isEnabled())
		{
			log.info("Aktivera button is enabled and clickable");
		}
		else
		{
			log.info("Aktivera button is not enabled");
		}
		
		log.info("logging out");
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
		admin.setEPost(un);
		admin.clickSearch();
		String memberStatus = admin.getMemberType();
		String subsType = admin.getSubsType();
		
			Assert.assertEquals(memberStatus, "MEMBER_PAYING_CANCELLED");
			log.info("Membership Status is: " +memberStatus + " in Admin Site");
			
			if(subs.equalsIgnoreCase("BAS"))
			{
				subs= "BASE";
			}
			
			else if(subs.contains("PREMIUM PLUS"))
			{
				subs="PREMIUM";
			}
			
			Assert.assertEquals(subsType, subs);
			log.info("Subscription Type is: " +subsType+ " in Admin Site");
		
		
		
		admin.clickLogout();
		driver.get(url);
		
	}
	
	
	
	//--------------------------------CHOOSING TO GO FOR LOWER SUBSCRIPTION RATHER THAN ENDING-------------------------//
	
	
	
	@Test(enabled=true, priority=11, groups={"ConfirmationsRuleMailerPositive" , "All"})
	public void churnPayingMemberTC100602()
	{
		log.info("CHOOSING TO GO FOR LOWER SUBSCRIPTION RATHER THAN ENDING");
		
		un=Excel.getCellValue(INPUT_PATH, "ChurnPayingMember", 1, 0);    //using the member_paying email		
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
		
		 try
		   {
			  
			   if(text.contains("PREMIUM") || text.contains("STANDARD"))
			   {
				   log.info("Subscription is " +subs);
			   
				   log.info("Clicking on End Subscription button");
				   MyAccountPage acc=new MyAccountPage(driver);
				   acc.clickToEndSubscription();

				   log.info("Clicking Ja Tack Button to go for Lower Subscription");
				   EndSubscriptionPage end=new EndSubscriptionPage(driver);
				   end.clickToGoForLowerSubs();
				   end.clickToAcceptForBas();
				   
				   Excel.shiftingRowsUp(INPUT_PATH, "ChurnPayingMember", 1);
				   System.out.println("text: "+text);
				  
				   
				   if(text.contains("PREMIUM PLUS"))
				   {
					   String act2= driver.findElement(By.xpath("//h3[@class='category-h1 dynamic' and contains(text(),'Du har abonnemanget')]")).getText();
					   String actTrim2= act2.replaceAll("\\s+", "");
					   String exp2= "Duharabonnemanget:PREMIUMPLUS";
					   
					   Assert.assertEquals(actTrim2, exp2);
					   
					   log.info("Subscription Till Next RunDate: " +act2);
				   }
				   
				   else if(text.contains("STANDARD"))
				   {
					   String act1= driver.findElement(By.xpath("//h3[@class='category-h1 dynamic' and contains(text(),'Du har abonnemanget')]")).getText();
					   String exp1= "Duharabonnemanget:STANDARD";
					   String actTrim1= act1.replaceAll("\\s+","");
					   
					   Assert.assertEquals(actTrim1, exp1);
					   
					   log.info("Subscription Till Next RunDate: " +act1);
				   }
				   
				   else if(text.contains("PREMIUM"))
				   {
					   String act1= driver.findElement(By.xpath("//h3[@class='category-h1 dynamic' and contains(text(),'Du har abonnemanget')]")).getText();
					   String exp1= "Duharabonnemanget:PREMIUM";
					   String actTrim1= act1.replaceAll("\\s+","");
					  
					   Assert.assertEquals(actTrim1, exp1);
					   
					   log.info("Subscription Till Next RunDate: " +act1);
				   }
				   
				  
				   String customerid= Database.executeQuery("select customerid from customerinfo where email='" +un+ "'"); 
				   String runDate= Database.executeQuery("select next_subscription_run_date from customer2subscriptionmap where customerid=" +customerid);
				   
				
						String exp= runDate;
						String act=driver.findElement(By.xpath("//h5[contains(text(),'Nästa betalning')]/../..//li[@class='left']")).getText();
						Assert.assertEquals(act, exp);	
						log.info("Expected date for Next Payment is : " +exp);
						log.info("Actual Next Payment date is : "+act);
						
						String expSub= "BAS:99kr/månad";
						String actSub=driver.findElement(By.xpath("//h5[contains(text(),'Nästa betalning')]/../..//li[@class='right']")).getText();
						String actSubTrim= actSub.replaceAll("\\s+", "");
						Assert.assertEquals(actSubTrim, expSub);
						
						log.info("Next Subscription will be : " +actSub);
					
				   
				   WebElement ele1=driver.findElement(By.xpath("//button[@class='blueButton flex nedgradering popupLink']"));
				   WebElement ele2=driver.findElement(By.xpath("//form[@action='/konto/vill-to-end-member-konto']/button[@class='btn btn-default greyButton button big']"));
				   if(ele1.isEnabled() && ele2.isEnabled())
				   {
					   log.info("Cancel Downgrade button is Clickable");
					   log.info("End Subscription button is Clickable");
				   }
				   else
				   {
					   log.info("Cancel Downgrade button or End Subscription button is not clickable");
				   }
			   
			   
			   }
			   
			   else if(text.contains("BAS"))
			   {
				   	log.info("Subscription is BAS");
				   
				   	log.info("Clicking on End Subscription button");
				   	MyAccountPage acc=new MyAccountPage(driver);
				   	acc.clickToEndSubscription();
				   
				   	log.info("Feedback Page");
					EndSubsFeedbackPage feed=new EndSubsFeedbackPage(driver);
					feed.clickFeedbackDropdown();
					feed.selectNoTimeToUse();
					feed.clickToEndButton();
					feed.clickClearButton();
					log.info("Subscription Ended");
					
					home.clickNextoryLogo();
					home.clickAccountLink();
					
					Excel.shiftingRowsUp(INPUT_PATH, "ChurnPayingMember", 1);
					
					String act1=driver.findElement(By.xpath("//h3[contains(text(),'konto är avslutat')]")).getText();
					String actTrim1= act1.replaceAll("\\s+", "");
					String exp1= "BASkontoäravslutat";
					
					Assert.assertEquals(actTrim1, exp1);
					log.info(act1);
					
					String customerid= Database.executeQuery("select customerid from customerinfo where email='" +un+ "'"); 
					String runDate= Database.executeQuery("select next_subscription_run_date from customer2subscriptionmap where customerid=" +customerid);
					
						String exp= runDate;
						String act=driver.findElement(By.xpath("//h5[contains(text(),'konto är giltigt')]/../..//li[@class='left']")).getText();
						Assert.assertEquals(act, exp);	
						log.info("Expected date for Account access is until : " +exp);
						log.info("Your Default account is valid until : "+act);
			
					WebElement elem = driver.findElement(By.xpath("//button[contains(text(),'Aktivera')]"));
					if(elem.isEnabled())
					{
						log.info("Aktivera button is enabled and clickable");
					}
					else
					{
						log.info("Aktivera button is not enabled");
					}
					
					new WebDriverWait(driver,30).until(ExpectedConditions.titleContains("Ljudböcker & E-böcker - Ladda ner Gratis Ljudbok/E-bok Online"));
					
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
					
						Assert.assertEquals(memberStatus, "MEMBER_PAYING_CANCELLED");
						log.info("Membership Status is: " +memberStatus + " in Admin Site");
						
						if(subs.equalsIgnoreCase("BAS"))
						{
							subs= "BASE";
						}
						
						if(subs.equalsIgnoreCase("PREMIUM PLUS"))
						{
							subs="PREMIUM";
						}
						
						Assert.assertEquals(subsType, subs);
						log.info("Subscription Type is: " +subsType+ " in Admin Site");
					
						admin.clickLogout();
						driver.get(url);
			   }
		   
		   }
		   
		 	catch(Exception e)
		   {
			   e.printStackTrace();
		   }
		
			home.clickNextoryLogo();
			home.clickAccountLink();
			
			log.info("logging out");
			MyAccountPage account=new MyAccountPage(driver);
			account.clickLogOut();
	
	}
	
//---------------------------------------------- NEGATIVE FLOWS ----------------------------------------------//	
	
	@Test(enabled=true, priority=100, groups={"ConfirmationsRuleMailerNegative" , "All"})
	public void churnPayingMemberNoSelection()
	{
		log.info("NOT SELECTING THE REASON FROM DROPDOWN");
		
		un=Excel.getCellValue(INPUT_PATH, "ChurnPayingMember", 1, 0);    //  <-------------- using the member_paying email		
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
		
	   try
	   {
		   
		   if(text.contains("PREMIUM") || text.contains("STANDARD"))
		   {
		   
		   log.info("Subscription is " +subs);
		   
		   log.info("Clicking on End Subscription button");
		   MyAccountPage acc=new MyAccountPage(driver);
		   acc.clickToEndSubscription();
		   
		   EndSubscriptionPage end=new EndSubscriptionPage(driver);
		   end.clickToConfirmEndSubscription();
		   }
		   
		   else
		   {
			   log.info("Subscription is BAS");
			   
			   log.info("Clicking on End Subscription button");
			   MyAccountPage acc=new MyAccountPage(driver);
			   acc.clickToEndSubscription();
		   }
		   
	   }
	  
	   catch(Exception e)
	   {
		   e.printStackTrace();
		  
	   }
	
	   
		log.info("Feedback Page");
		EndSubsFeedbackPage feed=new EndSubsFeedbackPage(driver);
		
		feed.clickToEndButton();
		//feed.clickClearButton();
		
		Alert a1 = driver.switchTo().alert();
		String alertText=a1.getText();
		if(alertText.equalsIgnoreCase("Välj ett alternativ"))
		{
			log.info("Alert box: " + alertText);
		}
		
		else 
		{
			log.info("No Alert Box found for no selection");
		}
		
		a1.accept();
		feed.clickToUndoButton();
		
//		log.info("Subscription Ended");
		home.clickNextoryLogo();
		home.clickAccountLink();
		
		//Excel.shiftingRowsUp(INPUT_PATH, "ChurnPayingMember", 1);
		log.info("logging out");
		MyAccountPage account=new MyAccountPage(driver);
		account.clickLogOut();
		
	}
	

}
