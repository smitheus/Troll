package za.co.sb.Troll.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import za.co.sb.Troll.db.ConnectionFactory;
import za.co.sb.Troll.db.DbUtil;
import za.co.sb.Troll.dto.TrollServerStatusDto;
import za.co.sb.Troll.dto.TrollerStatusDto;
import za.co.sb.Troll.enums.RagStatusEnum;

public class TrollStatusDao 
{
	private static final Logger LOG = LogManager.getLogger(TrollStatusDao.class.getName());
	
	// SP Calls
	private static final String SP_UPDATE_TROLL_SERVER = "{CALL updateTrollServer (?, ?, ?)}";
	private static final String SP_UPDATE_TROLLER = "{CALL updateTroller (?, ?, ?, ?, ?)}";
	private static final String SP_CHECK_TROLL_STATUS = "{CALL checkTrollStatus ()}";
	
	// Queries
	private static final String DB_STATUS_QUERY = "SELECT VERSION()";
	
	private static final String SELECT_ALL_TROLLER_STATUS_DTOS = "SELECT sourceSystem, " +
																 "       instNum, " +
																 "       serverInstNum, " +
																 "       shortStatus, " +
																 "       longStatus, " +
																 "       lastUpdated, " +
																 "       slaSeconds " +
																 "FROM trollerStatus " +
																 "ORDER BY sourceSystem, instNum";
	
	private static final String SELECT_ALL_TROLL_SERVER_STATUS_DTOS = "SELECT instNum, " + 
																      "       shortStatus, " + 
																      "       longStatus, " + 
																      "       lastUpdated, " + 
																      "       slaSeconds " + 
																      "FROM trollServers " + 
																      "ORDER BY instNum";
	private Connection connection;
	
	public void checkDbStatus() throws SQLException
    {
		try 
    	{
            connection = ConnectionFactory.getConnection();
            
            PreparedStatement preparedStatement = connection.prepareStatement(DB_STATUS_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next())
            {
            	LOG.info(resultSet.getString(1));
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
	
	/**
	 * Calls stored procedure "updateTrollServer" which will update the troll server status for the given instance number.
	 * 
	 * Stored Procedure Parameter List
	 * <ol>
	 * <li>INT pInstNum</li>
	 * <li>VARCHAR(1) pShortStatus</li>
	 * <li>VARCHAR(40) pLongStatus</li>
	 * </ol>
	 * 
	 * @param instNo
	 * @param shortStatus
	 * @param longStatus
	 * @throws SQLException
	 */
	public void updateTrollServer(int instanceNo, String shortStatus, String longStatus) throws SQLException
    {
		try 
    	{
            connection = ConnectionFactory.getConnection();
            
            CallableStatement callableStatement = connection.prepareCall(SP_UPDATE_TROLL_SERVER);
            callableStatement.setInt(1, instanceNo);
            callableStatement.setString(2, shortStatus);
            callableStatement.setString(3, longStatus);
            
            callableStatement.execute();
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
		
	/**
	 * Calls stored procedure "updateTroller" which will update the troll server status for the given instance number.
	 * 
	 * Stored Procedure Parameter List
	 * <ol>
	 * <li>VARCHAR(10) pInstNum</li>
	 * <li>INT pShortStatus</li>
	 * <li>INT pLongStatus</li>
	 * <li>VARCHAR(1) pLongStatus</li>
	 * <li>VARCHAR(40) pLongStatus</li>
	 * </ol>
	 *
	 * @param sourceSystem
	 * @param instanceNo
	 * @param severInstanceNo
	 * @param shortStatus
	 * @throws SQLException
	 */
	public void updateTroller(String sourceSystem, int instanceNo, int severInstanceNo, String shortStatus) throws SQLException
    {
		try 
    	{
            connection = ConnectionFactory.getConnection();
            
            CallableStatement callableStatement = connection.prepareCall(SP_UPDATE_TROLLER);
            callableStatement.setString(1, sourceSystem);
            callableStatement.setInt(2, instanceNo);
            callableStatement.setInt(3, severInstanceNo);
            callableStatement.setString(4, shortStatus);
            
            callableStatement.execute();
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
	
	/**
	 * Calls stored procedure "checkTrollStatus" which will update the troll server status for the given instance number.
	 * 
	 * Stored Procedure Parameter List
	 * <ol>
	 * <li>No parameters</li>
	 * </ol>
	 * 
	 * @throws SQLException
	 */
	public void checkTrollStatus() throws SQLException
    {
		try 
    	{
            connection = ConnectionFactory.getConnection();
            
            CallableStatement callableStatement = connection.prepareCall(SP_CHECK_TROLL_STATUS);
            
            callableStatement.execute();
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
	
	public List<TrollerStatusDto> selectAllTrollerStatusDtos() throws SQLException
    {
		List<TrollerStatusDto> trollerStatusDtoList = new ArrayList<TrollerStatusDto>();
		Calendar calendar = Calendar.getInstance();
		
    	try 
    	{
            connection = ConnectionFactory.getConnection();
            
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TROLLER_STATUS_DTOS);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
            	TrollerStatusDto trollerStatusDto = new TrollerStatusDto();
            	
            	
            	trollerStatusDto.setSourceSystem(resultSet.getString(1));
            	trollerStatusDto.setInstanceNo(resultSet.getInt(2));
            	trollerStatusDto.setServerInstanceNo(resultSet.getInt(3));
            	trollerStatusDto.setShortStatus(RagStatusEnum.getRagStatus(resultSet.getString(4))); 
            	trollerStatusDto.setLongStatus(resultSet.getString(5)); 
            	trollerStatusDto.setLastUpdated(resultSet.getDate(6, calendar));
            	trollerStatusDto.setSlaSeconds(resultSet.getInt(7));
            	
            	trollerStatusDtoList.add(trollerStatusDto);
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
    	
    	return trollerStatusDtoList;
    }
	
	public List<TrollServerStatusDto> selectAllTrollServerStatusDtos() throws SQLException
    {
		List<TrollServerStatusDto> trollServerStatusDtoList = new ArrayList<TrollServerStatusDto>();
		Calendar calendar = Calendar.getInstance();
		
    	try 
    	{
            connection = ConnectionFactory.getConnection();
            
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TROLL_SERVER_STATUS_DTOS);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
            	TrollServerStatusDto trollServerStatusDto = new TrollServerStatusDto();
            	
            	trollServerStatusDto.setInstanceNo(resultSet.getInt(1));
            	trollServerStatusDto.setShortStatus(RagStatusEnum.getRagStatus(resultSet.getString(2))); 
            	trollServerStatusDto.setLongStatus(resultSet.getString(3)); 
            	trollServerStatusDto.setLastUpdated(resultSet.getDate(4, calendar));
            	trollServerStatusDto.setSlaSeconds(resultSet.getInt(5));
            	
            	trollServerStatusDtoList.add(trollServerStatusDto);
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
    	
    	return trollServerStatusDtoList;
    }
}