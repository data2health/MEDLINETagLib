create table medline.article as select pmid,article_title,issn,volume,issue,pub_date_year,pub_date_month,pub_date_day,pub_date_season,pub_date_medline,start_page,end_page,medline_pgn,vernacular_title from medline19_staging.article;
create table medline.e_location_id as select * from medline19_staging.e_location_id ;
create table medline.abstract as select * from medline19_staging.abstract;
create table medline.author as select pmid,seqnum,equal_contrib,last_name,fore_name,initials,suffix,collective_name from author limit 1;
create table medline.author_identifier as select * from medline19_staging.author_identifier limit 1;
create table medline.author_affiliation as select * from medline19_staging.author_affiliation limit 1;
create table medline.language as select * from medline19_staging.language limit 1;
create table medline.data_bank as select pmid,seqnum,data_bank_name from medline19_staging.data_bank limit 1;
create table medline.accession_number as select * from medline19_staging.accession_number limit 1;
create table medline.grant_info as select * from medline19_staging.grant_info  limit 1;
create table medline.publication_type as select * from medline19_staging.publication_type limit 1;
create table medline.medline_journal_info as select * from medline19_staging.medline_journal_info limit 1;
create table medline.chemical as select * from medline19_staging.chemical limit 1;
create table medline.suppl_mesh_name as select * from medline19_staging.suppl_mesh_name limit 1;
create table medline.citation_subset as select * from medline19_staging.citation_subset limit 1;
create table medline.comments_corrections as select * from medline19_staging.comments_corrections limit 1;
create table medline.gene_symbol as select * from medline19_staging.gene_symbol limit 1;
create table medline.mesh_heading as select pmid,seqnum,major_topic,type,ui,descriptor_name from medline19_staging.mesh_heading limit 1;
create table medline.mesh_qualifier as select * from medline19_staging.mesh_qualifier limit 1;
create table medline.personal_name_subject as select * from medline19_staging.personal_name_subject limit 1;
create table medline.other_id as select * from medline19_staging.other_id limit 1;
create table medline.other_abstract as select * from medline19_staging.other_abstract limit 1;
create table medline.keyword as select * from medline19_staging.keyword limit 1;
create table medline.space_flight_mission as select * from medline19_staging.space_flight_mission limit 1;
create table medline.investigator as select pmid,seqnum,last_name,fore_name,initials,suffix from medline19_staging.investigator limit 1;
create table medline.investigator_identifier as select * from medline19_staging.investigator_identifier limit 1;
create table medline.investigator_affiliation as select * from medline19_staging.investigator_affiliation limit 1;
create table medline.general_note as select * from medline19_staging.general_note limit 1;
create table medline.history as select * from medline19_staging.history limit 1;
create table medline.article_id as select * from medline19_staging.article_id limit 1;
create table medline.object as select * from medline19_staging.object limit 1;
create table medline.reference as select pmid,seqnum,title,citation from medline19_staging.reference limit 1;
create table medline.reference_article_id as select * from medline19_staging.reference_article_id limit 1;