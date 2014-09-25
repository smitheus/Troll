package za.co.sb.Troll.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import za.co.sb.Troll.db.ConnectionFactory;
import za.co.sb.Troll.db.DbUtil;
import za.co.sb.Troll.dto.event.InterchangeEventDto;

public class TrollerLogMessageDao 
{
	static final String SP_INTERCHANGE_INSERT = "{CALL InterchangeInsert (?, ?, ?, ?, ?)}";

	private Connection connection;
	
	/**
	 * Calls Stored Procedure called <b>InterchangeInsert</b> which will insert
	 * an INTERCHANGE event in the database.
	 * 
	 * Stored Procedure Parameter List
	 * <ol>
	 * <li>VARCHAR pSourceSystem</li>
	 * <li>VARCHAR pInterchangeID</li>
	 * <li>INT pNumInstructions</li>
	 * <li>VARCHAR pCountry</li>
	 * <li>TIMESTAMP pSourceTimestamp</li>
	 * <li>VARCHAR pJmsProperties</li>
	 * <li>
	 * </ol>
	 * 
	 * @throws SQLException
	 */
	public void insertInterchangeEvent(InterchangeEventDto interchangeEventDto) throws SQLException
    {
		try 
    	{
            connection = ConnectionFactory.getConnection();
            
            CallableStatement callableStatement = connection.prepareCall(SP_INTERCHANGE_INSERT);
            callableStatement.setString(1, interchangeEventDto.getSourceSystem());
            callableStatement.setString(2, interchangeEventDto.getInterchangeId());
            callableStatement.setInt(3, interchangeEventDto.getNumInstructions());
            callableStatement.setString(4, interchangeEventDto.getCountry());
            //callableStatement.setTimestamp(5, new Timestamp(interchangeEventDto.getSourceTimeStamp().getTime()));
            callableStatement.setString(5, interchangeEventDto.getJmsProperties());
            
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
}
