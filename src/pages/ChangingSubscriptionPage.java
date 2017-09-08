package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import common.BasePage;

public class ChangingSubscriptionPage extends BasePage
{
	

	@FindBy(xpath="//li[@data-rank='10']")
	private WebElement SilverSubs;
	
	@FindBy(xpath="//li[@data-rank='20']")
	private WebElement GuldSubs;
	
	@FindBy(xpath="//li[@data-rank='30']")
	private WebElement FamiljSubs;
	
	@FindBy(xpath="//button[@class='blueButton responsive']")
	private WebElement Fortsätt;
	
	@FindBy(xpath="//button[@class='frameButton responsive goBack']")
	private WebElement Gåtillbaka;
	
	@FindBy(xpath="//button[@class='new blueButton nedgradering closePopUp-button closePopUp']")
	private WebElement Acceptera;
	
	@FindBy(xpath="//button[@class='new borderButton nedgradering closePopUp-button closePopUp']")
	private WebElement Avbryt;

	public ChangingSubscriptionPage(WebDriver driver)
	{
		super(driver);
		PageFactory.initElements(driver, this);
		// TODO Auto-generated constructor stub
	}
	
	public void changeToSilver()
	{
		waitTillElementIsVisible(SilverSubs);
		SilverSubs.click();
	}
	
	public void changeToGuld()
	{
		waitTillElementIsVisible(GuldSubs);
		GuldSubs.click();
	}
	
	public void changeToFamilj()
	{
		waitTillElementIsVisible(FamiljSubs);
		FamiljSubs.click();
	}
	
	public void clickToContinue()
	{
		waitTillElementIsVisible(Fortsätt);
		Fortsätt.click();
	}
	
	public void clickToGoBack()
	{
		waitTillElementIsVisible(Gåtillbaka);
		Gåtillbaka.click();
	}
	
	public void clickToAccept()
	{
		waitTillElementIsVisible(Acceptera);
		Acceptera.click();
	}
	
	public void clickToCancel()
	{
		waitTillElementIsVisible(Avbryt);
		Avbryt.click();
	}
	
	

}
