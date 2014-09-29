package za.co.sb.Troll.handler;

import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import za.co.sb.Troll.Troll;

public class LogMessageHandlerTests 
{
	private LogMessageHandler logMessageHandler = new LogMessageHandler();
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		Troll.loadProperties();
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
	public void testInvalidEvent() 
	{
		String eventLogMessage = "NBOL, 2014-09-11 12:23:41.201 [main] INFO - TROLL, INVALID_EVENT_NAME, nInter01, 1, KE";
		
		try 
		{
			logMessageHandler.handleLogMessage(eventLogMessage);
		} 
		catch (SQLException e) {
			fail("arse");
		}
	}
	
	

}
