package za.co.sb.Troll.dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ChannelInstructionDto 
{
	private String interchangeId;
	private String instructionId;
	private Date insertTimestamp;
	private Date sourceTimestamp;
	private int numTransactions;
	
	private List<ChannelInterchangeHistoryDto> channelInterchangeHistoryDtoList = new ArrayList<ChannelInterchangeHistoryDto>();
	
	public ChannelInstructionDto()
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

	public String getInstructionId() 
	{
		return instructionId;
	}

	public void setInstructionId(String instructionId) 
	{
		this.instructionId = instructionId;
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

	public int getNumTransactions() 
	{
		return numTransactions;
	}

	public void setNumTransactions(int numTransactions) 
	{
		this.numTransactions = numTransactions;
	}
}
