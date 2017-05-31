import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Links 
{
	public static void main(String[] args) 
	{
		WebDriver driver = new FirefoxDriver();
	    driver.get("http://130.211.74.42/");
	    
	    List<WebElement> alllinks = driver.findElements(By.tagName("a"));
	    
	    for(int i=0;i<alllinks.size();i++)
	        System.out.println(alllinks.get(i).getText());

	    for(int i1=0; i1<alllinks.size(); i1++)
	    {
	        alllinks.get(i1).click();
	        //driver.navigate().back();
	    }
	}
}
	
	    
	   /* public void getLinks() throws Exception
	    {
	        try 
	        {
	            List<WebElement> links = driver.findElements(By.tagName("a"));
	            int linkcount = links.size(); 
	             System.out.println(links.size()); 
	             
	              for (WebElement myElement : links){
	             String link = myElement.getText(); 
	             System.out.println(link);
	             System.out.println(myElement);   
	            if (link !=""){
	                 myElement.click();
	                 Thread.sleep(2000);
	                 System.out.println("third");
	                }
	                //Thread.sleep(5000);
	              } 
	            }catch (Exception e){
	                System.out.println("error "+e);
	            }
	        }
   }*/
