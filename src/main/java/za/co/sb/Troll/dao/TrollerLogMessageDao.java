package za.co.sb.Troll.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import za.co.sb.Troll.db.ConnectionFactory;
import za.co.sb.Troll.db.DbUtil;
import za.co.sb.Troll.dto.event.InstructionEventDto;
import za.co.sb.Troll.dto.event.InterchangeEventDto;
import za.co.sb.Troll.dto.event.TransactionEventDto;
import za.co.sb.Troll.enums.AckNakEnum;
import za.co.sb.Troll.enums.EventEnum;

public class TrollerLogMessageDao 
{
	static final String SP_INTERCHANGE_INSERT = "{CALL InterchangeInsert (?, ?, ?, ?, ?, ?, ?)}";
	static final String SP_INTERCHANGE_UPDATE = "{CALL InterchangeUpdate (?, ?, ?, ?, ?, ?)}";
	static final String SP_INSTRUCTION_INSERT = "{CALL InstructionInsert (?, ?, ?, ?, ?, ?, ?)}";
	static final String SP_INSTRUCTION_UPDATE = "{CALL InstructionUpdate (?, ?, ?, ?, ?, ?, ?)}";
	static final String SP_TRANSACTION_UPSERT = "{CALL TransactionUpSert (?, ?, ?, ?, ?, ?, ?, ?, ?)}";
	
	private Connection connection;
	
	/**
	 * Calls Stored Procedure called <b>InterchangeInsert</b> which will insert
	 * an INTERCHANGE event in the database.
	 * 
	 * Stored Procedure Parameter List
	 * <ol>
	 * <li>VARCHAR pSourceSystem</li>
	 * <li>TIMESTAMP pSourceTimestamp</li>
	 * <li>VARCHAR pInterchangeID</li>
	 * <li>INT pNumInstructions</li>
	 * <li>VARCHAR pCountry</li>
	 * <li>VARCHAR pMessageType</li>
	 * <li>VARCHAR pInstrumentGroup</li>
	 * </ol>
	 * 
	 * @throws SQLException
	 */
	public void insertInterchangeEvent(InterchangeEventDto interchangeEventDto) throws SQLException
    {
		try 
    	{
            connection = ConnectionFactory.getConnection();
            
            CallableStatement callableStatement = connection.prepareCall(SP_INTERCHANGE_INSERT);
            callableStatement.setString(1, interchangeEventDto.getSourceSystem());
            callableStatement.setTimestamp(2, new Timestamp(interchangeEventDto.getSourceTimeStamp().getTime()));
            callableStatement.setString(3, interchangeEventDto.getInterchangeId());
            callableStatement.setInt(4, interchangeEventDto.getNumInstructions());
            callableStatement.setString(5, interchangeEventDto.getCountry());
            callableStatement.setString(6, interchangeEventDto.getMessageType());
            callableStatement.setString(7, interchangeEventDto.getInstrumentGroup());
            
            callableStatement.execute();
        } 
    	catch (SQLException sqlex) 
        {
            throw sqlex;
        }
        finally 
        {
            DbUtil.close(connection);
        }
    }
	
	/**
	 * Calls Stored Procedure called <b>InterchangeUpdate</b> which will update
	 * an INTERCHANGE event in the database.
	 * 
	 * Stored Procedure Parameter List
	 * <ol>
	 * <li>VARCHAR pInterId</li>
	 * <li>TIMESTAMP pSourceTimestamp</li>
	 * <li>VARCHAR pSourceSystem</li>
	 * <li>VARCHAR pEvent</li>
	 * <li>VARCHAR pAckNak</li>
	 * <li>VARCHAR pText</li>
	 * </ol>
	 *
	 * @throws SQLException
	 */
	public void updateInterchangeEvent(InterchangeEventDto interchangeEventDto) throws SQLException
    {
		try 
    	{
            connection = ConnectionFactory.getConnection();
            
            CallableStatement callableStatement = connection.prepareCall(SP_INTERCHANGE_UPDATE);
            callableStatement.setString(1, interchangeEventDto.getInterchangeId());
            callableStatement.setTimestamp(2, new Timestamp(interchangeEventDto.getSourceTimeStamp().getTime()));
            callableStatement.setString(3, interchangeEventDto.getSourceSystem());
            callableStatement.setString(4, interchangeEventDto.getEvent() == EventEnum.UNKNOWN ? "" : interchangeEventDto.getEvent().toString());
            callableStatement.setString(5, interchangeEventDto.getAckNak() == AckNakEnum.UNKNOWN ? "" : interchangeEventDto.getAckNak().toString());
            callableStatement.setString(6, interchangeEventDto.getText());
            
            callableStatement.execute();
        } 
    	catch (SQLException sqlex) 
        {
            throw sqlex;
        }
        finally 
        {
            DbUtil.close(connection);
        }
    }
	
