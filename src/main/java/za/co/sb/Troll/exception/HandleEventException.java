package za.co.sb.Troll.exception;

@SuppressWarnings("serial")
public class HandleEventException extends Exception 
{
	public HandleEventException(String errorMessage)
	{
		super(errorMessage);
	}
	
	public HandleEventException(String errorMessage, Throwable exception)
	{
		super(errorMessage, exception);
	}
}
