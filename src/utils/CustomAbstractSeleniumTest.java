package utils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.xframium.Initializable;
import org.xframium.artifact.ArtifactManager;
import org.xframium.artifact.ArtifactTime;
import org.xframium.artifact.spi.DefaultSuiteReportingArtifact;
import org.xframium.artifact.spi.PerfectoHTMLReport;
import org.xframium.device.DeviceManager;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.device.ng.AbstractJavaTest;
import org.xframium.device.ng.TestContainer;
import org.xframium.device.ng.TestName;
import org.xframium.device.ng.TestPackage;
import org.xframium.driver.ConfigurationReader;
import org.xframium.driver.TXTConfigurationReader;
import org.xframium.driver.XMLConfigurationReader;
import org.xframium.exception.XFramiumException;
import org.xframium.spi.Device;

public class CustomAbstractSeleniumTest extends AbstractJavaTest
{

	
	private static ConfigurationReader cR= null;
	protected ThreadLocal<String> tcID = new ThreadLocal<String>();


	// To get Driver object

		public   DeviceWebDriver getWebDriver() 
		{
			TestPackage testPackage = testPackageContainer.get();
			DeviceWebDriver driver1 = testPackage.getConnectedDevice().getWebDriver();
			String xFID = testPackage.getxFID();
			
			System.out.println(xFID);
	//		driver.getExecutionContext().setxFID(xFID);
			
			driver1.getExecutionContext().setxFID(xFID);
		//	PageManager.instance(xFID).getPageCache().clear();
			return driver1;	
		}
		protected void reportException(Exception exception)
		{
			TestPackage testPackage = testPackageContainer.get();
			DeviceWebDriver reportingDeviceDriver = testPackage.getConnectedDevice().getWebDriver();
			this.tcID.get();
			reportingDeviceDriver.getCapabilities().getBrowserName();
			if(ExceptionUtils.indexOfType(exception, XFramiumException.class)== -1)
			{
				exception.printStackTrace();
			}else
			{
				
			}
			
		}
		
		protected String getTestName()
		{
			TestPackage testPackage = testPackageContainer.get();
			tcID.set(testPackage.getTestName().getTestName());
			testPackage.getTestName().getPersonaName();
		String dataSetName = testPackage.getTestName().getPersonaName();
			return (dataSetName);
			}
		
		
			
		@DataProvider(name ="mlqDeviceManager")
		public Object[][] getCustomDeviceData(Method currentMethod,ITestContext testContext)
		{
			String xFID;
			if( Initializable.xFID==null || Initializable.xFID.get()==null)
				xFID = (String) testContext.getAttribute("xFID");
			else
				xFID =Initializable.xFID.get();
			
			List<Device> deviceList = DeviceManager.instance(xFID).getDevices();
			Object[][] testContainer = getDeviceData(deviceList,currentMethod,testContext,xFID);
			return testContainer;
		}
		
		@BeforeSuite
		public void setupSuite(ITestContext testContext)
		{
			 String xFID= UUID.randomUUID().toString();
			Initializable.xFID.set(xFID);
			testContext.setAttribute("xFID",xFID);
			Map<String,String> customConfig = new HashMap<String,String>(10);
			
			customConfig.put("xF-ID", Initializable.xFID.get()); 
			
			ArtifactManager.instance(xFID).registerArtifact(ArtifactTime.AFTER_ARTIFACTS, "EXECUTION_SUITE_HTML", DefaultSuiteReportingArtifact.class);
			
			//this.xFID=xFID;
			File configurationFile;
			String configFileName  =System.getProperty("ConfigFile");
			System.out.println("FileName is:   "+configFileName);
			if(configFileName==null ||configFileName.equalsIgnoreCase(""))
				configurationFile = new File("resources/driverConfig.txt");
			else
				configurationFile = new File(configFileName);
			
			if(configurationFile.getName().toLowerCase().endsWith(".xml"))
				cR= new XMLConfigurationReader();
			
			else if(configurationFile.getName().toLowerCase().endsWith(".txt"))
				cR = new TXTConfigurationReader();
			
			//cR.readConfiguration(configurationFile, false,customConfig);
			
			cR.readConfiguration(configurationFile, false, customConfig);
			
			
		}
		
		@AfterSuite
		
		public void cleanSuite()
		{
			cR.afterSuite();
		}
		
		protected Object[][] getDeviceData(List<Device> deviceList,Method currentMethod,ITestContext testContext,String xFID)
		{
			List<String> dataSet = TestMatrixGenerator.instance().getTestList(currentMethod.getName());
			TestName[] newArray = null;
			newArray = new TestName[dataSet.size() * deviceList.size()];
			for(int i = 0 ;i<dataSet.size();i++)
			{
				for(int j = 0;j<deviceList.size();j++)
				{
					newArray[i * deviceList.size()+j] = new TestName(currentMethod.getName());
					newArray[i * deviceList.size()+j].setPersonaName(dataSet.get(i));
					
				}
			}
		
			
			List<Device> fullDevicesList = new ArrayList<Device>(10);
			for(Device d : deviceList)
			{
				for(int i =0; i < d.getAvailableDevices(); i++)
					fullDevicesList.add(d.cloneDevice() );
			}
			
	  		StringBuilder  logOut = new StringBuilder();
	  		
	  		logOut.append("preparing to execute");
	  		
	  		logOut.append("\r\nAgainst the following").append(deviceList.size()).append("devices\r\n");
	  		for(Device d: deviceList)
	  		{
	  			logOut.append("\t"+d.getEnvironment()+"\r\n");
	  		}
	  		
	  		try
	  		{
	  			testContext.getSuite().getXmlSuite().setDataProviderThreadCount(fullDevicesList.size());
	  			log.warn("thread count confiures as full device list");
	  			
	  		}
	  		catch(Exception e) 
	  		{
	  		System.setProperty("dataproviderthreadcount", fullDevicesList.size()+"");
	  		
	  		}
	  		
	  		
	  		
	  		Object[][] returnArray = new Object[newArray.length][1];
	  		for( int i =0; i<returnArray.length;i++)
	  		{
	  			TestName[] temp = new TestName[1];
	  			temp[0] = newArray[i];
	  			TestContainer testContainer =new TestContainer(temp,fullDevicesList.toArray(new Device[0]),xFID);
	  			returnArray[i][0]= testContainer;
	  		
	  		}
	  		return returnArray;
	  			
	  			
	  		}
	  		
		}

	
