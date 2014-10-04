package za.co.sb.Troll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import za.co.sb.Troll.db.ConnectionFactory;
import za.co.sb.Troll.db.DbUtil;
import za.co.sb.Troll.dto.TransactionViewItemDto;

public class TransactionViewDao 
{
	private static String SELECT_TRANSACTIONS_STATEMENT =	"SELECT ctran.id, " +
															"		cinte.interchangeId, " +
															"		cintr.instructionId, " +
															"		pesid.pInstructionId, " +
															"		ctran.transactionId, " +
															"		pesid.pTransactionId, " +
															"		ctran.insertTimestamp, " +
															"		ctran.sourceTimestamp, " +
															"		cbs.country, " +
															"		cbs.systemType, " +
															"		cbs.systemCode, " +
															"		ctran.underInvestigation, " +
															"		ctran.comments " +
															"FROM channelTransaction ctran " +
															"LEFT JOIN pesTransIDmap pesid ON ctran.instructionId = pesid.cInstructionID AND ctran.transactionId = pesid.cTransactionID " +
															"LEFT JOIN channelInstruction cintr ON ctran.instructionId = cintr.instructionId " +
															"LEFT JOIN channelInterchange cinte ON cintr.interchangeId = cinte.interchangeId " +
															"LEFT JOIN coreBankingSystems cbs ON cinte.country = cbs.country %s " +
															"ORDER BY ctran.id ";
	
	private static String UPDATE_TRANSACTION_STATEMENT = "UPDATE channelTransaction SET underInvestigation = ?, comments = ? WHERE id = ?;";
	
	public static String SYSTEM_TYPE_FILTER =  "cbs.systemType = '%s' ";
	public static String COUNTRY_FILTER =  "cbs.country = '%s' ";
	public static String DATE_FILTER =  "ctran.sourceTimestamp >= DATE_SUB(CURRENT_TIMESTAMP,INTERVAL %s %s)";
	
	public static String NBOL_TRANSACTION_ID_FILTER =  "ctran.transactionId = '%s' ";
	public static String NBOL_INSTRUCTION_ID_FILTER =  "cintr.instructionId = '%s' ";
	public static String NBOL_INTERCHANGE_ID_FILTER =  "cinte.interchangeId = '%s' ";
	public static String PAYEX_TRANSACTION_ID_FILTER =  "pesid.pTransactionId = '%s' ";
	public static String PAYEX_INSTRUCTION_ID_FILTER =  "pesid.pInstructionId = '%s' ";
	
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
            
            PreparedStatement preparedStatement = connection.prepareStatement(String.format(SELECT_TRANSACTIONS_STATEMENT, filterCriteriaString));
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
            	TransactionViewItemDto transactionViewItemDto = new TransactionViewItemDto();
            	
            	transactionViewItemDto.setId(resultSet.getInt(1));
            	transactionViewItemDto.setInterchangeId(resultSet.getString(2));
            	transactionViewItemDto.setInstructionId(resultSet.getString(3));
            	transactionViewItemDto.setPesInstructionId(resultSet.getString(4));
            	transactionViewItemDto.setTransactionId(resultSet.getString(5));
            	transactionViewItemDto.setPesTransactionId(resultSet.getString(6));
            	transactionViewItemDto.setInsertTimestamp(resultSet.getTimestamp(7, calendar));
            	transactionViewItemDto.setSourceTimestamp(resultSet.getTimestamp(8, calendar));
            	transactionViewItemDto.setCountry(resultSet.getString(9));
            	transactionViewItemDto.setSystemType(resultSet.getString(10));
            	transactionViewItemDto.setSystemCode(resultSet.getString(11));
            	transactionViewItemDto.setUnderInvestigation(resultSet.getBoolean(12));
            	transactionViewItemDto.setComments(resultSet.getString(13));
            	
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
}
