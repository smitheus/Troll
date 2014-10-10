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

import com.google.common.base.Strings;

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
	
	private static String SELECT_TRANSACTIONS_STATEMENT = "SELECT transactionview.* " + 
														  "FROM " +
			                                              "(" +
														  "    SELECT ft2.id, " + 
														  "           ft2.interchangeId, " + 
														  "           ft2.instructionId, " +  
														  "           ft2.transactionId, " +  
														  "           ft2.insertTimestamp, " +  
														  "           ft2.sourceTimestamp, " +  
														  "           ft2.country, " + 
														  "           ft2.systemType, " +  
														  "           ft2.systemCode, " + 
														  "           ft2.underInvestigation, " +  
													      "           ft2.comments, " + 
														  "           SUM(nak) as nakCnt, " +  
														  "           SUM(sla1) as sla1Cnt, " +  
														  "           SUM(sla2) as sla2Cnt " + 
														  "    FROM " + 
														  "    ( " + 
														  "        SELECT ft.underInvestigation, " + 
														  "	              ft.comments, " + 
														  "	              ft.interchangeId, " +  
														  "	              cth.instructionId, " +  
														  "	              cth.transactionId, " +  
														  "	              ft.insertTimestamp, " +  
														  "	              ft.sourceTimestamp, " +  
														  "	              ft.id, " +  
														  "	              ft.country, " + 
														  "	              ft.systemType, " + 
														  "	              ft.systemCode, " + 
														  "	              CASE WHEN cth.ackNak = 'NAK' THEN 1 ELSE 0 END as nak, " + 
														  "	              CASE WHEN (cth.responseRequired = 'Y' AND cth.sla1Due < CURRENT_TIMESTAMP) THEN 1 ELSE 0 END as sla1, " + 
														  "	              CASE WHEN (cth.responseRequired = 'Y' AND cth.sla2Due < CURRENT_TIMESTAMP) THEN 1 ELSE 0 END as sla2 " + 
														  "        FROM channelTransactionHistory cth,  " + 
														  "        ( " + 
														  "            SELECT ch.interchangeId,  " + 
														  "                   ch.country, " +  
														  "                   cbs.systemType, " + 
														  "                   cbs.systemCode, " + 
														  "                   ct.* " + 
														  "            FROM channelInterchange ch, " + 
														  "                 channelInstruction ci,  " + 
														  "                 channelTransaction ct, " + 
														  "                 coreBankingSystems cbs " + 
														  "            WHERE ch.interchangeId = ci.interchangeId  " + 
														  "            AND ci.instructionId = ct.instructionId " + 
														  "            AND ch.country = cbs.country %s " + 
														  "	       ) AS ft " + 
														  "        WHERE cth.instructionId = ft.instructionID " +
														  "        AND cth.transactionId = ft.transactionId " +
														  "    ) AS ft2 " +
														  "    GROUP BY ft2.interchangeId, " +
														  "             ft2.instructionId, " +
														  "             ft2.transactionId, " + 
														  "             ft2.sourceTimestamp, " +
														  "             ft2.country " +
														  ") AS transactionview " +
														  "%s ORDER BY transactionview.id ";
	
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
															      "       CASE WHEN (responseRequired = 'Y' AND sla1Due < CURRENT_TIMESTAMP) THEN 'Y' ELSE 'N' END as sla1Overdue, " + 
																  "	      CASE WHEN (responseRequired = 'Y' AND sla2Due < CURRENT_TIMESTAMP) THEN 'Y' ELSE 'N' END as sla2Overdue, " +
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
	public static String DATE_FILTER =  "ct.sourceTimestamp >= DATE_SUB(CURRENT_TIMESTAMP,INTERVAL %s %s)";
	public static String CUSTOM_DATE_FILTER =  "ct.sourceTimestamp BETWEEN '%s' AND '%s' ";
	
	public static String ALL_SUCCESS_FILTER =  "(nakCnt = 0 AND sla1Cnt = 0 AND sla2Cnt = 0) ";
	public static String ALL_FAILURE_FILTER =  "(nakCnt > 0 OR sla1Cnt > 0 OR sla2Cnt > 0) ";
	public static String SYSTEM_FAILURE_FILTER =  "(sla1Cnt > 0 OR sla2Cnt) ";
	public static String SLA2_FAILURE_FILTER =  "(sla2Cnt) ";
	public static String BUSINESS_FAILURE_FILTER =  "(nakCnt > 0) ";
	
	public static String NBOL_TRANSACTION_ID_FILTER =  "ctran.transactionId = '%s' ";
	public static String NBOL_INSTRUCTION_ID_FILTER =  "cintr.instructionId = '%s' ";
	public static String NBOL_INTERCHANGE_ID_FILTER =  "cinte.interchangeId = '%s' ";
	//public static String PAYEX_TRANSACTION_ID_FILTER =  "pesid.pTransactionId = '%s' ";
	//public static String PAYEX_INSTRUCTION_ID_FILTER =  "pesid.pInstructionId = '%s' ";
	
	private Connection connection;
			
	public Map<Integer, TransactionViewItemDto> selectTransactionViewItemDtos(List<String> filterCriteriaList, String successFailureFilter) throws SQLException
    {
		Map<Integer, TransactionViewItemDto> transactionViewItemDtoMap = new LinkedHashMap<Integer, TransactionViewItemDto>();
		Calendar calendar = Calendar.getInstance();
		
    	try 
    	{
            connection = ConnectionFactory.getConnection();
            
            String filterCriteriaString = "";
            for (int index = 0; index < filterCriteriaList.size(); index ++)
            {
            	filterCriteriaString += "AND " + filterCriteriaList.get(index);
            }
            
            LOG.debug(String.format(SELECT_TRANSACTIONS_STATEMENT, filterCriteriaString, Strings.isNullOrEmpty(successFailureFilter) ? "" : "WHERE " + successFailureFilter));
            
            PreparedStatement preparedStatement = connection.prepareStatement(String.format(SELECT_TRANSACTIONS_STATEMENT, filterCriteriaString, Strings.isNullOrEmpty(successFailureFilter) ? "" : "WHERE " + successFailureFilter));
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
            	transactionHistoryViewItemDto.setPesInstructionId(resultSet.getString(4)); 
            	transactionHistoryViewItemDto.setPesTransactionId(resultSet.getString(5)); 
            	transactionHistoryViewItemDto.setInsertTimestamp(resultSet.getTimestamp(6, calendar));
            	transactionHistoryViewItemDto.setSourceTimestamp(resultSet.getTimestamp(7, calendar));
            	transactionHistoryViewItemDto.setSourceSystem(resultSet.getString(8));
            	transactionHistoryViewItemDto.setEvent(EventEnum.getEvent(resultSet.getString(9)));
            	transactionHistoryViewItemDto.setAckNak(AckNakEnum.getAckNak(resultSet.getString(10)));
            	transactionHistoryViewItemDto.setText(resultSet.getString(11));
            	transactionHistoryViewItemDto.setResponseRequired(resultSet.getString(12));
            	transactionHistoryViewItemDto.setSla1Due(resultSet.getTimestamp(13, calendar)); 
            	transactionHistoryViewItemDto.setSla2Due(resultSet.getTimestamp(14, calendar));
            	transactionHistoryViewItemDto.setSla1Overdue(resultSet.getString(15)); 
            	transactionHistoryViewItemDto.setSla2Overdue(resultSet.getString(16));
            	transactionHistoryViewItemDto.setElapsedTime(resultSet.getString(17));
            	transactionHistoryViewItemDto.setSla1End(resultSet.getTimestamp(18, calendar));
            	transactionHistoryViewItemDto.setSla2End(resultSet.getTimestamp(19, calendar));
            	transactionHistoryViewItemDto.setSla1Breach(resultSet.getString(20));
            	transactionHistoryViewItemDto.setSla2Breach(resultSet.getString(21));
            	
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
