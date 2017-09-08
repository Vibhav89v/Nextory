package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import common.BasePage;

public class NewSubscriptionPage extends BasePage
{
	@FindBy(xpath="//button[contains(text(),'Silver')]")
	private WebElement SilverSubscription;
	
	@FindBy(xpath="//button[contains(text(),'Guld')]")
	private WebElement GuldSubscription;
	
	@FindBy(xpath="//button[contains(text(),'Familj')]")
	private WebElement FamiljSubscription;
	
	@FindBy(xpath="//button[contains(text(),'Fortsätt')]")
	private WebElement Fortsatt;
	
	public NewSubscriptionPage(WebDriver driver) 
	{
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public void clickSilverSub()
	{
		waitTillElementIsVisible(SilverSubscription);
		SilverSubscription.click();
	}
	
	public void clickGuldSub()
	{
		waitTillElementIsVisible(GuldSubscription);
		GuldSubscription.click();
	}
	
	public void clickFamiljSub()
	{
		waitTillElementIsVisible(FamiljSubscription);
		FamiljSubscription.click();
	}
	
	public void clickContinue()
	{
		waitTillElementIsVisible(Fortsatt);
		Fortsatt.click();
	}
}
