CREATE TABLE medline15.author_count (
       last_name TEXT NOT NULL
     , fore_name TEXT NOT NULL
     , count INT
     , PRIMARY KEY (last_name, fore_name)
);

CREATE TABLE medline_clustering.document_cluster (
       cid INT NOT NULL
     , last_name TEXT
     , fore_name TEXT
     , PRIMARY KEY (cid)
);

CREATE TABLE medline15.article (
       pmid INT NOT NULL
     , date_created DATE
     , date_completed DATE
     , date_revised DATE
     , title TEXT
     , start_page INT
     , end_page INT
     , medline_pgn TEXT
     , copyright TEXT
     , vernacular_title TEXT
     , country TEXT
     , ta TEXT
     , nlm_unique_id TEXT
     , issn_linking TEXT
     , reference_count INT
     , pub_model TEXT
     , status TEXT
     , PRIMARY KEY (pmid)
);

CREATE TABLE medline15.data_bank (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , name TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_data_bank_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.mesh_heading (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , descriptor_name TEXT
     , major BOOLEAN
     , type TEXT
     , id TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_mesh_heading_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.other_abstract (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , type TEXT
     , copyright TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_other_abstract_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.investigator (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , last_name TEXT
     , fore_name TEXT
     , initials TEXT
     , suffix TEXT
     , affiliation TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_investigator_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.author (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , last_name TEXT
     , fore_name TEXT
     , initials TEXT
     , suffix TEXT
     , collective_name TEXT
     , affiliation TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_author_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.elocation (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , eid TEXT
     , type TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_elocation_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.language (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , language TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_language_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.accession (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , accnum INT NOT NULL
     , accession TEXT
     , PRIMARY KEY (pmid, seqnum, accnum)
     , CONSTRAINT FK_accession_1 FOREIGN KEY (pmid, seqnum)
                  REFERENCES medline15.data_bank (pmid, seqnum) ON DELETE CASCADE
);

CREATE TABLE medline15.grant (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , gid TEXT
     , acronym TEXT
     , agency TEXT
     , country TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_grant_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.article_date (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , year INT
     , month INT
     , day INT
     , type TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_article_date_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.chemical (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , registry_number TEXT
     , substance_name TEXT
     , id TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_chemical_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.citation_subset (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , label TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_citation_subset_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.comments_corrections (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , ref_type TEXT
     , source TEXT
     , ref_pmid INT
     , note TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_comment_on_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.gene_symbol (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , symbol TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_gene_symbol_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.mesh_qualifier (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , qnum INT NOT NULL
     , qualifier_name TEXT
     , major BOOLEAN
     , id TEXT
     , PRIMARY KEY (pmid, seqnum, qnum)
     , CONSTRAINT FK_mesh_qualifier_1 FOREIGN KEY (pmid, seqnum)
                  REFERENCES medline15.mesh_heading (pmid, seqnum) ON DELETE CASCADE
);

CREATE TABLE medline15.personal_name_subject (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , last_name TEXT
     , fore_name TEXT
     , initials TEXT
     , suffix TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_personal_name_subject_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.other_id (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , source TEXT
     , other_id TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_other_id_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.keyword (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , keyword TEXT
     , major BOOLEAN
     , owner TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_keyword_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.spaceflight_mission (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , mission TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_spaceflight_mission_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.general_note (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , note TEXT
     , owner TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_general_note_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.publication_type (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , label TEXT
     , id TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_publication_type_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.supplemental_mesh (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , name TEXT
     , type TEXT
     , id TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_supplemental_mesh_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.name_id (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , nnum INT NOT NULL
     , name_id TEXT
     , source TEXT
     , PRIMARY KEY (pmid, seqnum, nnum)
     , CONSTRAINT FK_name_id_1 FOREIGN KEY (pmid, seqnum)
                  REFERENCES medline15.author (pmid, seqnum) ON DELETE CASCADE
);

CREATE TABLE medline15.abstr (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , abstract_text TEXT
     , label TEXT
     , category TEXT
     , PRIMARY KEY (pmid, seqnum)
     , CONSTRAINT FK_abstract_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

CREATE TABLE medline15.other_abstract_text (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , tnum INT NOT NULL
     , abstract_text TEXT
     , label TEXT
     , category TEXT
     , PRIMARY KEY (pmid, seqnum, tnum)
     , CONSTRAINT FK_other_abstract_text_1 FOREIGN KEY (pmid, seqnum)
                  REFERENCES medline15.other_abstract (pmid, seqnum) ON DELETE CASCADE
);

CREATE TABLE medline15.investigator_name_id (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , nnum INT NOT NULL
     , name_id TEXT
     , source TEXT
     , PRIMARY KEY (pmid, seqnum, nnum)
     , CONSTRAINT FK_investigator_name_id_1 FOREIGN KEY (pmid, seqnum)
                  REFERENCES medline15.investigator (pmid, seqnum)
);

CREATE TABLE medline15.affiliation (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , anum INT NOT NULL
     , label TEXT
     , source TEXT
     , identifier TEXT
     , PRIMARY KEY (pmid, seqnum, anum)
     , CONSTRAINT FK_affiliation_1 FOREIGN KEY (pmid, seqnum)
                  REFERENCES medline15.author (pmid, seqnum) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline15.investigator_affiliation (
       pmid INT NOT NULL
     , seqnum INT NOT NULL
     , anum INT NOT NULL
     , label TEXT
     , source TEXT
     , identifier TEXT
     , PRIMARY KEY (pmid, seqnum, anum)
     , CONSTRAINT FK_investigator_affiliation_1 FOREIGN KEY (pmid, seqnum)
                  REFERENCES medline15.investigator (pmid, seqnum) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline_clustering.cluster_document (
       cid INT NOT NULL
     , pmid INT NOT NULL
     , PRIMARY KEY (cid, pmid)
     , CONSTRAINT FK_cluster_document_2 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE ON UPDATE CASCADE
     , CONSTRAINT FK_cluster_document_1 FOREIGN KEY (cid)
                  REFERENCES medline_clustering.document_cluster (cid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline_clustering.cluster_author (
       cid INT NOT NULL
     , seqnum INT NOT NULL
     , last_name TEXT
     , fore_name TEXT
     , occurrences INT
     , PRIMARY KEY (cid, seqnum)
     , CONSTRAINT FK_cluster_author_1 FOREIGN KEY (cid)
                  REFERENCES medline_clustering.document_cluster (cid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline_clustering.cluster_pattern (
       cid INT NOT NULL
     , seqnum INT NOT NULL
     , last_name TEXT
     , fore_name TEXT
     , occurrences INT
     , PRIMARY KEY (cid, seqnum)
     , CONSTRAINT FK_cluster_pattern_1 FOREIGN KEY (cid)
                  REFERENCES medline_clustering.document_cluster (cid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medline15.journal (
       pmid INT NOT NULL
     , issn TEXT
     , volume TEXT
     , issue TEXT
     , pub_year INT
     , pub_month TEXT
     , pub_day INT
     , pub_season TEXT
     , medline_date TEXT
     , title TEXT
     , iso_abbreviation TEXT
     , PRIMARY KEY (pmid)
     , CONSTRAINT FK_journal_1 FOREIGN KEY (pmid)
                  REFERENCES medline15.article (pmid) ON DELETE CASCADE
);

