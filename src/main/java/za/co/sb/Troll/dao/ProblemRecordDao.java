package za.co.sb.Troll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import za.co.sb.Troll.db.ConnectionFactory;
import za.co.sb.Troll.db.DbUtil;
import za.co.sb.Troll.dto.ProblemRecordDto;

public class ProblemRecordDao 
{
	private static String SELECT_ALL_PROBLEM_RECORDS_STATEMENT = "SELECT * FROM problemRecord;";
	private static String INSERT_PROBLEM_RECORDS_STATEMENT = "INSERT INTO problemRecord (interchangeID, instructionID, transactionId, insertTimestamp, sourceTimestamp, sourceSystem, record, errorMessage) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
	
	private Connection connection;
	
	public List<ProblemRecordDto> selectAllProblemRecords() throws SQLException
    {
		List<ProblemRecordDto> problemRecordDtoList = new ArrayList<ProblemRecordDto>();
		Calendar calendar = Calendar.getInstance();
		
    	try 
    	{
            connection = ConnectionFactory.getConnection();
            
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PROBLEM_RECORDS_STATEMENT);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
            	ProblemRecordDto problemRecordDto = new ProblemRecordDto();
            	
            	problemRecordDto.setId(resultSet.getInt(1));
            	problemRecordDto.setInterchangeId(resultSet.getString(2));
            	problemRecordDto.setInstructionId(resultSet.getString(3));
            	problemRecordDto.setTransactionId(resultSet.getString(4));
            	problemRecordDto.setInsertTimestamp(resultSet.getTimestamp(5, calendar));
            	problemRecordDto.setSourceTimestamp(resultSet.getTimestamp(6, calendar));
            	problemRecordDto.setSourceSystem(resultSet.getString(7));
            	problemRecordDto.setRecord(resultSet.getString(8));
            	problemRecordDto.setErrorMessage(resultSet.getString(9));
            	
            	problemRecordDtoList.add(problemRecordDto);
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
    	
    	return problemRecordDtoList;
    }
	
	public void insertProblemRecords(ProblemRecordDto problemRecordDto) throws SQLException
    {
		try {
            connection = ConnectionFactory.getConnection();
            
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROBLEM_RECORDS_STATEMENT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, problemRecordDto.getInterchangeId());
            preparedStatement.setString(2, problemRecordDto.getInstructionId());
            preparedStatement.setString(3, problemRecordDto.getTransactionId());
            preparedStatement.setTimestamp(4, new Timestamp(problemRecordDto.getInsertTimestamp().getTime()));
            preparedStatement.setTimestamp(5, new Timestamp(problemRecordDto.getSourceTimestamp().getTime()));
            preparedStatement.setString(6, problemRecordDto.getSourceSystem());
            preparedStatement.setString(7, problemRecordDto.getRecord());
            preparedStatement.setString(8, problemRecordDto.getErrorMessage());
                    
            preparedStatement.execute();
            
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next())
            {
            	problemRecordDto.setId(rs.getInt(1));
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
    }
}
