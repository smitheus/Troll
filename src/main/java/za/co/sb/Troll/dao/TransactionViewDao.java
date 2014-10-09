package za.co.sb.Troll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import za.co.sb.Troll.db.ConnectionFactory;
import za.co.sb.Troll.db.DbUtil;
import za.co.sb.Troll.dto.TransactionHistoryViewItemDto;
import za.co.sb.Troll.dto.TransactionViewItemDto;
import za.co.sb.Troll.enums.AckNakEnum;
import za.co.sb.Troll.enums.EventEnum;

public class TransactionViewDao 
{
	private static final Logger LOG = LogManager.getLogger(TransactionViewDao.class);
	
	public static final SimpleDateFormat SQL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static String SELECT_TRANSACTIONS_STATEMENT = "SELECT ctran.id, " +
														  "       cinte.interchangeId, " +
														  "       cintr.instructionId, " +
														  "       ctran.transactionId, " +
														  "       ctran.insertTimestamp, " +
														  "       ctran.sourceTimestamp, " +
														  "       cbs.country, " +
														  "       cbs.systemType, " +
														  "       cbs.systemCode, " +
														  "       ctran.underInvestigation, " +
														  "       ctran.comments, " +
														  "       ( " +
														  "	          SELECT count(*) " + 
														  "		      FROM channelTransactionHistory cth " + 
														  "		      WHERE cth.ackNak = 'NAK' " +
														  "		      AND ctran.instructionId = cth.instructionID " + 
														  "		      AND ctran.transactionId = cth.transactionID " +
														  "	      ) AS nakCnt, " +
														  "       ( " +
														  "		      SELECT count(*) " + 
														  "		      FROM channelTransactionHistory cth " + 
														  "		      WHERE cth.responseRequired = 'Y' " +
														  "		      AND cth.sla1Due < CURRENT_TIMESTAMP " +
														  "		      AND ctran.instructionId = cth.instructionID " + 
														  "		      AND ctran.transactionId = cth.transactionID " +
														  "	      ) AS sla1BreachCnt, " +
														  "	      ( " +
														  "		      SELECT count(*) " + 
														  "		      FROM channelTransactionHistory cth " + 
														  "		      WHERE cth.responseRequired = 'Y' " +
														  "		      AND cth.sla2Due < CURRENT_TIMESTAMP " + 
														  "		      AND ctran.instructionId = cth.instructionID " + 
														  "		      AND ctran.transactionId = cth.transactionID " +
														  "	      ) AS sla2BreachCnt " +
														  "FROM channelTransaction ctran " +
														  "LEFT JOIN channelInstruction cintr ON ctran.instructionId = cintr.instructionId " +
														  "LEFT JOIN channelInterchange cinte ON cintr.interchangeId = cinte.interchangeId " +  
														  "LEFT JOIN coreBankingSystems cbs ON cinte.country = cbs.country %s " +
														  "ORDER BY ctran.id ";
	
	private static String SELECT_TRANSACTIONS_HISTORY_STATEMENT = "SELECT id, " +
															      "       instructionID, " +
															      "       transactionId, " + 
															      "       pesInstructionID, " + 
															      "       pesTransactionID, " + 
															      "       insertTimestamp, " +
															      "       sourceTimestamp, " +
															      "       sourceSystem, " +
															      "       event, " +
															      "       ackNak, " +
															      "       text, " +
															      "       responseRequired, " + 
															      "       sla1Due, " +
															      "       sla2Due, " +
															      "       elapsedTime, " + 
															      "       sla1End, " +
															      "       sla2End, " +
															      "       sla1Breach, " +
															      "       sla2Breach " +	
															      "FROM channelTransactionHistory " +
															      "WHERE instructionID = '%S' " +
															      "AND transactionId = '%s' " +
															      "ORDER BY id";
	
	private static String UPDATE_TRANSACTION_STATEMENT = "UPDATE channelTransaction SET underInvestigation = ?, comments = ? WHERE id = ?;";
	
