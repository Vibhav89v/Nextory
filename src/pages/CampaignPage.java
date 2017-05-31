package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import common.BasePage;

public class CampaignPage extends BasePage
{
	@FindBy(xpath="//input[@placeholder='Kampanjkod']")
	private WebElement Kampanjkod;
	
	@FindBy(xpath="//input[@placeholder='E-postadress']")
	private WebElement Epostadress;
	
	@FindBy(xpath="//input[@placeholder='Upprepa e-postadress']")
	private WebElement confirmemail;
	
	@FindBy(xpath="//input[@placeholder='L�senord']")
	private WebElement L�senord;
	
	@FindBy(xpath="//button[contains(text(),'Forts�tt')]")
	private WebElement Forts�tt;

	public CampaignPage(WebDriver driver) 
	{
		super(driver);
		PageFactory.initElements(driver, this);
		// TODO Auto-generated constructor stub
	}
	
	public void enterCampaignCode(String campCode)
	{
		waitTillElementIsVisible(Kampanjkod);
		Kampanjkod.sendKeys(campCode);
	}
	
	public void enterEmailId(String id)
	{
		waitTillElementIsVisible(Epostadress);
		Epostadress.sendKeys(id);
	}
	
	public void enterConfirmMailId(String confirm)
	{
		waitTillElementIsVisible(confirmemail);
		confirmemail.sendKeys(confirm);
	}
	
	public void enterPassword(String pswd)
	{
		waitTillElementIsVisible(L�senord);
		L�senord.sendKeys(pswd);
	}
	
	public void clickToContinue()
	{
		waitTillElementIsVisible(Forts�tt);
		Forts�tt.click();
	}
	

}
