package za.co.sb.Troll.dto;

import java.util.Date;

public class ChannelTransactionHistoryDto extends Dto
{
	private String instructionId;
	private String transactionId;
	private Date insertTimestamp;
	private Date sourceTimestamp;
	private String sourceSystem;
	private String event;
	private String ackNak;
	private String text;
	
	public ChannelTransactionHistoryDto()
	{
	}
	
	@Override
	public String toString() 
	{
		return "ChannelTransactionHistoryDto [instructionId=" + instructionId
				+ ", transactionId=" + transactionId + ", insertTimestamp="
				+ insertTimestamp + ", sourceTimestamp=" + sourceTimestamp
				+ ", sourceSystem=" + sourceSystem + ", event=" + event
				+ ", ackNak=" + ackNak + ", text=" + text + "]";
	}

	public String getInstructionId() {
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

	public String getEvent() 
	{
		return event;
	}

	public void setEvent(String event) 
	{
		this.event = event;
	}

	public String getAckNak() 
	{
		return ackNak;
	}

	public void setAckNak(String ackNak) 
	{
		this.ackNak = ackNak;
	}

	public String getText() 
	{
		return text;
	}

	public void setText(String text) 
	{
		this.text = text;
	}
}
