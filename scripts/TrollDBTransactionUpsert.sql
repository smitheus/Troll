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
		declare vSla1Due timestamp ;
		declare vSla2Due timestamp ;
		declare vElapsedTime int ;
		declare vSla1End timestamp ;
		declare vSla2End timestamp ;
		declare vSla1Breach varchar(1) ;
		declare vSla2Breach varchar(1) ;
		
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
			
			select responseRequired, 
				   DATE_ADD(pSourceTimestamp,INTERVAL sla1Period SECOND),
				   DATE_ADD(pSourceTimestamp,INTERVAL sla2Period SECOND) 
			into respReqd, vSla1Due, vSla2Due
			from ResponseProcessing rp 
			where rp.sourceSystem = pSourceSystem 
			and rp.event = pEvent ;
		end if ;
		
		if ( (respReqd = null) or (respReqd = '') ) then
			set respReqd = ' ' ;
			set vSla1Due = null ;
    		set vSla2Due = null ;
		end if ;
	
		select count(*) from channelTransaction tr where tr.instructionID = nInstructionID and tr.transactionID = nTransactionID into totalCount ;
		
		IF (totalCount = 0) THEN
			# persist the transaction			
			insert channelTransaction (instructionID, transactionID, sourceTimestamp, insertTimestamp) values (nInstructionID, nTransactionID, pSourceTimestamp, insertTimeStamp) ;
		END IF ;
		
		select nInstructionID, nTransactionID, pesInstrID, pesTransID ;
		
		# get SLA due dates and breaches
		SELECT TIMESTAMPDIFF(SECOND, cthsla.sourceTimestamp, pSourceTimestamp),
			   cthsla.sla1Due, 
			   cthsla.sla2Due, 
			   CASE WHEN (cthsla.sla1Due < pSourceTimestamp) THEN 'Y' ELSE 'N' END,
			   CASE WHEN (cthsla.sla2Due < pSourceTimestamp) THEN 'Y' ELSE 'N' END
		INTO vElapsedTime, vSla1End, vSla2End, vSla1Breach, vSla2Breach
		FROM channelTransactionHistory cthsla, 
		     responseProcessing rp
		WHERE cthsla.sourceSystem = rp.previousSource 
		AND cthsla.event = rp.previousEvent 
		AND rp.SourceSystem = pSourceSystem 
		AND rp.event = pEvent 
		AND cthsla.instructionID = nInstructionID
		AND cthsla.transactionID = nTransactionID
		AND cthsla.pesInstructionID <=> pesInstrID
		AND cthsla.pesTransactionID <=> pesTransID ;
		
		# insert a row in the history for this transaction
		INSERT INTO channelTransactionHistory 
		(
			instructionID, transactionID, pesInstructionID, pesTransactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text, 
			responseRequired, sla1Due, sla2Due, elapsedTime, sla1End, sla2End, sla1Breach, sla2Breach
		) 
		VALUES 
		(
			nInstructionID, nTransactionID, CASE WHEN (pesInstrID = '') THEN null ELSE pesInstrID END, CASE WHEN (pesTransID = '') THEN null ELSE pesTransID END, insertTimeStamp, pSourceTimestamp, pSourceSystem, pEvent, pAckNak, pText, 
			respReqd, vSla1Due, vSla2Due, vElapsedTime, vSla1End, vSla2End, vSla1Breach, vSla2Breach
		) ;

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
			set cth.responseRequired = ' ', sla1Due = null, sla2Due = null
			where cth.instructionID = nInstructionID and cth.transactionID = nTransactionID
			and cth.sourceSystem = rp.previousSource and cth.event = rp.previousEvent and rp.SourceSystem = pSourceSystem and rp.event = pEvent ;

	END ;