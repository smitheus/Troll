package za.co.sb.Troll.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import za.co.sb.Troll.Troll;
 
public class ConnectionFactory 
{
	private static final Logger LOG = LogManager.getLogger(ConnectionFactory.class.getName());
	
	private static ConnectionFactory INSTANCE = new ConnectionFactory();
      
    private ConnectionFactory() 
    {
    	try 
        {
            Class.forName(Troll.getDbProperties().getProperty("MYSQL_DB_DRIVER_CLASS"));
        } 
    	catch (ClassNotFoundException cnfe) 
        {
    		LOG.error("Exception instantiating ConnectingFactory", cnfe);
        }
    }
     
    private Connection createConnection() throws SQLException
    {
        Connection connection = null;
        
        try 
        {
        	String url = Troll.getDbProperties().getProperty("MYSQL_DB_URL");
        	String user = Troll.getDbProperties().getProperty("MYSQL_DB_USERNAME");
        	String password = Troll.getDbProperties().getProperty("MYSQL_DB_PASSWORD");
        	
            connection = DriverManager.getConnection(url, user, password);
        } 
        catch (SQLException sqle) 
        {
        	LOG.error("Exception creating DB connection", sqle);
        	throw sqle;
        }
        
        return connection;
    }   
     
    public static Connection getConnection() throws SQLException 
    {
        return INSTANCE.createConnection();
    }
}