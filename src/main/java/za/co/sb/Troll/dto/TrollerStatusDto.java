package za.co.sb.Troll.dto;

import java.util.Date;

import za.co.sb.Troll.enums.RagStatusEnum;

public class TrollerStatusDto
{
	private String sourceSystem;
	private int instanceNo; 
	private int serverInstanceNo;
	private RagStatusEnum shortStatus;
	private String longStatus;
	private Date lastUpdated;
	private int slaSeconds;
	
	public TrollerStatusDto()
	{
	}

	@Override
	public String toString() 
	{
		return "TrollerStatusDto [sourceSystem=" + sourceSystem
				+ ", instanceNo=" + instanceNo + ", serverInstanceNo="
				+ serverInstanceNo + ", shortStatus=" + shortStatus
				+ ", longStatus=" + longStatus + ", lastUpdated=" + lastUpdated
				+ ", slaSeconds=" + slaSeconds + "]";
	}

	public String getSourceSystem() 
	{
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) 
	{
		this.sourceSystem = sourceSystem;
	}

	public int getInstanceNo() 
	{
		return instanceNo;
	}

	public void setInstanceNo(int instanceNo) 
	{
		this.instanceNo = instanceNo;
	}
	
	public int getServerInstanceNo() 
	{
		return serverInstanceNo;
	}

	public void setServerInstanceNo(int serverInstanceNo) 
	{
		this.serverInstanceNo = serverInstanceNo;
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
