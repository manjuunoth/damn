package utils;

import org.xframium.page.element.Element;
import functions.MLQKeywordPageImplLib;

public abstract class CustomAbstractPage extends MLQKeywordPageImplLib 
{

	
	public Element getClonedElement(String Name) {
		
		return super.getElement(Name).cloneElement();
	}


	
	public Element getElement(String Name) {
		return super.getElement(Name).cloneElement();
		
	}


	
	
	public void initializePage() {
		
		
	}


	

/*	protected PageData[ ]getRecords(String testName,String tableName)
	{
		String xFID = getCustumWebDriver().getExecutionContext().getxFID();
		PageDataManager dataManager =PageDataManager.instance(xFID);
		String key = testName+"-"+tableName.toUpperCase();
		PageData[] pageData= dataManager.getRecords(key);
		
	}*/
}
