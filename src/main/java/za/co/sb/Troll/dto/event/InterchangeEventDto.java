package za.co.sb.Troll.dto.event;

import java.util.Date;

import za.co.sb.Troll.enums.AckNakEnum;
import za.co.sb.Troll.enums.EventEnum;

public class InterchangeEventDto 
{
	private String sourceSystem;
	private String interchangeId;
	private int numInstructions;
	private String country;
	private Date sourceTimeStamp;
	private EventEnum event;
	private AckNakEnum ackNak;
	private String text;
	private String messageType;
	private String instrumentGroup;
	
	public InterchangeEventDto()
	{
	}

	@Override
	public String toString() {
		return "InterchangeEventDto [sourceSystem=" + sourceSystem
				+ ", interchangeId=" + interchangeId + ", numInstructions="
				+ numInstructions + ", country=" + country
				+ ", sourceTimeStamp=" + sourceTimeStamp + ", event=" + event
				+ ", ackNak=" + ackNak + ", text=" + text + ", messageType="
				+ messageType + ", instrumentGroup=" + instrumentGroup + "]";
	}

	public String getSourceSystem() 
	{
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) 
	{
		this.sourceSystem = sourceSystem;
	}

	public String getInterchangeId() 
	{
		return interchangeId;
	}

	public void setInterchangeId(String interchangeId) 
	{
		this.interchangeId = interchangeId;
	}

	public int getNumInstructions() 
	{
		return numInstructions;
	}

	public void setNumInstructions(int numInstructions) 
	{
		this.numInstructions = numInstructions;
	}

	public String getCountry() 
	{
		return country;
	}

	public void setCountry(String country) 
	{
		this.country = country;
	}

	public Date getSourceTimeStamp() 
	{
		return sourceTimeStamp;
	}

	public void setSourceTimeStamp(Date sourceTimeStamp) 
	{
		this.sourceTimeStamp = sourceTimeStamp;
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

	public String getMessageType() 
	{
		return messageType;
	}

	public void setMessageType(String messageType) 
	{
		this.messageType = messageType;
	}

	public String getInstrumentGroup() 
	{
		return instrumentGroup;
	}

	public void setInstrumentGroup(String instrumentGroup) 
	{
		this.instrumentGroup = instrumentGroup;
	}
}
