

create view article as
select pmid,xmltable.* from 
xml,
xmltable(
    '//Article'
    passing raw
    columns
        issn text path 'Journal/ISSN/text()',
        volume text path 'Journal/JournalIssue/Volume/text()',
        issue text path 'Journal/JournalIssue/Issue/text()',
        pub_date_year text path 'Journal/JournalIssue/PubDate/Year/text()',
        pub_date_month text path 'Journal/JournalIssue/PubDate/Month/text()',
        pub_date_day text path 'Journal/JournalIssue/PubDate/Day/text()',
        pub_date_season text path 'Journal/JournalIssue/PubDate/Season/text()',
        pub_date_medline text path 'Journal/JournalIssue/PubDate/MedlineDate/text()',
        start_page text path 'Pagination/StartPage/text()',
        end_page text path 'Pagination/EndPage/text()',
        medline_pgn text path 'Pagination/MedlinePgn/text()',
        author_list xml path 'AuthorList'
    );

create view article_title as
select pmid,xmltable.* from
xml,
xmltable(
    '//Article/ArticleTitle'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        article_title text path '.'
    );

create view vernacular_title as
select pmid,xmltable.* from
xml,
xmltable(
    '//Article/VernacularTitle'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        vernacular_title text path '.'
    );

create view e_location_id as
select pmid,xmltable.* from 
xml,
xmltable(
    '//Article/ELocationID'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        e_id_type text PATH '@EIdType',
        e_location_id text path '.'
    );

create view abstract as
select pmid,xmltable.* from 
xml,
xmltable(
    '//Article/Abstract/AbstractText'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        label text PATH '@Label',
        nlm_category text PATH '@NlmCategory',
        abstract text path '.'
    );

create view author as
select pmid,xmltable.* from 
xml,
xmltable(
    '//Article/AuthorList/Author'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        equal_contrib text PATH '@EqualContrib',
        last_name text path 'LastName/text()',
        fore_name text path 'ForeName/text()',
        initials text path 'Initials/text()',
        suffix text path 'Suffix/text()',
        collective_name text path 'CollectiveName/text()',
        identifiers xml path '.', -- need to pass in the current node to have a well-formed qualifier list
        affiliation_info xml path '.' -- need to pass in the current node to have a well-formed qualifier list
    );

create view author_identifier as
select pmid,seqnum,xmltable.* from 
author,
xmltable(
    '//Author/Identifier'
    passing identifiers
    columns
        seqnum2 FOR ORDINALITY,
        source text PATH '@Source',
        identifier text path '.'
    );

create view author_affiliation as
select pmid,seqnum,xmltable.* from 
author,
xmltable(
    '//Author/AffiliationInfo/Affiliation'
    passing affiliation_info
    columns
        seqnum2 FOR ORDINALITY,
        affiliation text path '.',
        identifier xml path '..' -- need to pass in the current node to have a well-formed qualifier list
    );

create view language as
select pmid,xmltable.* from 
xml,
xmltable(
    '//Article/Language'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        language text path '.'
    );

create view data_bank as
select pmid,xmltable.* from 
xml,
xmltable(
    '//Article/DataBankList/DataBank'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        data_bank_name text path 'DataBankName/text()',
        accession_number_List xml path 'AccessionNumberList'
    );

create view accession_number as
select pmid,seqnum,xmltable.* from 
data_bank,
xmltable(
    '//AccessionNumberList/AccessionNumber'
    passing accession_number_List
    columns
        seqnum2 FOR ORDINALITY,
        affiliation text path '.'
    );

create view grant_info as
select pmid,xmltable.* from 
xml,
xmltable(
    '//Article/GrantList/Grant'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        grant_id text path 'GrantID/text()',
        acronym text path 'Acronym/text()',
        agency text path 'Agency/text()',
        country text path 'Country/text()'
    );

create view publication_type as
select pmid,xmltable.* from 
xml,
xmltable(
    '//Article/PublicationTypeList/PublicationType'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        ui text PATH '@UI',
        publication_type text path '.'
    );

create view medline_journal_info as
select pmid,xmltable.* from 
xml,
xmltable(
    '//MedlineJournalInfo'
    passing raw
    columns
        country text path 'Country/text()',
        medline_ta text path 'MedlineTA/text()',
        nlm_unique_id text path 'NlmUniqueID/text()',
        issn_linking text path 'ISSNLinking/text()'
    );

create view chemical as
select pmid,xmltable.* from 
xml,
xmltable(
    '//ChemicalList/Chemical'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        registry_number text path 'RegistryNumber/text()',
        substance_ui text PATH 'NameOfSubstance/@UI',
        name_of_substance text path 'NameOfSubstance/text()'
    );

create view suppl_mesh_name as
select pmid,xmltable.* from 
xml,
xmltable(
    '//SupplMeshList/SupplMeshName'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        type text PATH '@Type',
        ui text PATH '@UI',
        suppl_mesh_name text path '.'
    );

create view citation_subset as
select pmid,xmltable.* from 
xml,
xmltable(
    '//CitationSubset'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        citation_subset text path '.'
    );

