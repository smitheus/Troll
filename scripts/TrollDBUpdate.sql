use troll;

drop procedure InterchangeInsert ;
create procedure InterchangeInsert (pSourceSystem varchar(10), pSourceTimestamp timestamp, pInterId varchar(40), pNumInstructions int, pCountry varchar(10), pMessageType varchar(20), pInstrumentGroup varchar(20))
begin
	declare insertTimeStamp timestamp ;
	declare totalCount int ;
	
	select count(*) from channelInterchange where interchangeID = pInterId into totalCount ;
	if (totalCount = 0) then
		insert into channelInterchange (sourceSystem, interchangeID, insertTimeStamp, sourceTimestamp, numInstructions, country, messageType, instrumentGroup) values (pSourceSystem, pInterId, insertTimeStamp, pSourceTimestamp, pNumInstructions, pCountry, pMessageType, pInstrumentGroup) ;
	end if ;

		# add a row to the history
	insert into channelInterchangeHistory (interchangeID, insertTimeStamp, sourceTimeStamp, sourceSystem, event, ackNak, text) values (pInterId, insertTimeStamp, pSourceTimestamp, pSourceSystem, 'CREATE', '', '') ;
end ;

drop procedure InterchangeUpdate ;
create procedure InterchangeUpdate (pInterId varchar(40), pSourceTimestamp timestamp, pSourceSystem varchar(10), pEvent varchar(10), pAckNak varchar(3), pText varchar(80))
begin
	declare insertTimeStamp timestamp ;
	declare totalCount int ;
	declare respReqd varchar(1) ;
	declare sla1Due timestamp ;
	declare sla2Due timestamp ;
	
	
	select count(*) from channelInterchange where interchangeID = pInterId into totalCount ;
	if (totalCount = 0) then
		insert into channelInterchange (interchangeID, insertTimeStamp, numInstructions, country, messageType, instrumentGroup) values (pInterId, insertTimeStamp, -1, '', '', '') ;
	end if ;
	
	insert into channelInterchangeHistory (interchangeID, insertTimeStamp, sourceTimeStamp, sourceSystem, event, ackNak, text) values (pInterId, insertTimeStamp, pSourceTimestamp, pSourceSystem, pEvent, pAckNak, pText) ;
	
	# create and populate the temporary table
	
	create temporary table affectedTransactions
		select ct.instructionID, ct.transactionID from channelTransaction ct, channelInstruction ci
		where ci.interchangeID = pInterId and ci.instructionID = ct.instructionID
		union
		select ct.instructionID, ct.transactionID from channelTransaction ct, channelInstruction ci, pesTransIDmap pim
		where ci.interchangeID = pInterId and ci.instructionID = pim.pInstructionID
			and pim.cInstructionID = ct.instructionID and pim.cTransactionID = ct.transactionID ;
	
	# ripple the update down to the transactions
	
	if ( (pEvent != 'INTERIM') and (pEvent != 'FINAL') ) then	
		# do we need a response to this event?
	
		set respReqd = ' ' ; # force a value
		
		select sla1Period, sla2Period from responseProcessing rp where rp.SourceSystem = pSourceSystem and rp.event = pEvent ;
		
		if (pAckNak != 'NAK') then
			select responseRequired, 
				   DATE_ADD(CURRENT_TIMESTAMP,INTERVAL sla1Period SECOND),
				   DATE_ADD(CURRENT_TIMESTAMP,INTERVAL sla2Period SECOND) 
				into respReqd, sla2Due, sla2Due
				from ResponseProcessing rp where rp.sourceSystem = pSourceSystem and rp.event = pEvent ;
			if (  (respReqd = null) or (respReqd = '') ) then
				set respReqd = ' ' ;
			end if ;
		end if ;
		
		insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text, responseRequired, sla1Due, sla2Due)
			select atList.instructionID, atList.transactionID, insertTimestamp, pSourceTimestamp, pSourceSystem, pEvent, pAckNak, pText, respReqd, sla1Due, sla1Due
			from affectedTransactions atList ;
			
		update channelTransactionHistory cth, affectedTransactions atList, responseProcessing rp
			set cth.responseRequired = ' '
			where cth.instructionID = atList.instructionID and cth.transactionID = atList.transactionID
			and cth.sourceSystem = rp.previousSource and cth.event = rp.previousEvent and rp.SourceSystem = pSourceSystem and rp.event = pEvent ;
	end if ;
	
	drop table affectedTransactions ;
end ;

drop procedure InstructionInsert ;
# add row to hist for CREATE, add update
create procedure InstructionInsert (pInterID varchar(40), pInstrID varchar(40), cInstrID varchar(40), pSourceTimestamp timestamp, pSourceSystem varchar(10), pEvent varchar(10), numTransactions int)
BEGIN
	declare insertTimeStamp timestamp ;
	declare totalCount int ;
	
	# insert row in ID map
	if (cInstrID != '') then
		select count(*) from pesInstrIDmap pim where pim.pInstructionID = pInstrID into totalCount ;
		
		if (totalCount = 0) then
			insert into pesInstrIDmap (pInstructionID, cInstructionID) values (pInstrID, cInstrID) ;
		end if ;
	end if ;
	
	# insert or update the row in channelInstruction
	select count(*) from channelInstruction ci where ci.InterchangeID = pInterID and ci.InstructionID = pInstrID into totalCount ;
	if (totalCount = 0) then
		# persist the instruction
		insert into channelInstruction (sourceSystem, interchangeID, instructionID, sourceTimestamp, insertTimeStamp, numTransactions) values (pSourceSystem, pInterID, pInstrID, pSourceTimestamp, insertTimeStamp, numTransactions) ;
	end if ;
	
	# add a row to the history table
	insert into channelInstructionHistory (interchangeID, instructionID, insertTimeStamp, sourceTimestamp, sourceSystem, event, ackNak, text) values(pInterID, pInstrID, insertTimeStamp, pSourceTimestamp, pSourceSystem, pEvent, '', '') ;
END ;

drop procedure InstructionUpdate ;
create procedure InstructionUpdate (pInterchangeID varchar(40), pInstructionID varchar(40),  pSourceTimestamp timestamp, pSourceSystem varchar(10), pEvent varchar(10), pAckNak varchar(3), pText varchar(80))
BEGIN
	declare insertTimeStamp timestamp ;
	declare totalCount int ;
	declare nInstructionID varchar(40) ;
	
	set nInstructionID = pInstructionID ;
	
	select count(*) from channelInstruction where InterchangeID = pInterchangeID and InstructionID = nInstructionID into totalCount ;
	if (totalCount = 0) then
		# persist the instruction (it should already be persisted)
		insert into channelInstruction (interchangeID, instructionID, insertTimeStamp, numTransactions) values (pInterchangeID, nInstructionID, -1, insertTimeStamp) ;
	end if ;
	
	insert into channelInstructionHistory (interchangeID, instructionID, insertTimeStamp, sourceTimestamp, sourceSystem, event, ackNak, text) values(pInterchangeID, nInstructionID, insertTimeStamp, pSourceTimestamp, pSourceSystem, pEvent, pAckNak, pText) ;
END ;