package za.co.sb.Troll.handler;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import za.co.sb.Troll.Troll;
import za.co.sb.Troll.dao.TrollerLogMessageDao;
import za.co.sb.Troll.dto.event.InterchangeEventDto;
import za.co.sb.Troll.enums.EventEnum;
import za.co.sb.Troll.exception.HandleEventException;

public class LogMessageHander 
{
	public static final DateFormat NBOL_LOG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	public static final DateFormat PAYEX_LOG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	
	private TrollerLogMessageDao trollerLogMessageDao = new TrollerLogMessageDao();
	
	public void handleLogMessage(String trollerLogMessage)
	{
		String sourceSystem = trollerLogMessage.substring(0, trollerLogMessage.indexOf(','));
		String logMessage = trollerLogMessage.substring(trollerLogMessage.indexOf(',') + 1);
		
		String sourceTimestamp =  logMessage.substring(0, logMessage.indexOf('[')).trim();
		String[] logMessageProps = (logMessage.substring(logMessage.indexOf("TROLL") + "TROLL,".length())).split(",");
		
		System.out.println("sourceSystem = " + sourceSystem);
		System.out.println("sourceTimestamp = " + sourceTimestamp);
		
		for (String logMessageProp : logMessageProps)
		{
			System.out.println("logMessageProp = " + logMessageProp);
		}
		
		
		
		
		try 
		{
			EventEnum eventType = EventEnum.getEvent(logMessageProps[0].trim());
			
			switch (eventType) 
			{
	            case INTER :
	            	handleInterchangeEvent(sourceSystem, sourceTimestamp, logMessageProps);
	            	break;
	            case INSTR :
	            	handleInstructionEvent(sourceSystem, sourceTimestamp, logMessageProps);
	            	break;
	            default : 
	            	throw new HandleEventException("Invalid EVENT");
	        }
		}
		catch (HandleEventException heex)
		{
			// TODO Something exceptional happened while handling the EVENT. We should now write this to the "problemRecords" table.
		}
	}
	
	private void handleInterchangeEvent(String sourceSystem, String sourceTimestamp, String[] logMessageProps) throws HandleEventException 
	{
		InterchangeEventDto interchangeEventDto = new InterchangeEventDto();
		
		try 
		{
			interchangeEventDto.setSourceSystem(sourceSystem);
			interchangeEventDto.setInterchangeId(logMessageProps[1].trim());
			interchangeEventDto.setNumInstructions(Integer.parseInt(logMessageProps[2].trim()));
			interchangeEventDto.setCountry(logMessageProps[3].trim());
			interchangeEventDto.setSourceTimeStamp(parseSourceDate(sourceTimestamp, sourceSystem));
			interchangeEventDto.setJmsProperties(""/*logMessageProps[4].trim()*/);
			
			trollerLogMessageDao.insertInterchangeEvent(interchangeEventDto);
		}
		catch (NumberFormatException nfex)
		{
			throw new HandleEventException("", nfex);
		}
		catch (ParseException pex)
		{
			throw new HandleEventException("", pex);
		}
		catch (IndexOutOfBoundsException ioobex)
		{
			throw new HandleEventException("", ioobex);
		} 
		catch (SQLException sqlex) 
		{
			throw new HandleEventException("", sqlex);
		}
	}
	
	private void handleInstructionEvent(String sourceSystem, String sourceTimestamp, String[] logMessageProps) throws HandleEventException 
	{
		InterchangeEventDto interchangeEventDto = new InterchangeEventDto();
		
		try 
		{
			interchangeEventDto.setSourceSystem(sourceSystem);
			interchangeEventDto.setInterchangeId(logMessageProps[1].trim());
			interchangeEventDto.setNumInstructions(Integer.parseInt(logMessageProps[2].trim()));
			interchangeEventDto.setCountry(logMessageProps[3].trim());
			interchangeEventDto.setSourceTimeStamp(parseSourceDate(sourceTimestamp, sourceSystem));
			interchangeEventDto.setJmsProperties(""/*logMessageProps[4].trim()*/);
			
			trollerLogMessageDao.insertInterchangeEvent(interchangeEventDto);
		}
		catch (NumberFormatException nfex)
		{
			throw new HandleEventException("", nfex);
		}
		catch (ParseException pex)
		{
			throw new HandleEventException("", pex);
		}
		catch (IndexOutOfBoundsException ioobex)
		{
			throw new HandleEventException("", ioobex);
		} 
		catch (SQLException sqlex) 
		{
			throw new HandleEventException("", sqlex);
		}
	}
	
