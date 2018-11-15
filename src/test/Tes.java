package test;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.device.ng.TestContainer;

import pages.spi.LoginPageImpl;
import pages.spi.MenuItemsImpl;
import utils.CustomAbstractSeleniumTest;

public class Tes extends CustomAbstractSeleniumTest 
{

	@Test(dataProvider ="mlqDeviceManager")
	public void Manjunath(TestContainer testContainer) throws Exception
	{
		try
		{
		SoftAssert softAssert = new SoftAssert();
		DeviceWebDriver driver = getWebDriver();
		String tcID= getTestName();
		
		LoginPageImpl.instance(driver).login(tcID, softAssert);
		MenuItemsImpl.instance(driver).selectProduct("DRESSES", softAssert);
		LoginPageImpl.instance(driver).logout(tcID, softAssert);
		
		softAssert.assertAll();
		System.out.println("Completed");
		
		
		}
		catch(Exception e)
		{
			reportException(e);
			e.printStackTrace();
			throw e;
		}
		
	}
}