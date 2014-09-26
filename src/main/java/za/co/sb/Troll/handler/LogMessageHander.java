package za.co.sb.Troll.handler;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import za.co.sb.Troll.Troll;
import za.co.sb.Troll.dao.TrollerLogMessageDao;
import za.co.sb.Troll.dto.event.InstructionEventDto;
import za.co.sb.Troll.dto.event.InterchangeEventDto;
import za.co.sb.Troll.dto.event.TransactionEventDto;
import za.co.sb.Troll.enums.AckNakEnum;
import za.co.sb.Troll.enums.EventEnum;
import za.co.sb.Troll.exception.HandleEventException;

import com.google.common.base.Splitter;

public class LogMessageHander 
{
	public static final DateFormat NBOL_LOG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	public static final DateFormat PAYEX_LOG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	
	private TrollerLogMessageDao trollerLogMessageDao = new TrollerLogMessageDao();
	
	public void handleLogMessage(String eventLogMessage)
	{
		try 
		{
			String sourceSystem = extractSourceSytem(eventLogMessage);
			String logMessage = extractLogMessage(eventLogMessage);
			
			Date sourceTimestamp =  extractSourceTimestamp(logMessage, sourceSystem);
			List<String> logMessagePropList = extractLogMessagePropList(logMessage);
			
			EventEnum eventType = EventEnum.getEvent(logMessagePropList.size() > 0 ? logMessagePropList.get(0) : "");
			switch (eventType) 
			{
	            case INTER :
	            	handleInterchangeEvent(sourceSystem, sourceTimestamp, logMessagePropList);
	            	break;
	            case INSTR :
	            	handleInstructionEvent(sourceSystem, sourceTimestamp, logMessagePropList);
	            	break;
	            case TRANS :
	            case CORE :
	            	handleTransactionEvent(sourceSystem, sourceTimestamp, logMessagePropList);
	            	break;
	            case SENT :
	            case RECD :
	            case INTERIM :	
	            case MAX :	
	            case FINAL :
	            	handleInterchangeUpdateEvent(sourceSystem, sourceTimestamp, logMessagePropList);
	            	break;
	            default : 
	            	throw new HandleEventException("Invalid EVENT");
	        }
		}
		catch (HandleEventException heex) 
		{
			// TODO Use the problem record DTO created by the method throwing this exception to insert a problem record into the DB
			
		}
		catch (SQLException heex) 
		{
			// TODO How do we handle SQL Exceptions?
			
		}
	}
	
	private String extractSourceSytem(String eventLogMessage) throws HandleEventException
	{
		try
		{
			return eventLogMessage.substring(0, eventLogMessage.indexOf(','));
		}
		catch (Exception ex)
		{
			//insertProblemRecord();
			throw new HandleEventException("Unable to extract <SOURCE SYSTEM> from event log message <" + eventLogMessage + ">", ex);
		}
	}
	
	private String extractLogMessage(String eventLogMessage) throws HandleEventException
	{
		try
		{
			return eventLogMessage.substring(eventLogMessage.indexOf(',') + 1);
		}
		catch (Exception ex)
		{
			//insertProblemRecord();
			throw new HandleEventException("Unable to extract <LOG MESSAGE> from event log message <" + eventLogMessage + ">", ex);
		}
	}
	
	private Date extractSourceTimestamp(String logMessage, String sourceSystem) throws HandleEventException
	{
		try
		{
			String sourceTimestamp =  logMessage.substring(0, logMessage.indexOf('[')).trim();
			
			if ("NBOL".equals(sourceSystem))
			{
				return NBOL_LOG_DATE_FORMAT.parse(sourceTimestamp);
			}
			else
			{
				return PAYEX_LOG_DATE_FORMAT.parse(sourceTimestamp);
			}
		}
		catch (Exception ex)
		{
			//insertProblemRecord();
			throw new HandleEventException("Unable to extract <SOURCE TIMESTAMP> from event log message <" + logMessage + ">", ex);
		}
	}
	
	private List<String> extractLogMessagePropList(String logMessage) throws HandleEventException
	{
		try
		{
			return Splitter.on(",").trimResults().splitToList(logMessage.substring(logMessage.indexOf("TROLL") + "TROLL,".length()));
		}
		catch (Exception ex)
		{
			//insertProblemRecord();
			throw new HandleEventException("Unable to extract <LOG MESSAGE PROPERTIES> from event log message <" + logMessage + ">", ex);
		}
	}
	
