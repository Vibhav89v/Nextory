package confirmationsRuleMailer;

import org.testng.annotations.Test;

import common.SuperTestScript;
import generics.Api;

public class AccountTerminationExcludingNonPayment extends SuperTestScript
{
   @Test
   public void accountTerminationExcludingNonPaymentTC100801()
   {
	     log.info("Fetching data from database");
		 Api a=new Api();
		 a.fetchData();                  //CustometerInfo and customer2subscription
		 a.clickSurpriseDelight();
   }
}
