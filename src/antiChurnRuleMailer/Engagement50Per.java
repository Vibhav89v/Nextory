package antiChurnRuleMailer;

import java.util.ArrayList;
import java.util.Date;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.testng.Assert;
import org.testng.annotations.Test;

import common.SuperTestScript;
import generics.AddDate;
import generics.MongoDBUtilMorphia;
import interfaceForApiOrDB.InformationFromBackend;
import mongo.Email_logs.Email;
import mongo.Library.CustomerLibrary;

public class Engagement50Per extends SuperTestScript 
{
	public static String newEmail;
	public static String confirm;
	public static String newPwd;
	public static String cardNumber;
	public static String cvc;
	public static String fn;
	public static String ln;
	public static String cellNum;

	MongoDBUtilMorphia mongoutil = new MongoDBUtilMorphia();
	Datastore ds = mongoutil.getMorphiaDatastoreForNlob();
	Email email = new Email();

	public Engagement50Per()
	{
		loginRequired = false;
		logoutRequired = false;
	}

	@Test(enabled = true, priority = 1, groups = {})
	public void Engagement50PerTC110501() 
	{
		log.info("In Engagement 50 % Script");
				log.info("Fetching data from databse MySQL");
		InformationFromBackend info = new InformationFromBackend();
		info.getDataForCustomerInfo("Engagement50Per");

		String customerid = InformationFromBackend.result;
		log.info("Customerid selected from Sql is=" + customerid);
        //---------------mongo---------------------------------------
		log.info(" Dealing with Mongo query");
		Query query = ds.createQuery(CustomerLibrary.class);
	
		query.field("status").equalIgnoreCase("OFFLINE_READER");
		query.field("librarysyncmasterlog.percentage").greaterThanOrEq("0.5");
		query.field("librarysyncmasterlog.percentage").lessThan("0.9");
		query.order("createddate");
		query.retrievedFields(true, "status", "librarysyncmasterlog", "createddate");
		query.limit(2);
		try {
			ArrayList<CustomerLibrary> custList = (ArrayList<CustomerLibrary>) query.asList(); 

			for (CustomerLibrary library : custList) 
			{
				System.out.println("Status=" + library.getStatus());
				System.out.println("Sync master log=" + library.getLibrarysyncmasterlog());
				System.out.println("Created date=" + library.getCreateddate());
				// library
			}

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		log.info("Batch Execution");
		driver.get("http://130.211.74.42:8082/nextory_batch/jobs/buildrelationship-word-of-mouth");
		
		log.info("Checking the inbox");
		Query query1 = ds.createQuery(Email.class);
		query1.filter("customerid", customerid);
		
		try 
		{	
			ArrayList<Email> emailList = (ArrayList<Email>) query.asList();

			System.out.println("Email list=" + emailList);
			for (Email email : emailList) 
			{
				System.out.println("class=" + email.getClass());
				System.out.println("CustomerID=" + customerid);
				System.out.println("Firstname=" + email.getFirstname());
				email.getLastname();
				email.getFrom();
				email.getMobilenumber();
				System.out.println(email.getReason());
				System.out.println(email.getTriggerName());
				System.out.println(email.getSubject());

				log.info("Mail-Subject verification");
				String expectedSubject = "Vad tycker du om [Titel]?";
				String actualSubject = email.getSubject();
				Assert.assertEquals(actualSubject, expectedSubject);
				log.info("Subject verified successfully");

				log.info("Response verification");
				String expectedResponse = "Success";
				String actualResponse = email.getReason();
				Assert.assertEquals(actualResponse, expectedResponse);
				log.info("Response is verified successfully");

				log.info("Triggered message verification");
				String expectedTriggerName = "Engagement: 50% of book";
				String actualTriggerName = email.getTriggerName();
				Assert.assertEquals(actualTriggerName, expectedTriggerName);
				log.info("Message Triggered successfully");

				log.info("Mail sent Date verification");
				String expectedupdateddate = AddDate.currentDate();
				Date actualupdateddate = email.getMailsentdate();
				Assert.assertEquals(actualupdateddate, expectedupdateddate);
				log.info("Mail sent date verified successfully");

			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}
}
