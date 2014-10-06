package za.co.sb.Troll.dto;

import java.util.Date;

public class ProblemRecordDto extends Dto
{
	private String interchangeId;
	private String instructionId;
	private String transactionId;
	private Date insertTimestamp;
	private Date sourceTimestamp;
	private String sourceSystem;
	private String record;
	private String errorMessage;
	
	public ProblemRecordDto()
	{
	}
	
	public ProblemRecordDto(String interchangeId, String instructionId,
			String transactionId, Date sourceTimestamp, String sourceSystem,
			String errorMessage) 
	{
		this.interchangeId = interchangeId;
		this.instructionId = instructionId;
		this.transactionId = transactionId;
		this.sourceTimestamp = sourceTimestamp;
		this.sourceSystem = sourceSystem;
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "ProblemRecordDto [interchangeId=" + interchangeId
				+ ", instructionId=" + instructionId + ", transactionId="
				+ transactionId + ", insertTimestamp=" + insertTimestamp
				+ ", sourceTimestamp=" + sourceTimestamp + ", sourceSystem="
				+ sourceSystem + ", record=" + record + ", errorMessage="
				+ errorMessage + "]";
	}

	public String getInterchangeId() 
	{
		return interchangeId;
	}

	public void setInterchangeId(String interchangeId) 
	{
		this.interchangeId = interchangeId;
	}

	public String getInstructionId() 
	{
		return instructionId;
	}

	public void setInstructionId(String instructionId) 
	{
		this.instructionId = instructionId;
	}

	public String getTransactionId() 
	{
		return transactionId;
	}

	public void setTransactionId(String transactionId) 
	{
		this.transactionId = transactionId;
	}

	public Date getInsertTimestamp() 
	{
		return insertTimestamp;
	}

	public void setInsertTimestamp(Date insertTimestamp) 
	{
		this.insertTimestamp = insertTimestamp;
	}

	public Date getSourceTimestamp() 
	{
		return sourceTimestamp;
	}

	public void setSourceTimestamp(Date sourceTimestamp) 
	{
		this.sourceTimestamp = sourceTimestamp;
	}

	public String getSourceSystem() 
	{
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) 
	{
		this.sourceSystem = sourceSystem;
	}

	public String getRecord() 
	{
		return record;
	}

	public void setRecord(String record) 
	{
		this.record = record;
	}

	public String getErrorMessage() 
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) 
	{
		this.errorMessage = errorMessage;
	}
}
