package utils;

import org.xframium.exception.XFramiumException;

public class IgnoreMLQException extends XFramiumException 
{

	private static final long serialVersionUID = 4664456874499611218L;
	private String message;
	
	public IgnoreMLQException(String message)
	{
		super(ExceptionType.SCRIPT);
		this.message =message;
	}

@Override
public String toString()
{
	return message;
}
@Override
public String getMessage()
{
	return message;
}
}
