package za.co.sb.Troll.dto;

import java.util.Date;

import za.co.sb.Troll.enums.AckNakEnum;
import za.co.sb.Troll.enums.EventEnum;

public class ChannelTransactionHistoryDto extends Dto
{
	private String instructionId;
	private String transactionId;
	private String pesInstructionId;
	private String pesTransactionId;
	private Date insertTimestamp;
	private Date sourceTimestamp;
	private String sourceSystem;
	private EventEnum event;
	private AckNakEnum ackNak;
	private String text;
	private boolean responseRequired;
	
	public ChannelTransactionHistoryDto()
	{
	}

	@Override
	public String toString() 
	{
		return "ChannelTransactionHistoryDto [instructionId=" + instructionId
				+ ", transactionId=" + transactionId + ", pesInstructionID="
				+ pesInstructionId + ", pesTransactionID=" + pesTransactionId
				+ ", insertTimestamp=" + insertTimestamp + ", sourceTimestamp="
				+ sourceTimestamp + ", sourceSystem=" + sourceSystem
				+ ", event=" + event + ", ackNak=" + ackNak + ", text=" + text
				+ ", responseRequired=" + responseRequired + "]";
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

	public String getPesInstructionId() 
	{
		return pesInstructionId;
	}

	public void setPesInstructionId(String pesInstructionId) 
	{
		this.pesInstructionId = pesInstructionId;
	}

	public String getPesTransactionId() 
	{
		return pesInstructionId;
	}

	public void setPesTransactionId(String pesInstructionId) 
	{
		this.pesInstructionId = pesInstructionId;
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

	public EventEnum getEvent() 
	{
		return event;
	}

	public void setEvent(EventEnum event) 
	{
		this.event = event;
	}

	public AckNakEnum getAckNak() 
	{
		return ackNak;
	}

	public void setAckNak(AckNakEnum ackNak) 
	{
		this.ackNak = ackNak;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) 
	{
		this.text = text;
	}

	public boolean isResponseRequired() 
	{
		return responseRequired;
	}

	public void setResponseRequired(boolean responseRequired) 
	{
		this.responseRequired = responseRequired;
	}	
}
