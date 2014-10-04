package za.co.sb.Troll.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionViewItemDto extends Dto 
{
	public static final DateFormat CSV_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S");             
	
	// Core Banking System info
	private String country;
	private String systemType;
	private String systemCode;
	
	// Interchange Id
	private String interchangeId;
	
	// Instruction Id's
	private String instructionId;
	private String pesInstructionId;
	
	// Transaction Id's
	private String transactionId;
	private String pesTransactionId;
	
	// Transaction timestamps
	private Date insertTimestamp;
	private Date sourceTimestamp;
	
	private boolean underInvestigation;
	private String comments;
	
	public TransactionViewItemDto()
	{
	}
	
	@Override
	public String toString() 
	{
		return "TransactionViewItemDto [country=" + country + ", systemType="
				+ systemType + ", systemCode=" + systemCode
				+ ", interchangeId=" + interchangeId + ", instructionId="
				+ instructionId + ", pesInstructionId=" + pesInstructionId
				+ ", transactionId=" + transactionId + ", pesTransactionId="
				+ pesTransactionId + ", insertTimestamp=" + insertTimestamp
				+ ", sourceTimestamp=" + sourceTimestamp
				+ ", underInvestigation=" + underInvestigation + ", comments="
				+ comments + "]";
	}
	
	public List<String> getCsvExportValues() 
	{
		List<String> csvExprtValueList = new ArrayList<String>();
		
		csvExprtValueList.add(transactionId);
		csvExprtValueList.add(pesTransactionId);
		csvExprtValueList.add(instructionId);
		csvExprtValueList.add(pesInstructionId);
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
		csvExprtHeaderList.add("PES Transaction Id");
		csvExprtHeaderList.add("Instruction Id");
		csvExprtHeaderList.add("PES Instruction Id");
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

	public String getPesInstructionId() 
	{
		return pesInstructionId;
	}

	public void setPesInstructionId(String pesInstructionId) 
	{
		this.pesInstructionId = pesInstructionId;
	}

	public String getTransactionId() 
	{
		return transactionId;
	}

	public void setTransactionId(String transactionId) 
	{
		this.transactionId = transactionId;
	}

	public String getPesTransactionId() 
	{
		return pesTransactionId;
	}

	public void setPesTransactionId(String pesTransactionId) 
	{
		this.pesTransactionId = pesTransactionId;
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
}
