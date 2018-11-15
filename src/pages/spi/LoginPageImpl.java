package pages.spi;

import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.PageManager;
import org.xframium.page.StepStatus;

import pages.LoginPage;
import utils.CustomAbstractPage;
import utils.CustomReporting;
import utils.IgnoreMLQException;

public   class LoginPageImpl extends CustomAbstractPage implements LoginPage
{

	public static LoginPage instance (DeviceWebDriver driver)
	{
	
		String xFID = driver.getExecutionContext().getxFID();
		return (LoginPage)PageManager.instance(xFID).createPage(LoginPage.class, driver);
		
	}
	
	
	@Override
	public void login(String tcID,SoftAssert softAssert) 
	{
	
		CustomReporting.instance().startSyntheticStep("LogIn To Application", getCustumWebDriver(), new String[] {});
		String strUrl = getCustumWebDriver().getCurrentUrl();
	
		_click(getElement(icon_LoginM));
		_setValue(getElement(txt_usernameMMT),"manjuunothda@gmail.com");
		_setValue(getElement(txt_passwordMMT), "man@2018");
		_click(getElement(btn_signinMMT));
		
		_click(getElement(btn_logo));
		
		waitForPageLoad();
		
		CustomReporting.instance().completeStep(StepStatus.SUCCESS, getCustumWebDriver(), new IgnoreMLQException(strUrl));
	
	
	}


	@Override
	public void logout(String tcID, SoftAssert softAssert)
	{
		CustomReporting.instance().startSyntheticStep("LogOut Of Application", getCustumWebDriver(), new String[] {});
	
		_click(getElement(LoginPage.btn_signoffFP));
		
		
	//	CustomReporting.instance().completeStep(StepStatus.SUCCESS, getCustumWebDriver(), new IgnoreMLQException(strUrl));
	}


}
	
	

