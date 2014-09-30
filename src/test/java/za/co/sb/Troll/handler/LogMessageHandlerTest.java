package za.co.sb.Troll.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import za.co.sb.Troll.dto.event.InstructionEventDto;
import za.co.sb.Troll.dto.event.InterchangeEventDto;
import za.co.sb.Troll.dto.event.TransactionEventDto;
import za.co.sb.Troll.enums.AckNakEnum;
import za.co.sb.Troll.enums.EventEnum;
import za.co.sb.Troll.exception.HandleEventException;

import com.google.common.collect.Lists;

public class LogMessageHandlerTest 
{
	LogMessageHandler logMessageHandler = new LogMessageHandler();
	
	//@Rule
	//public ExpectedException exception = ExpectedException.none();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
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
	public final void testValidLogMessage() 
	{
		String validEventLogMessage = "SOURCESYSTEM, 2014-09-11 12:23:41.201 [main] INFO - TROLL, EVENT, PROP 1, PROP 2,, PROP 3,";
		
		try 
		{
			String sourceSystem = logMessageHandler.extractSourceSytem(validEventLogMessage);
			String logMessage = logMessageHandler.extractLogMessage(validEventLogMessage, sourceSystem);
			Date sourceTimestamp = logMessageHandler.extractSourceTimestamp(logMessage, sourceSystem);
			List<String> logMessagePropList = logMessageHandler.extractLogMessagePropList(logMessage, sourceSystem);
			
			assertEquals("SOURCESYSTEM", sourceSystem);
			assertEquals("2014-09-11 12:23:41.201 [main] INFO - TROLL, EVENT, PROP 1, PROP 2,, PROP 3,", logMessage);
			assertEquals(LogMessageHandler.DEFAULT_LOG_DATE_FORMAT.parse("2014-09-11 12:23:41.201"), sourceTimestamp);
			assertEquals("2014-09-11 12:23:41.201 [main] INFO - TROLL, EVENT, PROP 1, PROP 2,, PROP 3,", logMessage);
			assertEquals(logMessagePropList.size(), 6);
			assertEquals(logMessagePropList.get(0), "EVENT");
			assertEquals(logMessagePropList.get(1), "PROP 1");
			assertEquals(logMessagePropList.get(2), "PROP 2");
			assertEquals(logMessagePropList.get(3), "");
			assertEquals(logMessagePropList.get(4), "PROP 3");
			assertEquals(logMessagePropList.get(5), "");
		} 
		catch (HandleEventException | ParseException ex) 
		{
			fail(ex.getMessage());
		}
	}
	
	@Test
	public final void testUnparsableSourceTimestampInLogMessage() 
	{
		String invalidLogMessage = "99999999999999999 [main] INFO - TROLL, EVENT, PROP 1, PROP 2,, PROP 3,";
		
		try 
		{
			logMessageHandler.extractSourceTimestamp(invalidLogMessage, "SOURCESYSTEM");
			fail("Excepted exception <HandleEventException> not thrown");
			
		} 
		catch (HandleEventException heex) 
		{
			assertEquals("Unable to extract <SOURCE TIMESTAMP> from event log message", heex.getMessage());
		}
	}
	
	@Test
	public final void testUnparsablePropListInLogMessage() 
	{
		String invalidLogMessage = "2014-09-11 12:23:41.201 [main] INFO - EVENT, PROP 1, PROP 2,, PROP 3,";
		
		try 
		{
			logMessageHandler.extractLogMessagePropList(invalidLogMessage, "SOURCESYSTEM");
			fail("Excepted exception <HandleEventException> not thrown");
			
		} 
		catch (HandleEventException heex) 
		{
			assertEquals("Unable to extract <LOG MESSAGE PROPERTIES> from event log message", heex.getMessage());
		}
	}
	
	@Test
	public final void testHandleInterchangeEvent() 
	{
		try
		{
			String sourceSystem = "SOURCESYSTEM";
			Date sourceTimestamp = Calendar.getInstance().getTime();
			List<String> logMessagePropList =  Lists.newArrayList("INTER", "nInter01", "1", "KE", "PAYMENT", "LOWCARE"); 
					
			InterchangeEventDto interchangeEventDto = logMessageHandler.handleInterchangeEvent(sourceSystem, sourceTimestamp, logMessagePropList);
			
			assertEquals("nInter01", interchangeEventDto.getInterchangeId());
			assertEquals(1, interchangeEventDto.getNumInstructions());
			assertEquals("KE", interchangeEventDto.getCountry());
			assertEquals("PAYMENT", interchangeEventDto.getMessageType());
			assertEquals("LOWCARE", interchangeEventDto.getInstrumentGroup());
			assertEquals("SOURCESYSTEM", interchangeEventDto.getSourceSystem());
			assertEquals(sourceTimestamp, interchangeEventDto.getSourceTimeStamp());
		}
		catch (HandleEventException heex) 
		{
			fail(heex.getMessage());
		}
	}
	
