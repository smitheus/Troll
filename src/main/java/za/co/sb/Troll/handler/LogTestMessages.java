package za.co.sb.Troll.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogTestMessages 
{
	private static final Logger NBOL_LOG = LogManager.getLogger("NBOL");
	private static final Logger PAYEX_LOG = LogManager.getLogger("PAYEX");
	
	public static void main(String args[])
	{
		logTestMessage();
	}
	
	public static void logTestMessage()
	{
		// NBOL originates the request
		NBOL_LOG.error("TROLL, INTER, nInter01, 1, KE");
		NBOL_LOG.error("TROLL, INSTR, nInter01, nInstr01, 2");
		NBOL_LOG.error("TROLL, TRANS, nInter01, nTrans01");
		NBOL_LOG.error("TROLL, TRANS, nInter01, nTrans02");
		NBOL_LOG.error("TROLL, SENT, nInter01");

		// PAYEX receives and acknowledges it
		PAYEX_LOG.error("TROLL, RECD, nInter01");
		PAYEX_LOG.error("TROLL, INTERIM, INTER01, ACK,");

		// NBOL process the interim response (asynchronously)
		NBOL_LOG.error("TROLL, INTERIM, nInter01");

		// PAYEX forwards the instruction as two separate operations
		PAYEX_LOG.error("TROLL, INTER, pInter01, 1, 1, 1, KE");
		PAYEX_LOG.error("TROLL, INSTR, pInter01, pInstr01, 1, nInstr01");
		PAYEX_LOG.error("TROLL, TRANS, pInstr01, pTrans01, nInstr01, nTrans01, Section A");
		PAYEX_LOG.error("TROLL, SENT, pInter01");
		PAYEX_LOG.error("TROLL, INTER, pInter02, 1, 1, 1, KE");
		PAYEX_LOG.error("TROLL, INSTR, pInter02, pInstr02, 1, nInstr01");
		PAYEX_LOG.error("TROLL, TRANS, pInstr02, pTrans02, nTrans02, Section A");
		PAYEX_LOG.error("TROLL, SENT, pInter02");

		// MAX acks the two interchanges
		PAYEX_LOG.error("TROLL, MAX, pInter01, ACK,");
		PAYEX_LOG.error("TROLL, MAX, pInter02, NAK, Error in something");

		// core banking responds
		PAYEX_LOG.error("TROLL, CORE, pInstr01, pTrans01, ACK, CoreBanking_reference");

		// PAYEX issues final status
		PAYEX_LOG.error("TROLL, FINAL, nInter01");

		// NBOL processes the final response
		PAYEX_LOG.error("TROLL, FINAL, nInter01");
	}
}
