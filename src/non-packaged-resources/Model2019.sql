CREATE TABLE medline.article (
       pmid INT NOT NULL
     , issn TEXT
     , volume TEXT
     , issue TEXT
     , pub_date_year TEXT
     , pub_date_month TEXT
     , pub_date_day TEXT
     , pub_date_season TEXT
     , pub_date_medline TEXT
     , start_page TEXT
     , end_page TEXT
     , medline_pgn TEXT
     , vernacular_title TEXT
     , PRIMARY KEY (pmid)
);

CREATE TABLE medline.data_bank (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , data_bank_name TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_data_bank_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.investigator (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , last_name TEXT
     , fore_name TEXT
     , initials TEXT
     , suffix TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_investigator_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.mesh_heading (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , major_topic TEXT
     , type TEXT
     , ui TEXT
     , descriptor_name TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_mesh_heading_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.reference (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , title TEXT
     , citation TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_reference_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.author (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , equal_contrib TEXT
     , last_name TEXT
     , fore_name TEXT
     , initials TEXT
     , suffix TEXT
     , collective_name TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_author_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.accession_number (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , seqnum2 INT NOT NULL
     , affiliation TEXT
     , PRIMARY KEY (pmid, seqnum, seqnum2)
     , CONSTRAINT FK_accession_number_1 FOREIGN KEY (pmid, seqnum)
                  REFERENCES medline.data_bank (pmid, seqnum) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.article_id (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , id_type TEXT
     , article_id TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_article_id_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.author_affiliation (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , seqnum2 INT NOT NULL
     , affiliation TEXT
     , identifier TEXT
     , PRIMARY KEY (pmid, seqnum, seqnum2)
     , CONSTRAINT FK_author_affiliation_1 FOREIGN KEY (pmid, seqnum)
                  REFERENCES medline.author (pmid, seqnum) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.author_identifier (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , seqnum2 INT NOT NULL
     , source TEXT
     , identifier TEXT
     , PRIMARY KEY (pmid, seqnum, seqnum2)
     , CONSTRAINT FK_author_identifier_1 FOREIGN KEY (pmid, seqnum)
                  REFERENCES medline.author (pmid, seqnum) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.chemical (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , registry_number TEXT
     , substance_ui TEXT
     , name_of_substance TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_chemical_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.citation_subset (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , citation_subset TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_citation_subset_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.comments_corrections (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , ref_type TEXT
     , ref_source TEXT
     , ref_pmid TEXT
     , note TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_comments_corrections_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.e_location_id (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , e_id_type TEXT
     , e_location_id TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_e_location_id_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.gene_symbol (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , gene_symbol TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_gene_symbol_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.general_note (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , owner TEXT
     , general_note TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_general_note_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.grant_info (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , grant_id TEXT
     , acronym TEXT
     , agency TEXT
     , country TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_grant_info_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.history (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , pub_status TEXT
     , year TEXT
     , month TEXT
     , day TEXT
     , hour TEXT
     , minute TEXT
     , second TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_history_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.investigator_affiliation (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , seqnum2 INT NOT NULL
     , affiliation TEXT
     , identifier TEXT
     , PRIMARY KEY (pmid, seqnum, seqnum2)
     , CONSTRAINT FK_investigator_affiliation_1 FOREIGN KEY (pmid, seqnum)
                  REFERENCES medline.investigator (pmid, seqnum) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.investigator_identifier (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , seqnum2 INT NOT NULL
     , source TEXT
     , identifier TEXT
     , PRIMARY KEY (pmid, seqnum, seqnum2)
     , CONSTRAINT FK_investigator_identifier_1 FOREIGN KEY (pmid, seqnum)
                  REFERENCES medline.investigator (pmid, seqnum) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.keyword (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , major_topic TEXT
     , owner TEXT
     , keyword TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_keyword_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.language (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , language TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_language_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.medline_journal_info (
       pmid INT NOT NULL
     , country TEXT
     , medline_ta TEXT
     , nlm_unique_id TEXT
     , issn_linking TEXT
     , PRIMARY KEY (pmid)
     , CONSTRAINT FK_medline_journal_info_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.mesh_qualifier (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , seqnum2 INT NOT NULL
     , major_topic TEXT
     , ui TEXT
     , qualifier_name TEXT
     , PRIMARY KEY (pmid, seqnum, seqnum2)
     , CONSTRAINT FK_mesh_qualifier_1 FOREIGN KEY (pmid, seqnum)
                  REFERENCES medline.mesh_heading (pmid, seqnum) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.object (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , type TEXT
     , object TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_object_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.other_abstract (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , type TEXT
     , language TEXT
     , abstract TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_other_abstract_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.other_id (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , source TEXT
     , other_id TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_other_id_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.personal_name_subject (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , last_name TEXT
     , forename_name TEXT
     , initials TEXT
     , suffix TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_personal_name_subject_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.publication_type (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , ui TEXT
     , publication_type TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_publication_type_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.reference_article_id (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , seqnum2 INT NOT NULL
     , id_type TEXT
     , article_id TEXT
     , PRIMARY KEY (pmid, seqnum, seqnum2)
     , CONSTRAINT FK_reference_article_id_1 FOREIGN KEY (pmid, seqnum)
                  REFERENCES medline.reference (pmid, seqnum) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.space_flight_mission (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , space_flight_mission TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_space_flight_mission_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.suppl_mesh_name (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , type TEXT
     , ui TEXT
     , suppl_mesh_name TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_suppl_mesh_name_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.article_title (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , article_title TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_article_title_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline.abstract (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , label TEXT
     , nlm_category TEXT
     , abstract TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_abstract_1 FOREIGN KEY (pmid)
                  REFERENCES medline.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
);