	@Test
	public final void testHandleInterchangeUpdateEventSent() 
	{
		try
		{
			// TEST EventEnum.SENT
			String sourceSystem = "SOURCESYSTEM";
			Date sourceTimestamp = Calendar.getInstance().getTime();
			List<String> logMessagePropList =  Lists.newArrayList("SENT", "pInter01"); 
					
			InterchangeEventDto interchangeEventDto = logMessageHandler.handleInterchangeUpdateEvent(sourceSystem, sourceTimestamp, logMessagePropList);
			
			assertEquals(EventEnum.SENT, interchangeEventDto.getEvent());
			assertEquals("pInter01", interchangeEventDto.getInterchangeId());
			assertEquals("SOURCESYSTEM", interchangeEventDto.getSourceSystem());
			assertEquals(sourceTimestamp, interchangeEventDto.getSourceTimeStamp());
			assertEquals(0, interchangeEventDto.getNumInstructions());
			assertEquals(null, interchangeEventDto.getCountry());
			assertEquals(null, interchangeEventDto.getMessageType());
			assertEquals(null, interchangeEventDto.getInstrumentGroup());
			assertEquals(AckNakEnum.UNKNOWN, interchangeEventDto.getAckNak());
			assertEquals(null, interchangeEventDto.getText());
		}
		catch (HandleEventException heex) 
		{
			fail(heex.getMessage());
		}
	}
	
	@Test
	public final void testHandleInterchangeUpdateEventRecd() 
	{
		try
		{
			// TEST EventEnum.RECD
			String sourceSystem = "SOURCESYSTEM";
			Date sourceTimestamp = Calendar.getInstance().getTime();
			List<String> logMessagePropList =  Lists.newArrayList("RECD", "nInter01"); 
					
			InterchangeEventDto interchangeEventDto = logMessageHandler.handleInterchangeUpdateEvent(sourceSystem, sourceTimestamp, logMessagePropList);
			
			assertEquals(EventEnum.RECD, interchangeEventDto.getEvent());
			assertEquals("nInter01", interchangeEventDto.getInterchangeId());
			assertEquals("SOURCESYSTEM", interchangeEventDto.getSourceSystem());
			assertEquals(sourceTimestamp, interchangeEventDto.getSourceTimeStamp());
			assertEquals(0, interchangeEventDto.getNumInstructions());
			assertEquals(null, interchangeEventDto.getCountry());
			assertEquals(null, interchangeEventDto.getMessageType());
			assertEquals(null, interchangeEventDto.getInstrumentGroup());
			assertEquals(AckNakEnum.UNKNOWN, interchangeEventDto.getAckNak());
			assertEquals(null, interchangeEventDto.getText());
		}
		catch (HandleEventException heex) 
		{
			fail(heex.getMessage());
		}
	}
	
	@Test
	public final void testHandleInterchangeUpdateEventInterim() 
	{
		try
		{
			// TEST EventEnum.INTERIM
			String sourceSystem = "SOURCESYSTEM";
			Date sourceTimestamp = Calendar.getInstance().getTime();
			List<String> logMessagePropList =  Lists.newArrayList("INTERIM", "nInter01"); 
					
			InterchangeEventDto interchangeEventDto = logMessageHandler.handleInterchangeUpdateEvent(sourceSystem, sourceTimestamp, logMessagePropList);
			
			assertEquals(EventEnum.INTERIM, interchangeEventDto.getEvent());
			assertEquals("nInter01", interchangeEventDto.getInterchangeId());
			assertEquals("SOURCESYSTEM", interchangeEventDto.getSourceSystem());
			assertEquals(sourceTimestamp, interchangeEventDto.getSourceTimeStamp());
			assertEquals(0, interchangeEventDto.getNumInstructions());
			assertEquals(null, interchangeEventDto.getCountry());
			assertEquals(null, interchangeEventDto.getMessageType());
			assertEquals(null, interchangeEventDto.getInstrumentGroup());
			assertEquals(AckNakEnum.UNKNOWN, interchangeEventDto.getAckNak());
			assertEquals(null, interchangeEventDto.getText());
		}
		catch (HandleEventException heex) 
		{
			fail(heex.getMessage());
		}
	}
	
