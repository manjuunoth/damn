package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestMatrixGenerator 
{
public static HashMap<String, ArrayList<String>>  testMatrixList = new HashMap<>();
private static TestMatrixGenerator singleton = new TestMatrixGenerator();

	public static TestMatrixGenerator instance ()
	{
		return singleton;
		
	}
	private TestMatrixGenerator()
	{
		
	}
	
	public void generateTestMatrixList()
	{
		String testScriptName = "MANJUNATH";
		ArrayList<String> dataSetNameList = new ArrayList<String>();
		dataSetNameList.add("MANJUNATH");
		testMatrixList.put(testScriptName.toUpperCase(), dataSetNameList);
	}

	
	public HashMap<String ,ArrayList<String>> getTestMatrixList()
	{
		return testMatrixList;
	}
	
	
	public List<String> getTestList(String sMethodName)
	{
		List<String> returnList = new ArrayList<String>();
		
		String strKey = sMethodName.toUpperCase();
		if(testMatrixList.containsKey(strKey))
			returnList = testMatrixList.get(strKey);
		else
			returnList.add(strKey);
		
		return returnList;
	}
}
