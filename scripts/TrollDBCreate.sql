use troll ;

drop table coreBankingSystems ;
create table coreBankingSystems (
	systemCode varchar(10),
	systemType varchar(40),
	country varchar(10) ) ;

drop table responseProcessing ;
create table responseProcessing (
	closureName varchar(30),
	sourceSystem varchar(40),
	event varchar(10),
	closureRequired varchar(1),
	sla1Period int,
	sla2Period int,
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
	messageType varchar(20),
	instrumentGroup varchar(20),
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
	underInvestigation int,
	comments varchar(1000),
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
	sla1Due timestamp null,
	sla2Due timestamp null,
	elapsedTime int,	
	sla1End timestamp null,
	sla2End timestamp null,
	sla1Breach varchar (1),
	sla2Breach varchar (1),
	PRIMARY KEY (id) ) ;

drop table problemRecord ;
create table problemRecord (
	id MEDIUMINT NOT NULL AUTO_INCREMENT,
	interchangeID  varchar(40),
	instructionID  varchar(40),
	transactionId varchar(40),
	insertTimestamp timestamp,
	sourceTimestamp timestamp,
	sourceSystem varchar(40),
	record varchar (160),
	errorMessage varchar(160),
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