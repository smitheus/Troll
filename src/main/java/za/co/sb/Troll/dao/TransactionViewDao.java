package za.co.sb.Troll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
															"		cbs.systemCode " +
															"FROM channelTransaction ctran " +
															"LEFT JOIN pesTransIDmap pesid ON ctran.instructionId = pesid.cInstructionID AND ctran.transactionId = pesid.cTransactionID " +
															"LEFT JOIN channelInstruction cintr ON ctran.instructionId = cintr.instructionId " +
															"LEFT JOIN channelInterchange cinte ON cintr.interchangeId = cinte.interchangeId " +
															"LEFT JOIN coreBankingSystems cbs ON cinte.country = cbs.country ";
	
	private Connection connection;
			
	public List<TransactionViewItemDto> selectTransactionViewItemDtos() throws SQLException
    {
		List<TransactionViewItemDto> transactionViewItemDtoList = new ArrayList<TransactionViewItemDto>();
		Calendar calendar = Calendar.getInstance();
		
    	try 
    	{
            connection = ConnectionFactory.getConnection();
            
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TRANSACTIONS_STATEMENT);
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
            	
            	transactionViewItemDtoList.add(transactionViewItemDto);
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
    	
    	return transactionViewItemDtoList;
    }
}
