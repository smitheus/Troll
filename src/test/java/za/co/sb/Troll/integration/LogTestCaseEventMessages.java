package za.co.sb.Troll.integration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogTestCaseEventMessages 
{
	private static final Logger NBOL_LOG = LogManager.getLogger("NBOL");
	private static final Logger PAYEX_LOG = LogManager.getLogger("PAYEX");

	public static void main(String args[]) throws InterruptedException 
	{
		logAllTestCaseMessages();
	}

	public static void logAllTestCaseMessages() throws InterruptedException 
	{
		logTestCase_AllSuccess_PART1();
		Thread.sleep(22);
        logTestCase_AllSuccess_PART2();
        Thread.sleep(22);
		logTestCase_AllSuccess_PART3();
		Thread.sleep(22);
		logTestCase_AllSuccess_PART4();
		Thread.sleep(22);
		logTestCase_AllSuccess_PART5();
		Thread.sleep(22);
		logTestCase_AllSuccess_PART6();
		Thread.sleep(22);
		logTestCase_AllSuccess_PART7();
		Thread.sleep(22);
		logTestCase_AllSuccess_PART8();
		Thread.sleep(22);
		logTestCase_AllSuccess_PART9();
		Thread.sleep(22);
		logTestCase_AllSuccess_PART10();
/*		
		logTestCase_MaxNakSectionA();
		logTestCase_T24RejectsSectionA();
		logTestCase_T24RejectsSectionB();
		logTestCase_PayexRejects();
		logTestCase_MaxDoesNotRespond();
		logTestCase_MaxSlowResponse();
		logTestCase_MaxSlowResponseT24RejectsSectionA();
		logTestCase_MaxSlowResponseT24DoesNotRespond();
		logTestCase_MaxNakSectionB();*/
	}
	
	public static void logTestCase_AllSuccess_PART1() 
	{
		// NBOL originates the request
		NBOL_LOG.info("TROLL, INTER, nInter01, 1, KE, PAYMENT, LOWCARE");
		NBOL_LOG.info("TROLL, INSTR, nInter01, nInstr01, 2");
		NBOL_LOG.info("TROLL, TRANS, nInstr01, nTrans01");
		NBOL_LOG.info("TROLL, TRANS, nInstr01, nTrans02");
		NBOL_LOG.info("TROLL, SENT, nInter01");
	}
	
	public static void logTestCase_AllSuccess_PART2() 
	{
		// PAYEX receives and acknowledges it
		PAYEX_LOG.info("TROLL, RECD, nInter01");
		PAYEX_LOG.info("TROLL, INTERIM, nInter01, ACK,");

		// NBOL process the interim response (asynchronously)
		NBOL_LOG.info("TROLL, INTERIM, nInter01");
	}
	
	public static void logTestCase_AllSuccess_PART3() 
	{
		// PAYEX forwards the instruction as two separate FUNDING operations
		PAYEX_LOG.info("TROLL, INSTR,  , pInstr01, 1, nInstr01");
		PAYEX_LOG.info("TROLL, TRANS, pInstr01, pTrans01, nInstr01, nTrans01, FUNDING");
		PAYEX_LOG.info("TROLL, TRANS, pInstr01, pTrans01B, nInstr01, nTrans01, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, , pInstr01,,");

		PAYEX_LOG.info("TROLL, INSTR, , pInstr02, 1, nInstr01");
		PAYEX_LOG.info("TROLL, TRANS, pInstr02, pTrans02, nInstr01, nTrans02, FUNDING");
		PAYEX_LOG.info("TROLL, TRANS, pInstr02, pTrans02B, nInstr01, nTrans02, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, , pInstr02,,");
	}
	
	public static void logTestCase_AllSuccess_PART4() 
	{
		// MAX ACKs the two interchanges
		PAYEX_LOG.info("TROLL, MAX, ,pInstr01, ACK,");
		PAYEX_LOG.info("TROLL, MAX, ,pInstr02, ACK,");
	}
	
	public static void logTestCase_AllSuccess_PART5() 
	{		
		// core banking responds to the FUNDING messages
		PAYEX_LOG.info("TROLL, CORE, pInstr01, pTrans01, ACK, coreRef01");
		PAYEX_LOG.info("TROLL, CORE, pInstr02, pTrans02, ACK, coreRef02");
	}
	
	public static void logTestCase_AllSuccess_PART6() 
	{		
		// PAYEX sends the FUNDED interchanges
		PAYEX_LOG.info("TROLL, INSTR, , pInstr03, 1, nInstr01");
		PAYEX_LOG.info("TROLL, TRANS, pInstr03, pTrans03, nInstr01, nTrans01, FUNDED");
		PAYEX_LOG.info("TROLL, TRANS, pInstr03, pTrans03B, nInstr01, nTrans01, FUNDED");
		PAYEX_LOG.info("TROLL, SENT, , pInstr03,,");
		
		PAYEX_LOG.info("TROLL, INSTR, , pInstr04, 1, nInstr01");
		PAYEX_LOG.info("TROLL, TRANS, pInstr04, pTrans04, nInstr01, nTrans02, FUNDED");
		PAYEX_LOG.info("TROLL, TRANS, pInstr04, pTrans04B, nInstr01, nTrans02, FUNDED");
		PAYEX_LOG.info("TROLL, SENT, , pInstr04,,");
	}
	
	public static void logTestCase_AllSuccess_PART7() 
	{
		// MAX ACKs the two interchanges
		PAYEX_LOG.info("TROLL, MAX, ,pInstr03, ACK,");
		PAYEX_LOG.info("TROLL, MAX, ,pInstr04, ACK,");
	}
	
	public static void logTestCase_AllSuccess_PART8() 
	{
		// core banking responds to the FUNDED messages
		PAYEX_LOG.info("TROLL, CORE, pInstr03, pTrans03, ACK, coreRef03");
		PAYEX_LOG.info("TROLL, CORE, pInstr04, pTrans04, ACK, coreRef04");
	}
	
	public static void logTestCase_AllSuccess_PART9() 
	{		
		// PAYEX issues final status
		PAYEX_LOG.info("TROLL, FINAL, nInter01");
	}
	
	public static void logTestCase_AllSuccess_PART10() 
	{
		// NBOL processes the final response
		NBOL_LOG.info("TROLL, FINAL, nInter01");
	}
	
	public static void logTestCase_MaxNakSectionA() 
	{
		// NBOL originates the request
		NBOL_LOG.info("TROLL, INTER, nInter11, 1, KE, PAYMENT, LOWCARE");
		NBOL_LOG.info("TROLL, INSTR, nInter11, nInstr11, 2");
		NBOL_LOG.info("TROLL, TRANS, nInstr11, nTrans11");
		NBOL_LOG.info("TROLL, TRANS, nInstr11, nTrans12");
		NBOL_LOG.info("TROLL, SENT, nInter11");

		// PAYEX receives and acknowledges it
		PAYEX_LOG.info("TROLL, RECD, nInter11");
		PAYEX_LOG.info("TROLL, INTERIM, nInter11, ACK,");

		// NBOL process the interim response (asynchronously)
		NBOL_LOG.info("TROLL, INTERIM, nInter11");

		// PAYEX forwards the instruction as two separate FUNDING operations

		PAYEX_LOG.info("TROLL, INSTR, pInstr11, pInstr11, 1, nInstr11");
		PAYEX_LOG.info("TROLL, TRANS, pInstr11, pTrans11, nInstr11, nTrans11, FUNDING");
		PAYEX_LOG.info("TROLL, TRANS, pInstr11, pTrans11B, nInstr11, nTrans11, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, pInstr11");

		PAYEX_LOG.info("TROLL, INSTR, pInstr12, pInstr12, 1, nInstr11");
		PAYEX_LOG.info("TROLL, TRANS, pInstr12, pTrans12, nInstr11, nTrans12, FUNDING");
		PAYEX_LOG.info("TROLL, TRANS, pInstr12, pTrans12B, nInstr11, nTrans12, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, pInstr12");
		
		// MAX ACKs the two interchanges
		PAYEX_LOG.info("TROLL, MAX, pInstr11, ACK,");
		PAYEX_LOG.info("TROLL, MAX, pInstr12, NAK, Account field too long");
		
		// core banking responds to the FUNDING messages
		PAYEX_LOG.info("TROLL, CORE, pInstr11, pTrans11, ACK, coreRef11");
		
		// PAYEX sends the FUNDED interchanges

		PAYEX_LOG.info("TROLL, INSTR, pInstr13, pInstr13, 1, nInstr11");
		PAYEX_LOG.info("TROLL, TRANS, pInstr13, pTrans13, nInstr11, nTrans11, FUNDED");
		PAYEX_LOG.info("TROLL, TRANS, pInstr13, pTrans13B, nInstr11, nTrans11, FUNDED");
		PAYEX_LOG.info("TROLL, SENT, pInstr13");
		
		// MAX ACKs the two interchanges
		PAYEX_LOG.info("TROLL, MAX, pInstr13, ACK,");

		// core banking responds to the FUNDING messages
		PAYEX_LOG.info("TROLL, CORE, pInstr13, pTrans13, ACK, coreRef13");
		// PAYEX issues final status
		PAYEX_LOG.info("TROLL, FINAL, nInter11");

		// NBOL processes the final response
		NBOL_LOG.info("TROLL, FINAL, nInter11");
	}

	public static void logTestCase_T24RejectsSectionA() 
	{
		// NBOL originates the request
		NBOL_LOG.info("TROLL, INTER, nInter21, 1, KE, PAYMENT, LOWCARE");
		NBOL_LOG.info("TROLL, INSTR, nInter21, nInstr21, 2");
		NBOL_LOG.info("TROLL, TRANS, nInstr21, nTrans21");
		NBOL_LOG.info("TROLL, TRANS, nInstr21, nTrans22");
		NBOL_LOG.info("TROLL, SENT, nInter21");

		// PAYEX receives and acknowledges it
		PAYEX_LOG.info("TROLL, RECD, nInter21");
		PAYEX_LOG.info("TROLL, INTERIM, nInter21, ACK,");

		// NBOL process the interim response (asynchronously)
		NBOL_LOG.info("TROLL, INTERIM, nInter21");

		// PAYEX forwards the instruction as two separate FUNDING operations

		PAYEX_LOG.info("TROLL, INSTR, pInstr21, pInstr21, 1, nInstr21");
		PAYEX_LOG.info("TROLL, TRANS, pInstr21, pTrans21, nInstr21, nTrans21, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, pInstr21");

		PAYEX_LOG.info("TROLL, INSTR, pInstr22, pInstr22, 1, nInstr21");
		PAYEX_LOG.info("TROLL, TRANS, pInstr22, pTrans22, nInstr21, nTrans22, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, pInstr22");

		// MAX ACKs the two interchanges
		PAYEX_LOG.info("TROLL, MAX, pInstr21, ACK,");
		PAYEX_LOG.info("TROLL, MAX, pInstr22, ACK,");

		// core banking responds to the FUNDING messages
		PAYEX_LOG.info("TROLL, CORE, pInstr21, pTrans21, ACK, coreRef21");
		PAYEX_LOG.info("TROLL, CORE, pInstr22, pTrans22, NAK, Insufficient funds");
		
		// PAYEX sends the FUNDED interchange

		PAYEX_LOG.info("TROLL, INSTR, pInstr23, pInstr23, 1, nInstr21");
		PAYEX_LOG.info("TROLL, TRANS, pInstr23, pTrans23, nInstr21, nTrans21, FUNDED");
		PAYEX_LOG.info("TROLL, SENT, pInstr23");

		// MAX ACKs the two interchanges
		PAYEX_LOG.info("TROLL, MAX, pInstr23, ACK,");

		// core banking responds to the FUNDED message
		PAYEX_LOG.info("TROLL, CORE, pInstr23, pTrans23, ACK, coreRef23");
		// PAYEX issues final status
		PAYEX_LOG.info("TROLL, FINAL, nInter21");

		// NBOL processes the final response
		NBOL_LOG.info("TROLL, FINAL, nInter21");
	}

	public static void logTestCase_T24RejectsSectionB() 
	{
		// NBOL originates the request
		NBOL_LOG.info("TROLL, INTER, nInter31, 1, KE, PAYMENT, LOWCARE");
		NBOL_LOG.info("TROLL, INSTR, nInter31, nInstr31, 2");
		NBOL_LOG.info("TROLL, TRANS, nInstr31, nTrans31");
		NBOL_LOG.info("TROLL, TRANS, nInstr31, nTrans32");
		NBOL_LOG.info("TROLL, SENT, nInter31");

		// PAYEX receives and acknowledges it
		PAYEX_LOG.info("TROLL, RECD, nInter31");
		PAYEX_LOG.info("TROLL, INTERIM, nInter31, ACK,");

		// NBOL process the interim response (asynchronously)
		NBOL_LOG.info("TROLL, INTERIM, nInter31");

		// PAYEX forwards the instruction as two separate operations

		PAYEX_LOG.info("TROLL, INSTR, pInstr31, pInstr31, 1, nInstr31");
		PAYEX_LOG.info("TROLL, TRANS, pInstr31, pTrans31, nInstr31, nTrans31, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, pInstr31");

		PAYEX_LOG.info("TROLL, INSTR, pInstr32, pInstr32, 1, nInstr31");
		PAYEX_LOG.info("TROLL, TRANS, pInstr32, pTrans32, nInstr31, nTrans32, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, pInstr32");

		// MAX ACKs the two interchanges
		PAYEX_LOG.info("TROLL, MAX, pInstr31, ACK,");
		PAYEX_LOG.info("TROLL, MAX, pInstr32, ACK,");

		// core banking responds to the FUNDING messages
		PAYEX_LOG.info("TROLL, CORE, pInstr31, pTrans31, ACK, coreRef31");
		PAYEX_LOG.info("TROLL, CORE, pInstr32, pTrans32, ACK, coreRef32");
		
		// PAYEX sends the FUNDED interchanges

		PAYEX_LOG.info("TROLL, INSTR, pInstr33, pInstr33, 1, nInstr31");
		PAYEX_LOG.info("TROLL, TRANS, pInstr33, pTrans33, nInstr31, nTrans31, FUNDED");
		PAYEX_LOG.info("TROLL, SENT, pInstr33");

		PAYEX_LOG.info("TROLL, INSTR, pInstr34, pInstr32, 1, nInstr31");
		PAYEX_LOG.info("TROLL, TRANS, pInstr34, pTrans34, nInstr31, nTrans32, FUNDED");
		PAYEX_LOG.info("TROLL, SENT, pInstr34");

		// MAX ACKs the two interchanges
		PAYEX_LOG.info("TROLL, MAX, pInstr33, ACK,");
		PAYEX_LOG.info("TROLL, MAX, pInstr34, ACK,");

		// core banking responds to the FUNDED messages
		PAYEX_LOG.info("TROLL, CORE, pInstr33, pTrans33, ACK, coreRef33");
		PAYEX_LOG.info("TROLL, CORE, pInstr34, pTrans34, NAK, Account not found");
		
		// PAYEX sends a REVERSE

		PAYEX_LOG.info("TROLL, INSTR, pInstr35, pInstr35, 1, nInstr31");
		PAYEX_LOG.info("TROLL, TRANS, pInstr35, pTrans35, nInstr31, nTrans32, Reverse");
		PAYEX_LOG.info("TROLL, SENT, pInstr35");

		// MAX ACKs the reverse
		PAYEX_LOG.info("TROLL, MAX, pInstr35, ACK,");

		// core banking responds to the REVERSE message
		PAYEX_LOG.info("TROLL, CORE, pInstr35, pTrans35, ACK, coreRef35");
		// PAYEX issues final status
		PAYEX_LOG.info("TROLL, FINAL, nInter31");

		// NBOL processes the final response
		NBOL_LOG.info("TROLL, FINAL, nInter31");
	}

	public static void logTestCase_PayexRejects() 
	{
		// NBOL originates the request
		NBOL_LOG.info("TROLL, INTER, nInter41, 1, KE, PAYMENT, LOWCARE");
		NBOL_LOG.info("TROLL, INSTR, nInter41, nInstr41, 2");
		NBOL_LOG.info("TROLL, TRANS, nInstr41, nTrans41");
		NBOL_LOG.info("TROLL, TRANS, nInstr41, nTrans42");
		NBOL_LOG.info("TROLL, SENT, nInter41");

		// PAYEX receives and acknowledges it
		PAYEX_LOG.info("TROLL, RECD, nInter41");
		PAYEX_LOG.info("TROLL, INTERIM, nInter41, NAK,Error somewhere");

		// NBOL process the interim response (asynchronously)
		NBOL_LOG.info("TROLL, INTERIM, nInter41");
		
		// PAYEX issues final status
		PAYEX_LOG.info("TROLL, FINAL, nInter41");

		// NBOL processes the final response
		NBOL_LOG.info("TROLL, FINAL, nInter41");
	}

	public static void logTestCase_MaxDoesNotRespond() 
	{
		// NBOL originates the request
		NBOL_LOG.info("TROLL, INTER, nInter51, 1, KE, PAYMENT, LOWCARE");
		NBOL_LOG.info("TROLL, INSTR, nInter51, nInstr51, 2");
		NBOL_LOG.info("TROLL, TRANS, nInstr51, nTrans51");
		NBOL_LOG.info("TROLL, TRANS, nInstr51, nTrans52");
		NBOL_LOG.info("TROLL, SENT, nInter51");

		// PAYEX receives and acknowledges it
		PAYEX_LOG.info("TROLL, RECD, nInter51");
		PAYEX_LOG.info("TROLL, INTERIM, nInter51, ACK,");

		// NBOL process the interim response (asynchronously)
		NBOL_LOG.info("TROLL, INTERIM, nInter51");

		// PAYEX forwards the instruction as two separate FUNDING operations

		PAYEX_LOG.info("TROLL, INSTR, pInstr51, pInstr51, 1, nInstr51");
		PAYEX_LOG.info("TROLL, TRANS, pInstr51, pTrans51, nInstr51, nTrans51, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, pInstr51");

		PAYEX_LOG.info("TROLL, INSTR, pInstr52, pInstr52, 1, nInstr51");
		PAYEX_LOG.info("TROLL, TRANS, pInstr52, pTrans52, nInstr51, nTrans52, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, pInstr52");

		// expecting the MAX ACKs
	}

	public static void logTestCase_MaxSlowResponse() throws InterruptedException 
	{
		// NBOL originates the request
		NBOL_LOG.info("TROLL, INTER, nInter61, 1, KE, PAYMENT, LOWCARE");
		NBOL_LOG.info("TROLL, INSTR, nInter61, nInstr61, 2");
		NBOL_LOG.info("TROLL, TRANS, nInstr61, nTrans61");
		NBOL_LOG.info("TROLL, TRANS, nInstr61, nTrans62");
		NBOL_LOG.info("TROLL, SENT, nInter61");

		// PAYEX receives and acknowledges it
		PAYEX_LOG.info("TROLL, RECD, nInter61");
		PAYEX_LOG.info("TROLL, INTERIM, nInter61, ACK,");

		// NBOL process the interim response (asynchronously)
		NBOL_LOG.info("TROLL, INTERIM, nInter61");

		// PAYEX forwards the instruction as two separate FUNDING operations

		PAYEX_LOG.info("TROLL, INSTR, pInstr61, pInstr61, 1, nInstr61");
		PAYEX_LOG.info("TROLL, TRANS, pInstr61, pTrans61, nInstr61, nTrans61, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, pInstr61");

		PAYEX_LOG.info("TROLL, INSTR, pInstr62, pInstr62, 1, nInstr61");
		PAYEX_LOG.info("TROLL, TRANS, pInstr62, pTrans62, nInstr61, nTrans62, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, pInstr62");

		// MAX ACKs the two interchanges after a delay
		Thread.sleep(6000);
		PAYEX_LOG.info("TROLL, MAX, pInstr61, ACK,");
		PAYEX_LOG.info("TROLL, MAX, pInstr62, ACK,");

		// core banking responds to the FUNDING messages
		PAYEX_LOG.info("TROLL, CORE, pInstr61, pTrans61, ACK, coreRef61");
		PAYEX_LOG.info("TROLL, CORE, pInstr62, pTrans62, ACK, coreRef62");
		// PAYEX sends the FUNDED interchanges

		PAYEX_LOG.info("TROLL, INSTR, pInstr63, pInstr63, 1, nInstr61");
		PAYEX_LOG.info("TROLL, TRANS, pInstr63, pTrans63, nInstr61, nTrans61, FUNDED");
		PAYEX_LOG.info("TROLL, SENT, pInstr63");

		PAYEX_LOG.info("TROLL, INSTR, pInstr64, pInstr62, 1, nInstr61");
		PAYEX_LOG.info("TROLL, TRANS, pInstr64, pTrans64, nInstr61, nTrans62, FUNDED");
		PAYEX_LOG.info("TROLL, SENT, pInstr64");

		// MAX ACKs the two interchanges
		PAYEX_LOG.info("TROLL, MAX, pInstr63, ACK,");
		PAYEX_LOG.info("TROLL, MAX, pInstr64, ACK,");

		// core banking responds to the FUNDED messages
		PAYEX_LOG.info("TROLL, CORE, pInstr63, pTrans63, ACK, coreRef63");
		PAYEX_LOG.info("TROLL, CORE, pInstr64, pTrans64, ACK, coreRef64");
		// PAYEX issues final status
		PAYEX_LOG.info("TROLL, FINAL, nInter61");

		// NBOL processes the final response
		NBOL_LOG.info("TROLL, FINAL, nInter61");
	}

	public static void logTestCase_MaxSlowResponseT24RejectsSectionA() throws InterruptedException 
	{
		// NBOL originates the request
		NBOL_LOG.info("TROLL, INTER, nInter71, 1, KE, PAYMENT, LOWCARE");
		NBOL_LOG.info("TROLL, INSTR, nInter71, nInstr71, 2");
		NBOL_LOG.info("TROLL, TRANS, nInstr71, nTrans71");
		NBOL_LOG.info("TROLL, TRANS, nInstr71, nTrans72");
		NBOL_LOG.info("TROLL, SENT, nInter71");

		// PAYEX receives and acknowledges it
		PAYEX_LOG.info("TROLL, RECD, nInter71");
		PAYEX_LOG.info("TROLL, INTERIM, nInter71, ACK,");

		// NBOL process the interim response (asynchronously)
		NBOL_LOG.info("TROLL, INTERIM, nInter71");

		// PAYEX forwards the instruction as two separate FUNDING operations

		PAYEX_LOG.info("TROLL, INSTR, pInstr71, pInstr71, 1, nInstr71");
		PAYEX_LOG.info("TROLL, TRANS, pInstr71, pTrans71, nInstr71, nTrans71, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, pInstr71");

		PAYEX_LOG.info("TROLL, INSTR, pInstr72, pInstr72, 1, nInstr71");
		PAYEX_LOG.info("TROLL, TRANS, pInstr72, pTrans72, nInstr71, nTrans72, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, pInstr72");

		// MAX ACKs the two interchanges after a delay
		Thread.sleep(7000);
		PAYEX_LOG.info("TROLL, MAX, pInstr71, ACK,");
		PAYEX_LOG.info("TROLL, MAX, pInstr72, ACK,");

		// core banking responds to the FUNDING messages
		PAYEX_LOG.info("TROLL, CORE, pInstr71, pTrans71, ACK, coreRef71");
		PAYEX_LOG.info("TROLL, CORE, pInstr72, pTrans72, NAK, Insufficient funds");
		// PAYEX sends the FUNDED interchange

		PAYEX_LOG.info("TROLL, INSTR, pInstr73, pInstr73, 1, nInstr71");
		PAYEX_LOG.info("TROLL, TRANS, pInstr73, pTrans73, nInstr71, nTrans71, FUNDED");
		PAYEX_LOG.info("TROLL, SENT, pInstr73");
		// MAX ACKs the interchange
		PAYEX_LOG.info("TROLL, MAX, pInstr73, ACK,");

		// core banking responds to the FUNDED messages
		PAYEX_LOG.info("TROLL, CORE, pInstr73, pTrans73, ACK, coreRef73");
		// PAYEX issues final status
		PAYEX_LOG.info("TROLL, FINAL, nInter71");

		// NBOL processes the final response
		NBOL_LOG.info("TROLL, FINAL, nInter71");
	}

	public static void logTestCase_MaxSlowResponseT24DoesNotRespond() throws InterruptedException 
	{
		// NBOL originates the request
		NBOL_LOG.info("TROLL, INTER, nInter81, 1, KE, PAYMENT, LOWCARE");
		NBOL_LOG.info("TROLL, INSTR, nInter81, nInstr81, 2");
		NBOL_LOG.info("TROLL, TRANS, nInstr81, nTrans81");
		NBOL_LOG.info("TROLL, TRANS, nInstr81, nTrans82");
		NBOL_LOG.info("TROLL, SENT, nInter81");

		// PAYEX receives and acknowledges it
		PAYEX_LOG.info("TROLL, RECD, nInter81");
		PAYEX_LOG.info("TROLL, INTERIM, nInter81, ACK,");

		// NBOL process the interim response (asynchronously)
		NBOL_LOG.info("TROLL, INTERIM, nInter81");

		// PAYEX forwards the instruction as two separate FUNDING operations

		PAYEX_LOG.info("TROLL, INSTR, pInstr81, pInstr81, 1, nInstr81");
		PAYEX_LOG.info("TROLL, TRANS, pInstr81, pTrans81, nInstr81, nTrans81, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, pInstr81");

		PAYEX_LOG.info("TROLL, INSTR, pInstr82, pInstr82, 1, nInstr81");
		PAYEX_LOG.info("TROLL, TRANS, pInstr82, pTrans82, nInstr81, nTrans82, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, pInstr82");

		// MAX ACKs the two interchanges after a delay
		Thread.sleep(8000);
		PAYEX_LOG.info("TROLL, MAX, pInstr81, ACK,");
		PAYEX_LOG.info("TROLL, MAX, pInstr82, ACK,");

		// expecting core banking response ....
	}

	public static void logTestCase_MaxNakSectionB() 
	{
		// NBOL originates the request
		NBOL_LOG.info("TROLL, INTER, nInter91, 1, KE, PAYMENT, LOWCARE");
		NBOL_LOG.info("TROLL, INSTR, nInter91, nInstr91, 2");
		NBOL_LOG.info("TROLL, TRANS, nInstr91, nTrans91");
		NBOL_LOG.info("TROLL, TRANS, nInstr91, nTrans92");
		NBOL_LOG.info("TROLL, SENT, nInter91");

		// PAYEX receives and acknowledges it
		PAYEX_LOG.info("TROLL, RECD, nInter91");
		PAYEX_LOG.info("TROLL, INTERIM, nInter91, ACK,");

		// NBOL process the interim response (asynchronously)
		NBOL_LOG.info("TROLL, INTERIM, nInter91");

		// PAYEX forwards the instruction as two separate FUNDING operations

		PAYEX_LOG.info("TROLL, INSTR, pInstr91, pInstr91, 1, nInstr91");
		PAYEX_LOG.info("TROLL, TRANS, pInstr91, pTrans91, nInstr91, nTrans91, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, pInstr91");

		PAYEX_LOG.info("TROLL, INSTR, pInstr92, pInstr92, 1, nInstr91");
		PAYEX_LOG.info("TROLL, TRANS, pInstr92, pTrans92, nInstr91, nTrans92, FUNDING");
		PAYEX_LOG.info("TROLL, SENT, pInstr92");

		// MAX ACKs the two interchanges
		PAYEX_LOG.info("TROLL, MAX, pInstr91, ACK,");
		PAYEX_LOG.info("TROLL, MAX, pInstr92, ACK,");

		// core banking responds to the FUNDING messages
		PAYEX_LOG.info("TROLL, CORE, pInstr91, pTrans91, ACK, coreRef91");
		PAYEX_LOG.info("TROLL, CORE, pInstr92, pTrans92, ACK, coreRef92");
		// PAYEX sends the FUNDED interchanges

		PAYEX_LOG.info("TROLL, INSTR, pInstr93, pInstr93, 1, nInstr91");
		PAYEX_LOG.info("TROLL, TRANS, pInstr93, pTrans93, nInstr91, nTrans91, FUNDED");
		PAYEX_LOG.info("TROLL, SENT, pInstr93");

		PAYEX_LOG.info("TROLL, INSTR, pInstr94, pInstr94, 1, nInstr91");
		PAYEX_LOG.info("TROLL, TRANS, pInstr94, pTrans94, nInstr91, nTrans92, FUNDED");
		PAYEX_LOG.info("TROLL, SENT, pInstr94");

		// MAX ACKs the two interchanges
		PAYEX_LOG.info("TROLL, MAX, pInstr93, ACK,");
		PAYEX_LOG.info("TROLL, MAX, pInstr94, NAK, Account too long");

		// core banking responds to the FUNDED messages
		PAYEX_LOG.info("TROLL, CORE, pInstr93, pTrans93, ACK, coreRef93");
		// PAYEX issues final status
		PAYEX_LOG.info("TROLL, FINAL, nInter91");

		// NBOL processes the final response
		NBOL_LOG.info("TROLL, FINAL, nInter91");
	}
}
