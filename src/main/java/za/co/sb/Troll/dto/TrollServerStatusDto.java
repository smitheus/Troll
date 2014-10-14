package za.co.sb.Troll.dto;

import java.util.Date;

import za.co.sb.Troll.enums.RagStatusEnum;

public class TrollServerStatusDto
{
	private int instanceNo; 
	private RagStatusEnum shortStatus;
	private String longStatus;
	private Date lastUpdated;
	private int slaSeconds;
	
	public TrollServerStatusDto()
	{
	}

	@Override
	public String toString() 
	{
		return "TrollServerStatusDto [instanceNo=" + instanceNo
				+ ", shortStatus=" + shortStatus + ", longStatus=" + longStatus
				+ ", lastUpdated=" + lastUpdated + ", slaSeconds=" + slaSeconds
				+ "]";
	}

	public int getInstanceNo() 
	{
		return instanceNo;
	}

	public void setInstanceNo(int instanceNo) 
	{
		this.instanceNo = instanceNo;
	}

	public RagStatusEnum getShortStatus() 
	{
		return shortStatus;
	}

	public void setShortStatus(RagStatusEnum shortStatus) 
	{
		this.shortStatus = shortStatus;
	}

	public String getLongStatus() 
	{
		return longStatus;
	}

	public void setLongStatus(String longStatus) 
	{
		this.longStatus = longStatus;
	}

	public Date getLastUpdated() 
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) 
	{
		this.lastUpdated = lastUpdated;
	}

	public int getSlaSeconds() 
	{
		return slaSeconds;
	}

	public void setSlaSeconds(int slaSeconds) 
	{
		this.slaSeconds = slaSeconds;
	}
}

	