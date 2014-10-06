package za.co.sb.Troll.dto.event;

import java.util.Date;

import za.co.sb.Troll.enums.AckNakEnum;
import za.co.sb.Troll.enums.EventEnum;

public class TransactionEventDto 
{
	private String sourceSystem;
	private String instructionId;
	private String transactionId;
	private String recInstructionId;
	private String recTransactionId;
	private EventEnum event;
	private AckNakEnum ackNak;
	private String text;
	private Date sourceTimeStamp;
	
	public TransactionEventDto()
	{
	}
	
	@Override
	public String toString() {
		return "TransactionEventDto [sourceSystem=" + sourceSystem
				+ ", instructionId=" + instructionId + ", transactionId="
				+ transactionId + ", recInstructionId=" + recInstructionId
				+ ", recTransactionId=" + recTransactionId + ", event=" + event
				+ ", ackNak=" + ackNak + ", text=" + text
				+ ", sourceTimeStamp=" + sourceTimeStamp + "]";
	}

	public String getSourceSystem() 
	{
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) 
	{
		this.sourceSystem = sourceSystem;
	}

	public String getInstructionId() 
	{
		return instructionId;
	}

	public void setInstructionId(String instructionId) 
	{
		this.instructionId = instructionId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) 
	{
		this.transactionId = transactionId;
	}

	public String getRecInstructionId() 
	{
		return recInstructionId;
	}

	public void setRecInstructionId(String recInstructionId)
	{
		this.recInstructionId = recInstructionId;
	}

	public String getRecTransactionId() 
	{
		return recTransactionId;
	}

	public void setRecTransactionId(String recTransactionId) 
	{
		this.recTransactionId = recTransactionId;
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

	public String getText() 
	{
		return text;
	}

	public void setText(String text) 
	{
		this.text = text;
	}

	public Date getSourceTimeStamp() 
	{
		return sourceTimeStamp;
	}

	public void setSourceTimeStamp(Date sourceTimeStamp) 
	{
		this.sourceTimeStamp = sourceTimeStamp;
	}
}
