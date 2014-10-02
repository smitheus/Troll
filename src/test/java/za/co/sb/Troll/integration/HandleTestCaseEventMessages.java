package za.co.sb.Troll.integration;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import za.co.sb.Troll.Troll;
import za.co.sb.Troll.handler.LogMessageHandler;

public class HandleTestCaseEventMessages 
{
	private static List<String> TROLL_EVENT_MESSAGE_LIST = new ArrayList<String>();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		try
		{
			// Load properties
			Troll.loadProperties();
			LogTestCaseEventMessages.logAllTestCaseMessages();

			// Build list of test case messages
			BufferedReader br = new BufferedReader(new FileReader("logs/test/Troll.log"));

			String logLine;
			while ((logLine = br.readLine()) != null) 
			{
				if (logLine.contains("NBOL"))
				{
					TROLL_EVENT_MESSAGE_LIST.add("NBOL, " + logLine);
				} 
				else if (logLine.contains("PAYEX"))
				{
					TROLL_EVENT_MESSAGE_LIST.add("PAYEX, " + logLine);
				}
			}

			br.close();
		}
		catch (Exception ex)
		{
			throw new Exception("Exception occurred during test setup", ex);
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception 
	{
	}

	@Before
	public void setUp() throws Exception 
	{
	}

	@After
	public void tearDown() throws Exception 
	{
	}

	@Test
	public final void testHandleLogMessages() 
	{
		LogMessageHandler logMessageHandler = new LogMessageHandler();
		
		try
		{
			for (String eventLogMessage : TROLL_EVENT_MESSAGE_LIST)
			{
				logMessageHandler.handleLogMessage(eventLogMessage);
			}
		}
		catch (Exception ex)
		{
			fail(ex.getMessage());
		}
	}
}
