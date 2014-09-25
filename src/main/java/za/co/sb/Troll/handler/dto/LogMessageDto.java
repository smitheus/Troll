package za.co.sb.Troll.handler.dto;

import java.util.ArrayList;
import java.util.List;

public class LogMessageDto 
{
	private String sourceSystem;
	private String logMessage;
	
	public LogMessageDto()
	{
	}
	
	public LogMessageDto(String sourceSystem, String logMessage)
	{
		this.sourceSystem = sourceSystem;
		this.logMessage = logMessage;
	}
	
	public static List<LogMessageDto> buildTestLogMessageDto()
	{
		/*List<LogMessageDto> logMessageDtoList = new ArrayList<LogMessageDto>();
		
		logMessageDtoList.add(new LogMessageDto());
		
		logMessageDtoList.add(new LogMessageDto("NBOL","2014-09-11 12:23:41.201 [main] INFO - TROLL, INTER, nInter01, 1, KE"
				logMessageDtoList.add(new LogMessageDto("NBOL","2014-09-11 12:23:42.202 [main] INFO - TROLL, INSTR, nInter01, nInstr01, 2"
						logMessageDtoList.add(new LogMessageDto("NBOL","2014-09-11 12:23:43.202 [main] INFO - TROLL, TRANS, nInter01, nTrans01"
								logMessageDtoList.add(new LogMessageDto("NBOL","2014-09-11 12:23:44.202 [main] INFO - TROLL, TRANS, nInter01, nTrans02"
										logMessageDtoList.add(new LogMessageDto("NBOL","2014-09-11 12:23:45.202 [main] INFO - TROLL, SENT, nInter01"
												logMessageDtoList.add(new LogMessageDto("PAYEX",'2014-09-11 12:23:46.202 [main] INFO - TROLL, RECD, nInter01"
														logMessageDtoList.add(new LogMessageDto("PAYEX","2014-09-11 12:23:47.202 [main] INFO - TROLL, INTERIM, INTER01, ACK,"
																logMessageDtoList.add(new LogMessageDto("NBOL","2014-09-11 12:23:48.202 [main] INFO - TROLL, INTERIM, nInter01"
																		logMessageDtoList.add(new LogMessageDto("PAYEX","2014-09-11 12:23:49.202 [main] INFO - TROLL, INTER, pInter01, 1, 1, 1, KE"
																				logMessageDtoList.add(new LogMessageDto("PAYEX","2014-09-11 12:23:50.202 [main] INFO - TROLL, INSTR, pInter01, pInstr01, 1, nInstr01"
																						logMessageDtoList.add(new LogMessageDto("PAYEX","2014-09-11 12:23:51.202 [main] INFO - TROLL, TRANS, pInstr01, pTrans01, nInstr01, nTrans01, Section A"
																								logMessageDtoList.add(new LogMessageDto("PAYEX","2014-09-11 12:23:52.202 [main] INFO - TROLL, SENT, pInter01"
																										logMessageDtoList.add(new LogMessageDto("PAYEX","2014-09-11 12:23:43.202 [main] INFO - TROLL, INTER, pInter02, 1, 1, 1, KE"
																												logMessageDtoList.add(new LogMessageDto("PAYEX","2014-09-11 12:23:54.202 [main] INFO - TROLL, INSTR, pInter02, pInstr02, 1, nInstr01"
																														logMessageDtoList.add(new LogMessageDto(	"PAYEX","2014-09-11 12:23:55.202 [main] INFO - TROLL, TRANS, pInstr02, pTrans02, nTrans02, Section A"
																																logMessageDtoList.add(new LogMessageDto(	"PAYEX","2014-09-11 12:23:56.202 [main] INFO - TROLL, SENT, pInter02"
																																		logMessageDtoList.add(new LogMessageDto(	"PAYEX","2014-09-11 12:23:57.203 [main] INFO - TROLL, MAX, pInter01, ACK,"
																																				logMessageDtoList.add(new LogMessageDto("PAYEX","2014-09-11 12:23:58.203 [main] INFO - TROLL, MAX, pInter02, NAK, Error in something"
																																						logMessageDtoList.add(new LogMessageDto("PAYEX","2014-09-11 12:23:59.203 [main] INFO - TROLL, CORE, pInstr01, pTrans01, ACK, CoreBanking_reference"
																																								logMessageDtoList.add(new LogMessageDto("PAYEX","2014-09-11 12:23:60.203 [main] INFO - TROLL, FINAL, nInter01"
	"NBOL","2014-09-11 12:23:61.203 [main] INFO - TROLL, FINAL, nInter01"*/
		return null;
	}
}
