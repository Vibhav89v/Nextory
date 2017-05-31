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

public class SecondOutreach extends SuperTestScript
{
	MongoDBUtilMorphia mongoutil = new MongoDBUtilMorphia();
	Datastore ds = mongoutil.getMorphiaDatastoreForNlob();
	Email email=new Email();
	
	public SecondOutreach()
	{
		loginRequired=false;
		logoutRequired=false;
	}
	
   @Test(enabled=true,priority=1, groups={"AntiChurnRuleMailer" , "All"})
   public void secondOutreachTC110201()
   {
	   log.info("Running second Outreach Anti churn");
	   
	   log.info("Fetching data from databse MySQL");
		  InformationFromBackend info=new InformationFromBackend();
		  info.getDataForCustomerInfo("SecondOutreach");  
	
		String customerid =  InformationFromBackend.result;
		log.info("Customerid selected from Sql is="+customerid);
		
		log.info("Batch Execution");
		driver.get("http://130.211.74.42:8082/nextory_batch/jobs/visitors-tobecome-members-second-outreach");
	
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
				String expectedSubject="Prova nu – gratis i 30 dagar" ;	
		        String actualSubject=email.getSubject();
		        Assert.assertEquals(actualSubject, expectedSubject);
				log.info("Subject verified successfully");
		     
		   	    log.info("Response verification");
		   	    String expectedResponse="Success";
				String actualResponse=email.getReason();
				Assert.assertEquals(actualResponse,expectedResponse);
				log.info("Response is verified successfully");
				
			log.info("Triggered message verification");
			 String expectedTriggerName="Visitors: Second outreach" ;	
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
		
	   /*
	     log.info("Fetching data from databse");
		 Api a=new Api();
		 a.fetchData();                  //CustometerInfo
		 a.clickSecondOutreach();
		 */
   }
}
