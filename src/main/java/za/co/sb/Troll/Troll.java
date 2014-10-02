package za.co.sb.Troll;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import za.co.sb.Troll.dao.ChannelTransactionDao;
import za.co.sb.Troll.dto.ChannelTransactionDto;

public class Troll 
{
	
	private static final Logger LOG = LogManager.getLogger(Troll.class .getName());
	
	
	public static final String DB_PROPERTIES_FILENAME = "db.properties";
	
	public static Properties DB_PROPERTIES = new Properties();
	
    public static void main(String[] args)
    {
    	try
    	{
    		
    		loadProperties();
    		
    		ChannelTransactionDao channelTransactionDao = new ChannelTransactionDao();
    		
    		List<ChannelTransactionDto> list = channelTransactionDao.selectAllTransactions();
    		
    		for (ChannelTransactionDto channelTransactionDto : list)
    		{
    			System.out.println(channelTransactionDto.toString());
    		}
    		
    		
    		
	    	EventQueue.invokeLater(new Runnable() 
			{
				public void run() 
				{
					try 
					{
						//ConsoleFrame frame = new ConsoleFrame();
						//frame.setVisible(true);
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}
				}
			});
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
    
    public static void loadProperties() throws Exception
    {
    	InputStream input = null;
    	
		try 
		{
			
			input = Troll.class.getClassLoader().getResourceAsStream(DB_PROPERTIES_FILENAME);
			DB_PROPERTIES.load(input);
		} 
		catch (IOException ioex) 
		{
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
