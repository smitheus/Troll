package za.co.sb.Troll.dto.event;

import java.util.Date;

import za.co.sb.Troll.enums.AckNakEnum;
import za.co.sb.Troll.enums.EventEnum;

public class InstructionEventDto 
{
	private String interchangeId;
	private String instructionId;
	private String recInstructionId;
	private Date sourceTimeStamp;
	private String sourceSystem;
	private EventEnum event;
	private AckNakEnum ackNak;
	private String text;
	private int numInstructions;
	private String country;
	
	public InstructionEventDto()
	{
	}

	@Override
	public String toString() 
	{
		return "InstructionEventDto [interchangeId=" + interchangeId
				+ ", instructionId=" + instructionId + ", recInstructionId="
				+ recInstructionId + ", sourceTimeStamp=" + sourceTimeStamp
				+ ", sourceSystem=" + sourceSystem + ", event=" + event
				+ ", ackNak=" + ackNak + ", text=" + text
				+ ", numInstructions=" + numInstructions + ", country="
				+ country + "]";
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

	public String getRecInstructionId() 
	{
		return recInstructionId;
	}

	public void setRecInstructionId(String recInstructionId) 
	{
		this.recInstructionId = recInstructionId;
	}

	public Date getSourceTimeStamp() 
	{
		return sourceTimeStamp;
	}

	public void setSourceTimeStamp(Date sourceTimeStamp) 
	{
		this.sourceTimeStamp = sourceTimeStamp;
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
}
