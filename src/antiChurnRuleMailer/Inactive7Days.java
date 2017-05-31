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

public class Inactive7Days extends SuperTestScript 
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
     Email email=new Email();
		
	 public Inactive7Days()
	 {
		loginRequired=false;
		logoutRequired=false;
	 }
	 
   @Test
   public void inactive7DaysTC110301()
   {
	     log.info("In Inactive 7 days Script");
	     
	     
		 log.info("Fetching data from databse MySQL");
		  InformationFromBackend info=new InformationFromBackend();
		  info.getDataForCustomerInfo("Inactive7Days");  
	
		String customerid =  InformationFromBackend.result;
		log.info("Customerid selected from Sql is="+customerid);
		
		log.info("Batch Execution");
		driver.get("http://130.211.74.42:8082/nextory_batch/jobs/antichurn-inactive-sevendays");
	
		 log.info("Fetching data from MongoDB");
		 
		Query query=ds.createQuery(Email.class);
		query.filter("customerid",customerid);
		/*query1.field("from").equals("kundservice@nextory.se");*/
		//query.field("customerid").equals("");
		/*query1.field("mobilenumber").equals("");
		query1.field("firstname").equals("");
		query1.field("lastname").equals("");
		query1.field("to").equals("");
		query1.field("subject").equals("");
		query1.field("mailsentdate").equals("");
		query1.field("reason").equals("");*/
		try
		{
			//custList = (Membership) query.get();
		     // if(custList != null)
			//Email emailList=(Email) query1.get();
			ArrayList<Email> emailList=(ArrayList<Email>)query.asList();
			
			 System.out.println("Email list="+emailList);
			 for(Email email:emailList)
			 {
				System.out.println("class="+email.getClass());
				System.out.println( "CustomerID=" + customerid );
				System.out.println("Firstname="+email.getFirstname());
				email.getLastname();
				email.getFrom();
				email.getMobilenumber();
				System.out.println(email.getReason());
				System.out.println(email.getTriggerName());
				System.out.println(email.getSubject());
				
				log.info("Mail-Subject verification");
				String expectedSubject="Beh�ver du hj�lp att hitta en fantastisk bok" ;	
		        String actualSubject=email.getSubject();
		        Assert.assertEquals(actualSubject, expectedSubject);
				log.info("Subject verified successfully");
		     
		   	    log.info("Response verification");
		   	    String expectedResponse="Success";
				String actualResponse=email.getReason();
				Assert.assertEquals(actualResponse,expectedResponse);
				log.info("Response is verified successfully");
				
			log.info("Triggered message verification");
			 String expectedTriggerName="Anti-churn: Inactive 7 days" ;	
		     String actualTriggerName=email.getTriggerName();
		    Assert.assertEquals(actualTriggerName, expectedTriggerName);
			log.info("Message Triggered successfully"); 
			
			log.info("Mail sent Date verification");
			String expectedupdateddate=AddDate.currentDate();
		   	Date actualupdateddate=email.getMailsentdate();
		   	Assert.assertEquals(actualupdateddate, expectedupdateddate);
		   	log.info("Mail sent date verified successfully");
		   	 
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
		 
   }
}