	public static String SYSTEM_TYPE_FILTER =  "cbs.systemType = '%s' ";
	public static String COUNTRY_FILTER =  "cbs.country = '%s' ";
	public static String DATE_FILTER =  "ctran.sourceTimestamp >= DATE_SUB(CURRENT_TIMESTAMP,INTERVAL %s %s)";
	public static String CUSTOM_DATE_FILTER =  "ctran.sourceTimestamp BETWEEN '%s' AND '%s' ";
	
	public static String ALL_SUCCESS_FILTER =  "(nakCnt =  0 AND sla1BreachCnt = 0 AND sla2BreachCnt) ";
	public static String ALL_FAILURE_FILTER =  "(nakCnt > 0 OR sla1BreachCnt > 0 OR sla2BreachCnt) ";
	public static String SYSTEM_FAILURE_FILTER =  "(sla1BreachCnt > 0 OR sla2BreachCnt) ";
	public static String BUSINESS_FAILURE_FILTER =  "nakCnt > 0 ";
	
	public static String NBOL_TRANSACTION_ID_FILTER =  "ctran.transactionId = '%s' ";
	public static String NBOL_INSTRUCTION_ID_FILTER =  "cintr.instructionId = '%s' ";
	public static String NBOL_INTERCHANGE_ID_FILTER =  "cinte.interchangeId = '%s' ";
	//public static String PAYEX_TRANSACTION_ID_FILTER =  "pesid.pTransactionId = '%s' ";
	//public static String PAYEX_INSTRUCTION_ID_FILTER =  "pesid.pInstructionId = '%s' ";
	
	private Connection connection;
			
	public Map<Integer, TransactionViewItemDto> selectTransactionViewItemDtos(List<String> filterCriteriaList) throws SQLException
    {
		Map<Integer, TransactionViewItemDto> transactionViewItemDtoMap = new LinkedHashMap<Integer, TransactionViewItemDto>();
		Calendar calendar = Calendar.getInstance();
		
    	try 
    	{
            connection = ConnectionFactory.getConnection();
            
            String filterCriteriaString = "";
            for (int index = 0; index < filterCriteriaList.size(); index ++)
            {
            	if (index == 0) 
            	{
            		filterCriteriaString += "WHERE ";
            	}
            	else 
            	{
            		filterCriteriaString += "AND ";
            	}
            	
            	filterCriteriaString += filterCriteriaList.get(index);
            }
            
            LOG.debug(String.format(SELECT_TRANSACTIONS_STATEMENT, filterCriteriaString));
            
            PreparedStatement preparedStatement = connection.prepareStatement(String.format(SELECT_TRANSACTIONS_STATEMENT, filterCriteriaString));
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
            	TransactionViewItemDto transactionViewItemDto = new TransactionViewItemDto();
            	
            	transactionViewItemDto.setId(resultSet.getInt(1));
            	transactionViewItemDto.setInterchangeId(resultSet.getString(2));
            	transactionViewItemDto.setInstructionId(resultSet.getString(3));
            	transactionViewItemDto.setTransactionId(resultSet.getString(4));
            	transactionViewItemDto.setInsertTimestamp(resultSet.getTimestamp(5, calendar));
            	transactionViewItemDto.setSourceTimestamp(resultSet.getTimestamp(6, calendar));
            	transactionViewItemDto.setCountry(resultSet.getString(7));
            	transactionViewItemDto.setSystemType(resultSet.getString(8));
            	transactionViewItemDto.setSystemCode(resultSet.getString(9));
            	transactionViewItemDto.setUnderInvestigation(resultSet.getBoolean(10));
            	transactionViewItemDto.setComments(resultSet.getString(11));
            	transactionViewItemDto.setNakCnt(resultSet.getInt(12));
            	transactionViewItemDto.setSla1BreachCnt(resultSet.getInt(13));
            	transactionViewItemDto.setSla2BreachCnt(resultSet.getInt(14));

            	transactionViewItemDtoMap.put(transactionViewItemDto.getId(), transactionViewItemDto);
            }
        } 
    	catch (SQLException sqlex) 
        {
            throw sqlex;
        }
        finally 
        {
            DbUtil.close(connection);
        }
    	