create view comments_corrections as
select pmid,xmltable.* from 
xml,
xmltable(
    '//CommentsCorrectionsList/CommentsCorrections'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        ref_type text PATH '@RefType',
        ref_source text path 'RefSource/text()',    
        ref_pmid text path 'PMID/text()',
        note text path 'Note/text()'
    );

create view gene_symbol as
select pmid,xmltable.* from 
xml,
xmltable(
    '//GeneSymbolList/GeneSymbol'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        gene_symbol text path '.'
    );

create view mesh_heading as
select pmid,xmltable.* from 
xml,
xmltable(
    '//MeshHeadingList/MeshHeading'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        major_topic text PATH 'DescriptorName/@MajorTopicYN',
        type text PATH 'DescriptorName/@Type',
        ui text PATH 'DescriptorName/@UI',
        descriptor_name text path 'DescriptorName/text()',    
        qualifier_name xml path '.' -- need to pass in the current node to have a well-formed qualifier list
    );

create view mesh_qualifier as
select pmid,seqnum,xmltable.* from 
mesh_heading,
xmltable(
    '//MeshHeading/QualifierName'
    passing qualifier_name
    columns
        seqnum2 FOR ORDINALITY,
        major_topic text PATH '@MajorTopicYN',
        ui text PATH '@UI',
        qualifier_name text path '.'
    );

-- todo NumberOfReferences, CoiStatement, PublicationStatus


create view personal_name_subject as
select pmid,xmltable.* from 
xml,
xmltable(
    '//PersonalNameSubjectList/PersonalNameSubject'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        last_name text path 'LastName/text()',
        fore_name text path 'ForeName/text()',
        initials text path 'Initials/text()',
        suffix text path 'Suffix/text()'
    );

create view other_id as
select pmid,xmltable.* from 
xml,
xmltable(
    '//OtherID'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        source text PATH '@Source',
        other_id text path '.'
    );

create view other_abstract as
select pmid,xmltable.* from 
xml,
xmltable(
    '//OtherAbstract/AbstractText'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        type text PATH '@Type',
        language text PATH '@Language',
        abstract text path '.'
    );

create view keyword as
select pmid,xmltable.* from 
xml,
xmltable(
    '//KeywordList/Keyword'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        major_topic text PATH '@MajorTopicYN',
        owner text PATH '../@Owner',
        keyword text path '.'
    );

create view space_flight_mission as
select pmid,xmltable.* from 
xml,
xmltable(
    '//SpaceFlightMission'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        space_flight_mission text path '.'
    );

create view investigator as
select pmid,xmltable.* from 
xml,
xmltable(
    '//InvestigatorList/Investigator'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        last_name text path 'LastName/text()',
        fore_name text path 'ForeName/text()',
        initials text path 'Initials/text()',
        suffix text path 'Suffix/text()',
        identifiers xml path 'Identifier',
        affiliation_info xml path '.' -- need to pass in the current node to have a well-formed affiliation list
    );

create view investigator_identifier as
select pmid,seqnum,xmltable.* from 
investigator,
xmltable(
    '//Identifier'
    passing identifiers
    columns
        seqnum2 FOR ORDINALITY,
        source text PATH '@Source',
        identifier text path '.'
    );

create view investigator_affiliation as
select pmid,seqnum,xmltable.* from 
investigator,
xmltable(
    '//Investigator/AffiliationInfo'
    passing affiliation_info
    columns
        seqnum2 FOR ORDINALITY,
        affiliation text path 'Affiliation/text()',
        identifier xml path 'Identifier'
    );

create view general_note as
select pmid,xmltable.* from 
xml,
xmltable(
    '//GeneralNote'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        owner text PATH '@Owner',
        general_note text path '.'
    );

create view history as
select pmid,xmltable.* from 
xml,
xmltable(
    '//PubmedData/History/PubMedPubDate'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        pub_status text PATH '@PubStatus',
        year text path 'Year/text()',
        month text path 'Month/text()',
        day text path 'Day/text()',
        hour text path 'Hour/text()',
        minute text path 'Minute/text()',
        second text path 'Second/text()'
    );

create view article_id as
select pmid,xmltable.* from 
xml,
xmltable(
    '//ArticleIdList/ArticleId'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        id_type text PATH '@IdType',
        article_id text path '.'
    );

create view object as
select pmid,xmltable.* from 
xml,
xmltable(
    '//ObjectList/Object'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        type text PATH '@Type',
        object text path '.'
    );

-- citation is unwrapped as unnest(xpath('.',citation)) as citation in the insert
-- this is just a weird Postgres query optimization issue
create view reference as
select pmid,seqnum,title,citation,article_ids from 
xml_staging,
xmltable(
    '//ReferenceList/Reference'
    passing raw
    columns
        seqnum FOR ORDINALITY,
        title text path 'Title/text()',
        citation xml path 'Citation',
        article_ids xml path 'ArticleIdList'
    );

create view reference_article_id as
select pmid,seqnum,xmltable.* from 
reference,
xmltable(
    '//ArticleIdList/ArticleId'
    passing article_ids
    columns
        seqnum2 FOR ORDINALITY,
        id_type text PATH '@IdType',
        article_id text path '.'
    );