	@Test
	public final void testHandleInterchangeUpdateEventInterimAck() 
	{
		try
		{
			// TEST EventEnum.INTERIM
			String sourceSystem = "SOURCESYSTEM";
			Date sourceTimestamp = Calendar.getInstance().getTime();
			List<String> logMessagePropList =  Lists.newArrayList("INTERIM", "nInter01", "ACK"); 
					
			InterchangeEventDto interchangeEventDto = logMessageHandler.handleInterchangeUpdateEvent(sourceSystem, sourceTimestamp, logMessagePropList);
			
			assertEquals(EventEnum.INTERIM, interchangeEventDto.getEvent());
			assertEquals("nInter01", interchangeEventDto.getInterchangeId());
			assertEquals("SOURCESYSTEM", interchangeEventDto.getSourceSystem());
			assertEquals(sourceTimestamp, interchangeEventDto.getSourceTimeStamp());
			assertEquals(0, interchangeEventDto.getNumInstructions());
			assertEquals(null, interchangeEventDto.getCountry());
			assertEquals(null, interchangeEventDto.getMessageType());
			assertEquals(null, interchangeEventDto.getInstrumentGroup());
			assertEquals(AckNakEnum.ACK, interchangeEventDto.getAckNak());
			assertEquals(null, interchangeEventDto.getText());
		}
		catch (HandleEventException heex) 
		{
			fail(heex.getMessage());
		}
	}
	
	@Test
	public final void testHandleInterchangeUpdateEventInterimNak() 
	{
		try
		{
			// TEST EventEnum.INTERIM
			String sourceSystem = "SOURCESYSTEM";
			Date sourceTimestamp = Calendar.getInstance().getTime();
			List<String> logMessagePropList =  Lists.newArrayList("INTERIM", "nInter01", "NAK", "ERROR"); 
					
			InterchangeEventDto interchangeEventDto = logMessageHandler.handleInterchangeUpdateEvent(sourceSystem, sourceTimestamp, logMessagePropList);
			
			assertEquals(EventEnum.INTERIM, interchangeEventDto.getEvent());
			assertEquals("nInter01", interchangeEventDto.getInterchangeId());
			assertEquals("SOURCESYSTEM", interchangeEventDto.getSourceSystem());
			assertEquals(sourceTimestamp, interchangeEventDto.getSourceTimeStamp());
			assertEquals(0, interchangeEventDto.getNumInstructions());
			assertEquals(null, interchangeEventDto.getCountry());
			assertEquals(null, interchangeEventDto.getMessageType());
			assertEquals(null, interchangeEventDto.getInstrumentGroup());
			assertEquals(AckNakEnum.NAK, interchangeEventDto.getAckNak());
			assertEquals("ERROR", interchangeEventDto.getText());
		}
		catch (HandleEventException heex) 
		{
			fail(heex.getMessage());
		}
	}
	
	@Test
	public final void testHandleInterchangeUpdateEventMaxAck() 
	{
		try
		{
			// TEST EventEnum.MAX ACK
			String sourceSystem = "SOURCESYSTEM";
			Date sourceTimestamp = Calendar.getInstance().getTime();
			List<String> logMessagePropList =  Lists.newArrayList("MAX", "pInter01", "ACK"); 
					
			InterchangeEventDto interchangeEventDto = logMessageHandler.handleInterchangeUpdateEvent(sourceSystem, sourceTimestamp, logMessagePropList);
			
			assertEquals(EventEnum.MAX, interchangeEventDto.getEvent());
			assertEquals("pInter01", interchangeEventDto.getInterchangeId());
			assertEquals("SOURCESYSTEM", interchangeEventDto.getSourceSystem());
			assertEquals(sourceTimestamp, interchangeEventDto.getSourceTimeStamp());
			assertEquals(0, interchangeEventDto.getNumInstructions());
			assertEquals(null, interchangeEventDto.getCountry());
			assertEquals(null, interchangeEventDto.getMessageType());
			assertEquals(null, interchangeEventDto.getInstrumentGroup());
			assertEquals(AckNakEnum.ACK, interchangeEventDto.getAckNak());
			assertEquals(null, interchangeEventDto.getText());
		}
		catch (HandleEventException heex) 
		{
			fail(heex.getMessage());
		}
	}
	
