package PaymentIssues;

import java.util.ArrayList;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.testng.Assert;
import org.testng.annotations.Test;

import common.SuperTestScript;
import generics.MongoDBUtilMorphia;
import interfaceForApiOrDB.InformationFromBackend;
import mongo.Email_logs.Email;
import mongo.Membership_change_log.Membership;

public class CancelledTx2 extends SuperTestScript 
{
	MongoDBUtilMorphia mongoutil = new MongoDBUtilMorphia();
	Datastore ds = mongoutil.getMorphiaDatastoreForNlob();
	Membership member = new Membership();
	Email email = new Email();
	InformationFromBackend info = new InformationFromBackend();
	 
	public CancelledTx2()
	{
		 loginRequired=false;
		 logoutRequired=false;  
	}
   @Test(enabled=true,priority=5,groups={"PaymentIssuesRuleMailer","All"})
   public void cancelledTx1TC120601()
   {
		log.info("Inside cancelled tx 2 script");
		log.info("Fetching data from databse MySQL");
		InformationFromBackend info = new InformationFromBackend();
		info.getDataForCustomerInfo("CancelledTx2");
		log.info("Fetching data from MongoDB");
		Membership custList = null;
		String customerid = InformationFromBackend.result;
		
		log.info("Batch execution");
		driver.get("http://130.211.74.42:8080/payment/testExtentedFailedTransaction?customerid="+customerid+"&provider=ADYEN");
        

		Query query1=ds.createQuery(Email.class);
		query1.filter("customerid",customerid);
		query1.order("-_id");
		query1.limit(1);
		
		try{
			ArrayList<Email> emailList=(ArrayList<Email>)query1.asList();
			
			   
			for(Email email:emailList)
			{
				// email.getClass();
				log.info("CustomerID=" + customerid);
				log.info(email.getFirstname());
				email.getLastname();
				email.getFrom();
				email.getMobilenumber();
				log.info(email.getReason());
				log.info(email.getSubject());

				String expectedResponse = "Success";
				String actualResponse = email.getReason();
				Assert.assertEquals(actualResponse, expectedResponse);

				String expectedTriggerName = "Cancelled tx 2 unknown error";
				String actualTriggerName = email.getTriggerName();

				String expectedSubject = "Snart pausas ditt abonnemang";
				String actualSubject = email.getSubject();

				log.info("Triggered message verification");
				Assert.assertEquals(actualTriggerName, expectedTriggerName);
				log.info("Message Triggered successfully");

				// log.info("Mail sent Date verification");
				// expectedupdateddate=member.getUpdateddate();
				// actualupdateddate=email.getMailsentdate();
				// Assert.assertEquals(actualupdateddate, expectedupdateddate);

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
   }
}