	private static Date parseSourceDate(String dateString, String sourceSystem) throws ParseException 
	{
		if ("NBOL".equals(sourceSystem))
		{
			return NBOL_LOG_DATE_FORMAT.parse(dateString);
		}
		else if ("PAYEX".equals(sourceSystem))
		{
			return PAYEX_LOG_DATE_FORMAT.parse(dateString);
		}
		
		// TODO what happens when source system is unknown? Is this even possible?
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String args[]) throws Exception
	{
		Troll.loadProperties();
		
		List<String> testCaseTrollerLogMessageList = new ArrayList<String>();
		
		testCaseTrollerLogMessageList.add("NBOL,2014-09-11 12:23:41.201 [main] INFO - TROLL, INTER, nInter01, 1, KE");
		testCaseTrollerLogMessageList.add("NBOL,2014-09-11 12:23:42.202 [main] INFO - TROLL, INSTR, nInter01, nInstr01, 2");
		testCaseTrollerLogMessageList.add("NBOL,2014-09-11 12:23:43.202 [main] INFO - TROLL, TRANS, nInter01, nTrans01");
		testCaseTrollerLogMessageList.add("NBOL,2014-09-11 12:23:44.202 [main] INFO - TROLL, TRANS, nInter01, nTrans02");
		testCaseTrollerLogMessageList.add("NBOL,2014-09-11 12:23:45.202 [main] INFO - TROLL, SENT, nInter01");
		testCaseTrollerLogMessageList.add("PAYEX,2014-09-11 12:23:46.202 [main] INFO - TROLL, RECD, nInter01");
		testCaseTrollerLogMessageList.add("PAYEX,2014-09-11 12:23:47.202 [main] INFO - TROLL, INTERIM, INTER01, ACK,");
		testCaseTrollerLogMessageList.add("NBOL,2014-09-11 12:23:48.202 [main] INFO - TROLL, INTERIM, nInter01");
		testCaseTrollerLogMessageList.add("PAYEX,2014-09-11 12:23:49.202 [main] INFO - TROLL, INTER, pInter01, 1, 1, 1, KE");
		testCaseTrollerLogMessageList.add("PAYEX,2014-09-11 12:23:50.202 [main] INFO - TROLL, INSTR, pInter01, pInstr01, 1, nInstr01");
		testCaseTrollerLogMessageList.add("PAYEX,2014-09-11 12:23:51.202 [main] INFO - TROLL, TRANS, pInstr01, pTrans01, nInstr01, nTrans01, Section A");
		testCaseTrollerLogMessageList.add("PAYEX,2014-09-11 12:23:52.202 [main] INFO - TROLL, SENT, pInter01");
		testCaseTrollerLogMessageList.add("PAYEX,2014-09-11 12:23:43.202 [main] INFO - TROLL, INTER, pInter02, 1, 1, 1, KE");
		testCaseTrollerLogMessageList.add("PAYEX,2014-09-11 12:23:54.202 [main] INFO - TROLL, INSTR, pInter02, pInstr02, 1, nInstr01");
		testCaseTrollerLogMessageList.add("PAYEX,2014-09-11 12:23:55.202 [main] INFO - TROLL, TRANS, pInstr02, pTrans02, nTrans02, Section A");
		testCaseTrollerLogMessageList.add("PAYEX,2014-09-11 12:23:56.202 [main] INFO - TROLL, SENT, pInter02");
		testCaseTrollerLogMessageList.add("PAYEX,2014-09-11 12:23:57.203 [main] INFO - TROLL, MAX, pInter01, ACK,");
		testCaseTrollerLogMessageList.add("PAYEX,2014-09-11 12:23:58.203 [main] INFO - TROLL, MAX, pInter02, NAK, Error in something");
		testCaseTrollerLogMessageList.add("PAYEX,2014-09-11 12:23:59.203 [main] INFO - TROLL, CORE, pInstr01, pTrans01, ACK, CoreBanking_reference");
		testCaseTrollerLogMessageList.add("PAYEX,2014-09-11 12:23:60.203 [main] INFO - TROLL, FINAL, nInter01");
		testCaseTrollerLogMessageList.add("NBOL,2014-09-11 12:23:61.203 [main] INFO - TROLL, FINAL, nInter01");
		
		for (String testCaseTrollerLogMessage : testCaseTrollerLogMessageList)
		{
			new LogMessageHander().handleLogMessage(testCaseTrollerLogMessage);
		}
	}
}