	private void handleInterchangeEvent(String sourceSystem, Date sourceTimestamp, List<String> logMessagePropList) throws HandleEventException, SQLException 
	{
		InterchangeEventDto interchangeEventDto = new InterchangeEventDto();
		
		try 
		{
			interchangeEventDto.setSourceSystem(sourceSystem);
			interchangeEventDto.setSourceTimeStamp(sourceTimestamp);
			
			interchangeEventDto.setInterchangeId(logMessagePropList.get(1));
			interchangeEventDto.setNumInstructions(Integer.parseInt(logMessagePropList.get(2)));
			interchangeEventDto.setCountry(logMessagePropList.get(3));
		}
		catch (Exception ex)
		{
			throw new HandleEventException("Unable to process Interchange Event <" +  EventEnum.getEvent(logMessagePropList.get(0)) + ">", ex);
		}
			
		trollerLogMessageDao.insertInterchangeEvent(interchangeEventDto);
	}
	
	private void handleInterchangeUpdateEvent(String sourceSystem, Date sourceTimestamp, List<String> logMessagePropList) throws HandleEventException, SQLException 
	{
		InterchangeEventDto interchangeEventDto = new InterchangeEventDto();
		
		try 
		{
			interchangeEventDto.setSourceSystem(sourceSystem);
			interchangeEventDto.setSourceTimeStamp(sourceTimestamp);
			
			interchangeEventDto.setEvent(EventEnum.getEvent(logMessagePropList.get(0)));
			interchangeEventDto.setInterchangeId(logMessagePropList.get(1));
			interchangeEventDto.setAckNak(logMessagePropList.size() > 2 ? AckNakEnum.getAckNak(logMessagePropList.get(2)) : AckNakEnum.UNKNOWN);
			interchangeEventDto.setText(interchangeEventDto.getAckNak() == AckNakEnum.NAK ? logMessagePropList.get(3) : "");
		}
		catch (Exception ex)
		{
			throw new HandleEventException("Unable to process Interchange Update Event <" +  EventEnum.getEvent(logMessagePropList.get(0)) + ">", ex);
		}			
			
		trollerLogMessageDao.updateInterchangeEvent(interchangeEventDto);
		
	}
	
	private void handleInstructionEvent(String sourceSystem, Date sourceTimestamp, List<String> logMessagePropList) throws HandleEventException, SQLException 
	{
		InstructionEventDto instructionEventDto = new InstructionEventDto();
		
		try
		{
			instructionEventDto.setSourceSystem(sourceSystem);
			instructionEventDto.setEvent(EventEnum.CREATE);
			instructionEventDto.setSourceTimeStamp(sourceTimestamp);
			
			instructionEventDto.setInterchangeId(logMessagePropList.get(1));
			instructionEventDto.setInstructionId(logMessagePropList.get(2));
			instructionEventDto.setNumInstructions(Integer.parseInt(logMessagePropList.get(3)));
			instructionEventDto.setRecInstructionId(logMessagePropList.get(4));
		}
		catch (Exception ex)
		{
			throw new HandleEventException("Unable to process Instruction Event <" +  EventEnum.getEvent(logMessagePropList.get(0)) + ">", ex);
		}
			
		trollerLogMessageDao.insertInstructionEvent(instructionEventDto);
	}
	
