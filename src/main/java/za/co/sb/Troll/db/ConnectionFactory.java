package za.co.sb.Troll.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import za.co.sb.Troll.Troll;
 
public class ConnectionFactory 
{
	private static ConnectionFactory instance = new ConnectionFactory();
      
    private ConnectionFactory() 
    {
        try 
        {
            Class.forName(Troll.DB_PROPERTIES.getProperty("MYSQL_DB_DRIVER_CLASS"));
        } 
    	catch (ClassNotFoundException e) 
        {
            e.printStackTrace();
        }
    }
     
    private Connection createConnection() 
    {
        Connection connection = null;
        
        try 
        {
        	String url = Troll.DB_PROPERTIES.getProperty("MYSQL_DB_URL");
        	String user = Troll.DB_PROPERTIES.getProperty("MYSQL_DB_USERNAME");
        	String password = Troll.DB_PROPERTIES.getProperty("MYSQL_DB_PASSWORD");
        	
            connection = DriverManager.getConnection(url, user, password);
        } 
        catch (SQLException e) 
        {
        	e.printStackTrace();
        }
        
        return connection;
    }   
     
    public static Connection getConnection() 
    {
        return instance.createConnection();
    }
}