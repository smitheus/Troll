package za.co.sb.Troll.dto.event;

import java.util.Date;

public class InterchangeEventDto 
{
	private String sourceSystem;
	private String interchangeId;
	private int numInstructions;
	private String country;
	private Date sourceTimeStamp;
	private String jmsProperties;
	
	public InterchangeEventDto()
	{
	}

	@Override
	public String toString() {
		return "InterchangeEventDto [sourceSystem=" + sourceSystem
				+ ", interchangeId=" + interchangeId + ", numInstructions="
				+ numInstructions + ", country=" + country
				+ ", sourceTimeStamp=" + sourceTimeStamp + ", jmsProperties="
				+ jmsProperties + "]";
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

	public String getJmsProperties() 
	{
		return jmsProperties;
	}

	public void setJmsProperties(String jmsProperties) 
	{
		this.jmsProperties = jmsProperties;
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