	@Test
	public final void testHandleInterchangeUpdateEventMaxNak() 
	{
		try
		{
			// TEST EventEnum.MAX NAK
			String sourceSystem = "SOURCESYSTEM";
			Date sourceTimestamp = Calendar.getInstance().getTime();
			List<String> logMessagePropList =  Lists.newArrayList("MAX", "pInter01", "NAK", "ERROR"); 
					
			InterchangeEventDto interchangeEventDto = logMessageHandler.handleInterchangeUpdateEvent(sourceSystem, sourceTimestamp, logMessagePropList);
			
			assertEquals(EventEnum.MAX, interchangeEventDto.getEvent());
			assertEquals("pInter01", interchangeEventDto.getInterchangeId());
			assertEquals("SOURCESYSTEM", interchangeEventDto.getSourceSystem());
			assertEquals(sourceTimestamp, interchangeEventDto.getSourceTimeStamp());
			assertEquals(0, interchangeEventDto.getNumInstructions());
			assertEquals(null, interchangeEventDto.getCountry());
			assertEquals(null, interchangeEventDto.getMessageType());
			assertEquals(null, interchangeEventDto.getInstrumentGroup());
			assertEquals(AckNakEnum.NAK, interchangeEventDto.getAckNak());
			assertEquals("ERROR", interchangeEventDto.getText());
		}
		catch (HandleEventException heex) 
		{
			fail(heex.getMessage());
		}
	}
	
	@Test
	public final void testHandleInterchangeUpdateEventFinal() 
	{
		try
		{
			// TEST EventEnum.FINAL
			String sourceSystem = "SOURCESYSTEM";
			Date sourceTimestamp = Calendar.getInstance().getTime();
			List<String> logMessagePropList =  Lists.newArrayList("FINAL", "pInter01"); 
					
			InterchangeEventDto interchangeEventDto = logMessageHandler.handleInterchangeUpdateEvent(sourceSystem, sourceTimestamp, logMessagePropList);
			
			assertEquals(EventEnum.FINAL, interchangeEventDto.getEvent());
			assertEquals("pInter01", interchangeEventDto.getInterchangeId());
			assertEquals("SOURCESYSTEM", interchangeEventDto.getSourceSystem());
			assertEquals(sourceTimestamp, interchangeEventDto.getSourceTimeStamp());
			assertEquals(0, interchangeEventDto.getNumInstructions());
			assertEquals(null, interchangeEventDto.getCountry());
			assertEquals(null, interchangeEventDto.getMessageType());
			assertEquals(null, interchangeEventDto.getInstrumentGroup());
			assertEquals(AckNakEnum.UNKNOWN, interchangeEventDto.getAckNak());
			assertEquals(null, interchangeEventDto.getText());
		}
		catch (HandleEventException heex) 
		{
			fail(heex.getMessage());
		}
	}
	
	@Test
	public final void testHandleInstructionEvent() 
	{
		try
		{
			// TEST EventEnum.INSTR
			String sourceSystem = "SOURCESYSTEM";
			Date sourceTimestamp = Calendar.getInstance().getTime();
			List<String> logMessagePropList =  Lists.newArrayList("INSTR", "nInter01", "nInstr01", "1"); 
				
			InstructionEventDto instructionEventDto = logMessageHandler.handleInstructionEvent(sourceSystem, sourceTimestamp, logMessagePropList);
			
			assertEquals(EventEnum.CREATE, instructionEventDto.getEvent());
			assertEquals("nInter01", instructionEventDto.getInterchangeId());
			assertEquals("nInstr01", instructionEventDto.getInstructionId());
			assertEquals("", instructionEventDto.getRecInstructionId());
			assertEquals("SOURCESYSTEM", instructionEventDto.getSourceSystem());
			assertEquals(sourceTimestamp, instructionEventDto.getSourceTimeStamp());
			assertEquals(1, instructionEventDto.getNumInstructions());
			assertEquals(null, instructionEventDto.getCountry());
		}
		catch (HandleEventException heex) 
		{
			fail(heex.getMessage());
		}
	}
	
	@Test
	public final void testHandleInstructionEventWithRecId() 
	{
		try
		{
			// TEST EventEnum.INSTR
			String sourceSystem = "SOURCESYSTEM";
			Date sourceTimestamp = Calendar.getInstance().getTime();
			List<String> logMessagePropList =  Lists.newArrayList("INSTR", "pInter01", "pInstr01", "1", "nInstr01"); 
			
			InstructionEventDto instructionEventDto = logMessageHandler.handleInstructionEvent(sourceSystem, sourceTimestamp, logMessagePropList);
			
			assertEquals(EventEnum.CREATE, instructionEventDto.getEvent());
			assertEquals("pInter01", instructionEventDto.getInterchangeId());
			assertEquals("pInstr01", instructionEventDto.getInstructionId());
			assertEquals("nInstr01", instructionEventDto.getRecInstructionId());
			assertEquals("SOURCESYSTEM", instructionEventDto.getSourceSystem());
			assertEquals(sourceTimestamp, instructionEventDto.getSourceTimeStamp());
			assertEquals(1, instructionEventDto.getNumInstructions());
			assertEquals(null, instructionEventDto.getCountry());
		}
		catch (HandleEventException heex) 
		{
			fail(heex.getMessage());
		}
	}
	
