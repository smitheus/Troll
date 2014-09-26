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
insert into responseProcessing values ('NBOL', 'SENT', 'Y', '', '') ;
insert into responseProcessing values ('PAYEX', 'RECD', ' ', 'NBOL', 'SENT') ;
insert into responseProcessing values ('PAYEX', 'SENT', 'Y', '', '') ;
insert into responseProcessing values ('PAYEX', 'MAX', 'Y', 'PAYEX', 'SENT') ;
insert into responseProcessing values ('PAYEX', 'CORE', ' ', 'PAYEX', 'MAX') ;