	private void handleTransactionEvent(String sourceSystem, Date sourceTimestamp, List<String> logMessagePropList) throws HandleEventException, SQLException 
	{
		TransactionEventDto transactionEventDto = new TransactionEventDto();
		
		try
		{
			transactionEventDto.setSourceSystem(sourceSystem);
			transactionEventDto.setSourceTimeStamp(sourceTimestamp);
					
			EventEnum eventType = EventEnum.getEvent(logMessagePropList.get(0));
			if (eventType == EventEnum.CORE)
			{
				transactionEventDto.setEvent(eventType);
				transactionEventDto.setInstructionId("");
				transactionEventDto.setTransactionId("");
				transactionEventDto.setRecInstructionId(logMessagePropList.get(1));
				transactionEventDto.setRecTransactionId(logMessagePropList.get(2));
				transactionEventDto.setAckNak(AckNakEnum.getAckNak(logMessagePropList.get(3)));
				transactionEventDto.setText(logMessagePropList.get(4));
			}
			else
			{
				transactionEventDto.setEvent(EventEnum.CREATE);
				transactionEventDto.setAckNak(AckNakEnum.UNKNOWN);
				
				if ("NBOL".equalsIgnoreCase(sourceSystem))
				{
					transactionEventDto.setInstructionId(logMessagePropList.get(1));
					transactionEventDto.setTransactionId(logMessagePropList.get(2));
					transactionEventDto.setRecInstructionId("");
					transactionEventDto.setRecTransactionId("");
					transactionEventDto.setText("");
				}
				else if ("PAYEX".equalsIgnoreCase(sourceSystem))
				{
					transactionEventDto.setRecInstructionId(logMessagePropList.get(1));
					transactionEventDto.setRecTransactionId(logMessagePropList.get(2));
					transactionEventDto.setInstructionId(logMessagePropList.get(3));
					transactionEventDto.setTransactionId(logMessagePropList.get(4));
					transactionEventDto.setText(logMessagePropList.get(5));
				}
			}
		}
		catch (Exception ex)
		{
			throw new HandleEventException("Unable to process Transaction Event <" +  EventEnum.getEvent(logMessagePropList.get(0)) + ">", ex);
		}
		
		trollerLogMessageDao.upsertTransactionEvent(transactionEventDto);
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String args[]) throws Exception
	{
		Troll.loadProperties();
		
		List<String> testCaseTrollerLogMessageList = new ArrayList<String>();
		
		
		testCaseTrollerLogMessageList.add("NBOL, 2014-09-11 12:23:41.201 [main] INFO - TROLL, INTER, nInter01, 1, KE");
		testCaseTrollerLogMessageList.add("NBOL, 2014-09-11 12:23:42.201 [main] INFO - TROLL, INSTR, nInter01, nInstr01, 2,");
		testCaseTrollerLogMessageList.add("NBOL, 2014-09-11 12:23:43.201 [main] INFO - TROLL, TRANS, nInstr01, nTrans01");
		testCaseTrollerLogMessageList.add("NBOL, 2014-09-11 12:23:44.201 [main] INFO - TROLL, TRANS, nInstr01, nTrans02");
		testCaseTrollerLogMessageList.add("NBOL, 2014-09-11 12:23:45.201 [main] INFO - TROLL, SENT, nInter01");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:46.201 [main] INFO - TROLL, RECD, nInter01");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:47.201 [main] INFO - TROLL, INTERIM, nInter01, ACK,");
		testCaseTrollerLogMessageList.add("NBOL, 2014-09-11 12:23:48.201 [main] INFO - TROLL, INTERIM, nInter01");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:49.201 [main] INFO - TROLL, INTER, pInter01, 1,");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:50.201 [main] INFO - TROLL, INSTR, pInter01, pInstr01, 1, nInstr01");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:51.201 [main] INFO - TROLL, TRANS, pInstr01, pTrans01, nInstr01, nTrans01, Section A");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:52.201 [main] INFO - TROLL, SENT, pInter01");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:53.201 [main] INFO - TROLL, INTER, pInter02, 1,"); 
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:54.201 [main] INFO - TROLL, INSTR, pInter02, pInstr02, 1, nInstr01");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:55.201 [main] INFO - TROLL, TRANS, pInstr02, pTrans02, nInstr01, nTrans02, Section A");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:56.201 [main] INFO - TROLL, SENT, pInter02");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:57.201 [main] INFO - TROLL, MAX, pInter01, ACK,");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:58.201 [main] INFO - TROLL, MAX, pInter02, NAK, Error in something");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:59.201 [main] INFO - TROLL, CORE, pInstr01, pTrans01, ACK, CoreBanking_reference");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:60.201 [main] INFO - TROLL, FINAL, nInter01");
		testCaseTrollerLogMessageList.add("NBOL, 2014-09-11 12:23:61.201 [main] INFO - TROLL, FINAL, nInter01");
		
		for (String testCaseTrollerLogMessage : testCaseTrollerLogMessageList)
		{
			new LogMessageHander().handleLogMessage(testCaseTrollerLogMessage);
		}
	}
}
