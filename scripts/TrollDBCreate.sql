use troll ;

drop table coreBankingSystems ;
use troll ;

drop table coreBankingSystems ;
create table coreBankingSystems (
	systemCode varchar(10),
	systemType varchar(40),
	country varchar(10) ) ;

drop table responseProcessing ;
create table responseProcessing (
	sourceSystem varchar(40),
	event varchar(10),
	responseRequired varchar(1),
	previousSource varchar(40),
	previousEvent varchar(10) ) ;
	
drop table channelInterchange ;
create table channelInterchange (
	id MEDIUMINT NOT NULL AUTO_INCREMENT,
	interchangeID  varchar(40),
	insertTimestamp timestamp,
	sourceTimestamp timestamp,
	sourceSystem varchar(40),
	numInstructions int, 
	country varchar(10),
	tempJmsHeaders varchar(80),
	PRIMARY KEY (id) ) ;

drop table channelInterchangeHistory ;
create table channelInterchangeHistory (
	id MEDIUMINT NOT NULL AUTO_INCREMENT,
	interchangeID  varchar(40),
	insertTimestamp timestamp,
	sourceTimestamp timestamp,
	sourceSystem varchar(40),
	event varchar(10),
	ackNak varchar(3),
	text varchar(80),
	PRIMARY KEY (id) ) ;
	
drop table channelInstruction ;
create table channelInstruction (
	id MEDIUMINT NOT NULL AUTO_INCREMENT,
	interchangeID  varchar(40),
	instructionID  varchar(40),
	insertTimestamp timestamp,
	sourceTimestamp timestamp,
	sourceSystem varchar(40),
	numTransactions int,
	PRIMARY KEY (id) ) ;

drop table channelInstructionHistory ;
create table channelInstructionHistory (
	id MEDIUMINT NOT NULL AUTO_INCREMENT,
	interchangeID  varchar(40),
	instructionID  varchar(40),
	insertTimestamp timestamp,
	sourceTimestamp timestamp,
	sourceSystem varchar(40),
	event varchar(10),
	ackNak varchar(3),
	text varchar(80),
	PRIMARY KEY (id) ) ;

drop table channelTransaction ;
create table channelTransaction (
	id MEDIUMINT NOT NULL AUTO_INCREMENT,
	instructionID  varchar(40),
	transactionId varchar(40),
	insertTimestamp timestamp,
	sourceTimestamp timestamp,
	PRIMARY KEY (id) ) ;

drop table channelTransactionHistory ;
create table channelTransactionHistory (
	id MEDIUMINT NOT NULL AUTO_INCREMENT,
	instructionID varchar(40),
	transactionId varchar(40),
	pesInstructionID varchar(40),
	pesTransactionID varchar(40),
	insertTimestamp timestamp,
	sourceTimestamp timestamp,
	sourceSystem varchar(40),
	event varchar(10),
	ackNak varchar(3),
	text varchar(80),
	responseRequired varchar (1),
	PRIMARY KEY (id) ) ;

drop table problemRecords ;
create table problemRecords (
	id MEDIUMINT NOT NULL AUTO_INCREMENT,
	interchangeID  varchar(40),
	instructionID  varchar(40),
	transactionId varchar(40),
	insertTimestamp timestamp,
	sourceTimestamp timestamp,
	sourceSystem varchar(40),
	record varchar (160),
	PRIMARY KEY (id) ) ;

drop table pesInstrIDmap ;
create table pesInstrIDmap (
	cInstructionID varchar(40),
	pInstructionID  varchar(40) ) ;

drop table pesTransIDmap ;
create table pesTransIDmap (
	cInstructionID varchar(40),
	cTransactionID varchar(40),
	pInstructionID  varchar(40),
	pTransactionId varchar(40) ) ;
	
drop procedure InterchangeInsert ;
# add row to hist for CREATE, add update
create procedure InterchangeInsert (pSourceSystem varchar(10), pInterchangeID varchar(40), pNumInstructions int, pCountry varchar(10), pJmsProperties varchar(80))
begin
                declare insertTimeStamp timestamp ;
                declare totalCount int ;
                
                select count(*) from channelInterchange where interchangeID = pInterchangeID into totalCount ;
                if (totalCount = 0) then
                                insert into channelInterchange (sourceSystem, interchangeID, insertTimeStamp, numInstructions, country, tempJmsHeaders) values (pSourceSystem, pInterchangeID, insertTimeStamp, pNumInstructions, pCountry, pJmsProperties) ;
                end if ;
end ;
