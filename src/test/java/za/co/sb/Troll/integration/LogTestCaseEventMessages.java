package za.co.sb.Troll.integration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogTestCaseEventMessages 
{
	private static final Logger NBOL_LOG = LogManager.getLogger("NBOL");
	private static final Logger PAYEX_LOG = LogManager.getLogger("PAYEX");
	
	public static void main(String args[])
	{
		logTestCase1Messages();
	}
	
	public static void logAllTestCaseMessages()
	{
		logTestCase1Messages();
		logTestCase2Messages();
		logTestCase3Messages();
		logTestCase4Messages();
	}
	
	/**
	 * 
	 * 
	 * 
	 */
	public static void logTestCase1Messages()
	{
		// NBOL originates the request
		NBOL_LOG.info("TROLL, INTER, nInter01, 1, KE, PAYMENT, LOWCARE");
		NBOL_LOG.info("TROLL, INSTR, nInter01, nInstr01, 2");
		NBOL_LOG.info("TROLL, TRANS, nInstr01, nTrans01");
		NBOL_LOG.info("TROLL, TRANS, nInstr01, nTrans02");
		NBOL_LOG.info("TROLL, SENT, nInter01");

		// PAYEX receives and acknowledges it
		PAYEX_LOG.info("TROLL, RECD, nInter01");
		PAYEX_LOG.info("TROLL, INTERIM, nInter01, ACK,");

		// NBOL process the interim response (asynchronously)
		NBOL_LOG.info("TROLL, INTERIM, nInter01");

		// PAYEX forwards the instruction as two separate operations
		PAYEX_LOG.info("TROLL, INTER, pInter01, 1,,,");
	    PAYEX_LOG.info("TROLL, INSTR, pInter01, pInstr01, 1, nInstr01");
		PAYEX_LOG.info("TROLL, TRANS, pInstr01, pTrans01, nInstr01, nTrans01, Section A");
		PAYEX_LOG.info("TROLL, SENT, pInter01");
		PAYEX_LOG.info("TROLL, INTER, pInter02, 1,,,");
		PAYEX_LOG.info("TROLL, INSTR, pInter02, pInstr02, 1, nInstr01");
		PAYEX_LOG.info("TROLL, TRANS, pInstr02, pTrans02, nInstr01, nTrans02, Section A");
		PAYEX_LOG.info("TROLL, SENT, pInter02");

		// MAX acks the two interchanges
		PAYEX_LOG.info("TROLL, MAX, pInter01, ACK,");
		PAYEX_LOG.info("TROLL, MAX, pInter02, NAK, Error in something");

		// core banking responds
		PAYEX_LOG.info("TROLL, CORE, pInstr01, pTrans01, ACK, CoreBanking_reference");

		// PAYEX issues final status
		PAYEX_LOG.info("TROLL, FINAL, nInter01");

		// NBOL processes the final response
		NBOL_LOG.info("TROLL, FINAL, nInter01");
	}
	
	/**
	 * 
	 * 
	 * 
	 */
	public static void logTestCase2Messages()
	{
		// NBOL originates the request
		NBOL_LOG.info("TROLL, INTER, nInter05, 1, KE, PAYMENT, LOWCARE");
		NBOL_LOG.info("TROLL, INSTR, nInter05, nInstr05, 2");
		NBOL_LOG.info("TROLL, TRANS, nInstr05, nTrans05");
		NBOL_LOG.info("TROLL, TRANS, nInstr05, nTrans06");
		NBOL_LOG.info("TROLL, SENT, nInter05");
	}
	
	/**
	 * 
	 * 
	 * WHY DOES THIS NOT RESULT IN NBOL HAVING NAK?
	 */
	public static void logTestCase3Messages()
	{
		// NBOL originates the request
		NBOL_LOG.info("TROLL, INTER, nInter10, 1, KE, PAYMENT, LOWCARE");
		NBOL_LOG.info("TROLL, INSTR, nInter10, nInstr10, 2");
		NBOL_LOG.info("TROLL, TRANS, nInstr10, nTrans10");
		NBOL_LOG.info("TROLL, TRANS, nInstr10, nTrans11");
		NBOL_LOG.info("TROLL, SENT, nInter10");
		
		// PAYEX receives and acknowledges it
		PAYEX_LOG.info("TROLL, RECD, nInter10");
		PAYEX_LOG.info("TROLL, INTERIM, nInter10, NAK, Error Blah blah Blah");
	}
	
	
	public static void logTestCase4Messages()
	{
		// NBOL originates the request
		NBOL_LOG.info("TROLL, INTER, nInter15, 1, GH, PAYMENT, LOWCARE");
		NBOL_LOG.info("TROLL, INSTR, nInter15, nInstr15, 2");
		NBOL_LOG.info("TROLL, TRANS, nInstr15, nTrans15");
		NBOL_LOG.info("TROLL, TRANS, nInstr15, nTrans16");
		NBOL_LOG.info("TROLL, SENT, nInter15");
		
		// PAYEX receives and acknowledges it
		PAYEX_LOG.info("TROLL, RECD, nInter15");
		PAYEX_LOG.info("TROLL, INTERIM, nInter15, ACK, ");
		
		// PAYEX forwards the instruction as two separate operations
		PAYEX_LOG.info("TROLL, INTER, pInter15, 1,,,");
	    PAYEX_LOG.info("TROLL, INSTR, pInter15, pInstr15, 1, nInstr15");
		PAYEX_LOG.info("TROLL, TRANS, pInstr15, pTrans15, nInstr15, nTrans15, Section A");
		PAYEX_LOG.info("TROLL, SENT, pInter15");
		PAYEX_LOG.info("TROLL, INTER, pInter16, 1,,,");
		PAYEX_LOG.info("TROLL, INSTR, pInter16, pInstr16, 1, nInstr16");
		PAYEX_LOG.info("TROLL, TRANS, pInstr16, pTrans16, nInstr15, nTrans16, Section A");
		//PAYEX_LOG.info("TROLL, SENT, pInter02");
	}
}
