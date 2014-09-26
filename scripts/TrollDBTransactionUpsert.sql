drop procedure TransactionUpSert ;
CREATE PROCEDURE TransactionUpSert(chanInstrID varchar(40), chanTransID varchar(40), pesInstrID varchar(40), pesTransID varchar(40), pSourceTimestamp timestamp, pSourceSystem varchar(10), pEvent varchar(10), pAckNak varchar(3), pText varchar(80))
	BEGIN
		declare totalCount, sameEventCount int ;
		declare insertTimeStamp timestamp ;
		declare nInstructionID varchar(40) ;
		declare nTransactionID varchar(40) ;
		declare respReqd varchar(1) ;
		declare previousEvent varchar(10) ;
		declare previousSource varchar(10) ;
		
		# create a mapping table entry
		if (pesInstrID != '' and chanInstrId != '') then
			select count(*) from pesTransIDmap pim where pim.pInstructionID = pesInstrID and pim.pTransactionID = pesTransID into totalCount ;
			
			if (totalCount = 0) then
				insert into pesTransIDmap values (chanInstrID, chanTransID, pesInstrID, pesTransID) ;
			end if ;
		end if ;
		
		# map the pes references into channel references if necessary
		
		set nInstructionID = chanInstrID ;
		set nTransactionID = chanTransID ;
		
		if (chanTransID = '') then
			select pim.cInstructionID from pesTransIDmap pim where pim.pInstructionID = pesInstrID and pim.pTransactionID = pesTransID into nInstructionID ;
			select pim.cTransactionID from pesTransIDmap pim where pim.pInstructionID = pesInstrID and pim.pTransactionID = pesTransID into nTransactionID ;
		end if ;
		
		# decide if a response is required
		
		set respReqd = ' ' ;
		if (pAckNak != 'NAK') then
			select rp.responseRequired from ResponseProcessing rp where rp.sourceSystem = pSourceSystem and rp.event = pEvent into respReqd ;
			
			if ( (respReqd = null) or (respReqd = '') ) then
				set respReqd = ' ' ;
			end if ;
		end if ;
	
	
		select count(*) from channelTransaction tr where tr.instructionID = nInstructionID and tr.transactionID = nTransactionID into totalCount ;
		
		IF (totalCount = 0) THEN
			# persist the transaction			
			insert channelTransaction (instructionID, transactionID, insertTimestamp) values (nInstructionID, nTransactionID, insertTimeStamp) ;
		END IF ;
		
		# insert a row in the history for this transaction
		insert into channelTransactionHistory (instructionID, transactionID, pesInstructionID, pesTransactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text, responseRequired) values (nInstructionID, nTransactionID, pesInstrID, pesTransID, insertTimeStamp, pSourceTimestamp, pSourceSystem, pEvent, pAckNak, pText, respReqd) ;

		# count how many transactions there are and how many in the same state
		select count(*) from channelTransaction tr
			where tr.instructionID = nInstructionID
			into totalCount ;
		select count(*) from (
			select distinct transactionID from channelTransactionHistory th
				where th.instructionID = nInstructionID and th.sourceSystem = pSourceSystem and th.event = pEvent) tmp1
			into sameEventCount ;
		# is this required now?
		#IF (sameEventCount = totalCount) THEN
			# time to update the instruction status
			#insert into channelInstructionHistory (interchangeID, instructionID, insertTimeStamp, sourceTimestamp, sourceSystem, event) values ('', nInstructionID, insertTimeStamp, pSourceTimestamp, pSourceSystem, pEvent) ;
		#END IF ;
		
		# clear down any previous responseRequired flag
		update channelTransactionHistory cth, responseProcessing rp
			set cth.responseRequired = ' '
			where cth.instructionID = nInstructionID and cth.transactionID = nTransactionID
			and cth.sourceSystem = rp.previousSource and cth.event = rp.previousEvent and rp.SourceSystem = pSourceSystem and rp.event = pEvent ;

	END ;