package functions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.device.factory.MorelandWebElement;
import org.xframium.exception.ScriptException;
import org.xframium.page.AbstractPage;
import org.xframium.page.StepStatus;
import org.xframium.page.element.Element;
import org.xframium.page.element.SeleniumElement;

import utils.CustomReporting;
import utils.IgnoreMLQException;

public class MLQGenericKeyWordPageImpl extends AbstractPage 
{
	
	//used to get webDriver  Object
	public DeviceWebDriver getCustumWebDriver()
	{
		return (DeviceWebDriver) webDriver;
	}
	
	//hoverover
	
	public void houseOver(Element element)
	{
		WebElement ele = ((WebElement)element.getNative());
		Actions actions = new Actions(getCustumWebDriver());
		actions.moveToElement(ele).build().perform();
		
	}
	
	
	//used to wait for page load
	public void waitForPageLoad()
	{
		try
		{
		WebDriverWait wait = new WebDriverWait(getCustumWebDriver(),6000);

		wait.until(new ExpectedCondition<Boolean>() 
				{
			
			public Boolean apply(WebDriver driver) {
			
				return ((JavascriptExecutor)driver).executeScript("return.document.readyState").toString().equals("complete");
			}

			
		});
		}catch(Exception ex)
		{
			
		}
	
	}
	
	//reset to  zoom level
	public void resetZoomLevel()
	{
		waitForPageLoad();
		WebElement html = getCustumWebDriver().findElement(By.tagName("html"));
		html.sendKeys(Keys.chord(Keys.CONTROL,"0"));
		
		waitForPageLoad();
	}
	
	
	// scorll to Element 
	
	public void scrollToElement(Element element)
	{
	//	if(isVisible(element))
	//	{
			waitForPageLoad();
			WebElement nativeElement = (WebElement)element.getNative();
			if(nativeElement instanceof MorelandWebElement)
					nativeElement =((MorelandWebElement)nativeElement).getWebElement();
		((JavascriptExecutor)getCustumWebDriver()).executeScript("arguments[0].scrollIntoView(true);", nativeElement); 
		
		}
	//}

public boolean isVisible(String Xpath)
{
 try{
return getCustumWebDriver().findElement(By.xpath(Xpath)).isDisplayed();
}
catch(Exception ex)
{
return false;
}
}

	
	//maximize the browser
	
	public void maximizeBrowser(WebDriver driver)
	{
		driver.manage().window().maximize();
		
	}
	
	// to get the list of dropdown values
	
	
	
	public  String [] _getDValueDropDownBOx(Element element)
	{
		int count =0;
		List <WebElement> options;
		WebElement elm;
		Select select;
		while(true)
		{
			elm = (WebElement)element.getNative();
			select= new Select(elm);
			options= select.getOptions();
			if(count >5 || options.size()>1)
				break;
			count++;
		//	_wait(2000);
		}
		
		String[] values = new String [options.size()];
		int i =0;
		for(WebElement option: options)
		{
			values[i] = option.getText();
			i++;
		}
		return values;
	}
	
	
		//set Value
	
	public void _setValue(Element element, String strValue)
	{
		String xFID = element.getExecutionContext().getxFID();
		
		try{
			if(!(strValue.isEmpty()))
		{
			if(element instanceof SeleniumElement)
				CustomReporting.instance().startStep("SET",element,new String[]{strValue});
		
	/* WebElement elm = (WebElement)element.getNative();
		String tagName = elm.getTageName(); */
				//waitForPageLoad();
				element.setValue(strValue, xFID);
		//	if(tagName.equalsIgnoreCase("input"))
				_setValueJs(element,strValue);
			
	//temp reporting solution for 1.0.12
				if(element instanceof SeleniumElement)
					
						CustomReporting.instance().completeStep(StepStatus.SUCCESS,element,null);
				
				waitForPageLoad();
				
			}

		}catch(Exception ex)
		{
			if(element instanceof SeleniumElement)
				
				//	CustomReporting.instance().completeStep(StepStatus.FAILURE,element,null);
				throw new ScriptException(ex.toString());
			throw ex;
		}
		
	}

	
	
	
	// to check checkbox is visible or not
	
