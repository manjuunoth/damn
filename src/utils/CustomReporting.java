package utils;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.exception.XFramiumException;
import org.xframium.page.StepStatus;
import org.xframium.page.element.Element;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordParameter.ParameterType;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordStep.StepFailure;
import org.xframium.page.keyWord.step.KeyWordStepFactory;
import org.xframium.page.keyWord.step.SyntheticStep;
import org.xframium.reporting.ExecutionContextStep;

public class CustomReporting extends CustomAbstractSeleniumTest
{
private static CustomReporting singleton = new CustomReporting();

public static CustomReporting instance()
{
	return singleton;
}

private CustomReporting() {}

public void startStep (String type, Element element, String []args)
{
	
	KeyWordStep step = KeyWordStepFactory.instance().createStep(element.getName(), element.getPageName(), true, type, "",false, StepFailure.IGNORE, false, "", "", "", 0, "", 0, "", "", "", null, "", false, false, "", "", null, "");
	if(args!=null)
	{
		for (String  value : args)
		{
			step.addParameter(new KeyWordParameter(ParameterType.STATIC, value, "", ""));
			
		}
	}
	element.getExecutionContext().startStep(step, null, null);
}

public void startSynthericStep(String type,Element element,String[] args)
{
	startStep(type, element, args);
}
public void startSyntheticStep (String type, Element element, String [] args)
{
	String message = null;
	if(type != null) 
	{
		try
		{
			String name = element.getName();
			String pageName = element.getPageName();
			List<Object> arrayList = new ArrayList<Object>();
			arrayList.add(name);
			arrayList.add(pageName);
			if(args!=null)
			
				for(String value: args)
				{
					arrayList.add(value);
				}
		message = CustomOutputFormatter.instance().getFormattedMessages(type.trim().toLowerCase());
		
		if(message !=null)
			message = String.format(message, arrayList.toArray());
		else
			message = type;
		
		element.getExecutionContext().startStep(new SyntheticStep(element.getName(),element.getPageName(),message),null,null);
			
		}
			catch(Exception e)
			{
				
				log.error("Error formatting message",new ScriptException(e.getMessage()));
				startSyntheticStep(StepStatus.FAILURE, "Error in getting report statement", element.getWebDriver(), new ScriptException(e.getMessage()));
				
			}
		}
}


public void startSyntheticStep (String type, DeviceWebDriver driver, String [] args)
{
	driver.getExecutionContext().startStep(new SyntheticStep("Step","Step",type), null, null);
}

public void startSyntheticStep(Exception ex , DeviceWebDriver driver)
{
	startSyntheticStep(StepStatus.FAILURE,"click link for more information", driver, ex);
}




public void startSyntheticStep(String message, DeviceWebDriver driver)
{
	startSyntheticStep(StepStatus.REPORT,message, driver, null);
}


public void startSyntheticStep(StepStatus status, String message, DeviceWebDriver driver, Exception ex)

{
driver.getExecutionContext().startStep(new SyntheticStep("Step","Step",message), null, null);
driver.getExecutionContext().completeStep(status, ex);

}

public void completeStep(StepStatus status,Element element, Exception ex)
{
completeStep(status,element.getWebDriver(),ex);
}
public void completeStep(DeviceWebDriver driver, Exception ex)
{
	if(driver.getExecutionContext().getStep()!=null)
	{
		ExecutionContextStep currentStep = driver.getExecutionContext().getStep();
		StepStatus status = currentStep.getStatus()==true?StepStatus.SUCCESS:StepStatus.FAILURE;
		if(ExceptionUtils.indexOfType(ex, XFramiumException.class)==-1 && ex!=null)
			driver.getExecutionContext().completeStep(status, new ScriptException(ex.getMessage()));
		else
			driver.getExecutionContext().completeStep(status, ex);
		
	}
}
public void completeStep(StepStatus status, DeviceWebDriver driver, Exception ex)
{
	if(driver.getExecutionContext().getStep()!= null && status.toString().equals("SUCESS"))
	{
		ExecutionContextStep currentStep = driver.getExecutionContext().getStep();
	status = currentStep.getStatus()==true?StepStatus.SUCCESS:StepStatus.FAILURE_IGNORED;
	
	}
	if(ExceptionUtils.indexOfType(ex, XFramiumException.class)==-1 && ex!=null)
		driver.getExecutionContext().completeStep(status, new ScriptException(ex.getMessage()));
	else
		driver.getExecutionContext().completeStep(status, ex);
}


}
