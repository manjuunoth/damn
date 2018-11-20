package functions;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.CurrencyValidator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.device.factory.MorelandWebElement;
import org.xframium.exception.ScriptException;
import org.xframium.page.AbstractPage;
import org.xframium.page.StepStatus;
import org.xframium.page.element.Element;
import org.xframium.page.element.Element.WAIT_FOR;
import org.xframium.page.element.SeleniumElement;

import utils.CustomReporting;
import utils.IgnoreMLQException;

public class MLQGenaricKeyWordPageImpl extends AbstractPage
{

	
	

	/* ******************************************************************
	*	Name              :	getCustumWebDriver
	*	Description       : Used to get the webDriver Object
	*/
	public DeviceWebDriver getCustumWebDriver()
	{
		//return getWebDriver();
		return (DeviceWebDriver) webDriver;
	}
	
		
	/* ******************************************************************
	*	Name              :	waitForPageLoad
	*	Description       : Used wait for page load
	*/
	private boolean isVisible(String XPATH)
	{
		try{
			return getCustumWebDriver().findElement(By.xpath(XPATH)).isDisplayed();
		}catch(Exception ex)
		{
			return false;
		}
	}
	public void waitForPageLoad()
	{
		String LOBWithoutLoaderXPATH = "//*[contains(@class, 'c-overlay') and contains(@style,'none')]//div[@role='progressbar']";
		String LOBLoaderXPATH = "//*[contains(@class, 'c-overlay') and contains(@style,'flex')]";
		String FoundationLoaderXPATH = "//*[@role='progressbar']";
		long implWait = getCustumWebDriver().getImplicitWait();
		getCustumWebDriver().manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
		WebDriverWait wait = new WebDriverWait(getCustumWebDriver(), 300);
		try{
			if(isVisible(LOBLoaderXPATH))
			{
			  wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[contains(@class, 'c-overlay') and contains(@style,'flex')]")));	
			}else if(isVisible(FoundationLoaderXPATH) && !isVisible(LOBWithoutLoaderXPATH))
			{
			  wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(FoundationLoaderXPATH)));	
			} 
		}catch(Exception ex)
		{
			
		}
		
		getCustumWebDriver().manage().timeouts().implicitlyWait(implWait, TimeUnit.MILLISECONDS);	
		 
	}
		
	/* ******************************************************************
	*	Name              :	resetZoomLevel
	*	Description       : Used to reset the browser zoom level 100%

	*/
	public void resetZoomLevel()
	{
		

		WebElement html = getCustumWebDriver().findElement(By.tagName("html"));
		html.sendKeys(Keys.chord(Keys.CONTROL, "0"));
		
		
		
	}
	
	/*******************************************************************
	*	Name              :	scrollToElement
	*	Description       : Java script function get the element on focus
	*/
	public void scrollToElement(Element element)
	{
		if(_isVisible(element))
		{
			
			WebElement nativeElement = (WebElement) element.getNative();		
			
			if ( nativeElement instanceof MorelandWebElement )
			       nativeElement = ( (MorelandWebElement) nativeElement ).getWebElement();
			
			((JavascriptExecutor)getCustumWebDriver()).executeScript("arguments[0].scrollIntoView(true);", nativeElement);
			
		}
	}
	/*******************************************************************
	*	Name              :	maximizeBrowser
	*	Description       : Used to Maximize the Browser
	*/
	public void maximizeBrowser(WebDriver driver)
	{
		driver.manage().window().maximize();
		resetZoomLevel();
		
	}
	/*******************************************************************
	*	Name              :	_getValueDropDownBox
	*	Description       : Used to get list of drop down values
	*/
	public String[] _getValueDropDownBox(Element element)
	{
		int count=0;
		List <WebElement> options;
		WebElement elm ;
		Select select;
		while(true){
			elm = (WebElement) element.getNative();
			select=new Select (elm);
			options=select.getOptions();			
			if(count>5 || options.size()>1)
				break;
			
			count++;
			_wait(2000);
		}
		
		String[] values=new String[options.size()];
		int i=0;
		for (WebElement option : options) 
		{
			values[i]=option.getText();
			i++;
		}
		return values;
	}
	
	/*******************************************************************
	*	Name              :	_setValue
	*	Description       : Used to set the value to any object, Only when there is data in DB
	********************************************************************/
	public void _setValue(Element element, String strValue)
	{
		_setValue(element, strValue,"");
	}
	public void _setValue(Element element, String strValue,String splitChars)
	{
		try
        {
        	if(!(strValue.isEmpty()))
        	{
        		CustomReporting.instance().startStep("SET", element, new String[]{strValue});
        		WebElement webEle = (WebElement) element.getNative();
        		if ( webEle instanceof MorelandWebElement )
        			webEle = ( (MorelandWebElement) webEle ).getWebElement();
        		String strTagName = webEle.getTagName();
        		if(strTagName.equalsIgnoreCase("select"))
        			_selectDropdownValue(element, strValue, splitChars);
        		else
        		{
        			String strType = webEle.getAttribute("type");
        			switch(strType.toLowerCase())
        			{
        			case "radio":Element radioElement;
		        				if(strValue.equalsIgnoreCase("Yes"))
		        					radioElement=element.addToken("tokenOption", "Yes");
		        				else if (strValue.equalsIgnoreCase("No"))
		        					radioElement=element.addToken("tokenOption", "No");	
		        				else 
		        					radioElement=element.addToken("tokenOption", strValue);
		        				_click(radioElement); 
        							break;
        			case "checkbox":  
			        				if(strValue.equalsIgnoreCase("Yes"))
			        					strValue="true";
			        				else if(strValue.equalsIgnoreCase("No"))
			        					strValue="false";
			        				if((strValue.equalsIgnoreCase("false") & (_isSelected(element))) | (strValue.equalsIgnoreCase("true") & !_isSelected(element)))
							 		{
			        					_click(element); 
							 		}
							 		break;
					default : webEle.sendKeys(strValue);
							  _setValueJS(element, strValue);
							  break;
        			}
        		}
        		
            	CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);
        	}
        	
       } catch(Exception ex)
        {
    	  // if(!_getGlobalVariableValue(GLOBAL_VARIABLES.RANDOM_ADDRESS_FLAG).equalsIgnoreCase("Yes"))
    	   {   if(element instanceof SeleniumElement)
	   			{
	   				CustomReporting.instance().completeStep(StepStatus.FAILURE, element, ex);
	   				throw new ScriptException(ex.toString());
	   			}
    		   throw ex;
    	   }
        }
	}
	/*******************************************************************
	*	Name              :	_setValue
	*	Description       : Used to set the value to any object, Only when there is data in DB
	*/
	/*	public void _setValue(Element element, String strValue)
	{
		String xFID = element.getExecutionContext().getxFID();
		try
        {
        	if(!(strValue.isEmpty()))
        	{
        		//Temp Reporting Solution for 1.0.12
        		if(element instanceof SeleniumElement)
        			CustomReporting.instance().startStep("SET", element, new String[]{strValue});
        		
        		WebElement elm = (WebElement) element.getNative();
        		String tagName = elm.getTagName();
        		        			
        	//	_waitForObject(element, 30);
        		
        		//
        		element.setValue(strValue,xFID);
        		
        		
            	//if(tagName.equalsIgnoreCase("input"))
            		_setValueJS(element, strValue);
            	
            	
            	//Temp Reporting Solution for 1.0.12
        		if(element instanceof SeleniumElement)
        			CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);
        		
        		
        	}
        	
       } catch(Exception ex)
        {
    	   //Ignore this error for Random Address randomization
    	//   if(!_getGlobalVariableValue(GLOBAL_VARIABLES.RANDOM_ADDRESS_FLAG).equalsIgnoreCase("Yes"))
    	   {   
    		   if(element instanceof SeleniumElement)
	   			{
	   				CustomReporting.instance().completeStep(StepStatus.FAILURE, element, ex);
	   				throw new ScriptException(ex.toString());
	   			}
    		   throw ex;
    	   }
    	  
        }
       
      
	}*/
	
	
	
	
	/*******************************************************************
	*	Name              :	_setRelativeValueOfDropdown
	*	Description       : Used to select the dropdown values with partial match, when exact match not available 
	********************************************************************/
	public void _selectDropdownValue(Element element, String strValue,String splitChars)
	{
		boolean optionfound =false;
		try
		{
			if(!(strValue.isEmpty()))
			{
				CustomReporting.instance().startStep("SET", element, new String[]{strValue});
		
				WebElement elm = (WebElement) element.getNative();
				Select select = new Select(elm);
				
				
				
				String[] options = _getValueDropDownBox(element);
				//Select by Visible Text
				if(splitChars.isEmpty())
				{
					for (String option : options) {
		
						String textContent = option;   // .getText();
						String requiredOption = strValue.replaceAll("[^a-zA-Z0-9]+","").toLowerCase();
						String actualOption = textContent.replaceAll("[^a-zA-Z0-9]+","").toLowerCase();
						
						if (actualOption.startsWith(requiredOption)) {
							select.selectByVisibleText(textContent);							
							optionfound =true;
							break;
						}
					}
					
					if(!optionfound)
					{
						for (String option : options) {
							
							String textContent = option;   // .getText();
							String requiredOption = strValue.replaceAll("[^a-zA-Z0-9]+","").toLowerCase();
							String actualOption = textContent.replaceAll("[^a-zA-Z0-9]+","").toLowerCase();
							
							if (actualOption.contains(requiredOption)) {
								select.selectByVisibleText(textContent);							
								optionfound =true;
								break;
							}
						}
					}
				}else
				{
					for (String option : options) {
		
						String textContent = option;//.getAttribute("textContent");
						String[] temp = textContent.split("\\"+splitChars);
						String textLocal = temp[0].trim();
		
						if (textLocal.equalsIgnoreCase(strValue)) {
							select.selectByVisibleText(textContent);
							optionfound =true;
							break;
						}
		
					}
				}
				
				
				if(!optionfound)
					throw new IgnoreMLQException("Can not locate option with value - " +strValue);
				
				CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);
			}
		}catch(Exception ex)
		{
			CustomReporting.instance().completeStep(StepStatus.FAILURE, element, ex);
			throw new ScriptException(ex.toString());
		}
		      
	}
	/*******************************************************************
	*	Name              :	_waitFor
	*	Description       : Used to wait for an object, Default wait_for is VISIBLE
							User can pass this option explicitly also
	
	********************************************************************/
	public void _waitFor(Element element, long timeOut, WAIT_FOR waitType, String value)
	{
		try{
			if(element instanceof SeleniumElement)
    			CustomReporting.instance().startStep("WAIT_FOR", element, new String[]{});
			
			// Added this by Vishnu
			if(value == "" || value == null)
				element.waitFor(timeOut, TimeUnit.SECONDS, waitType, value);
			else
				new WebDriverWait(getCustumWebDriver(), timeOut).until(ExpectedConditions.textToBePresentInElement((WebElement) element.getNative(), value));
			
			if(element instanceof SeleniumElement)
    			CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);
			
			waitForPageLoad();
			
		}catch(Exception ex)
		{
			if(element instanceof SeleniumElement)
			{
				CustomReporting.instance().completeStep(StepStatus.FAILURE_IGNORED, element, ex);
				//throw new ScriptException(ex.toString());
			}
			//Ignore
		}
	}
	
	
	
	
	public void _waitFor(Element element, long timeOut, WAIT_FOR waitType)
	{
		_waitFor(element,timeOut, waitType, "");
		
	}
	
	public void _waitFor(Element element, long timeOut)
	{
		_waitFor(element,timeOut, WAIT_FOR.VISIBLE,"");
		
	}
	
	
	public void _wait(long timeOut)  
	{
		
		try {
			Thread.sleep(timeOut);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	/*******************************************************************
	*	Name              :	_isCheckboxVisible
	*	Description       : Used to check Checkbox visible or not

	********************************************************************/
	public boolean _isCheckboxVisible(Element element,int timeInMilliSeconds)
	{
		long implWait = element.getWebDriver().getImplicitWait();
		element.getWebDriver().manage().timeouts().implicitlyWait(timeInMilliSeconds, TimeUnit.MILLISECONDS);
		try
		{
			
			
			WebElement eleTemp = (WebElement) element.getNative();
			
			WebElement eleSpan = eleTemp.findElement(By.xpath("./following-sibling::span[1]"));
			
			return eleSpan.isDisplayed();
			
			
		}catch(NoSuchElementException ex)
		{
			return false;
			
		}
	}
	/*******************************************************************
	*	Name              :	_isCheckboxVisible
	*	Description       : Used to check Checkbox visible or not

	********************************************************************/
	public void _isCheckboxVisible(Element element,boolean expState,SoftAssert assertSoft)
	{
				
		boolean actState =false;
		long implWait = element.getWebDriver().getImplicitWait();
		element.getWebDriver().manage().timeouts().implicitlyWait(3000, TimeUnit.MILLISECONDS);
		try
		{
			
			
			CustomReporting.instance().startSyntheticStep("COMPARE",element, new String[]{"Visible",expState+""});
			WebElement eleTemp = (WebElement) element.getNative();
			
			WebElement eleSpan = eleTemp.findElement(By.xpath("./following-sibling::span[1]"));
			
			actState = eleSpan.isDisplayed();
			
			
		}catch(NoSuchElementException ex)
		{
			actState=false;
			
		}finally
		{
			if (expState == actState)
				CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);
			else
			{
				CustomReporting.instance().completeStep(StepStatus.FAILURE, element, new IgnoreMLQException("Object visibility state is \n" + actState));
				assertSoft.assertEquals(true, false);
			}
			
			element.getWebDriver().manage().timeouts().implicitlyWait(implWait, TimeUnit.MILLISECONDS);
		}
		
	}
	
	/*******************************************************************
	*	Name              :	_checkLableForCheckbox
	*	Description       : Used to validate the Label of an Checkbox
	
	********************************************************************/
	public void _checkLableForCheckbox(Element element,String strExpLabeName,SoftAssert assertSoft)
	{
		boolean success = false;
		final String LABEL_PATERN = "./following::span[@class='c-option__label__text'][1]";
		String strActLabeName ="";
		CustomReporting.instance().startSyntheticStep("COMPARE",element, new String[]{"Label",strExpLabeName});
		//Check The label Of an Object
		try
		{
			WebElement ele = (WebElement) element.getNative();
			WebElement eleLabel =   ele.findElement(By.xpath(LABEL_PATERN));
			strActLabeName = eleLabel.getText();
			if(strActLabeName.equals(strExpLabeName))
			{
				CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);
				success=true;
			}
			else
				CustomReporting.instance().completeStep(StepStatus.FAILURE, element, new IgnoreMLQException("Actual label text was  - " + strActLabeName));
		 
		}catch(Exception ex)
        {
			CustomReporting.instance().completeStep(StepStatus.FAILURE, element, ex);
        	throw new IgnoreMLQException(ex.getMessage());
        }
		finally {
	        assertSoft.assertEquals(success, true);
		}
	
	}
	/*******************************************************************
	*	Name              :	_checkAttributeValue
	*	Description       : Used  to compare the attribute value of an object
	
	*	Parameters 		  : element  - Object In Action
	*					  :	strAttribute - Attribute Name
	*					  : strExpValue  - Expected value
	*					  : assertSoft   - Assert variable
 	*	Modification Log  :                                                     
	*	Date		Initials     	Description of Modifications 
	********************************************************************/
	public void _checkAttributeValue(Element element,  String strAttribute, String strExpValue,SoftAssert assertSoft)
	{
		_checkAttributeValue(element,  strAttribute, strExpValue,false,false,assertSoft);
	}
	
	public void _checkAttributeValue(Element element,  String strAttribute, String strExpValue,boolean partialCompare,boolean caseSensitive,SoftAssert assertSoft)
	{
		String strActValue="";
		
		String strTempExpected = strExpValue;		
		if(strExpValue.isEmpty())
			strTempExpected = "is Empty";
		
		CustomReporting.instance().startSyntheticStep("COMPARE",element, new String[]{strAttribute,strTempExpected});	
			
		//Compare the value
		try
		{
			if(_isVisible(element))
			{
				//Get the Attribute value
				strActValue = _getAttributeValue(element, strAttribute, assertSoft);
				
				strActValue = strActValue.replace("\n", " ");
				strActValue = strActValue.replace("  ", " ");
				
				//Comparison based on boolean values
				boolean compare;
				if(!caseSensitive)
				{
					strActValue=strActValue.toUpperCase();
					strExpValue=strExpValue.toUpperCase();
				}
				
				if(partialCompare)
					compare = strActValue.contains(strExpValue.trim());
				else
					compare = strActValue.equalsIgnoreCase(strExpValue.trim());
					
					
					
				
				if ( !compare )
				{
					assertSoft.assertEquals(true, false);
					String strMessage = "Mismatch in the values: Expected [" +strExpValue +"] and Actual value ["+strActValue+"]";
					CustomReporting.instance().completeStep(StepStatus.FAILURE, element, new IgnoreMLQException(strMessage));
				}
				else
				{
					CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);	
					
				}
				
			}
			else
			{
				CustomReporting.instance().completeStep(StepStatus.FAILURE, element, new IgnoreMLQException("Object is not visible"));
				assertSoft.assertEquals(true, false);
			}
			
		
		}catch(Exception ex)
		{
			CustomReporting.instance().completeStep(StepStatus.FAILURE, element, ex);
			assertSoft.assertEquals(true, false);
        	throw new IgnoreMLQException(ex.getMessage());
        	
		}
	}
	
	/*******************************************************************
	*	Name              :	_checkDropDownDefaultValue
	*	Description       : Used  to compare the default value of an dropdown

	*	Parameters 		  : element  - Object In Action
	*					  :	strAttribute - Attribute Name
	*					  : strExpValue  - Expected value
	*					  : assertSoft   - Assert variable
 	*	Modification Log  :                                                     
	*	Date		Initials     	Description of Modifications 
	********************************************************************/
	public void _checkDropDownDefaultValue(Element element, String strExpValue,SoftAssert assertSoft)
	{
		String strActValue="";
		
			
		//Compare the value
		try
		{
			CustomReporting.instance().startSyntheticStep("COMPARE",element, new String[]{"Default",strExpValue});
			
			if(_isVisible(element))
			{
				//Get the Attribute value
							
				Select sel= new Select((WebElement) element.getNative());
			
				strActValue= sel.getFirstSelectedOption().getText().trim();
				if(strActValue.equalsIgnoreCase("Select One") && strExpValue.equals(""))
				{
					strActValue="";
					strExpValue="";
				}
								
				if ( !strActValue.equalsIgnoreCase( strExpValue ) )
				{
					CustomReporting.instance().completeStep(StepStatus.FAILURE, element, new IgnoreMLQException("Actaul default value is  - " + strActValue));
					
				}
				else
				{
					CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);
					
				}
			}
			else
			{
				CustomReporting.instance().completeStep(StepStatus.FAILURE, element, new IgnoreMLQException("Object not visible to check the default selection"));
				
				
			}
			
		
		}catch(Exception ex)
		{
			CustomReporting.instance().completeStep(StepStatus.FAILURE, element, ex);
        	throw new IgnoreMLQException(ex.getMessage());
		}finally
		{
			assertSoft.assertEquals(strExpValue.toUpperCase(), strActValue.toUpperCase());
		}
	}
	
	
	/*******************************************************************
	*	Name              :	_getAttributeValue
	*	Description       : Used  to retrive the attribute value of an object

	********************************************************************/
	public String _getAttributeValue(Element element, String strAttribute, SoftAssert assertSoft)
	{
		String strActValue="";
		
		if(strAttribute.equalsIgnoreCase("TEXT"))
		{
			try{
				CustomReporting.instance().startStep("ATTRIBUTE", element, new String[]{strAttribute});
				WebElement ele = (WebElement) element.getNative();
				if(ele.getTagName().equalsIgnoreCase("select"))
				{
					try{
					Select sel= new Select(ele);	
					strActValue= sel.getFirstSelectedOption().getText().trim();
					}catch(Exception ex)
					{
						if(ex.getMessage().contains("No options are selected"))
							strActValue="";
						else
							throw ex;
					}
				}
				else
				{
					strActValue=ele.getText();
				}
				CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);
			}catch(Exception ex)
			{
				CustomReporting.instance().completeStep(StepStatus.FAILURE, element, ex);
				assertSoft.assertEquals(true, false);
				throw new IgnoreMLQException(ex.getMessage());
			}
			
		}
		else
		{
			//Temp Reporting Solution for 1.0.12
    		if(element instanceof SeleniumElement)
    			CustomReporting.instance().startStep("ATTRIBUTE", element, new String[]{strAttribute});
    		
    		
			strActValue = element.getAttribute(strAttribute);
			
			if(element instanceof SeleniumElement)
    			CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);
		}
			
		return strActValue.trim();
	}
	
	/*******************************************************************
	*	Name              :	_click
	*	Description       : Used to click any type of object
	
	********************************************************************/
	public void _click(Element element)
	{
		//element.click();
		CustomReporting.instance().startStep("CLICK", element, null);
        try
        {
        	
        	WebDriver driver= getCustumWebDriver();
    		WebElement nativeElement = (WebElement) element.getNative();		
    		
    		if ( nativeElement instanceof MorelandWebElement )
    		       nativeElement = ( (MorelandWebElement) nativeElement ).getWebElement();	
    		
    		if(nativeElement.getTagName().equalsIgnoreCase("svg"))
    			nativeElement = nativeElement.findElement(By.xpath("./.."));
    		
    		if(element.getWebDriver().getCapabilities().getBrowserName().toLowerCase().contains("internet"))
    			_wait(500);
    		
    		try{
    			new Actions(getCustumWebDriver()).moveToElement(nativeElement).perform();
    			((JavascriptExecutor) driver).executeScript("arguments[0].click()",nativeElement);
    		}catch(Exception ex)
    		{
    			element.click();
    		}
	    		
	    	
    		CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);	
        	
        } catch(Exception ex)
        {
        	
        	CustomReporting.instance().completeStep(StepStatus.FAILURE, element, ex);
        	throw new IgnoreMLQException(ex.getMessage());
        }
        
	}
	
	/*******************************************************************
	*	Name              :	_formatDate
	*	Description       : Used to format the date required format

	********************************************************************/
	public   Date _formatDate(String sdate,String sFormat)
	{
	    SimpleDateFormat formatter = new SimpleDateFormat(sFormat);
	    try {
			return formatter.parse(sdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	/*******************************************************************
	*	Name              :	_getCurrentDate
	*	Description       : Used to get current date
	
	********************************************************************/
	public String _getCurrentDate()
	{		
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
		String sDate = DATE_FORMAT.format(Calendar.getInstance().getTime());
		return sDate;
	}
	
	
	
	
	/*******************************************************************
	*	Name              :	_getRandomString
	*	Description       : Used to get the relative date wrt current date

	********************************************************************/
	public enum DATATYPE {
		number,varchar,character,alphanumeric;
	}

	public String _getRandomString(DATATYPE type , int length)
	{
		
		String strType = type.toString();
		String defaultString = "";
		
		if(!strType.equalsIgnoreCase("number"))
			defaultString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		if(!strType.equalsIgnoreCase("character"))
			defaultString = defaultString + "0123456789";
		
		if(strType.equals("alphanumeric"))
		{
			defaultString = defaultString + "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		}
		
		
		StringBuilder strCh = new StringBuilder();
		Random rnd = new Random();
		
		for(int i=0;i<length;i++)
			strCh.append(defaultString.charAt(rnd.nextInt(defaultString.length())));
			
		return strCh.toString();
		
	
	}
	
	
	/*******************************************************************
	*	Name              :	_isVisible
	*	Description       : Used to check object is Visible or not
	
	********************************************************************/
	public void _isVisible(Element element,boolean expState,SoftAssert assertSoft)
	{
		boolean actState = false;
		CustomReporting.instance().startSyntheticStep("COMPARE",element, new String[]{"Visible",expState+""});
		try
		{
				
			actState =_isVisible(element,200);
			
			if(expState==actState)
				CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);		
			else
				CustomReporting.instance().completeStep(StepStatus.FAILURE, element, new IgnoreMLQException("Object visibility state is \n" + actState));
				
		
		}catch(Exception ex)
		{
			CustomReporting.instance().completeStep(StepStatus.FAILURE, element, ex);
			assertSoft.assertEquals(true, false);
			
		}finally
		{
			assertSoft.assertEquals(expState, actState);
		}
		
	}
	
	/*******************************************************************
	*	Name              :	_isVisible
	*	Description       : Conditional statement to check object is Visible or not
	
	********************************************************************/
	public boolean _isVisible(Element element)
	{
		return _isVisible(element,5000);		
	}
	
	public boolean _isVisible(Element element,long timeInMilliSeconds)
	{
		
		boolean success = false;
		long implWait = element.getWebDriver().getImplicitWait();
		element.getWebDriver().manage().timeouts().implicitlyWait(timeInMilliSeconds, TimeUnit.MILLISECONDS);
		try
		{
			WebElement ele =(WebElement)element.getNative();
			success = ele.isDisplayed();
			return success;
            
		}catch(Exception ex)
        {
		    return success;
        }finally
        {
        	element.getWebDriver().manage().timeouts().implicitlyWait(implWait, TimeUnit.MILLISECONDS);
        }
	}
	
	/*******************************************************************
	*	Name              :	_isOptionPresentInDropDown
	*	Description       : return true, if given option is present in dropdown
	
	********************************************************************/
	public boolean _isOptionPresentInDropDown(Element dropDownElement,String expOption)
	{
		boolean success=false;
		
    	Select sel= new Select((WebElement) dropDownElement.getNative());
    	
    	List<WebElement> options= sel.getOptions();
    	//get actual values and convert to upper case if comparison is not case sensitive
	    for (WebElement option : options) 
	    {
	    	if(option.getText().toUpperCase().trim().equalsIgnoreCase(expOption))
	    	{
	    		success=true;
	    		break;
	    	}
	    }
		return success;
	}
	/*******************************************************************
	*	Name              :	_validateDropdownValues
	*	Description       : Used to validate values of a dropdown against given list of values
	
	********************************************************************/
	public void _validateDropdownValue(Element dropDownElement,ArrayList<String> expOptions,boolean bOrder,boolean bIgnoreCase,String splitChar, SoftAssert assertSoft)
	{
		boolean success = false;
		List<String> expected= new ArrayList<String>();
    	List<String> actual= new ArrayList<String>();
    	
	    try
	    {
	    	//get expected values and convert to upper case if comparison is not case sensitive
		    for(String option: expOptions)
		    {
		    	if(bIgnoreCase)
		    		expected.add(option.toUpperCase().trim());
		    	else 
		    		expected.add(option.trim());
		    }
		    if(!bOrder)
		      	Collections.sort(expected);
		    
		    
	    	CustomReporting.instance().startSyntheticStep("COMPARE",dropDownElement, new String[]{"Dropdown values",expected.toString()});
	    	
	    	Select sel= new Select((WebElement) dropDownElement.getNative());
	    	
	    	List<WebElement> options= sel.getOptions();
	    	
	    	
	    	//get actual values and convert to upper case if comparison is not case sensitive
		    for (WebElement option : options) 
		    {
		    	String actValue="";
		    	if(bIgnoreCase)
		    		actValue = option.getText().toUpperCase().trim();
		    	else 
		    		actValue= option.getText().trim();
		    	
		    	if(splitChar.isEmpty())
		    		actual.add(actValue);
		    	else
		    		actual.add(actValue.split(splitChar)[0]);

	    	}
		    
		    //sort the values if order is not matter in comparison
		    if(!bOrder)
		      	Collections.sort(actual);
		    
		    //compare expected vs actual
		    	
		    if(actual.equals(expected))
	    	{
	    		success=true;
	    		CustomReporting.instance().completeStep(StepStatus.SUCCESS, dropDownElement, null);
	    	}
	    	else
	    	{
	    		CustomReporting.instance().completeStep(StepStatus.FAILURE, dropDownElement, new IgnoreMLQException("Actual DropDown list was " + actual.toString()));
	    	}
	    		
	    } catch(Exception ex)
	    {
	    	CustomReporting.instance().completeStep(StepStatus.FAILURE, dropDownElement, ex);
        	throw new IgnoreMLQException(ex.getMessage());
	    	
	    }  
	    
	    finally {
	        assertSoft.assertEquals(success, true);
		}
	}
	/*******************************************************************
	*	Name              :	_validateDropdownValues
	*	Description       : Used to validate values of a dropdown against given list of values

	********************************************************************/
	private void _validateDropdownValue(Element dropDownElement,ArrayList<String> expOptions,boolean bOrder,boolean bIgnoreCase,boolean bPartialCompare,String splitChar, SoftAssert assertSoft)
	{
		boolean success = true;
		
		List<String> expected= new ArrayList<String>();
    	List<String> actual= new ArrayList<String>();
    	
	    try
	    {
	    	//get expected values and convert to upper case if comparison is not case sensitive
		    for(String option: expOptions)
		    {
		    	if(bIgnoreCase)
		    		expected.add(option.toUpperCase().trim());
		    	else 
		    		expected.add(option.trim());
		    }
		    if(!bOrder)
		      	Collections.sort(expected);
		    
		    
	    	
	    	Select sel= new Select((WebElement) dropDownElement.getNative());
	    	
	    	List<WebElement> options= sel.getOptions();
	    	
	    	
	    	//get actual values and convert to upper case if comparison is not case sensitive
		    for (WebElement option : options) 
		    {
		    	String actValue="";
		    	if(bIgnoreCase)
		    		actValue = option.getText().toUpperCase().trim();
		    	else 
		    		actValue= option.getText().trim();
		    	
		    	if(splitChar.isEmpty())
		    		actual.add(actValue);
		    	else
		    		actual.add(actValue.split(splitChar)[0]);

	    	}
		    
		    //sort the values if order is not matter in comparison
		    if(!bOrder)
		      	Collections.sort(actual);
		    boolean[] bActual=new boolean[actual.size()];
		    boolean result;
		    for(int i=0;i<expected.size();i++)
		    {
		    	//compare expected vs actual		    
		    	boolean bFound=false;
		    	for(int j=0;j<actual.size();j++)
			    {
		    		if(expected.get(i).equalsIgnoreCase("") || !bPartialCompare)
		    			result=!bActual[j] && actual.get(j).equalsIgnoreCase(expected.get(i)); // for empty value
		    		else	    		
		    			result=!bActual[j] && actual.get(j).contains(expected.get(i));
		    			
		    		if(result)
		    		{
		    				CustomReporting.instance().startSyntheticStep(StepStatus.SUCCESS,expected.get(i) + "  found at " + actual.get(j),getCustumWebDriver(), null);
		    				bActual[j]=true;
		    				bFound=true;
		    				success=true;
		    				break;
		    		}
			    }
		    	if(!bFound){
	    	   		CustomReporting.instance().startSyntheticStep(StepStatus.FAILURE, expected.get(i) + "  Not found", getCustumWebDriver(),new IgnoreMLQException(expected.get(i)  +" NOT Found"));
	    	     success=false;
		    	}
		    	
		    }
		    for(int j=0;j<actual.size();j++)
		    {
	    		if(!bActual[j])
	    	   		CustomReporting.instance().startSyntheticStep(StepStatus.FAILURE, "Invalid" + actual.get(j) , getCustumWebDriver(),new IgnoreMLQException("Invalid " + actual.get(j)));

		    }
	    		
	    } catch(Exception ex)
	    {
	    	CustomReporting.instance().completeStep(StepStatus.FAILURE, dropDownElement, ex);
        	throw new IgnoreMLQException(ex.getMessage());
	    	
	    }  
	    
	    finally {
	        assertSoft.assertEquals(success, true);
		}
	}
		
	/*******************************************************************
	*	Name              :	_validateDropdownValue
	*	Description       : Used to validate values of a dropdown
	
	********************************************************************/
	public void _validateDropdownValue(Element element,String dropdownName,SoftAssert assertSoft)
	{
		ArrayList<String> expOptions = MLQKeywordPageImplLib._getExpectedDroprDownValues(getCustumWebDriver(),dropdownName,",");
		_validateDropdownValue(element,expOptions,false,true,"",assertSoft);
	}
	
	public void _validateDropdownValue(Element element,String dropdownName,boolean blnEmptyValue,boolean blnSelectOne,SoftAssert assertSoft)
	{
		ArrayList<String> expOptions = MLQKeywordPageImplLib._getExpectedDroprDownValues(getCustumWebDriver(),dropdownName,",");
		if(!blnSelectOne)
		{
			expOptions.remove("Select One");
			if(!expOptions.contains(""))
			expOptions.add("");
			
			
		}
		
		if(!blnEmptyValue)
		{
			expOptions.remove("");
		}
		
		
		
		_validateDropdownValue(element,expOptions,false,true,"",assertSoft);
	}
	/*******************************************************************
	*	Name              :	_validateDropdownValue
	*	Description       : Used to validate values of a dropdown by providing a split chars
	
	********************************************************************/
	public void _validateDropdownValue(Element element,String dropdownName,String splitChars, SoftAssert assertSoft)
	{	
		ArrayList<String> expOptions = MLQKeywordPageImplLib._getExpectedDroprDownValues(getCustumWebDriver(),dropdownName,splitChars);
		_validateDropdownValue(element,expOptions,false,true,splitChars,assertSoft);
	}
	
	public void _validateDropdownValue(Element element,String dropdownName,String splitChars,boolean bOrder,boolean bIgnoreCase, SoftAssert assertSoft)
	{	
		ArrayList<String> expOptions = MLQKeywordPageImplLib._getExpectedDroprDownValues(getCustumWebDriver(),dropdownName,splitChars);
		_validateDropdownValue(element,expOptions,bOrder,bIgnoreCase,splitChars,assertSoft);
	}
	public void _validateDropdownValue(Element element,String dropdownName,String splitChars,boolean blnEmptyValue,boolean blnSelectOne,boolean bPartialCompare, SoftAssert assertSoft)
	{	
		ArrayList<String> expOptions = MLQKeywordPageImplLib._getExpectedDroprDownValues(getCustumWebDriver(),dropdownName,splitChars);
		if(!blnSelectOne)
			expOptions.remove("Select One");
		if(blnEmptyValue)
			expOptions.add("");

		_validateDropdownValue(element,expOptions,false,true,bPartialCompare,splitChars,assertSoft);
	}


	/*******************************************************************
	*	Name              :	_isEnabled
	*	Description       : Used to check Object is Enabled
	
	********************************************************************/
	public void _isEnabled(Element element,boolean value,SoftAssert assertSoft)
	{
		boolean enabled = false;
		CustomReporting.instance().startSyntheticStep("COMPARE",element, new String[]{"Enabled",value+""});
		try
		{
				
			enabled =_isEnabled(element);
			
			if(value==enabled)
				CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);	
			else
				CustomReporting.instance().completeStep(StepStatus.FAILURE, element, new IgnoreMLQException("Actaul state of object enabled is " + enabled));
				
		
		}catch(NoSuchElementException ex)
		{
			CustomReporting.instance().completeStep(StepStatus.FAILURE, element, ex);
			assertSoft.assertEquals(true, false);
			
		}catch(Exception ex)
		{
			CustomReporting.instance().completeStep(StepStatus.FAILURE, element, ex);
			throw new IgnoreMLQException(ex.getMessage());
		}finally
		{
			assertSoft.assertEquals(value, enabled);
		}
	}
	
	
	/*******************************************************************
	*	Name              :	_isEnabled
	*	Description       : Used to check Object is enabled or disabled
	
	********************************************************************/
	public boolean _isEnabled(Element element)
	{
	//	boolean success = false;
		
		try{
		
		WebElement ele = (WebElement) element.getNative();
		//By pass for Span and div elements
		if( ele.getTagName().equalsIgnoreCase("div")||ele.getTagName().equalsIgnoreCase("span"))
			return false;
					
		String isDisabled = ele.getAttribute("disabled");
		String isReadonly = ele.getAttribute("Readonly");
		if (isDisabled==null && isReadonly==null )
			return true;
		else
			return false;
			
		}catch(Exception ex)
		{
			CustomReporting.instance().startSyntheticStep(StepStatus.FAILURE, "Check " + element.getName() + " on "+element.getPageName() +" Enabled", getCustumWebDriver(), ex);
			return false;
		}
			
	}
	
	
	/*******************************************************************
	*	Name              :	_clearValue
	*	Description       : Used to Clear/Blank out the value to any object
	
	********************************************************************/
	public void _clearValue(Element element)
	{
		try
        {
			//Temp Reporting Solution for 1.0.12
    		if(element instanceof SeleniumElement)
    			CustomReporting.instance().startStep("SET", element, new String[]{"Empty"});
    		
			String xFID = element.getExecutionContext().getxFID();
			WebElement ele = (WebElement) element.getNative() ;
			
			if(((WebElement) element.getNative()).getTagName().equalsIgnoreCase("select"))
			{
				_selectDropdownValueByIndex(element, 0);
				
				
			}
			else
			{
				
				//((JavascriptExecutor)getCustumWebDriver()).executeScript("arguments[0].value='';",ele.findElement(By.xpath(".")));
				//element.setValue("",xFID);
				ele.click();
				element.setValue("",xFID);
				
			}
				
			ele = (WebElement) element.getNative();
			ele.sendKeys(Keys.TAB);
			
		    //Temp Reporting Solution for 1.0.12
		    if(element instanceof SeleniumElement)
        			CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);
        	
        	
       } catch(Exception ex)
        {
    	   //Temp Reporting Solution for 1.0.12
   			if(element instanceof SeleniumElement)
   			{
   				CustomReporting.instance().completeStep(StepStatus.FAILURE, element, ex);
   				throw new ScriptException(ex.toString());
   			}
   		
        	throw ex;
        }
    
		
	}
	
	/*******************************************************************
	*	Name              :	_isSelected
	*	Description       : Used  to check the checkbox and radio status, compare with required value

	********************************************************************/
	protected void _isSelected(Element element, boolean value,SoftAssert assertSoft)
	{
		
		boolean blnStatus =false;
		
		
		//WebElement eleSpan = ele.findElement(By.xpath("./following-sibling::span"));
		
		//Compare the value
		try
		{
			CustomReporting.instance().startSyntheticStep("COMPARE",element, new String[]{"Checked",value+""});		
			blnStatus =_isSelected(element);
			
			if(value==blnStatus)
				CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);
			else
				CustomReporting.instance().completeStep(StepStatus.FAILURE, element, new IgnoreMLQException("Actaul state on object enabled is " + blnStatus));
				
		
		}catch(Exception ex)
		{
			CustomReporting.instance().completeStep(StepStatus.FAILURE, element, ex);
			throw new IgnoreMLQException(ex.getMessage());
		}finally
		{
			assertSoft.assertEquals(value, blnStatus);
		}
	}
	

	/*******************************************************************
	*	Name              :	_isSelected
	*	Description       : Used  to check checked state of element
	
	********************************************************************/
	protected boolean _isSelected(Element element)
	{
		
		WebElement ele = (WebElement) element.getNative();			
		
		if(ele.isSelected())
			return true;
		else
			return false;
		
	
	}
	
	
	/*******************************************************************
	*	Name              :	_splitDatabyDelimiter
	*	Description       : Used to split the data by delimiter and return the data as an array
	
	********************************************************************/

	public String[] _splitDatabyDelimiter(String strChar, String strDelimiter)
	{			
		String[] strData;
		strData = strChar.split(strDelimiter);
		return strData;
		
	}


	@Override
	public void initializePage() {
		
		
	}
	
	

	/*******************************************************************
	*	Name              :	_addToGlobalVariableList
	*	Description       : Used to add global variable to HashMap
	
	********************************************************************
	public void _addToGlobalVariableList(GLOBAL_VARIABLES strKey,String strValue)
	{			
		GlobalVariableContainer.instance().addVariable(strKey, strValue);
	}
	public void _removeAllGlobalVariables()
	{
		GlobalVariableContainer.instance().removeAllVariables();
	}
	public void _removeGlobalVariable(GLOBAL_VARIABLES strKey)
	{
		GlobalVariableContainer.instance().removeVariable(strKey);
	}
	/*******************************************************************
	*	Name              :	_addToGlobalVariableList
	*	Description       : Used to get global variable value from HashMap

	********************************************************************
	public String _getGlobalVariableValue(GLOBAL_VARIABLES strKey)
	{			
		return GlobalVariableContainer.instance().getVariable(strKey);
	}

	/*******************************************************************
	*	Name              :	_setValueJS
	*	Description       : Used to set value using Java Script
	
	********************************************************************/
	protected void _actionClick(Element element)
	{			
		
		try{
			CustomReporting.instance().startStep("CLICK", element, null);
    		
			WebElement ele= (WebElement) element.getNative();
			
    		if ( ele instanceof MorelandWebElement )
    			ele = ( (MorelandWebElement) ele ).getWebElement();	
    		
			
			Actions ob = new Actions(getCustumWebDriver());
			if(ele.getTagName().equalsIgnoreCase("svg"))
				ob.moveToElement(ele.findElement(By.xpath("./.."))).click().build().perform();
			else
				ob.moveToElement(ele.findElement(By.xpath("."))).click().build().perform();
			
			CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);	
    		
		}catch(Exception ex)
		{
			ex.printStackTrace();
			CustomReporting.instance().completeStep(StepStatus.FAILURE, element, ex);	
			
		}
		
	
	}
	
	/*******************************************************************
	*	Name              :	_setValueJS
	*	Description       : Used to set value using Java Script
	
	********************************************************************/
	protected void _setValueJS(Element element,String strValue)
	{			
		int blnSetValueCounter=0;
		try{
    		  		
			WebElement ele= (WebElement) element.getNative();
			String type = ele.getTagName();
			String strTempValue = strValue.replace("(", "").replace(")", "").replace(",", "").replace(" ", "").replace("-", "").replace("/", "").trim();
    		
    		while(true && type.equalsIgnoreCase("input"))
    		{   
				String strValueEntered=((WebElement) element.getNative()).getAttribute("value"); 
				strValueEntered = strValueEntered.replace("(", "").replace(")", "").replace(",", "").replace(" ", "").replace("-", "").replace("/", "").trim();    			
    			if(strValueEntered.equalsIgnoreCase(strTempValue) || blnSetValueCounter==2)
    			{
    				ele.sendKeys(Keys.TAB);
    				break;
    			}
    			ele.clear();
    			((JavascriptExecutor)getCustumWebDriver()).executeScript("arguments[0].value='"+strValue+"';",ele.findElement(By.xpath(".")));
    			blnSetValueCounter=blnSetValueCounter+1; 
    		}
    		
		}catch(Exception ex)
		{
			ex.printStackTrace();				
		}
		
	
	}
	
	/*******************************************************************
	*	Name              :	_waitForObject
	*	Description       : Used for object to ready, This internal function called before setValue, Click etc

	********************************************************************/
	private void _waitForVisible(Element element,long timeOut)
	{

        long implicitWait =  getCustumWebDriver().getImplicitWait();
        getCustumWebDriver().manage().timeouts().implicitlyWait( 1, TimeUnit.SECONDS );
        WebElement webElement = (WebElement) element.getNative();
       
         try
            {
        	 
        	  WebDriverWait wait = new WebDriverWait( getCustumWebDriver(), timeOut, 60 );
        	 wait.until(ExpectedConditions.visibilityOf(webElement));                
            }
            catch ( Exception e )
            {                  
            }finally {
            	getCustumWebDriver().manage().timeouts().implicitlyWait( implicitWait, TimeUnit.MILLISECONDS );
			}
    
	}
	
	/*******************************************************************
	*	Name              :	_hoveronElement
	*	Description       : Used for hovering on element

	********************************************************************/
	public void hoveronElement(Element element)
	{
		WebElement ele = (WebElement) element.getNative();		
		Actions actions = new Actions(getCustumWebDriver());		
		actions.moveToElement(ele).build().perform();	
	}
	/*******************************************************************
	*	Name              :	 _selectDropdownValueByIndex
	*	Description       : Used to select dropdown value by index
	
	********************************************************************/
	public void  _selectDropdownValueByIndex(Element element,int index)
	{
		WebElement elm = (WebElement) element.getNative();
		Select select = new Select(elm);
		
		List <WebElement> options = select.getOptions();
		
		if(options.size()>0)
		{
	    
		select.selectByIndex(index);
		
		}
	 }
	
	/*******************************************************************
     *      Name              :  _checkValueNotEmpty
     *      Description       : check the element value is empty
    
     ********************************************************************/
     public void _checkValueNotEmpty(Element element,String atrribute,SoftAssert assertSoft)
     {
            boolean success=false;
     
          
     
            try{
            	CustomReporting.instance().startSyntheticStep("CHECKVALUENOTEMPTY",element, new String[]{atrribute});   
            	String strActValue=_getAttributeValue(element, atrribute, assertSoft);
                   
            if(!strActValue.isEmpty() && strActValue!=null ){
                   success=true;
                   CustomReporting.instance().completeStep(StepStatus.SUCCESS, element, null);
            }
            else{
                   success=false;
                   CustomReporting.instance().completeStep(StepStatus.FAILURE, element, new IgnoreMLQException("Value is not empty"));
            }
            }
            catch(Exception ex){
                   CustomReporting.instance().completeStep(StepStatus.FAILURE, element, ex);
                   ex.printStackTrace();
            
            }
     
            finally {
             assertSoft.assertEquals(success, true);
            }
     }

	
     

 	/*******************************************************************
 	*	Name              :	_validateFormat
 	*	Description       : Used to validate amount Format
 
 	********************************************************************/
 	public void _validateFormat(String strValue,Locale country, SoftAssert softAssert)
 	{
 		CustomReporting.instance().startSyntheticStep("COMPARE",getCustumWebDriver(), new String[]{"Currency",country.toString()});
 		BigDecimalValidator validator = CurrencyValidator.getInstance();
	    BigDecimal amount = validator.validate(strValue, country); 
	    if(amount==null)
	    	CustomReporting.instance().completeStep(StepStatus.FAILURE, getCustumWebDriver(), new IgnoreMLQException("Actual Value" +strValue));
	    else
	    	CustomReporting.instance().completeStep(StepStatus.SUCCESS, getCustumWebDriver(), null);
		 

 	}
 	
 	 /*******************************************************************
     * Name              : toCamelCase
     * Description       : To Camel case the words in a String
    
     ********************************************************************/
     public String _toCamelCase(String string) {
         if (string==null)
             return null;

      /*  StringBuilder ret = new StringBuilder(string.length());

         for (String word : string.split(" ")) {
             if (!word.isEmpty()) {
                 ret.append(word.substring(0, 1).toUpperCase());
                 ret.append(word.substring(1).toLowerCase());
             }
             if (!(ret.length()==string.length()))
                 ret.append(" ");
         }

         return ret.toString();*/
         return WordUtils.capitalizeFully(string);
     }

     /*******************************************************************
  	*	Name              :	_FormatNumber
  	*	Description       : Used to get drop option for given value
  	
  	********************************************************************/
  	
  	public String _formatNumber(String strNumber,boolean blnDecimal)
  	{
  		 double amount = Double.parseDouble(strNumber);
  		DecimalFormat formatter;
  		 if(blnDecimal)
  			 formatter = new DecimalFormat("#,###.00");
  		 else
  			formatter = new DecimalFormat("#,###");
  		 
  		strNumber = formatter.format(amount);
		return strNumber;	
  	}
    


     /*******************************************************************
 	*	Name              :	_getDropDownOptionWithValue
 	*	Description       : Used to get drop option for given value
 
 	********************************************************************/
 	public String _getDropDownOptionWithValue(Element element,String strValueNeeded)
 	{
 		int count=0;
 		List <WebElement> options;
 		WebElement elm ;
 		Select select;
 		while(true){
 			elm = (WebElement) element.getNative();
 			select=new Select (elm);
 			options=select.getOptions();			
 			if(count>5 || options.size()>1)
 				break;
 			
 			count++;
 			_wait(2000);
 		}
 		
 		String strText="";
 		for (WebElement option : options) 
 		{
 			if(option.getAttribute("value").equalsIgnoreCase(strValueNeeded))
 				strText = option.getText();
 		}
 		return strText;
 	}
 		
}