	public boolean _isCheckboxVisible(Element element,int timeInMilliSeconds)
	{
		long implWait = element.getWebDriver().getImplicitWait();
		element.getWebDriver().manage().timeouts().implicitlyWait(timeInMilliSeconds, TimeUnit.MILLISECONDS);
		
		try
		{
			WebElement eleTemp = (WebElement)element.getNative();
			WebElement eleSpan = eleTemp.findElement(By.xpath("./following-sibiling::span[1]"));
			return eleSpan.isDisplayed();
		}catch(NoSuchElementException ex)
		{
			return false;
		}
	}
	
	
	//validate the label of checkbox
	public void _checkLabelForCheckBox(Element element, String strExpLabelName, SoftAssert softAssert) 
	{
		boolean success = false;
		final String LABEL_PATTERN = "./following:span[@class='c_option__label_text'][1]";
		String strActLabelName ="";
		CustomReporting.instance().startSynthericStep("COMPARE", element, new String[] {"Label",strExpLabelName});
	try
	{
		WebElement ele = (WebElement)element.getNative();
		WebElement eleLabel = ele.findElement(By.xpath(LABEL_PATTERN));
		strActLabelName=eleLabel.getText();
		if(strActLabelName.equals(strExpLabelName))
		{
			CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);
			success = true;
		}else
		CustomReporting.instance().completeStep(StepStatus.FAILURE, element, new IgnoreMLQException("Acutal label text was "+ strActLabelName));
	}
		catch(Exception ex)
	{
			CustomReporting.instance().completeStep(StepStatus.FAILURE, element, ex);
			throw new IgnoreMLQException(ex.getMessage());
	}
	finally
	{
		softAssert.assertEquals(success, true);
	}
	}
	
	
	
	//click method
	
	private void _setValueJs(Element element, String strValue) 
	{
	int blnSetValueCounter=0;
	try 
	{
		WebElement ele = (WebElement)element.getNative();
		String type = ele.getTagName();
		String strTempValue= strValue.replace("(", "").replace(")","").replace(",", "").replace("-", "").replace("/", "").trim();
		
		while (true && type.equalsIgnoreCase("input"))
		{
			String strValueEntered = ((WebElement)element.getNative()).getAttribute("value");
			strValueEntered = strValueEntered.replace("(", "").replace(")","").replace(",", "").replace("-", "").replace("/", "").trim();
			if(strValueEntered.equalsIgnoreCase(strTempValue) || blnSetValueCounter==2)
			{
				ele.sendKeys(Keys.TAB);
				break;
			}
			ele.clear();
			((JavascriptExecutor)getCustumWebDriver()).executeScript("arguments[0].value='"+strValue+"';",ele.findElement(By.xpath(".")));
		
	blnSetValueCounter = blnSetValueCounter+1;
		}
		
	}
	catch(Exception e)
	{
		e.printStackTrace();
		
	}
	
	
		
	}



	public void _click(Element element)
	{
		
	//	WebDriver driver = getCustumWebDriver();
		
		WebElement nativeElement = (WebElement)element.getNative();
		CustomReporting.instance().startStep("CLICK",element,new String[]{});
		nativeElement.click();
		CustomReporting.instance().completeStep(StepStatus.SUCCESS,element,null);
		
	/*	try
		{
		WebDriver driver = getCustumWebDriver();
		WebElement nativeElement1 = (WebElement)element.getNative();
		
		nativeElement1.click();
		
		if(nativeElement1 instanceof MorelandWebElement)
			nativeElement1 =((MorelandWebElement) nativeElement1).getWebElement();
		
		if(nativeElement1.getTagName().equalsIgnoreCase("svg"))
			nativeElement1 = nativeElement1.findElement(By.xpath("./.."));
		if(element.getWebDriver().getCapabilities().getBrowserName().toLowerCase().contains("internet"))
			//_wait(500);
	try{
					new Actions(getCustumWebDriver()).moveToElement(nativeElement1).perform();
					((JavascriptExecutor)driver).executeScript("arguments[0].click()",nativeElement1);
		}catch(Exception ex)
		{
					element.click();
		}
			CustomReporting.instance().completeStep(StepStatus.SUCCESS,element,null);
		
		}catch(Exception ex)
		{

			ex.printStackTrace();
			
			//throw new Exception(ex.getMessage());
		}*/
	}
	

	
	
	public void setZoomLevel(WebDriver driver)
	{
		
		try
		{
			WebElement html = driver.findElement(By.tagName("html"));
			html.sendKeys(Keys.chord(Keys.CONTROL,Keys.SUBTRACT));
			
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
	}
	
	
	public boolean isVisible(WebDriver driver, String name)
	{
		
		boolean success =false;
		try
		{
//	WebElement ele = getElement(driver, name);
//	success=ele.isDisplayed();
	return success;
		}
		
		catch (Exception e)
		{
			return success;
		}
	
		
		
	}



	@Override
	public void initializePage() {
		
		
	}
        
        
      
   
		
	}