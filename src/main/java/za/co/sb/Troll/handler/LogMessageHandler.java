package za.co.sb.Troll.handler;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import za.co.sb.Troll.dao.ProblemRecordDao;
import za.co.sb.Troll.dao.TrollerLogMessageDao;
import za.co.sb.Troll.dto.ProblemRecordDto;
import za.co.sb.Troll.dto.event.InstructionEventDto;
import za.co.sb.Troll.dto.event.InterchangeEventDto;
import za.co.sb.Troll.dto.event.TransactionEventDto;
import za.co.sb.Troll.enums.AckNakEnum;
import za.co.sb.Troll.enums.EventEnum;
import za.co.sb.Troll.exception.HandleEventException;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class LogMessageHandler 
{
	private static final Logger LOG = LogManager.getLogger(LogMessageHandler.class);
	
	public static final DateFormat NBOL_LOG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S");                                                                          
	public static final DateFormat PAYEX_LOG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S");
	public static final DateFormat DEFAULT_LOG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	
	private TrollerLogMessageDao trollerLogMessageDao = new TrollerLogMessageDao();
	private ProblemRecordDao problemRecordDao = new ProblemRecordDao();
	
	/**
	 * Attempts to parse the Event Log Message and up the DB based on the EVENT
	 * TYPE extracted.
	 * 
	 * @param eventLogMessage
	 * @throws SQLException
	 */
	public void handleLogMessage(String eventLogMessage) throws SQLException
	{
		String sourceSystem = "";
		String logMessage = "";
		Date sourceTimestamp = null;
		List<String> logMessagePropList = new ArrayList<String>();
		
		try 
		{
			sourceSystem = extractSourceSytem(eventLogMessage);
			logMessage = extractLogMessage(eventLogMessage, sourceSystem);
			sourceTimestamp =  extractSourceTimestamp(logMessage, sourceSystem);
			logMessagePropList = extractLogMessagePropList(logMessage, sourceSystem);
			
			EventEnum eventType = EventEnum.getEvent(logMessagePropList.size() > 0 ? logMessagePropList.get(0) : "");
			switch (eventType) 
			{
	            case INTER :
	            	trollerLogMessageDao.insertInterchangeEvent(handleInterchangeEvent(sourceSystem, sourceTimestamp, logMessagePropList));
	            	break;
	            case INSTR :
	            	trollerLogMessageDao.insertInstructionEvent(handleInstructionEvent(sourceSystem, sourceTimestamp, logMessagePropList));
	            	break;
	            case TRANS :
	            case CORE :
	            	trollerLogMessageDao.upsertTransactionEvent(handleTransactionEvent(sourceSystem, sourceTimestamp, logMessagePropList));
	            	break;
	            case SENT :
	            	if ("PAYEX".equals(sourceSystem))
	            	{
	            		trollerLogMessageDao.updateInstructionEvent(handleInstructionUpdateEvent(sourceSystem, sourceTimestamp, logMessagePropList));
	            		break;
	            	}
	            case RECD :
	            case INTERIM :	
	            case MAX :	
	            case FINAL :
	            	trollerLogMessageDao.updateInterchangeEvent(handleInterchangeUpdateEvent(sourceSystem, sourceTimestamp, logMessagePropList));
	            	break;
	            default : 
	            	String errorMessage = "Unknown <EVENT>";
	    			ProblemRecordDto problemRecordDto = new ProblemRecordDto(null, null, null, sourceTimestamp, sourceSystem, errorMessage);
	    			
	    			throw new HandleEventException(errorMessage, problemRecordDto);
	        }
		}
		catch (HandleEventException heex) 
		{
			LOG.error("Exception handling event.", heex);
			
			heex.getProblemRecordDto().setInsertTimestamp(Calendar.getInstance().getTime());
			heex.getProblemRecordDto().setRecord(eventLogMessage);
			
			problemRecordDao.insertProblemRecords(heex.getProblemRecordDto());
		}
	}
	
	/**
	 * Extracts the SOURCE SYSTEM from the message.
	 * 
	 * @param eventLogMessage
	 * @return the SOURCE SYSTEM
	 * @throws HandleEventException
	 */
	public String extractSourceSytem(String eventLogMessage) throws HandleEventException
	{
		try
		{
			return eventLogMessage.substring(0, eventLogMessage.indexOf(','));
		}
		catch (Exception ex)
		{
			String errorMessage = "Unable to extract <SOURCE SYSTEM> from event log message";
			ProblemRecordDto problemRecordDto = new ProblemRecordDto(null, null, null, null, null, errorMessage);
			
			throw new HandleEventException(errorMessage, problemRecordDto, ex);
		}
	}
	
	/**
	 * Extracts the actual LOG MESSAGE written by the SOURCE SYSTEM.
	 * 
	 * @param eventLogMessage
	 * @param sourceSystem
	 * @return the LOG MESSAGE
	 * @throws HandleEventException
	 */
	public String extractLogMessage(String eventLogMessage, String sourceSystem) throws HandleEventException
	{
		try
		{
			return eventLogMessage.substring(eventLogMessage.indexOf(',') + 1).trim();
		}
		catch (Exception ex)
		{
			String errorMessage = "Unable to extract <LOG MESSAGE> from event log message";
			ProblemRecordDto problemRecordDto = new ProblemRecordDto(null, null, null, null, sourceSystem, errorMessage);
			
			throw new HandleEventException(errorMessage, problemRecordDto, ex);
		}
	}
	
	/**
	 * Extracts the SOURCE SYSTEM TIMESTAMP from the LOG MESSAGE.
	 * 
	 * @param eventLogMessage
	 * @param sourceSystem
	 * @return the SOURCE SYSTEM TIMESTAMP
	 * @throws HandleEventException
	 */
	public Date extractSourceTimestamp(String logMessage, String sourceSystem) throws HandleEventException
	{
		try
		{
			String sourceTimestamp =  logMessage.substring(0, logMessage.indexOf('[')).trim();
			
			if ("NBOL".equals(sourceSystem))
			{
				return NBOL_LOG_DATE_FORMAT.parse(sourceTimestamp);
			}
			else if ("PAYEX".equals(sourceSystem))
			{
				return PAYEX_LOG_DATE_FORMAT.parse(sourceTimestamp);
			}
			else
			{
				return DEFAULT_LOG_DATE_FORMAT.parse(sourceTimestamp);
			}
		}
		catch (Exception ex)
		{
			String errorMessage = "Unable to extract <SOURCE TIMESTAMP> from event log message";
			ProblemRecordDto problemRecordDto = new ProblemRecordDto(null, null, null, null, sourceSystem, errorMessage);
			
			throw new HandleEventException(errorMessage, problemRecordDto, ex);
		}
	}
	
	/**
	 * Extracts the list of EVENT properties from the LOG MESSAGE. 
	 * 
	 * @param logMessage
	 * @param sourceSystem
	 * @return List<String> of EVENT properties
	 * @throws HandleEventException
	 */
	public List<String> extractLogMessagePropList(String logMessage, String sourceSystem) throws HandleEventException
	{
		ArrayList<String> propList = new ArrayList<String>();
		
		try
		{
			propList = Lists.newArrayList(Splitter.on(",").trimResults().splitToList(logMessage.substring(logMessage.indexOf("TROLL"))));
			propList.remove(0);
			
			return propList;
		}
		catch (Exception ex)
		{
			String errorMessage = "Unable to extract <LOG MESSAGE PROPERTIES> from event log message";
			ProblemRecordDto problemRecordDto = new ProblemRecordDto(null, null, null, null, sourceSystem, errorMessage);
			
			throw new HandleEventException(errorMessage, problemRecordDto, ex);
		}
	}
	
	/**
	 * Handles LOG MESSAGE events of types
	 * <ul>
	 * <li>EventEnum.INTER</li>
	 * </ul>
	 * 
	 * Builds a DTO of parameters using properties extracted from the LOG
	 * MESSAGE that will be passed to the relevant stored procedure for the type
	 * of event
	 * 
	 * @param sourceSystem
	 * @param sourceTimestamp
	 * @param logMessagePropList
	 * @throws HandleEventException
	 * @throws SQLException
	 */
	public InterchangeEventDto handleInterchangeEvent(String sourceSystem, Date sourceTimestamp, List<String> logMessagePropList) throws HandleEventException 
	{
		InterchangeEventDto interchangeEventDto = new InterchangeEventDto();
		
		try 
		{
			EventEnum event = EventEnum.getEvent(logMessagePropList.get(0));
			if (event != EventEnum.INTER) 
			{
				throw new Exception("Invalid transaction <EVENT>");
			}
			
			interchangeEventDto.setSourceSystem(sourceSystem);
			interchangeEventDto.setSourceTimeStamp(sourceTimestamp);
			
			interchangeEventDto.setInterchangeId(logMessagePropList.get(1));
			interchangeEventDto.setNumInstructions(Integer.parseInt(logMessagePropList.get(2)));
			interchangeEventDto.setCountry(logMessagePropList.get(3));
			interchangeEventDto.setMessageType(logMessagePropList.get(4));
			interchangeEventDto.setInstrumentGroup(logMessagePropList.get(5));
		}
		catch (Exception ex)
		{
			String errorMessage = "Unable to process Interchange Event <" +  EventEnum.getEvent(logMessagePropList.get(0)) + ">";
			ProblemRecordDto problemRecordDto = new ProblemRecordDto(interchangeEventDto.getInterchangeId(), null, null, sourceTimestamp, sourceSystem, errorMessage);
			
			throw new HandleEventException(errorMessage, problemRecordDto, ex);
		}
		
		return interchangeEventDto;
	}
	
	/**
	 * Handles LOG MESSAGE events of types
	 * <ul>
	 * <li>EventEnum.SENT AND SOURCE SYSTEM IS NBOL</li>
	 * <li>EventEnum.RECD</li>
	 * <li>EventEnum.INTERIM</li>
	 * <li>EventEnum.MAX</li>
	 * <li>EventEnum.FINAL</li>
	 * </ul>
	 * 
	 * Builds a DTO of parameters using properties extracted from the LOG
	 * MESSAGE that will be passed to the relevant stored procedure for the type
	 * of event
	 * 
	 * @param sourceSystem
	 * @param sourceTimestamp
	 * @param logMessagePropList
	 * @throws HandleEventException
	 * @throws SQLException
	 */
	public InterchangeEventDto handleInterchangeUpdateEvent(String sourceSystem, Date sourceTimestamp, List<String> logMessagePropList) throws HandleEventException 
	{
		InterchangeEventDto interchangeEventDto = new InterchangeEventDto();
		
		try 
		{
			EventEnum event = EventEnum.getEvent(logMessagePropList.get(0));
			if (event != EventEnum.SENT && event != EventEnum.RECD
					&& event != EventEnum.INTERIM && event != EventEnum.MAX
					&& event != EventEnum.FINAL) 
			{
				throw new Exception("Invalid transaction <EVENT>");
			}
			
			interchangeEventDto.setSourceSystem(sourceSystem);
			interchangeEventDto.setSourceTimeStamp(sourceTimestamp);
			
			interchangeEventDto.setEvent(EventEnum.getEvent(logMessagePropList.get(0)));
			interchangeEventDto.setInterchangeId(logMessagePropList.get(1));
			interchangeEventDto.setAckNak(logMessagePropList.size() > 2 ? AckNakEnum.getAckNak(logMessagePropList.get(2)) : AckNakEnum.UNKNOWN);
			interchangeEventDto.setText(interchangeEventDto.getAckNak() == AckNakEnum.NAK ? logMessagePropList.get(3) : null);
		}
		catch (Exception ex)
		{
			String errorMessage = "Unable to process Interchange Update Event <" +  EventEnum.getEvent(logMessagePropList.get(0)) + ">";
			ProblemRecordDto problemRecordDto = new ProblemRecordDto(interchangeEventDto.getInterchangeId(), null, null, sourceTimestamp, sourceSystem, errorMessage);
			
			throw new HandleEventException(errorMessage, problemRecordDto, ex);
		}			
			
		return interchangeEventDto;
	}
	
	/**
	 * Handles LOG MESSAGE events of types
	 * <ul>
	 * <li>EventEnum.INSTR</li>
	 * </ul>
	 * 
	 * Builds a DTO of parameters using properties extracted from the LOG
	 * MESSAGE that will be passed to the relevant stored procedure for the type
	 * of event
	 * 
	 * @param sourceSystem
	 * @param sourceTimestamp
	 * @param logMessagePropList
	 * @throws HandleEventException
	 * @throws SQLException
	 */
	public InstructionEventDto handleInstructionEvent(String sourceSystem, Date sourceTimestamp, List<String> logMessagePropList) throws HandleEventException 
	{
		InstructionEventDto instructionEventDto = new InstructionEventDto();
		
		try
		{
			EventEnum event = EventEnum.getEvent(logMessagePropList.get(0));
			if (event != EventEnum.INSTR)
			{
				throw new Exception("Invalid transaction <EVENT>");
			}
			
			instructionEventDto.setSourceSystem(sourceSystem);
			instructionEventDto.setEvent(EventEnum.CREATE);
			instructionEventDto.setSourceTimeStamp(sourceTimestamp);
			
			instructionEventDto.setInterchangeId(logMessagePropList.get(1));
			instructionEventDto.setInstructionId(logMessagePropList.get(2));
			instructionEventDto.setNumInstructions(Integer.parseInt(logMessagePropList.get(3)));
			instructionEventDto.setRecInstructionId(logMessagePropList.size() > 4 ? logMessagePropList.get(4) : "");
		}
		catch (Exception ex)
		{
			String errorMessage = "Unable to process Instruction Event <" +  EventEnum.getEvent(logMessagePropList.get(0)) + ">";
			ProblemRecordDto problemRecordDto = new ProblemRecordDto(instructionEventDto.getInterchangeId(), instructionEventDto.getInstructionId(), null, sourceTimestamp, sourceSystem, errorMessage);
			
			throw new HandleEventException(errorMessage, problemRecordDto, ex);
		}
			
		return instructionEventDto;
	}
	
	/**
	 * Handles LOG MESSAGE events of types
	 * <ul>
	 * <li>EventEnum.SENT WHERE SOURCE SYSTEM IS PAYEX</li>
	 * </ul>
	 * 
	 * Builds a DTO of parameters using properties extracted from the LOG
	 * MESSAGE that will be passed to the relevant stored procedure for the type
	 * of event
	 * 
	 * @param sourceSystem
	 * @param sourceTimestamp
	 * @param logMessagePropList
	 * @throws HandleEventException
	 * @throws SQLException
	 */
	public InstructionEventDto handleInstructionUpdateEvent(String sourceSystem, Date sourceTimestamp, List<String> logMessagePropList) throws HandleEventException 
	{
		InstructionEventDto instructionEventDto = new InstructionEventDto();
		
		try
		{
			EventEnum event = EventEnum.getEvent(logMessagePropList.get(0));
			if (event != EventEnum.SENT && !"PAYEX".equalsIgnoreCase(sourceSystem))
			{
				throw new Exception("Invalid transaction <EVENT>");
			}
			
			//call InstructionUpdate ('', 'pInstr01', '2014-01-01 00:00:24', 'PAYEX', 'SENT', '', '') ;
			
			instructionEventDto.setSourceSystem(sourceSystem);
			instructionEventDto.setEvent(event);
			instructionEventDto.setSourceTimeStamp(sourceTimestamp);
			
			instructionEventDto.setInterchangeId(logMessagePropList.get(1));
			instructionEventDto.setInstructionId(logMessagePropList.get(2));
			instructionEventDto.setAckNak(AckNakEnum.getAckNak(logMessagePropList.get(3)));
			instructionEventDto.setText(logMessagePropList.get(4));
			
		}
		catch (Exception ex)
		{
			String errorMessage = "Unable to process Instruction Event <" +  EventEnum.getEvent(logMessagePropList.get(0)) + ">";
			ProblemRecordDto problemRecordDto = new ProblemRecordDto(instructionEventDto.getInterchangeId(), instructionEventDto.getInstructionId(), null, sourceTimestamp, sourceSystem, errorMessage);
			
			throw new HandleEventException(errorMessage, problemRecordDto, ex);
		}
			
		return instructionEventDto;
	}
	
	/**
	 * Handles LOG MESSAGE events of types
	 * <ul>
	 * <li>EventEnum.TRANS</li>
	   <li>EventEnum.CORE</li>
	 * </ul>
	 * 
	 * Builds a DTO of parameters using properties extracted from the LOG
	 * MESSAGE that will be passed to the relevant stored procedure for the type
	 * of event
	 * 
	 * @param sourceSystem
	 * @param sourceTimestamp
	 * @param logMessagePropList
	 * @throws HandleEventException
	 * @throws SQLException
	 */
	public TransactionEventDto handleTransactionEvent(String sourceSystem, Date sourceTimestamp, List<String> logMessagePropList) throws HandleEventException 
	{
		TransactionEventDto transactionEventDto = new TransactionEventDto();
		
		try
		{
			EventEnum event = EventEnum.getEvent(logMessagePropList.get(0));
			if (event != EventEnum.CORE && event != EventEnum.TRANS)
			{
				throw new Exception("Invalid transaction <EVENT>");
			}
			
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
					transactionEventDto.setText(null);
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
			String errorMessage = "Unable to process Transaction Event <" +  EventEnum.getEvent(logMessagePropList.get(0)) + ">";
			ProblemRecordDto problemRecordDto = new ProblemRecordDto(null, transactionEventDto.getInstructionId(), transactionEventDto.getTransactionId(), sourceTimestamp, sourceSystem, errorMessage);
			
			throw new HandleEventException(errorMessage, problemRecordDto, ex);
		}
		
		return transactionEventDto;
	}
	
	/**
	 * Test case
	 * 
	 * @param args
	 * @throws Exception
	 */
	/*public static void main(String args[]) throws Exception
	{
		Troll.loadProperties();
		
		List<String> testCaseTrollerLogMessageList = new ArrayList<String>();
		
		testCaseTrollerLogMessageList.add("NBOL, 2014-09-11 12:23:41.201 [main] INFO - TROLL, INTER, nInter01, 1, KE, PAYMENT, LOWCARE");
		testCaseTrollerLogMessageList.add("NBOL, 2014-09-11 12:23:42.201 [main] INFO - TROLL, INSTR, nInter01, nInstr01, 2,");
		testCaseTrollerLogMessageList.add("NBOL, 2014-09-11 12:23:43.201 [main] INFO - TROLL, TRANS, nInstr01, nTrans01");
		testCaseTrollerLogMessageList.add("NBOL, 2014-09-11 12:23:44.201 [main] INFO - TROLL, TRANS, nInstr01, nTrans02");
		testCaseTrollerLogMessageList.add("NBOL, 2014-09-11 12:23:45.201 [main] INFO - TROLL, SENT, nInter01");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:46.201 [main] INFO - TROLL, RECD, nInter01");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:47.201 [main] INFO - TROLL, INTERIM, nInter01, ACK,");
		testCaseTrollerLogMessageList.add("NBOL, 2014-09-11 12:23:48.201 [main] INFO - TROLL, INTERIM, nInter01");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:49.201 [main] INFO - TROLL, INTER, pInter01, 1,,PAYMENT, LOWCARE");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:50.201 [main] INFO - TROLL, INSTR, pInter01, pInstr01, 1, nInstr01");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:51.201 [main] INFO - TROLL, TRANS, pInstr01, pTrans01, nInstr01, nTrans01, Section A");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:52.201 [main] INFO - TROLL, SENT, pInter01");
		testCaseTrollerLogMessageList.add("PAYEX, 2014-09-11 12:23:53.201 [main] INFO - TROLL, INTER, pInter02, 1,,PAYMENT, LOWCARE"); 
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
			new LogMessageHandler().handleLogMessage(testCaseTrollerLogMessage);
		}
	}*/
}

