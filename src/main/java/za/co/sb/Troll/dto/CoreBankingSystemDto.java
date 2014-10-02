package za.co.sb.Troll.dto;

public class CoreBankingSystemDto
{
	private String systemCode;
	private String systemType;
	private String country;
	
	public CoreBankingSystemDto()
	{
	}

	@Override
	public String toString() 
	{
		return "CoreBankingSystemDto [systemCode=" + systemCode
				+ ", systemType=" + systemType + ", country=" + country + "]";
	}

	public String getSystemCode() 
	{
		return systemCode;
	}

	public void setSystemCode(String systemCode) 
	{
		this.systemCode = systemCode;
	}

	public String getSystemType() 
	{
		return systemType;
	}

	public void setSystemType(String systemType) 
	{
		this.systemType = systemType;
	}

	public String getCountry() 
	{
		return country;
	}

	public void setCountry(String country) 
	{
		this.country = country;
	}
}
