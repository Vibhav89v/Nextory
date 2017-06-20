package postRegistrationRuleMailer;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.testng.Assert;
import org.testng.annotations.Test;

import common.SuperTestScript;
import generics.AddDate;
import generics.MongoDBUtilMorphia;
import interfaceForApiOrDB.InformationFromBackend;
import mongo.Email_logs.Email;
import mongo.Membership_change_log.Membership;

public class SurpriseDelight extends SuperTestScript
{
	public static String newEmail;
	public static String confirm;
	public static String newPwd;
	public static String cardNumber;
	public static String cvc;
	public static String fn;
	public static String ln;
	public static String cellNum;
	public static String dbUrl;
	public static String username;
	public static String password;
	public static String query;
	static Logger log = Logger.getLogger(SurpriseDelight.class);
	//private SoftAssert softAssert=new SoftAssert();
	MongoDBUtilMorphia mongoutil = new MongoDBUtilMorphia();
	Datastore ds = mongoutil.getMorphiaDatastoreForNlob();
    Membership member=new Membership();
    Email email=new Email();
	String result="";
	 
	String expectedupdateddate;
	Date actualupdateddate;
	 
	public SurpriseDelight()
	{
		loginRequired=false;
		logoutRequired=false;
	}
	
	@Test(enabled=true, priority=4, groups={"PostRegistrationRuleMailerPositive" , "All"})
	public void SurpriseDelightTC080201() throws InterruptedException
	{
		
		log.info("Running Surprise and Delight");

		log.info("Fetching data from databse MySQL");
		InformationFromBackend info=new InformationFromBackend();
		info.getDataForCustomerInfo("SurpriseDelight");                  //CustometerInfo and orders
		
		log.info("Batch execution");
		driver.get("http://130.211.74.42:8082/nextory_batch/jobs/surprise-delight-user");
		
		log.info("Fetching data from MongoDB");
		Membership custList = null;
		String customerid = InformationFromBackend.result;
		
		log.info("customerid"+customerid);
		//-------------------------------------------nx_customer_email_logs--------------------------------------
	      		// db.nx_customer_email_logs.find({"customerid" :$a}).sort({"_id":-1}).limit(1).pretty();
		    Query query1=ds.createQuery(Email.class);
			query1.filter("customerid", customerid);
			query1.order("-_id");
			query1.limit(1);
			
			log.info("Inside Email.class");
			
			try{
				
				log.info("entering inside try block");
				
				ArrayList<Email> emailList=(ArrayList<Email>)query1.asList();
				System.out.println(emailList);
				log.info("In array");
				   
				for(Email email:emailList)
				{
					log.info("in for loop");
					email.getClass();
					System.out.println("class="+email.getClass());
					System.out.println( "CustomerID=" + customerid);
					email.getFirstname();
					System.out.println("Firstname="+email.getFirstname());
					email.getLastname();
					email.getFrom();
					System.out.println("Subject="+email.getSubject());
					email.getMobilenumber();
					System.out.println("Triggered message="+email.getTriggerName());
					
					log.info("Mail-Subject verification");
					String expectedSubject="Beh�ver du n�gon hj�lp att komma ig�ng?";
					String actualSubject=email.getSubject();
					Assert.assertEquals(actualSubject, expectedSubject);
					log.info("Subject verified successfully");
					
					 log.info("Response verification");
					String expectedResponse="Success";
					String actualResponse=email.getReason();
					Assert.assertEquals(actualResponse,expectedResponse);
					log.info("Response is success");
					
				 log.info("Triggered message verification");
				  String expectedTriggerName="Post-reg 2: Surprise & delight" ;	
			      String actualTriggerName=email.getTriggerName();
			    Assert.assertEquals(actualTriggerName, expectedTriggerName);
				log.info("Message Triggered successfully"); 
				
			  log.info("Mail sent Date verification");
		      expectedupdateddate=AddDate.currentDate();
			  actualupdateddate=email.getMailsentdate();
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
