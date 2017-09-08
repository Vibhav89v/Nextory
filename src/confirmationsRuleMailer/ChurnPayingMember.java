package confirmationsRuleMailer;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

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
		loginRequired = false;
		logoutRequired = false;
	}

	SoftAssert soft = new SoftAssert();
	// -----------------------------------------ENDING THE
	// SUBSCRIPTION---------------------------------------------//

	@Test(enabled = true, priority = 51, groups = { "ConfirmationsRuleMailerPositive", "All" })
	public void churnPayingMemberTC100601() 
	{
		log.info("ENDING THE SUBSCRIPTION");

		un = Excel.getCellValue(INPUT_PATH, "ChurnPayingMember", 1, 0); // <--------------
																		// using
																		// the
																		// member_paying
																		// email
		pwd = PasswordFromAdmin.gettingPasswordFromAdmin(un);
		adminUn = Property.getPropertyValue(CONFIG_PATH + CONFIG_FILE, "ADMINUN");
		adminPwd = Property.getPropertyValue(CONFIG_PATH + CONFIG_FILE, "ADMINPWD");
		adminUrl = Property.getPropertyValue(CONFIG_PATH + CONFIG_FILE, "ADMINURL");

		log.info("Clicking on Login Button");
		HomePage home = new HomePage(driver);
		home.clickLoginLink();
		log.info("Navigating to Login Page");

		log.info("Entering login details with username as : '" + un + "' and password as : '" + pwd + "'");
		LoginPage login = new LoginPage(driver);
		login.setEmailId(un);
		login.setPassword(pwd);
		login.clickLoginButton();
		log.info("Navigating to My Account Page");

		MyAccountPage acc = new MyAccountPage(driver);

		String text = acc.getMySubscription();
		log.info(text);
		String subs = text.substring(22);

		try 
		{
			if (text.contains("FAMILJ") || text.contains("GULD")) 
			{
				log.info("Subscription is " + subs);

				log.info("Clicking on End Subscription button");

				acc.clickToEndSubscription();

				log.info("Confirming");
				String actual = driver.findElement(By.xpath("//p[@class='OmUnlimited-p']")).getText();
				String expected = "betala bara 99 kr/m�n?";

				Assert.assertTrue(actual.contains(expected));
				log.info("TIP: " + expected);

				EndSubscriptionPage end = new EndSubscriptionPage(driver);
				end.clickToConfirmEndSubscription();
			}

			else 
			{
				log.info("Subscription is SILVER");
				log.info("Clicking on End Subscription button");
				acc.clickToEndSubscription();
			}

		}

		catch (Exception e) 
		{
			e.printStackTrace();
		}

		log.info("Feedback Page");
		EndSubsFeedbackPage feed = new EndSubsFeedbackPage(driver);
		feed.clickFeedbackDropdown();
		feed.selectNoTimeToUse();
		feed.clickToEndButton();
		feed.clickClearButton();

		log.info("Subscription Ended");
		home.clickNextoryLogo();
		home.clickAccountLink();

		Excel.shiftingRowsUp(INPUT_PATH, "ChurnPayingMember", 1);

		if (text.contains("SILVER")) 
		{
			String act1 = driver.findElement(By.xpath("//h3[contains(text(),'kontot �r avslutat')]")).getText();
			String actTrim1 = act1.replaceAll("\\s+", "");
			String exp1 = "SILVERkontot�ravslutat";

			Assert.assertEquals(actTrim1, exp1);
			log.info(act1);
		}

		else if (text.contains("GULD")) 
		{
			String act1 = driver.findElement(By.xpath("//h3[contains(text(),'kontot �r avslutat')]")).getText();
			String actTrim1 = act1.replaceAll("\\s+", "");
			String exp1 = "GULDkontot�ravslutat";

			Assert.assertEquals(actTrim1, exp1);
			log.info(act1);
		}

		else if (text.contains("FAMILJ")) 
		{
			String act1 = driver.findElement(By.xpath("//h3[contains(text(),'kontot �r avslutat')]")).getText();
			String actTrim1 = act1.replaceAll("\\s+", "");
			String exp1 = "FAMILJkontot�ravslutat";

			Assert.assertEquals(actTrim1, exp1);
			log.info(act1);
		}

		String customerid = Database.executeQuery("select customerid from customerinfo where email='" + un + "'");
		String runDate = Database.executeQuery(
				"select next_subscription_run_date from customer2subscriptionmap where customerid=" + customerid);

		String exp = runDate;
		String act = driver.findElement(By.xpath("//h5[contains(text(),'kontot �r giltigt')]/../..//li[@class='left']"))
				.getText();
		Assert.assertEquals(act, exp);
		log.info("Expected date for Account access is until : " + exp);
		log.info("Your Default account is valid until : " + act);

		if (acc.activeraIsClickable())
		{
			log.info("Aktivera button is enabled and clickable");
		} 
		else 
		{
			log.info("Aktivera button is not enabled");
		}

		log.info("logging out");
		MyAccountPage account = new MyAccountPage(driver);
		account.clickLogOut();

		new WebDriverWait(driver, 30)
				.until(ExpectedConditions.titleContains("Ljudb�cker & E-b�cker - Lyssna & l�s gratis i mobilen"));

		driver.manage().deleteAllCookies();
		driver.get(adminUrl);

		AdminPage admin = new AdminPage(driver);
		admin.setUserName(adminUn);
		admin.setPassword(adminPwd);
		admin.clickLogin();
		admin.clickCustMgmt();
		admin.setEPost(un);
		admin.clickSearch();
		String memberStatus = admin.getMemberType();
		String subsType = admin.getSubsType();

		Assert.assertEquals(memberStatus, "MEMBER_PAYING_CANCELLED");
		log.info("Membership Status is: " + memberStatus + " in Admin Site");

		Assert.assertEquals(subsType, subs);
		log.info("Subscription Type is: " + subsType + " in Admin Site");

		admin.clickLogout();
		driver.get(url);

	}

	// --------------------------------CHOOSING TO GO FOR LOWER SUBSCRIPTION
	// RATHER THAN ENDING-------------------------//

	@Test(enabled = true, priority = 52, groups = { "ConfirmationsRuleMailerPositive", "All" })
	public void churnPayingMemberTC100602() {
		log.info("CHOOSING TO GO FOR LOWER SUBSCRIPTION RATHER THAN ENDING");

		un = Excel.getCellValue(INPUT_PATH, "ChurnPayingMember", 1, 0); // using
																		// the
																		// member_paying
																		// email
		pwd = PasswordFromAdmin.gettingPasswordFromAdmin(un);
		adminUn = Property.getPropertyValue(CONFIG_PATH + CONFIG_FILE, "ADMINUN");
		adminPwd = Property.getPropertyValue(CONFIG_PATH + CONFIG_FILE, "ADMINPWD");
		adminUrl = Property.getPropertyValue(CONFIG_PATH + CONFIG_FILE, "ADMINURL");

		log.info("Clicking on Login Button");
		HomePage home = new HomePage(driver);
		home.clickLoginLink();
		log.info("Navigating to Login Page");

		log.info("Entering login details with username as : '" + un + "' and password as : '" + pwd + "'");
		LoginPage login = new LoginPage(driver);
		login.setEmailId(un);
		login.setPassword(pwd);
		login.clickLoginButton();
		log.info("Navigating to My Account Page");

		MyAccountPage acc = new MyAccountPage(driver);

		String text = acc.getMySubscription();
		log.info(text);
		String subs = text.substring(22);

		try {

			if (text.contains("FAMILJ") || text.contains("GULD"))
			{
				log.info("Subscription is " + subs);

				log.info("Clicking on End Subscription button");

				acc.clickToEndSubscription();

				log.info("Clicking Ja Tack Button to go for Lower Subscription");
				EndSubscriptionPage end = new EndSubscriptionPage(driver);
				end.clickToGoForLowerSubs();
				end.clickToAcceptForBas();

				Excel.shiftingRowsUp(INPUT_PATH, "ChurnPayingMember", 1);
				System.out.println("text: " + text);

				if (text.contains("GULD")) 
				{
					String act1 = acc.getMySubscription();
					String exp1 = "Duharabonnemanget:GULD";
					String actTrim1 = act1.replaceAll("\\s+", "");

					Assert.assertEquals(actTrim1, exp1);

					log.info("Subscription Till Next RunDate: " + act1);
				}

				else if (text.contains("FAMILJ")) 
				{
					String act1 = acc.getMySubscription();
					String exp1 = "Duharabonnemanget:FAMILJ";
					String actTrim1 = act1.replaceAll("\\s+", "");

					Assert.assertEquals(actTrim1, exp1);

					log.info("Subscription Till Next RunDate: " + act1);
				}

				String customerid = Database
						.executeQuery("select customerid from customerinfo where email='" + un + "'");
				String runDate = Database.executeQuery(
						"select next_subscription_run_date from customer2subscriptionmap where customerid="
								+ customerid);

				String exp = runDate;
				String act = acc.getRunDate();
				Assert.assertEquals(act, exp);
				log.info("Expected date for Next Payment is : " + exp);
				log.info("Actual Next Payment date is : " + act);

				String expSub = "SILVER:119kr/m�nad";
				String actSub = acc.getMyOrder();
				String actSubTrim = actSub.replaceAll("\\s+", "");
				Assert.assertEquals(actSubTrim, expSub);

				log.info("Next Subscription will be : " + actSub);

				if (acc.avslutaAbonnemangIsClickable() && acc.avbrytIsClickable()) 
				{
					log.info("Cancel Downgrade button is Clickable");
					log.info("End Subscription button is Clickable");
				} 
				else 
				{
					log.info("Cancel Downgrade button or End Subscription button is not clickable");
				}

			}

			else if (text.contains("SILVER")) 
			{
				log.info("Subscription is SILVER");

				log.info("Clicking on End Subscription button");

				acc.clickToEndSubscription();

				log.info("Feedback Page");
				EndSubsFeedbackPage feed = new EndSubsFeedbackPage(driver);
				feed.clickFeedbackDropdown();
				feed.selectNoTimeToUse();
				feed.clickToEndButton();
				feed.clickClearButton();
				log.info("Subscription Ended");

				home.clickNextoryLogo();
				home.clickAccountLink();

				Excel.shiftingRowsUp(INPUT_PATH, "ChurnPayingMember", 1);

				String act1 = driver.findElement(By.xpath("//h3[contains(text(),'kontot �r avslutat')]")).getText();
				String actTrim1 = act1.replaceAll("\\s+", "");
				String exp1 = "SILVERkontot�ravslutat";

				Assert.assertEquals(actTrim1, exp1);
				log.info(act1);

				String customerid = Database
						.executeQuery("select customerid from customerinfo where email='" + un + "'");
				String runDate = Database.executeQuery(
						"select next_subscription_run_date from customer2subscriptionmap where customerid="
								+ customerid);

				String exp = runDate;
				String act = driver
						.findElement(By.xpath("//h5[contains(text(),'kontot �r giltigt')]/../..//li[@class='left']"))
						.getText();
				Assert.assertEquals(act, exp);
				log.info("Expected date for Account access is until : " + exp);
				log.info("Your Default account is valid until : " + act);

				if (acc.activeraIsClickable()) {
					log.info("Aktivera button is enabled and clickable");
				} else {
					log.info("Aktivera button is not enabled");
				}

				home.clickNextoryLogo();
				home.clickAccountLink();

				log.info("logging out");
				MyAccountPage account = new MyAccountPage(driver);
				account.clickLogOut();

				new WebDriverWait(driver, 30).until(
						ExpectedConditions.titleContains("Ljudb�cker & E-b�cker - Lyssna & l�s gratis i mobilen"));

				driver.manage().deleteAllCookies();
				driver.get(adminUrl);

				AdminPage admin = new AdminPage(driver);
				admin.setUserName(adminUn);
				admin.setPassword(adminPwd);
				admin.clickLogin();
				admin.clickCustMgmt();
				admin.setEPost(un);
				admin.clickSearch();
				String memberStatus = admin.getMemberType();
				String subsType = admin.getSubsType();

				Assert.assertEquals(memberStatus, "MEMBER_PAYING_CANCELLED");
				log.info("Membership Status is: " + memberStatus + " in Admin Site");

				Assert.assertEquals(subsType, subs);
				log.info("Subscription Type is: " + subsType + " in Admin Site");

				admin.clickLogout();
				driver.get(url);
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ---------------------------------------------- NEGATIVE FLOWS
	// ----------------------------------------------//

	@Test(enabled = true, priority = 53, groups = { "ConfirmationsRuleMailerNegative", "All" })
	public void churnPayingMemberNoSelection() {
		log.info("NOT SELECTING THE REASON FROM DROPDOWN");

		un = Excel.getCellValue(INPUT_PATH, "ChurnPayingMember", 1, 0); // <--------------
																		// using
																		// the
																		// member_paying
																		// email
		pwd = PasswordFromAdmin.gettingPasswordFromAdmin(un);
		adminUn = Property.getPropertyValue(CONFIG_PATH + CONFIG_FILE, "ADMINUN");
		adminPwd = Property.getPropertyValue(CONFIG_PATH + CONFIG_FILE, "ADMINPWD");
		adminUrl = Property.getPropertyValue(CONFIG_PATH + CONFIG_FILE, "ADMINURL");

		log.info("Clicking on Login Button");
		HomePage home = new HomePage(driver);
		home.clickLoginLink();
		log.info("Navigating to Login Page");

		log.info("Entering login details with username as : '" + un + "' and password as : '" + pwd + "'");
		LoginPage login = new LoginPage(driver);
		login.setEmailId(un);
		login.setPassword(pwd);
		login.clickLoginButton();
		log.info("Navigating to My Account Page");

		MyAccountPage acc = new MyAccountPage(driver);

		String text = acc.getMySubscription();
		log.info(text);
		String subs = text.substring(22);

		acc.clickToEndSubscription();

		EndSubscriptionPage end = new EndSubscriptionPage(driver);

		try {

			if (text.contains("FAMILJ") || text.contains("GULD"))
			{

				log.info("Subscription is " + subs);

				log.info("Clicking on End Subscription button");

				end.clickNotToEndSubscription(); // goes back to Konto Page

				acc.clickToEndSubscription(); // navigates to End Subscription
												// Page
				end.clickToConfirmEndSubscription(); // navigates to Feedback
														// Dropdown Page
			}

			else 
			{
				log.info("Subscription is SILVER");
				log.info("Clicking on End Subscription button");
			}

		}

		catch (Exception e) 
		{
			e.printStackTrace();
		}

		log.info("Feedback Page");
		EndSubsFeedbackPage feed = new EndSubsFeedbackPage(driver);

		feed.clickToUndoButton();

		acc.clickToEndSubscription();

		if (text.contains("FAMILJ") || text.contains("GULD")) 
		{
			end.clickToConfirmEndSubscription();
		}

		feed.clickToEndButton();
		// feed.clickClearButton();

		Alert a1 = driver.switchTo().alert();
		String alertText = a1.getText();
		if (alertText.equalsIgnoreCase("V�lj ett alternativ")) 
		{
			log.info("Alert box: " + alertText);
		}

		else 
		{
			log.info("No Alert Box found for no selection");
		}

		a1.accept();

		feed.clickFeedbackDropdown();
		feed.selectNoTimeToUse();
		feed.clickToEndButton();
		feed.clickClearButton();

		log.info("Subscription Ended");
		home.clickNextoryLogo();
		home.clickAccountLink();

		Excel.shiftingRowsUp(INPUT_PATH, "ChurnPayingMember", 1);

		if (text.contains("SILVER")) 
		{
			String act1 = driver.findElement(By.xpath("//h3[contains(text(),'kontot �r avslutat')]")).getText();
			String actTrim1 = act1.replaceAll("\\s+", "");
			String exp1 = "SILVERkontot�ravslutat";

			Assert.assertEquals(actTrim1, exp1);
			log.info(act1);
		}

		else if (text.contains("GULD")) 
		{
			String act1 = driver.findElement(By.xpath("//h3[contains(text(),'kontot �r avslutat')]")).getText();
			String actTrim1 = act1.replaceAll("\\s+", "");
			String exp1 = "GULDkontot�ravslutat";

			Assert.assertEquals(actTrim1, exp1);
			log.info(act1);
		}

		else if (text.contains("FAMILJ")) 
		{
			String act1 = driver.findElement(By.xpath("//h3[contains(text(),'kontot �r avslutat')]")).getText();
			String actTrim1 = act1.replaceAll("\\s+", "");
			String exp1 = "FAMILJkontot�ravslutat";

			Assert.assertEquals(actTrim1, exp1);
			log.info(act1);
		}

		String customerid = Database.executeQuery("select customerid from customerinfo where email='" + un + "'");
		String runDate = Database.executeQuery(
				"select next_subscription_run_date from customer2subscriptionmap where customerid=" + customerid);

		String exp = runDate;
		String act = driver.findElement(By.xpath("//h5[contains(text(),'kontot �r giltigt')]/../..//li[@class='left']"))
				.getText();
		Assert.assertEquals(act, exp);
		log.info("Expected date for Account access is until : " + exp);
		log.info("Your Default account is valid until : " + act);

		if (acc.activeraIsClickable()) 
		{
			log.info("Aktivera button is enabled and clickable");
		}
		else 
		{
			log.info("Aktivera button is not enabled");
		}

		log.info("logging out");
		MyAccountPage account = new MyAccountPage(driver);
		account.clickLogOut();

		new WebDriverWait(driver, 30)
				.until(ExpectedConditions.titleContains("Ljudb�cker & E-b�cker - Lyssna & l�s gratis i mobilen"));

		driver.manage().deleteAllCookies();
		driver.get(adminUrl);

		AdminPage admin = new AdminPage(driver);
		admin.setUserName(adminUn);
		admin.setPassword(adminPwd);
		admin.clickLogin();
		admin.clickCustMgmt();
		admin.setEPost(un);
		admin.clickSearch();
		String memberStatus = admin.getMemberType();
		String subsType = admin.getSubsType();

		Assert.assertEquals(memberStatus, "MEMBER_PAYING_CANCELLED");
		log.info("Membership Status is: " + memberStatus + " in Admin Site");

		Assert.assertEquals(subsType, subs);
		log.info("Subscription Type is: " + subsType + " in Admin Site");

		admin.clickLogout();
		driver.get(url);

	}

}
