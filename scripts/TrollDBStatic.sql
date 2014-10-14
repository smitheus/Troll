truncate table coreBankingSystems ;

insert into coreBankingSystems values ("T24_KE", "T24", "KE") ; # Kenya
insert into coreBankingSystems values ("T24_AN", "T24", "AO") ; # Angola
insert into coreBankingSystems values ("FIN_GH", "FIN", "GH") ; # Ghana
insert into coreBankingSystems values ("ZM_BM",  "BM", "ZM") ; # Zambia
insert into coreBankingSystems values ("FIN_BW", "FIN", "BW") ; # Botswana
insert into coreBankingSystems values ("BM_CD", "BM", "CD") ; # DR Congo
insert into coreBankingSystems values ("BM_LS", "BM", "LS") ; # Lesotho
insert into coreBankingSystems values ("BM_MW", "BM", "MW") ; # Malawi
insert into coreBankingSystems values ("BM_MU", "BM", "MU") ; # Mauritius
insert into coreBankingSystems values ("T24_MZ", "T24", "MZ") ; # Mozambique
insert into coreBankingSystems values ("FIN_NA", "FIN", "NA") ; # Namibia
insert into coreBankingSystems values ("BM_SZ", "BM", "SZ") ; # Swaziland
insert into coreBankingSystems values ("FIN_TZ","FIN","TZ") ; # Tanzania
insert into coreBankingSystems values ("FIN_UG","FIN","UG") ; # Uganda
insert into coreBankingSystems values ("MUB_ZW","MUB","ZW") ; # Zimbabwe

truncate table responseProcessing ;
insert into responseProcessing values ('NBOL->PAYEX', 'NBOL', 'SENT', 'Y', 100, 1000, '', '') ;
insert into responseProcessing values ('PAYEX', 'PAYEX', 'RECD', ' ', 0, 0, 'NBOL', 'SENT') ;
insert into responseProcessing values ('PAYEX->MAX', 'PAYEX', 'SENT', 'Y', 100, 1000, '', '') ;
insert into responseProcessing values ('MAX->T24', 'MAX', 'EV2', 'Y', 100, 1000, 'PAYEX', 'SENT') ;
insert into responseProcessing values ('T24', 'PAYEX', 'CORE', ' ', 0, 0, 'MAX', 'EV2') ;

truncate table trollerStatus ;
insert into trollerStatus values ("nBOL", 1, 0, 'R', 'Down', '2000-01-01 00:00:00', 30) ;
insert into trollerStatus values ("nBOL", 2, 0, 'R', 'Down', '2000-01-01 00:00:00', 30) ;
insert into trollerStatus values ("nBOL", 3, 0, 'R', 'Down', '2000-01-01 00:00:00', 30) ;
insert into trollerStatus values ("nBOL", 4, 0, 'R', 'Down', '2000-01-01 00:00:00', 30) ;
insert into trollerStatus values ("PAYEX", 1, 0, 'R', 'Down', '2000-01-01 00:00:00', 30) ;
insert into trollerStatus values ("PAYEX", 2, 0, 'R', 'Down', '2000-01-01 00:00:00', 30) ;
insert into trollerStatus values ("MAX", 1, 0, 'R', 'Down', '2000-01-01 00:00:00', 30) ;
insert into trollerStatus values ("MAX", 2, 0, 'R', 'Down', '2000-01-01 00:00:00', 30) ;
 
truncate table trollServers ;
insert into trollServers values (1, 'R', 'Down', '2000-01-01 00:00:00', 30) ;
insert into trollServers values (2, 'R', 'Down', '2000-01-01 00:00:00', 30) ;
