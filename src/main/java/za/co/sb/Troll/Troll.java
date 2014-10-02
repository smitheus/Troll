package za.co.sb.Troll;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import za.co.sb.Troll.gui.component.TrollConsole;

public class Troll 
{
	private static final Logger LOG = LogManager.getLogger(Troll.class .getName());
	private static final String DB_PROPERTIES_FILENAME = "db.properties";
	private static final String CONSOLE_COMMAND = "Console";

	private static Properties DB_PROPERTIES = null;
	
    public static void main(String[] args)
    {
    	String command = "";
    	
    	// Validate command line arguments
    	if (args.length < 1) 
    	{
    		LOG.error("No command specified");
    		throw new IllegalArgumentException("No command specified");
    	}
    	else 
    	{
    		command = args[0];
    		
    		if (!CONSOLE_COMMAND.equalsIgnoreCase(command)) 
    		{
    			LOG.error("Invalid command specified <" + command + ">.");
    			throw new IllegalArgumentException("Invalid command specified <" + command + ">.");
    		}
    	}
    	
    	// Initialise application and run command
    	try
    	{
    		loadDbProperties();
    		
    		if (CONSOLE_COMMAND.equalsIgnoreCase(command)) 
    		{
    			runConsole();
    		}
    	}
    	catch (Exception ex)
    	{
    		LOG.error("Application Exception caught", ex);
    		ex.printStackTrace();
    	}
    }
    
    private static void runConsole() throws Exception
    {
	    EventQueue.invokeLater(new Runnable() 
	   	{
	   		public void run() 
	   		{
	   			try 
	   			{
	   				TrollConsole frame = new TrollConsole();
	   				frame.setVisible(true);
	   			} 
	   			catch (Exception e) 
	   			{
	   				e.printStackTrace();
	   			}
	   		}
	   	});
    }
    
    public static Properties getDbProperties()
    {
    	return DB_PROPERTIES;
    }
    
    
    public static void loadDbProperties() throws Exception
    {
    	InputStream input = null;
    	
		try 
		{
			input = Troll.class.getClassLoader().getResourceAsStream(DB_PROPERTIES_FILENAME);
			DB_PROPERTIES.load(input);
		} 
		catch (IOException ioex) 
		{
			LOG.error("Exception loading properties", ioex);
			throw new Exception("Exception loading properties", ioex);
		}
		finally 
		{
			if (input != null) 
			{
				try 
				{
					input.close();
				} 
				catch (IOException e) {
				}
			}
		}
    }
}
