package za.co.sb.Troll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import za.co.sb.Troll.db.ConnectionFactory;
import za.co.sb.Troll.db.DbUtil;

public class DbStatusDao 
{
	private static final Logger LOG = LogManager.getLogger(DbStatusDao.class.getName());
	
	private static String DB_STATUS_QUERY = "SELECT VERSION()";

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
}
