package za.co.sb.Troll.dto;

import java.sql.Date;

public class ChannelInterchangeHistoryDto 
{
	private String interchangeId;
	private Date insertTimestamp;
	private Date sourceTimestamp;
	private String sourceSystem;
	private String event;
	private String ackNak;
	private String text;
	
	public ChannelInterchangeHistoryDto()
	{
	}

	public String getInterchangeId() 
	{
		return interchangeId;
	}

	public void setInterchangeId(String interchangeId) 
	{
		this.interchangeId = interchangeId;
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