	@Test
	public final void testHandleTransactionEventCore() 
	{
		try
		{
			// TEST EventEnum.INSTR
			String sourceSystem = "SOURCESYSTEM";
			Date sourceTimestamp = Calendar.getInstance().getTime();
			List<String> logMessagePropList =  Lists.newArrayList("CORE", "pInstr01", "pTrans01", "ACK", "CORE BANKING REFERENCE"); 
			
			TransactionEventDto transactionEventDto = logMessageHandler.handleTransactionEvent(sourceSystem, sourceTimestamp, logMessagePropList);
			
			assertEquals(EventEnum.CORE, transactionEventDto.getEvent());
			assertEquals("", transactionEventDto.getInstructionId());
			assertEquals("", transactionEventDto.getTransactionId());
			assertEquals("pInstr01", transactionEventDto.getRecInstructionId());
			assertEquals("pTrans01", transactionEventDto.getRecTransactionId());
			assertEquals("SOURCESYSTEM", transactionEventDto.getSourceSystem());
			assertEquals(sourceTimestamp, transactionEventDto.getSourceTimeStamp());
			assertEquals(AckNakEnum.ACK, transactionEventDto.getAckNak());
			assertEquals("CORE BANKING REFERENCE", transactionEventDto.getText());
		}
		catch (HandleEventException heex) 
		{
			fail(heex.getMessage());
		}
	}
	
	@Test
	public final void testHandleTransactionEventTransNbol() 
	{
		try
		{
			// TEST EventEnum.INSTR
			String sourceSystem = "NBOL";
			Date sourceTimestamp = Calendar.getInstance().getTime();
			List<String> logMessagePropList =  Lists.newArrayList("TRANS", "nInstr01", "nTrans02"); 
			
			TransactionEventDto transactionEventDto = logMessageHandler.handleTransactionEvent(sourceSystem, sourceTimestamp, logMessagePropList);
			
			assertEquals(EventEnum.CREATE, transactionEventDto.getEvent());
			assertEquals("nInstr01", transactionEventDto.getInstructionId());
			assertEquals("nTrans02", transactionEventDto.getTransactionId());
			assertEquals("", transactionEventDto.getRecInstructionId());
			assertEquals("", transactionEventDto.getRecTransactionId());
			assertEquals("NBOL", transactionEventDto.getSourceSystem());
			assertEquals(sourceTimestamp, transactionEventDto.getSourceTimeStamp());
			assertEquals(AckNakEnum.UNKNOWN, transactionEventDto.getAckNak());
			assertEquals(null, transactionEventDto.getText());
		}
		catch (HandleEventException heex) 
		{
			fail(heex.getMessage());
		}
	}
	
	@Test
	public final void testHandleTransactionEventTransPayex() 
	{
		try
		{
			// TEST EventEnum.INSTR
			String sourceSystem = "PAYEX";
			Date sourceTimestamp = Calendar.getInstance().getTime();
			List<String> logMessagePropList =  Lists.newArrayList("TRANS", "pInstr01", "pTrans01", "nInstr01", "nTrans02", "SECTION A"); 

			TransactionEventDto transactionEventDto = logMessageHandler.handleTransactionEvent(sourceSystem, sourceTimestamp, logMessagePropList);
			
			assertEquals(EventEnum.CREATE, transactionEventDto.getEvent());
			assertEquals("nInstr01", transactionEventDto.getInstructionId());
			assertEquals("nTrans02", transactionEventDto.getTransactionId());
			assertEquals("pInstr01", transactionEventDto.getRecInstructionId());
			assertEquals("pTrans01", transactionEventDto.getRecTransactionId());
			assertEquals("PAYEX", transactionEventDto.getSourceSystem());
			assertEquals(sourceTimestamp, transactionEventDto.getSourceTimeStamp());
			assertEquals(AckNakEnum.UNKNOWN, transactionEventDto.getAckNak());
			assertEquals("SECTION A", transactionEventDto.getText());
		}
		catch (HandleEventException heex) 
		{
			fail(heex.getMessage());
		}
	}
}
