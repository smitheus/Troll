package za.co.sb.Troll.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChannelTransactionDto extends Dto
{
	private String instructionId;
	private String transactionId;
	private Date insertTimestamp;
	private Date sourceTimestamp;
	
	private List<ChannelTransactionHistoryDto> channelTransactionHistoryDtoList = new ArrayList<ChannelTransactionHistoryDto>();
	
	public ChannelTransactionDto()
	{
	}
	
	public ChannelTransactionDto(int id, String instructionId, String transactionId, Date insertTimestamp, Date sourceTimestamp)
	{
		this.id = id;
		this.instructionId = instructionId;
		this.transactionId = transactionId;
		this.insertTimestamp = insertTimestamp;
		this.sourceTimestamp = sourceTimestamp;
	}
	
	@Override
	public String toString() 
	{
		return "ChannelTransactionDto [instructionId=" + instructionId
				+ ", transactionId=" + transactionId + ", insertTimestamp="
				+ insertTimestamp + ", sourceTimestamp=" + sourceTimestamp
				+ ", channelTransactionHistoryDtoList="
				+ channelTransactionHistoryDtoList + "]";
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

	public List<ChannelTransactionHistoryDto> getChannelTransactionHistoryDtoList() 
	{
		return channelTransactionHistoryDtoList;
	}

	public void setChannelTransactionHistoryDtoList(List<ChannelTransactionHistoryDto> channelTransactionHistoryDtoList) 
	{
		this.channelTransactionHistoryDtoList = channelTransactionHistoryDtoList;
	}
}
