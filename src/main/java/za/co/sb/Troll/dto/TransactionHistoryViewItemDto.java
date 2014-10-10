package za.co.sb.Troll.dto;

import java.util.Date;

import com.google.common.base.Strings;

import za.co.sb.Troll.enums.AckNakEnum;
import za.co.sb.Troll.enums.EventEnum;

public class TransactionHistoryViewItemDto extends Dto 
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
	private String responseRequired;
	private Date sla1Due; 
	private Date sla2Due; 
	private String sla1Overdue;
	private String sla2Overdue;
	private Long elapsedTime; 
	private Date sla1End;
	private Date sla2End;
	private String sla1Breach;
	private String sla2Breach;
	
	public TransactionHistoryViewItemDto()
	{
	}

	@Override
	public String toString() 
	{
		return "TransactionHistoryViewItemDto [instructionId=" + instructionId
				+ ", transactionId=" + transactionId + ", pesInstructionId="
				+ pesInstructionId + ", pesTransactionId=" + pesTransactionId
				+ ", insertTimestamp=" + insertTimestamp + ", sourceTimestamp="
				+ sourceTimestamp + ", sourceSystem=" + sourceSystem
				+ ", event=" + event + ", ackNak=" + ackNak + ", text=" + text
				+ ", responseRequired=" + responseRequired + ", sla1Due="
				+ sla1Due + ", sla2Due=" + sla2Due + ", sla1Overdue="
				+ sla1Overdue + ", sla2Overdue=" + sla2Overdue
				+ ", elapsedTime=" + elapsedTime + ", sla1End=" + sla1End
				+ ", sla2End=" + sla2End + ", sla1Breach=" + sla1Breach
				+ ", sla2Breach=" + sla2Breach + "]";
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
		return pesTransactionId;
	}

	public void setPesTransactionId(String pesTransactionId) 
	{
		this.pesTransactionId = pesTransactionId;
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

	public String getText() 
	{
		return text;
	}

	public void setText(String text) 
	{
		this.text = text;
	}

	public String isResponseRequired() 
	{
		return responseRequired;
	}

	public void setResponseRequired(String responseRequired) 
	{
		this.responseRequired = responseRequired;
	}

	public Date getSla1Due() 
	{
		return sla1Due;
	}

	public void setSla1Due(Date sla1Due) 
	{
		this.sla1Due = sla1Due;
	}

	public Date getSla2Due() 
	{
		return sla2Due;
	}

	public void setSla2Due(Date sla2Due) 
	{
		this.sla2Due = sla2Due;
	}

	public Long getElapsedTime() 
	{
		return elapsedTime;
	}
	
	public void setElapsedTime(String elapsedTimeString) 
	{
		this.elapsedTime = Strings.isNullOrEmpty(elapsedTimeString) ? null : Long.valueOf(elapsedTimeString);
	}

	public void setElapsedTime(Long elapsedTime) 
	{
		this.elapsedTime = elapsedTime;
	}

	public Date getSla1End() 
	{
		return sla1End;
	}

	public void setSla1End(Date sla1End) 
	{
		this.sla1End = sla1End;
	}

	public Date getSla2End() 
	{
		return sla2End;
	}

	public void setSla2End(Date sla2End) 
	{
		this.sla2End = sla2End;
	}

	public String isSla1Breach() 
	{
		return sla1Breach;
	}

	public void setSla1Breach(String sla1Breach) 
	{
		this.sla1Breach = sla1Breach;
	}

	public String isSla2Breach() 
	{
		return sla2Breach;
	}

	public void setSla2Breach(String sla2Breach) 
	{
		this.sla2Breach = sla2Breach;
	}
	
	public String isSla1Overdue() 
	{
		return sla1Overdue;
	}

	public void setSla1Overdue(String sla1Overdue) 
	{
		this.sla1Overdue = sla1Overdue;
	}
	
	public String isSla2Overdue() 
	{
		return sla2Overdue;
	}

	public void setSla2Overdue(String sla2Overdue) 
	{
		this.sla2Overdue = sla2Overdue;
	}
}
