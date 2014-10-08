package za.co.sb.Troll.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionViewItemDto extends Dto 
{
	public static final DateFormat CSV_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S");             
	
	private String interchangeId;
	private String instructionId;
	private String transactionId;
	private Date insertTimestamp;
	private Date sourceTimestamp;
	private boolean underInvestigation;
	private String comments;
	private String country;
	private String systemType;
	private String systemCode;
	private int nakCnt;
	private int sla1BreachCnt;
	private int sla2BreachCnt;
	
	public TransactionViewItemDto()
	{
	}
	
	
	
	@Override
	public String toString() {
		return "TransactionViewItemDto [interchangeId=" + interchangeId
				+ ", instructionId=" + instructionId + ", transactionId="
				+ transactionId + ", insertTimestamp=" + insertTimestamp
				+ ", sourceTimestamp=" + sourceTimestamp
				+ ", underInvestigation=" + underInvestigation + ", comments="
				+ comments + ", country=" + country + ", systemType="
				+ systemType + ", systemCode=" + systemCode + ", nakCnt="
				+ nakCnt + ", sla1BreachCnt=" + sla1BreachCnt
				+ ", sla2BreachCnt=" + sla2BreachCnt + "]";
	}



	public List<String> getCsvExportValues() 
	{
		List<String> csvExprtValueList = new ArrayList<String>();
		
		csvExprtValueList.add(transactionId);
		csvExprtValueList.add(instructionId);
		csvExprtValueList.add(interchangeId);
		csvExprtValueList.add(CSV_DATE_FORMAT.format(insertTimestamp));
		csvExprtValueList.add(CSV_DATE_FORMAT.format(sourceTimestamp));
		csvExprtValueList.add(country);
		csvExprtValueList.add(systemType);
		csvExprtValueList.add(systemCode);
		csvExprtValueList.add(underInvestigation ? "YES" : "NO");
		csvExprtValueList.add(comments);
		
		return csvExprtValueList;
	}
	
	public static List<String> getCsvExportHeaders() 
	{
		List<String> csvExprtHeaderList = new ArrayList<String>();
		
		csvExprtHeaderList.add("Transaction Id");
		csvExprtHeaderList.add("Instruction Id");
		csvExprtHeaderList.add("Interchange Id");
		csvExprtHeaderList.add("Insert Timestamp");
		csvExprtHeaderList.add("Source Timestamp");
		csvExprtHeaderList.add("Country");
		csvExprtHeaderList.add("System Type");
		csvExprtHeaderList.add("System Code");
		csvExprtHeaderList.add("Under Investigation");
		csvExprtHeaderList.add("Comments");
		
		return csvExprtHeaderList;
	}

	public String getCountry() 
	{
		return country;
	}

	public void setCountry(String country) 
	{
		this.country = country;
	}

	public String getSystemType() 
	{
		return systemType;
	}

	public void setSystemType(String systemType) 
	{
		this.systemType = systemType;
	}

	public String getSystemCode() 
	{
		return systemCode;
	}

	public void setSystemCode(String systemCode) 
	{
		this.systemCode = systemCode;
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
	
	public boolean isUnderInvestigation() 
	{
		return underInvestigation;
	}

	public void setUnderInvestigation(boolean underInvestigation) 
	{
		this.underInvestigation = underInvestigation;
	}

	public String getComments() 
	{
		return comments;
	}

	public void setComments(String comments) 
	{
		this.comments = comments;
	}

	public int getNakCnt() 
	{
		return nakCnt;
	}

	public void setNakCnt(int nakCnt) 
	{
		this.nakCnt = nakCnt;
	}

	public int getSla1BreachCnt() 
	{
		return sla1BreachCnt;
	}

	public void setSla1BreachCnt(int sla1BreachCnt) 
	{
		this.sla1BreachCnt = sla1BreachCnt;
	}

	public int getSla2BreachCnt() 
	{
		return sla2BreachCnt;
	}

	public void setSla2BreachCnt(int sla2BreachCnt) 
	{
		this.sla2BreachCnt = sla2BreachCnt;
	}
}
