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
	private WebElement ConfirmEmail;
	
	@FindBy(xpath="//input[@placeholder='L�senord']")
	private WebElement L�senord;
	
	@FindBy(xpath="//button[contains(text(),'Forts�tt')]")
	private WebElement Forts�tt;
	
	@FindBy(xpath="//label[@class='error' and @for='campcode']")				//V�nligen fyll i kampanjkod.
	private WebElement KampanjkodError;
	
	@FindBy(xpath="//label[@class='error' and @for='email']")					//V�nligen fyll i en giltig e-postadress.
	private WebElement EmailError;
	
	@FindBy(xpath="//label[@class='error' and @for='confirmemail']")			//V�nligen fyll i en giltig e-postadress.
	private WebElement ConfEmailError;
	
	@FindBy(xpath="//label[@class='error' and @for='password']")				//V�nligen fyll ditt l�senord.
	private WebElement PasswordError;
	
	@FindBy(xpath="//span[@class='error']")
	private WebElement WrongCampCodeErr;

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
		waitTillElementIsVisible(ConfirmEmail);
		ConfirmEmail.sendKeys(confirm);
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
	
	public String getCampaignErrMsg()
	{
		waitTillElementIsVisible(KampanjkodError);
		return KampanjkodError.getText();
	}
	
	public String getEmailErrMsg()
	{
		waitTillElementIsVisible(EmailError);
		return EmailError.getText();
	}
	
	public String getConfEmailErrMsg()
	{
		waitTillElementIsVisible(ConfEmailError);
		return ConfEmailError.getText();
	}
	
	public String getPswdErrMsg()
	{
		waitTillElementIsVisible(PasswordError);
		return PasswordError.getText();
	}
	
	public String getWrongCodError()
	{
		waitTillElementIsVisible(WrongCampCodeErr);
		return WrongCampCodeErr.getText();
	}
	
	public void clearKampanjkod()
	{
		waitTillElementIsVisible(Kampanjkod);
		Kampanjkod.clear();
	}
	
	public void clearEpostadress()
	{
		waitTillElementIsVisible(Epostadress);
		Epostadress.clear();
	}
	
	public void clearConfirmEmail()
	{
		waitTillElementIsVisible(ConfirmEmail);
		ConfirmEmail.clear();
	}
	
	public void clearPassword()
	{
		waitTillElementIsVisible(L�senord);
		L�senord.clear();
	}

}
