package test;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.device.ng.TestContainer;

import pages.spi.LoginPageImpl;

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
		
		LoginPageImpl.instance(driver).logout(tcID, softAssert);
		System.out.println("Job Testing");
		softAssert.assertAll();
		System.out.println("Finished");
		
		
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		
	}
}