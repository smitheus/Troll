use troll ;

truncate table channelInterchange ;
truncate table channelInterchangeHistory ;
truncate table channelInstruction ;
truncate table channelInstructionHistory ;
truncate table channelTransaction ;
truncate table channelTransactionHistory ;
truncate table payexInterchange ;
truncate table payexInstruction ;
truncate table payexTransaction ;
truncate table coreBankingSystems ;

insert into coreBankingSystems values ("T24_KE", "T24", "KE") ;                # Kenya
insert into coreBankingSystems values ("T24_AN", "T24", "AO") ;              # Angola
insert into coreBankingSystems values ("FIN_GH", "FIN", "GH") ;              # Ghana
insert into coreBankingSystems values ("ZM_BM", "BM", "ZM") ;             # Zambia
insert into coreBankingSystems values ("FIN_BW", "FIN", "BW") ; # Botswana
insert into coreBankingSystems values ("BM_CD", "BM", "CD") ;               # DR Congo
insert into coreBankingSystems values ("BM_LS", "BM", "LS") ; # Lesotho
insert into coreBankingSystems values ("BM_MW", "BM", "MW") ;         # Malawi
insert into coreBankingSystems values ("BM_MU", "BM", "MU") ;           # Mauritius
insert into coreBankingSystems values ("T24_MZ", "T24", "MZ") ;             # Mozambique
insert into coreBankingSystems values ("FIN_NA", "FIN", "NA") ; # Namibia
insert into coreBankingSystems values ("BM_SZ", "BM", "SZ") ;                 # Swaziland
insert into coreBankingSystems values ("FIN_TZ","FIN","TZ") ;   # Tanzania
insert into coreBankingSystems values ("FIN_UG","FIN","UG") ;               # Uganda
insert into coreBankingSystems values ("MUB_ZW","MUB","ZW") ;        # Zimbabwe

insert into channelInterchange (interchangeID, insertTimeStamp, numInstructions, country, tempJmsHeaders) values ('inter_0001', '2014-01-01 00:00:01', 1, 'KE', 'jmsHeaders') ;

insert into channelInterchangeHistory (interchangeID, insertTimeStamp, sourceTimeStamp, sourceSystem, event) values('inter_0001', '2014-01-01 00:00:01', '2014-01-01 00:00:01', 'nBOL', 'SENT') ;
insert into channelInterchangeHistory (interchangeID, insertTimeStamp, sourceTimeStamp, sourceSystem, event) values('inter_0001', '2014-01-01 00:00:02', '2014-01-01 00:00:11', 'PAYEX', 'RECD') ;
insert into channelInterchangeHistory (interchangeID, insertTimeStamp, sourceTimeStamp, sourceSystem, event) values('inter_0001', '2014-01-01 00:00:02', '2014-01-01 00:00:12', 'PAYEX', 'SUBM') ;

insert into channelInstruction (interchangeID, instructionID, insertTimeStamp, numTransactions) values ('inter_0001', 'instr_0001', '2014-01-01 00:00:01', 2) ;

insert into channelInstructionHistory (interchangeID, instructionID, insertTimeStamp, sourceTimestamp, sourceSystem, event, ackNak, text) values('inter_0001', 'instr_0001', '2014-01-01 00:00:01', '2014-01-01 00:00:21', 'nBOL', 'SENT', '', '') ;
insert into channelInstructionHistory (interchangeID, instructionID, insertTimeStamp, sourceTimestamp, sourceSystem, event, ackNak, text) values('inter_0001', 'instr_0001', '2014-01-01 00:00:02', '2014-01-01 00:00:22', 'PAYEX', 'RECD', 'ACK', '') ;
insert into channelInstructionHistory (interchangeID, instructionID, insertTimeStamp, sourceTimestamp, sourceSystem, event, ackNak, text) values('inter_0001', 'instr_0001', '2014-01-01 00:00:02', '2014-01-01 00:00:23', 'PAYEX', 'SUBM', '', '') ;

insert into channelTransaction (instructionID, transactionID, insertTimestamp) values ('instr_0001', 'trans_0001', '2014-01-01 00:00:04', '2014-01-01 00:00:04') ;