	/**
	 * Calls Stored Procedure called <b>InstructionInsert</b> which will insert
	 * an INSTRUCTION event in the database.
	 * 
	 * Stored Procedure Parameter List
	 * <ol>
	 * <li>VARCHAR pInterID</li>
	 * <li>VARCHAR pInstrID</li>
	 * <li>VARCHAR cInstrID</li>
	 * <li>TIMESTAMP pSourceTimestamp</li>
	 * <li>VRACHAR pSourceSystem</li>
	 * <li>VARCHAR pEvent</li>
	 * <li>INT numTransactions</li>
	 * </ol>
	 * 
	 * @throws SQLException
	 */
	public void insertInstructionEvent(InstructionEventDto instructionEventDto) throws SQLException
    {
		try 
    	{
            connection = ConnectionFactory.getConnection();
            
            CallableStatement callableStatement = connection.prepareCall(SP_INSTRUCTION_INSERT);
            callableStatement.setString(1, instructionEventDto.getInterchangeId());
            callableStatement.setString(2, instructionEventDto.getInstructionId());
            callableStatement.setString(3, instructionEventDto.getRecInstructionId());
            callableStatement.setTimestamp(4, new Timestamp(instructionEventDto.getSourceTimeStamp().getTime()));
            callableStatement.setString(5, instructionEventDto.getSourceSystem());
            callableStatement.setString(6, instructionEventDto.getEvent().toString());
            callableStatement.setInt(7, instructionEventDto.getNumInstructions());
            
            callableStatement.execute();
        } 
    	catch (SQLException sqlex) 
        {
            throw sqlex;
        }
        finally 
        {
            DbUtil.close(connection);
        }
    }

	/**
	 * Calls Stored Procedure called <b>InstructionInsert</b> which will insert
	 * an INSTRUCTION event in the database.
	 * 
	 * Stored Procedure Parameter List
	 * <ol>
	 * <li>VARCHAR pInterID</li>
	 * <li>VARCHAR pInstrID</li>
	 * <li>VARCHAR cInstrID</li>
	 * <li>TIMESTAMP pSourceTimestamp</li>
	 * <li>VRACHAR pSourceSystem</li>
	 * <li>VARCHAR pEvent</li>
	 * <li>INT numTransactions</li>
	 * </ol>
	 * 
	 * @throws SQLException
	 */
	public void updateInstructionEvent(InstructionEventDto instructionEventDto) throws SQLException
    {
		try 
    	{
            connection = ConnectionFactory.getConnection();
            
            CallableStatement callableStatement = connection.prepareCall(SP_INSTRUCTION_UPDATE);
            callableStatement.setString(1, instructionEventDto.getInterchangeId());
            callableStatement.setString(2, instructionEventDto.getInstructionId());
            callableStatement.setTimestamp(3, new Timestamp(instructionEventDto.getSourceTimeStamp().getTime()));
            callableStatement.setString(4, instructionEventDto.getSourceSystem());
            callableStatement.setString(5, instructionEventDto.getEvent().toString());
            callableStatement.setString(6, instructionEventDto.getAckNak() == AckNakEnum.UNKNOWN ? "" : instructionEventDto.getAckNak().toString());
            callableStatement.setString(7, instructionEventDto.getText());
            
            callableStatement.execute();
        } 
    	catch (SQLException sqlex) 
        {
            throw sqlex;
        }
        finally 
        {
            DbUtil.close(connection);
        }
    }
	
	/**
	 * Calls Stored Procedure called <b>TransactionUpSert</b> which will insert 
	 * or update a TRANSACTION event in the database.
	 * 
	 * Stored Procedure Parameter List
	 * <ol>
	 * <li>VARCHAR chanInstrID</li>
	 * <li>VARCHAR chanTransID</li>
	 * <li>VARCHAR pesInstrID</li>
	 * <li>VARCHAR pesTransID</li>
	 * <li>TIMESTAMP pSourceTimestamp</li>
	 * <li>VARCHAR pSourceSystem</li>
	 * <li>VARCHAR pEvent</li>
	 * <li>VARCHAR pAckNak</li>
	 * <li>VARCHAR pText</li>
	 * </ol>
	 * 
	 * @throws SQLException
	 */
	public void upsertTransactionEvent(TransactionEventDto transactionEventDto) throws SQLException
    {
		try 
    	{
            connection = ConnectionFactory.getConnection();
            
            CallableStatement callableStatement = connection.prepareCall(SP_TRANSACTION_UPSERT);
            callableStatement.setString(1, transactionEventDto.getInstructionId());
            callableStatement.setString(2, transactionEventDto.getTransactionId());
            callableStatement.setString(3, transactionEventDto.getRecInstructionId());
            callableStatement.setString(4, transactionEventDto.getRecTransactionId());
            callableStatement.setTimestamp(5, new Timestamp(transactionEventDto.getSourceTimeStamp().getTime()));
            callableStatement.setString(6, transactionEventDto.getSourceSystem());
            callableStatement.setString(7, transactionEventDto.getEvent().toString());
            callableStatement.setString(8, transactionEventDto.getAckNak() == AckNakEnum.UNKNOWN ? "" : transactionEventDto.getAckNak().toString());
            callableStatement.setString(9, transactionEventDto.getText());
            
            callableStatement.execute();
        } 
    	catch (SQLException sqlex) 
        {
            throw sqlex;
        }
        finally 
        {
            DbUtil.close(connection);
        }
    }
}