    	return transactionViewItemDtoMap;
    }
	
	public void updateTransactionViewItemDto(TransactionViewItemDto transactionViewItemDto) throws SQLException
    {
    	try 
    	{
            connection = ConnectionFactory.getConnection();
            
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TRANSACTION_STATEMENT);
            
            preparedStatement.setBoolean(1, transactionViewItemDto.isUnderInvestigation());
            preparedStatement.setString(2, transactionViewItemDto.getComments());
            preparedStatement.setInt(3, transactionViewItemDto.getId());
            
            preparedStatement.execute();
        } 
    	catch (SQLException sqlex) 
        {
            throw sqlex;
        }
        finally 
        {
            DbUtil.close(connection);
        }
    }
	
	public List<TransactionHistoryViewItemDto> selectTransactionHistoryViewItemDtos(String instructionId, String transactionId) throws SQLException
    {
		List<TransactionHistoryViewItemDto> transactionHistoryViewItemDtoList = new ArrayList<TransactionHistoryViewItemDto>();
		Calendar calendar = Calendar.getInstance();
		
    	try 
    	{
            connection = ConnectionFactory.getConnection();
            
            LOG.debug(String.format(SELECT_TRANSACTIONS_HISTORY_STATEMENT, instructionId, transactionId));
            
            PreparedStatement preparedStatement = connection.prepareStatement(String.format(SELECT_TRANSACTIONS_HISTORY_STATEMENT, instructionId, transactionId));
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
            	TransactionHistoryViewItemDto transactionHistoryViewItemDto = new TransactionHistoryViewItemDto();
            	
            	transactionHistoryViewItemDto.setId(resultSet.getInt(1));
            	transactionHistoryViewItemDto.setInstructionId(resultSet.getString(2));
            	transactionHistoryViewItemDto.setTransactionId(resultSet.getString(3));
            	transactionHistoryViewItemDto.setPesInstructionID(resultSet.getString(4)); 
            	transactionHistoryViewItemDto.setPesTransactionID(resultSet.getString(5)); 
            	transactionHistoryViewItemDto.setInsertTimestamp(resultSet.getTimestamp(6, calendar));
            	transactionHistoryViewItemDto.setSourceTimestamp(resultSet.getTimestamp(7, calendar));
            	transactionHistoryViewItemDto.setSourceSystem(resultSet.getString(8));
            	transactionHistoryViewItemDto.setEvent(EventEnum.getEvent(resultSet.getString(9)));
            	transactionHistoryViewItemDto.setAckNak(AckNakEnum.getAckNak(resultSet.getString(10)));
            	transactionHistoryViewItemDto.setText(resultSet.getString(11));
            	transactionHistoryViewItemDto.setResponseRequired(resultSet.getString(12));
            	transactionHistoryViewItemDto.setSla1Due(resultSet.getTimestamp(13, calendar)); 
            	transactionHistoryViewItemDto.setSla2Due(resultSet.getTimestamp(14, calendar));
            	transactionHistoryViewItemDto.setElapsedTime(resultSet.getLong(15));
            	transactionHistoryViewItemDto.setSla1End(resultSet.getTimestamp(16, calendar));
            	transactionHistoryViewItemDto.setSla2End(resultSet.getTimestamp(17, calendar));
            	transactionHistoryViewItemDto.setSla1Breach(resultSet.getString(18));
            	transactionHistoryViewItemDto.setSla2Breach(resultSet.getString(19));
            	
            	transactionHistoryViewItemDtoList.add(transactionHistoryViewItemDto);
            }
        } 
    	catch (SQLException sqlex) 
        {
            throw sqlex;
        }
        finally 
        {
            DbUtil.close(connection);
        }
    	
    	return transactionHistoryViewItemDtoList;
    }
}