# this transaction is suspect since there is a NAK from T24
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0001', 'trans_0001', '2014-01-01 00:00:04', '2014-01-01 00:00:04', 'nBOL', 'SENT', '', '') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0001', 'trans_0001', '2014-01-01 00:00:11', '2014-01-01 00:00:11', 'PAYEX', 'RECD', 'ACK', '') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0001', 'trans_0001', '2014-01-01 00:00:12', '2014-01-01 00:00:12', 'PAYEX', 'SUBM', '', '') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0002', 'trans_0002', '2014-01-01 00:00:32', '2014-01-01 00:00:32', 'PAYEX', 'MAX', 'ACK', '') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0001', 'trans_0001', '2014-01-01 00:00:13', '2014-01-01 00:00:13', 'PAYEX', 'T24_KE', 'NAK', 'Insufficient funds') ;

insert into channelInterchange (interchangeID, insertTimeStamp, numInstructions, country, tempJmsHeaders) values ('inter_0002', '2014-01-01 00:00:10', 1, 'ZA', '') ;

insert into channelInterchangeHistory (interchangeID, insertTimeStamp, sourceTimeStamp, sourceSystem, event) values('inter_0001', '2014-01-01 00:00:01', '2014-01-01 00:00:21', 'nBOL', 'SENT') ;
insert into channelInterchangeHistory (interchangeID, insertTimeStamp, sourceTimeStamp, sourceSystem, event) values('inter_0001', '2014-01-01 00:00:02', '2014-01-01 00:00:22', 'PAYEX', 'RECD') ;
insert into channelInterchangeHistory (interchangeID, insertTimeStamp, sourceTimeStamp, sourceSystem, event) values('inter_0001', '2014-01-01 00:00:02', '2014-01-01 00:00:23', 'PAYEX', 'SUBM') ;

insert into channelInstruction (interchangeID, instructionID, insertTimeStamp, numTransactions) values ('inter_0002', 'instr_0002', '2014-01-01 00:00:02', 5) ;

insert into channelInstructionHistory (interchangeID, instructionID, insertTimeStamp, sourceTimestamp, sourceSystem, event, ackNak, text) values('inter_0001', 'instr_0002', '2014-01-01 00:00:01', '2014-01-01 00:00:21', 'nBOL', 'SENT', '', '') ;

# this transaction is good since there is an ACK from T24
insert into channelTransaction (instructionID, transactionID, insertTimestamp) values ('instr_0002', 'trans_0002', '2014-01-01 00:00:24', '2014-01-01 00:00:24') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0002', 'trans_0002', '2014-01-01 00:00:24', '2014-01-01 00:00:24', 'nBOL', 'SENT', '', '') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0002', 'trans_0002', '2014-01-01 00:00:31', '2014-01-01 00:00:31', 'PAYEX', 'RECD', 'ACK', '') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0002', 'trans_0002', '2014-01-01 00:00:32', '2014-01-01 00:00:32', 'PAYEX', 'SUBM', '', '') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0002', 'trans_0002', '2014-01-01 00:00:32', '2014-01-01 00:00:32', 'PAYEX', 'MAX', 'ACK', '') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0002', 'trans_0002', '2014-01-01 00:00:33', '2014-01-01 00:00:33', 'PAYEX', 'T24_KE', 'ACK', 't24_reference') ;

# this transaction is suspect since there is a MAX ACK but nothing from T24 (yet)
insert into channelTransaction (instructionID, transactionID, insertTimestamp) values ('instr_0002', 'trans_0003', '2014-01-01 00:00:24', '2014-01-01 00:00:24') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0002', 'trans_0003', '2014-01-01 00:00:24', '2014-01-01 00:00:24', 'nBOL', 'SENT', '', '') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0002', 'trans_0003', '2014-01-01 00:00:31', '2014-01-01 00:00:31', 'PAYEX', 'RECD', 'ACK', '') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0002', 'trans_0003', '2014-01-01 00:00:32', '2014-01-01 00:00:32', 'PAYEX', 'SUBM', '', '') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0002', 'trans_0003', '2014-01-01 00:00:32', '2014-01-01 00:00:32', 'PAYEX', 'MAX', 'ACK', '') ;

