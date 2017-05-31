package customerEducationRuleMailer;

import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.testng.Assert;
import org.testng.annotations.Test;

import common.SuperTestScript;
import generics.MongoDBUtilMorphia;
import interfaceForApiOrDB.InformationFromBackend;
import mongo.Email_logs.Email;
import mongo.Membership_change_log.Membership;

public class OfflineAndSleep extends SuperTestScript
{
	public static String newEmail;
	 public static String confirm;
	 public static String newPwd;
	 public static String cardNumber;
	 public static String cvc;
	 public static String fn;
	 public static String ln;
	 public static String cellNum;
	 
	 //private SoftAssert softAssert=new SoftAssert();
	 MongoDBUtilMorphia mongoutil = new MongoDBUtilMorphia();
	 Datastore ds = mongoutil.getMorphiaDatastoreForNlob();
     Membership member=new Membership();
     Email email=new Email();
     
     InformationFromBackend info=new InformationFromBackend();
     
	 String expectedSubject="";
	 String actualSubject="";
	 String result="";
	 
//	 String expectedEmail="";
//	 String actualEmail="";
	 
	 Date expectedupdateddate;
	 Date actualupdateddate;
	 
	 
		public OfflineAndSleep()
		{
			loginRequired=false;
			logoutRequired=false;
		}
		
		@Test(enabled=true, priority=12, groups={"CustomerEducationRuleMail", "All"})
		public void offlineAndSleepTC090401() throws InterruptedException
		  {
			log.info("Inside Offline and sleep Free Trial script");
			
			
			  log.info("Fetching data from databse MySQL");
			  result=info.getDataForCustomerInfo("OfflineAndSleep"); 
			  log.info("mailsent_once : " + result);
			
			 log.info("Fetching data from databse Mongo");
			String customerid =InformationFromBackend.result;
			 Membership custList = null;
			 log.info("Customerid="+customerid);
			 try
			 {
				//--------------------------------------------nx_membership_change_log---------------------------------
				 Query query = ds.createQuery(Membership.class);	
				  query.criteria("mem_type_code_old").exists();
				  query.criteria("mem_type_code_old").notEqual(null);
				  query. where("this.mem_type_code_old != this.mem_type_code_new");
			         	     
			    
				/*  query.and(new Criteria []{ ds.createQuery(Membership.class).criteria("customerid").equal(customerid),
			    		  ds.createQuery(Membership.class).criteria("mem_type_code_old").exists(),
			    		  ds.createQuery(Membership.class).criteria("mem_type_code_old").notEqual(null),
			    		  ds.createQuery(Membership.class).criteria("mem_type_code_old") .notEqual("mem_type_code_new")}); */
				  query.order("-_id");
			      query.limit(1);
			           
			     custList = (Membership)query.get();   
			     if(custList != null)
			      {
			    	 log.info("Id is="+custList.getId());
			    	  log.info("Mem type code old="+custList.getMem_type_code_old());
			    	  log.info("Mem type code new="+custList.getMem_type_code_new());
			    	  log.info("Customer id ="+custList.getCustomerid());
			      }
			     log.info( custList.getMem_type_code_previous());
			     Date latestdate = null;            		    
			     } 
			     catch(Exception e)
			     {
			    	e.printStackTrace();
			     } 	
			 
			    DateTime d = new DateTime( custList.getUpdateddate());
			    Date sevenday = d.minusDays( 7 ).toDate();
			    
				 //db.nx_membership_change_log.update({"_id":17},{$set : {"updateddate" : -7day }})
				UpdateOperations<Membership> ops = ds.createUpdateOperations(Membership.class).set("updateddate", sevenday);
				ds.update(ds.createQuery(Membership.class).field("customerid").equal( customerid ), ops,true);
				//log.info("Updated date....."+sevenday);
					
				//-------------------------------------------nx_customer_email_logs--------------------------------------
					Query query1=ds.createQuery(Email.class);
					query1.filter("customerid",customerid);
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
						ArrayList<Email> emailList=(ArrayList<Email>)query1.asList();
						
						   
						 for(Email email:emailList)
						 {
							//email.getClass();
							log.info( "CustomerID....." + customerid );
							email.getFirstname();
							email.getLastname();
							email.getFrom();
							email.getMobilenumber();
							email.getReason();
							email.getTriggerName();
						
						    expectedSubject="Läsa, resa, sova" ;	
							actualSubject=email.getSubject();
							
							log.info("Email......"+email.getTo());
//							String expectedEmail = "michaela.lilja@hotmail.com";
//							String actualEmail = email.getTo() ;
							expectedupdateddate=member.getUpdateddate();
					   	    actualupdateddate=email.getMailsentdate();
							email.getMailsentdate();
							
							 String expectedResponse="Success";
							  String actualResponse=email.getReason();
								Assert.assertEquals(actualResponse,expectedResponse);
								
							   String expectedTriggerName="Education 4 - Offline and sleep" ;	
						       String actualTriggerName=email.getTriggerName();
						     
						    log.info("Triggered message verification");
						    Assert.assertEquals(actualTriggerName, expectedTriggerName);
							log.info("Message Triggered successfully"); 
							
							log.info("Mail sent Date verification");
						     expectedupdateddate=member.getUpdateddate();
						   	 actualupdateddate=email.getMailsentdate();
						   	 Assert.assertEquals(actualupdateddate, expectedupdateddate);
						   	 
						   	 log.info("Response verification");
						   	 Assert.assertEquals(actualResponse, expectedResponse);
						   	 log.info("Response is success");
						   	 
						   	log.info("Mail-Subject verification");
							Assert.assertEquals(actualSubject, expectedSubject);
							log.info("Subject verified successfully");
						 }
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}	
					
					 log.info("Batch Execution");
					  driver.get("http://130.211.74.42:8082/nextory_batch/jobs/job-tip4-offline-and-sleep");
					  
			
		  }
		
		@Test(enabled=true, priority=13, groups={"CustomerEducationRuleMail", "All"})
		public void offlineAndSleepFreeGiftCard() throws InterruptedException
		{
			log.info("In Basics FreeGiftCardMember Script");
			
		}
		
		@Test(enabled=true, priority=14, groups={"CustomerEducationRuleMail", "All"})
		public void offlineAndSleepFreeCampaign() throws InterruptedException
		{
			log.info("Clicking on Kampanjkod link");
		
		}
	}	 
