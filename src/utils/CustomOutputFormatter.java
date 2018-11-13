package utils;

import java.util.Properties;

public class CustomOutputFormatter
{
	
	
	private static CustomOutputFormatter  singleton = new CustomOutputFormatter();
	private Properties tempProperties;
	private static Properties outputFormatter = new Properties();
	private CustomOutputFormatter() {}
	public static CustomOutputFormatter instance()
	{
		return singleton;
	}

	// test
	public void loadCustomFormats()
	{
		
		tempProperties = new Properties();
		
		try
		{
		tempProperties.load(getClass().getResourceAsStream("additionalOutputFormatter.properties"));
		
		for(String strKey: tempProperties.stringPropertyNames())
		{
			outputFormatter.put(strKey.trim().toLowerCase(),tempProperties.getProperty(strKey));	
		}
		
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public String getFormattedMessages(String name)
	{
		return outputFormatter.getProperty(name);
	}
	
}
