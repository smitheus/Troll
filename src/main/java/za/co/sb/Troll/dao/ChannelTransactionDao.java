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
import za.co.sb.Troll.dto.ChannelTransactionDto;
import za.co.sb.Troll.dto.ChannelTransactionHistoryDto;
import za.co.sb.Troll.enums.AckNakEnum;
import za.co.sb.Troll.enums.EventEnum;

public class ChannelTransactionDao 
{
	private static String SELECT_ALL_TRANSACTIONS_STATEMENT = "SELECT * FROM channelTransaction;";
	private static String SELECT_ALL_TRANSACTION_HISTORY_FOR_TRANSACTION_STATEMENT = "SELECT * FROM channelTransactionHistory WHERE instructionId = ? AND transactionId = ?;";

	private Connection connection;
	
	public List<ChannelTransactionDto> selectAllTransactions() throws SQLException
    {
		List<ChannelTransactionDto> channelTransactionDtoList = new ArrayList<ChannelTransactionDto>();
		Calendar calendar = Calendar.getInstance();
		
    	try 
    	{
            connection = ConnectionFactory.getConnection();
            
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TRANSACTIONS_STATEMENT);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
            	ChannelTransactionDto channelTransactionDto = new ChannelTransactionDto();
            	
            	channelTransactionDto.setId(resultSet.getInt(1));
            	channelTransactionDto.setInstructionId(resultSet.getString(2));
            	channelTransactionDto.setTransactionId(resultSet.getString(3));
            	channelTransactionDto.setInsertTimestamp(resultSet.getTimestamp(4, calendar));
            	channelTransactionDto.setSourceTimestamp(resultSet.getTimestamp(5, calendar));
            	
				channelTransactionDto
						.setChannelTransactionHistoryDtoList(selectAllTransactionHistoryForTransaction(
								channelTransactionDto.getInstructionId(),
								channelTransactionDto.getTransactionId()));
            	
            	channelTransactionDtoList.add(channelTransactionDto);
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
    	
    	return channelTransactionDtoList;
    }
	
	public List<ChannelTransactionHistoryDto> selectAllTransactionHistoryForTransaction(String instructionId, String transactionId) throws SQLException
    {
		List<ChannelTransactionHistoryDto> channelTransactionHistoryDtoList = new ArrayList<ChannelTransactionHistoryDto>();
		Calendar calendar = Calendar.getInstance();
		
    	try 
    	{
            connection = ConnectionFactory.getConnection();
            
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TRANSACTION_HISTORY_FOR_TRANSACTION_STATEMENT);
            preparedStatement.setString(1, instructionId);
            preparedStatement.setString(2, transactionId);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
           while (resultSet.next())
            {
            	ChannelTransactionHistoryDto channelTransactionHistoryDto = new ChannelTransactionHistoryDto();
            	
            	channelTransactionHistoryDto.setId(resultSet.getInt(1));
            	channelTransactionHistoryDto.setInstructionId(resultSet.getString(2));
            	channelTransactionHistoryDto.setTransactionId(resultSet.getString(3));
            	channelTransactionHistoryDto.setPesInstructionId(resultSet.getString(4));
            	channelTransactionHistoryDto.setPesTransactionId(resultSet.getString(5));
            	channelTransactionHistoryDto.setInsertTimestamp(resultSet.getTimestamp(6, calendar));
            	channelTransactionHistoryDto.setSourceTimestamp(resultSet.getTimestamp(7, calendar));
            	channelTransactionHistoryDto.setSourceSystem(resultSet.getString(8));
            	channelTransactionHistoryDto.setEvent(EventEnum.getEvent(resultSet.getString(9)));
            	channelTransactionHistoryDto.setAckNak(AckNakEnum.getAckNak(resultSet.getString(10)));
            	channelTransactionHistoryDto.setText(resultSet.getString(11));
            	
            	channelTransactionHistoryDtoList.add(channelTransactionHistoryDto);
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
    	
    	return channelTransactionHistoryDtoList;
    }
}