# this transaction is suspect since there is a SUBM but no MAX ACK
insert into channelTransaction (instructionID, transactionID, insertTimestamp) values ('instr_0002', 'trans_0004', '2014-01-01 00:00:24', '2014-01-01 00:00:24') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0002', 'trans_0004', '2014-01-01 00:00:24', '2014-01-01 00:00:24', 'nBOL', 'SENT', '', '') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0002', 'trans_0004', '2014-01-01 00:00:31', '2014-01-01 00:00:31', 'PAYEX', 'RECD', 'ACK', '') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0002', 'trans_0004', '2014-01-01 00:00:32', '2014-01-01 00:00:32', 'PAYEX', 'SUBM', '', '') ;

# this transaction is suspect since there is a NAK from PAYEX
insert into channelTransaction (instructionID, transactionID, insertTimestamp) values ('instr_0002', 'trans_0005', '2014-01-01 00:00:24', '2014-01-01 00:00:24') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0002', 'trans_0005', '2014-01-01 00:00:24', '2014-01-01 00:00:24', 'nBOL', 'SENT', '', '') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0002', 'trans_0005', '2014-01-01 00:00:31', '2014-01-01 00:00:31', 'PAYEX', 'RECD', 'NAK', 'Payex no likee') ;

# this transaction is suspect since there is a SEND but no RECD
insert into channelTransaction (instructionID, transactionID, insertTimestamp) values ('instr_0002', 'trans_0006', '2014-01-01 00:00:24', '2014-01-01 00:00:24') ;
insert into channelTransactionHistory (instructionID, transactionID, insertTimestamp, sourceTimestamp, sourceSystem, event, ackNak, text) values ('instr_0002', 'trans_0006', '2014-01-01 00:00:24', '2014-01-01 00:00:24', 'nBOL', 'SENT', '', '') ;
	
insert into payexInterchange (pInterchangeID, nInterchangeID, insertTimestamp, sourceTimestamp, tempJmsHeaders) values ('pyInter_0001', 'inter_0001', '2014-01-01 00:01:32', '2014-01-01 00:01:32', 'jmsHeaders') ;
insert into payexInstruction (pInterchangeID, nInterchangeID, pInstructionID, nInstructionID, numTransactions, insertTimestamp, sourceTimestamp) values ('pyInter_0001', 'inter_0001', 'pyInstr_0001', 'instr_0001', 1, '2014-01-01 00:01:32', '2014-01-01 00:01:32') ;
insert into payexTransaction (pInstructionID, nInstructionID, pTransactionID, nTransactionID, insertTimestamp, sourceTimestamp) values ('pyInstr_0001', 'instr_0001', 'pyTrans_0001', 'trans_0001', '2014-01-01 00:01:32', '2014-01-01 00:01:32') ;

insert into payexInterchange (pInterchangeID, nInterchangeID, insertTimestamp, sourceTimestamp, tempJmsHeaders) values ('pyInter_0002', 'inter_0002', '2014-01-01 00:01:32', '2014-01-01 00:01:32', 'jmsHeaders') ;
insert into payexInstruction (pInterchangeID, nInterchangeID, pInstructionID, nInstructionID, numTransactions, insertTimestamp, sourceTimestamp) values ('pyInter_0002', 'inter_0002', 'pyInstr_0002', 'instr_0002', 5, '2014-01-01 00:01:32', '2014-01-01 00:01:32') ;

insert into payexTransaction (pInstructionID, nInstructionID, pTransactionID, nTransactionID, insertTimestamp, sourceTimestamp) values ('pyInstr_0002', 'instr_0002', 'pyTrans_0001', 'trans_0001', '2014-01-01 00:01:32', '2014-01-01 00:01:32') ;
insert into payexTransaction (pInstructionID, nInstructionID, pTransactionID, nTransactionID, insertTimestamp, sourceTimestamp) values ('pyInstr_0002', 'instr_0002', 'pyTrans_0002', 'trans_0002', '2014-01-01 00:01:32', '2014-01-01 00:01:32') ;
insert into payexTransaction (pInstructionID, nInstructionID, pTransactionID, nTransactionID, insertTimestamp, sourceTimestamp) values ('pyInstr_0002', 'instr_0002', 'pyTrans_0003', 'trans_0003', '2014-01-01 00:01:32', '2014-01-01 00:01:32') ;

