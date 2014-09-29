package za.co.sb.Troll.exception;

import za.co.sb.Troll.dto.ProblemRecordDto;

@SuppressWarnings("serial")
public class HandleEventException extends Exception 
{
	private ProblemRecordDto problemRecordDto;
	
	public HandleEventException(String errorMessage, ProblemRecordDto problemRecordDto)
	{
		super(errorMessage);
		
		this.problemRecordDto = problemRecordDto;
	}
	
	public HandleEventException(String errorMessage, ProblemRecordDto problemRecordDto, Throwable exception)
	{
		super(errorMessage, exception);
		
		this.problemRecordDto = problemRecordDto;
	}

	public ProblemRecordDto getProblemRecordDto() 
	{
		return problemRecordDto;
	}
}
