package za.co.sb.Troll.dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ChannelInterchangeDto extends Dto
{
	private String interchangeId;
	private Date insertTimestamp;
	private Date sourceTimestamp;
	private int numInstructions; 
	private String country;
	private CoreBankingSystemsDto coreBankingSystem;
	private String tempJmsHeaders;
	
	private List<ChannelInterchangeHistoryDto> channelInterchangeHistoryDtoList = new ArrayList<ChannelInterchangeHistoryDto>();
	
	public ChannelInterchangeDto()
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

	public CoreBankingSystemsDto getCoreBankingSystem() 
	{
		return coreBankingSystem;
	}

	public void setCoreBankingSystem(CoreBankingSystemsDto coreBankingSystem) 
	{
		this.coreBankingSystem = coreBankingSystem;
	}

	public String getTempJmsHeaders() 
	{
		return tempJmsHeaders;
	}

	public void setTempJmsHeaders(String tempJmsHeaders) 
	{
		this.tempJmsHeaders = tempJmsHeaders;
	}
}
