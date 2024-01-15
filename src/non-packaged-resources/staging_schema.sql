--
-- PostgreSQL database dump
--

-- Dumped from database version 16.0
-- Dumped by pg_dump version 16.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: medline24_staging; Type: SCHEMA; Schema: -; Owner: eichmann
--

CREATE SCHEMA medline24_staging;


ALTER SCHEMA medline24_staging OWNER TO eichmann;

--
-- Name: table_notify(); Type: FUNCTION; Schema: medline24_staging; Owner: eichmann
--

CREATE FUNCTION medline24_staging.table_notify() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
  channel TEXT;
  old_row JSON;
  new_row JSON;
  notification JSON;
  xmin BIGINT;
  _primary_keys TEXT [];
  _foreign_keys TEXT [];

BEGIN
    -- database is also the channel name.
    channel := CURRENT_DATABASE();

    IF TG_OP = 'DELETE' THEN

        SELECT primary_keys
        INTO _primary_keys
        FROM medline24_staging._view
        WHERE table_name = TG_TABLE_NAME;

        old_row = ROW_TO_JSON(OLD);
        old_row := (
            SELECT JSONB_OBJECT_AGG(key, value)
            FROM JSON_EACH(old_row)
            WHERE key = ANY(_primary_keys)
        );
        xmin := OLD.xmin;
    ELSE
        IF TG_OP <> 'TRUNCATE' THEN

            SELECT primary_keys, foreign_keys
            INTO _primary_keys, _foreign_keys
            FROM medline24_staging._view
            WHERE table_name = TG_TABLE_NAME;

            new_row = ROW_TO_JSON(NEW);
            new_row := (
                SELECT JSONB_OBJECT_AGG(key, value)
                FROM JSON_EACH(new_row)
                WHERE key = ANY(_primary_keys || _foreign_keys)
            );
            IF TG_OP = 'UPDATE' THEN
                old_row = ROW_TO_JSON(OLD);
                old_row := (
                    SELECT JSONB_OBJECT_AGG(key, value)
                    FROM JSON_EACH(old_row)
                    WHERE key = ANY(_primary_keys || _foreign_keys)
                );
            END IF;
            xmin := NEW.xmin;
        END IF;
    END IF;

    -- construct the notification as a JSON object.
    notification = JSON_BUILD_OBJECT(
        'xmin', xmin,
        'new', new_row,
        'old', old_row,
        'tg_op', TG_OP,
        'table', TG_TABLE_NAME,
        'schema', TG_TABLE_SCHEMA
    );

    -- Notify/Listen updates occur asynchronously,
    -- so this doesn't block the Postgres trigger procedure.
    PERFORM PG_NOTIFY(channel, notification::TEXT);

  RETURN NEW;
END;
$$;


ALTER FUNCTION medline24_staging.table_notify() OWNER TO eichmann;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: xml_staging; Type: TABLE; Schema: medline24_staging; Owner: eichmann
--

CREATE TABLE medline24_staging.xml_staging (
    pmid integer,
    raw xml
);


ALTER TABLE medline24_staging.xml_staging OWNER TO eichmann;

--
-- Name: abstract; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.abstract AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".label,
    "xmltable".nlm_category,
    "xmltable".abstract
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//Article/Abstract/AbstractText'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, label text PATH ('@Label'::text), nlm_category text PATH ('@NlmCategory'::text), abstract text PATH ('.'::text));


ALTER VIEW medline24_staging.abstract OWNER TO eichmann;

--
-- Name: data_bank; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.data_bank AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".data_bank_name,
    "xmltable".accession_number_list
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//Article/DataBankList/DataBank'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, data_bank_name text PATH ('DataBankName/text()'::text), accession_number_list xml PATH ('AccessionNumberList'::text));


ALTER VIEW medline24_staging.data_bank OWNER TO eichmann;

--
-- Name: accession_number; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.accession_number AS
 SELECT data_bank.pmid,
    data_bank.seqnum,
    "xmltable".seqnum2,
    "xmltable".affiliation
   FROM medline24_staging.data_bank,
    LATERAL XMLTABLE(('//AccessionNumberList/AccessionNumber'::text) PASSING (data_bank.accession_number_list) COLUMNS seqnum2 FOR ORDINALITY, affiliation text PATH ('.'::text));


ALTER VIEW medline24_staging.accession_number OWNER TO eichmann;

--
-- Name: article; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.article AS
 SELECT xml_staging.pmid,
    "xmltable".issn,
    "xmltable".volume,
    "xmltable".issue,
    "xmltable".pub_date_year,
    "xmltable".pub_date_month,
    "xmltable".pub_date_day,
    "xmltable".pub_date_season,
    "xmltable".pub_date_medline,
    "xmltable".start_page,
    "xmltable".end_page,
    "xmltable".medline_pgn,
    "xmltable".author_list
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//Article'::text) PASSING (xml_staging.raw) COLUMNS issn text PATH ('Journal/ISSN/text()'::text), volume text PATH ('Journal/JournalIssue/Volume/text()'::text), issue text PATH ('Journal/JournalIssue/Issue/text()'::text), pub_date_year text PATH ('Journal/JournalIssue/PubDate/Year/text()'::text), pub_date_month text PATH ('Journal/JournalIssue/PubDate/Month/text()'::text), pub_date_day text PATH ('Journal/JournalIssue/PubDate/Day/text()'::text), pub_date_season text PATH ('Journal/JournalIssue/PubDate/Season/text()'::text), pub_date_medline text PATH ('Journal/JournalIssue/PubDate/MedlineDate/text()'::text), start_page text PATH ('Pagination/StartPage/text()'::text), end_page text PATH ('Pagination/EndPage/text()'::text), medline_pgn text PATH ('Pagination/MedlinePgn/text()'::text), author_list xml PATH ('AuthorList'::text));


ALTER VIEW medline24_staging.article OWNER TO eichmann;

--
-- Name: article_id; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.article_id AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".id_type,
    "xmltable".article_id
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('/PubmedArticle/PubmedData/ArticleIdList/ArticleId'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, id_type text PATH ('@IdType'::text), article_id text PATH ('.'::text));


ALTER VIEW medline24_staging.article_id OWNER TO eichmann;

--
-- Name: article_title; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.article_title AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".article_title
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//Article/ArticleTitle'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, article_title text PATH ('.'::text));


ALTER VIEW medline24_staging.article_title OWNER TO eichmann;

--
-- Name: author; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.author AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".equal_contrib,
    "xmltable".last_name,
    "xmltable".fore_name,
    "xmltable".initials,
    "xmltable".suffix,
    "xmltable".collective_name,
    "xmltable".identifiers,
    "xmltable".affiliation_info
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//Article/AuthorList/Author'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, equal_contrib text PATH ('@EqualContrib'::text), last_name text PATH ('LastName/text()'::text), fore_name text PATH ('ForeName/text()'::text), initials text PATH ('Initials/text()'::text), suffix text PATH ('Suffix/text()'::text), collective_name text PATH ('CollectiveName/text()'::text), identifiers xml PATH ('.'::text), affiliation_info xml PATH ('.'::text));


ALTER VIEW medline24_staging.author OWNER TO eichmann;

--
-- Name: author_affiliation; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.author_affiliation AS
 SELECT author.pmid,
    author.seqnum,
    "xmltable".seqnum2,
    "xmltable".affiliation,
    "xmltable".identifier
   FROM medline24_staging.author,
    LATERAL XMLTABLE(('//Author/AffiliationInfo/Affiliation'::text) PASSING (author.affiliation_info) COLUMNS seqnum2 FOR ORDINALITY, affiliation text PATH ('.'::text), identifier xml PATH ('..'::text));


ALTER VIEW medline24_staging.author_affiliation OWNER TO eichmann;

--
-- Name: author_identifier; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.author_identifier AS
 SELECT author.pmid,
    author.seqnum,
    "xmltable".seqnum2,
    "xmltable".source,
    "xmltable".identifier
   FROM medline24_staging.author,
    LATERAL XMLTABLE(('//Author/Identifier'::text) PASSING (author.identifiers) COLUMNS seqnum2 FOR ORDINALITY, source text PATH ('@Source'::text), identifier text PATH ('.'::text));


ALTER VIEW medline24_staging.author_identifier OWNER TO eichmann;

--
-- Name: chemical; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.chemical AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".registry_number,
    "xmltable".substance_ui,
    "xmltable".name_of_substance
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//ChemicalList/Chemical'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, registry_number text PATH ('RegistryNumber/text()'::text), substance_ui text PATH ('NameOfSubstance/@UI'::text), name_of_substance text PATH ('NameOfSubstance/text()'::text));


ALTER VIEW medline24_staging.chemical OWNER TO eichmann;

--
-- Name: citation_subset; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.citation_subset AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".citation_subset
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//CitationSubset'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, citation_subset text PATH ('.'::text));


ALTER VIEW medline24_staging.citation_subset OWNER TO eichmann;

--
-- Name: comments_corrections; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.comments_corrections AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".ref_type,
    "xmltable".ref_source,
    "xmltable".ref_pmid,
    "xmltable".note
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//CommentsCorrectionsList/CommentsCorrections'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, ref_type text PATH ('@RefType'::text), ref_source text PATH ('RefSource/text()'::text), ref_pmid text PATH ('PMID/text()'::text), note text PATH ('Note/text()'::text));


ALTER VIEW medline24_staging.comments_corrections OWNER TO eichmann;

--
-- Name: e_location_id; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.e_location_id AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".e_id_type,
    "xmltable".e_location_id
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//Article/ELocationID'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, e_id_type text PATH ('@EIdType'::text), e_location_id text PATH ('.'::text));


ALTER VIEW medline24_staging.e_location_id OWNER TO eichmann;

--
-- Name: gene_symbol; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.gene_symbol AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".gene_symbol
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//GeneSymbolList/GeneSymbol'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, gene_symbol text PATH ('.'::text));


ALTER VIEW medline24_staging.gene_symbol OWNER TO eichmann;

--
-- Name: general_note; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.general_note AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".owner,
    "xmltable".general_note
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//GeneralNote'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, owner text PATH ('@Owner'::text), general_note text PATH ('.'::text));


ALTER VIEW medline24_staging.general_note OWNER TO eichmann;

--
-- Name: grant_info; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.grant_info AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".grant_id,
    "xmltable".acronym,
    "xmltable".agency,
    "xmltable".country
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//Article/GrantList/Grant'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, grant_id text PATH ('GrantID/text()'::text), acronym text PATH ('Acronym/text()'::text), agency text PATH ('Agency/text()'::text), country text PATH ('Country/text()'::text));


ALTER VIEW medline24_staging.grant_info OWNER TO eichmann;

--
-- Name: history; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.history AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".pub_status,
    "xmltable".year,
    "xmltable".month,
    "xmltable".day,
    "xmltable".hour,
    "xmltable".minute,
    "xmltable".second
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//PubmedData/History/PubMedPubDate'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, pub_status text PATH ('@PubStatus'::text), year text PATH ('Year/text()'::text), month text PATH ('Month/text()'::text), day text PATH ('Day/text()'::text), hour text PATH ('Hour/text()'::text), minute text PATH ('Minute/text()'::text), second text PATH ('Second/text()'::text));


ALTER VIEW medline24_staging.history OWNER TO eichmann;

--
-- Name: investigator; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.investigator AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".last_name,
    "xmltable".fore_name,
    "xmltable".initials,
    "xmltable".suffix,
    "xmltable".identifiers,
    "xmltable".affiliation_info
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//InvestigatorList/Investigator'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, last_name text PATH ('LastName/text()'::text), fore_name text PATH ('ForeName/text()'::text), initials text PATH ('Initials/text()'::text), suffix text PATH ('Suffix/text()'::text), identifiers xml PATH ('Identifier'::text), affiliation_info xml PATH ('.'::text));


ALTER VIEW medline24_staging.investigator OWNER TO eichmann;

--
-- Name: investigator_affiliation; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.investigator_affiliation AS
 SELECT investigator.pmid,
    investigator.seqnum,
    "xmltable".seqnum2,
    "xmltable".affiliation,
    "xmltable".identifier
   FROM medline24_staging.investigator,
    LATERAL XMLTABLE(('//Investigator/AffiliationInfo'::text) PASSING (investigator.affiliation_info) COLUMNS seqnum2 FOR ORDINALITY, affiliation text PATH ('Affiliation/text()'::text), identifier xml PATH ('Identifier'::text));


ALTER VIEW medline24_staging.investigator_affiliation OWNER TO eichmann;

--
-- Name: investigator_identifier; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.investigator_identifier AS
 SELECT investigator.pmid,
    investigator.seqnum,
    "xmltable".seqnum2,
    "xmltable".source,
    "xmltable".identifier
   FROM medline24_staging.investigator,
    LATERAL XMLTABLE(('//Identifier'::text) PASSING (investigator.identifiers) COLUMNS seqnum2 FOR ORDINALITY, source text PATH ('@Source'::text), identifier text PATH ('.'::text));


ALTER VIEW medline24_staging.investigator_identifier OWNER TO eichmann;

--
-- Name: keyword; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.keyword AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".major_topic,
    "xmltable".owner,
    "xmltable".keyword
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//KeywordList/Keyword'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, major_topic text PATH ('@MajorTopicYN'::text), owner text PATH ('../@Owner'::text), keyword text PATH ('.'::text));


ALTER VIEW medline24_staging.keyword OWNER TO eichmann;

--
-- Name: language; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.language AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".language
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//Article/Language'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, language text PATH ('.'::text));


ALTER VIEW medline24_staging.language OWNER TO eichmann;

--
-- Name: medline_journal_info; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.medline_journal_info AS
 SELECT xml_staging.pmid,
    "xmltable".country,
    "xmltable".medline_ta,
    "xmltable".nlm_unique_id,
    "xmltable".issn_linking
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//MedlineJournalInfo'::text) PASSING (xml_staging.raw) COLUMNS country text PATH ('Country/text()'::text), medline_ta text PATH ('MedlineTA/text()'::text), nlm_unique_id text PATH ('NlmUniqueID/text()'::text), issn_linking text PATH ('ISSNLinking/text()'::text));


ALTER VIEW medline24_staging.medline_journal_info OWNER TO eichmann;

--
-- Name: mesh_heading; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.mesh_heading AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".major_topic,
    "xmltable".type,
    "xmltable".ui,
    "xmltable".descriptor_name,
    "xmltable".qualifier_name
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//MeshHeadingList/MeshHeading'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, major_topic text PATH ('DescriptorName/@MajorTopicYN'::text), type text PATH ('DescriptorName/@Type'::text), ui text PATH ('DescriptorName/@UI'::text), descriptor_name text PATH ('DescriptorName/text()'::text), qualifier_name xml PATH ('.'::text));


ALTER VIEW medline24_staging.mesh_heading OWNER TO eichmann;

--
-- Name: mesh_qualifier; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.mesh_qualifier AS
 SELECT mesh_heading.pmid,
    mesh_heading.seqnum,
    "xmltable".seqnum2,
    "xmltable".major_topic,
    "xmltable".ui,
    "xmltable".qualifier_name
   FROM medline24_staging.mesh_heading,
    LATERAL XMLTABLE(('//MeshHeading/QualifierName'::text) PASSING (mesh_heading.qualifier_name) COLUMNS seqnum2 FOR ORDINALITY, major_topic text PATH ('@MajorTopicYN'::text), ui text PATH ('@UI'::text), qualifier_name text PATH ('.'::text));


ALTER VIEW medline24_staging.mesh_qualifier OWNER TO eichmann;

--
-- Name: object; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.object AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".type,
    "xmltable".object
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//ObjectList/Object'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, type text PATH ('@Type'::text), object text PATH ('.'::text));


ALTER VIEW medline24_staging.object OWNER TO eichmann;

--
-- Name: other_abstract; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.other_abstract AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".type,
    "xmltable".language,
    "xmltable".abstract
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//OtherAbstract/AbstractText'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, type text PATH ('@Type'::text), language text PATH ('@Language'::text), abstract text PATH ('.'::text));


ALTER VIEW medline24_staging.other_abstract OWNER TO eichmann;

--
-- Name: other_id; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.other_id AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".source,
    "xmltable".other_id
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//OtherID'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, source text PATH ('@Source'::text), other_id text PATH ('.'::text));


ALTER VIEW medline24_staging.other_id OWNER TO eichmann;

--
-- Name: personal_name_subject; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.personal_name_subject AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".last_name,
    "xmltable".fore_name,
    "xmltable".initials,
    "xmltable".suffix
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//PersonalNameSubjectList/PersonalNameSubject'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, last_name text PATH ('LastName/text()'::text), fore_name text PATH ('ForeName/text()'::text), initials text PATH ('Initials/text()'::text), suffix text PATH ('Suffix/text()'::text));


ALTER VIEW medline24_staging.personal_name_subject OWNER TO eichmann;

--
-- Name: publication_type; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.publication_type AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".ui,
    "xmltable".publication_type
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//Article/PublicationTypeList/PublicationType'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, ui text PATH ('@UI'::text), publication_type text PATH ('.'::text));


ALTER VIEW medline24_staging.publication_type OWNER TO eichmann;

--
-- Name: queue; Type: TABLE; Schema: medline24_staging; Owner: eichmann
--

CREATE TABLE medline24_staging.queue (
    pmid integer
);


ALTER TABLE medline24_staging.queue OWNER TO eichmann;

--
-- Name: queue_indexing; Type: TABLE; Schema: medline24_staging; Owner: eichmann
--

CREATE TABLE medline24_staging.queue_indexing (
    pmid integer
);


ALTER TABLE medline24_staging.queue_indexing OWNER TO eichmann;

--
-- Name: reference; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.reference AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".title,
    "xmltable".citation,
    "xmltable".article_ids
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//ReferenceList/Reference'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, title text PATH ('Title/text()'::text), citation xml PATH ('Citation'::text), article_ids xml PATH ('ArticleIdList'::text));


ALTER VIEW medline24_staging.reference OWNER TO eichmann;

--
-- Name: reference_article_id; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.reference_article_id AS
 SELECT reference.pmid,
    reference.seqnum,
    "xmltable".seqnum2,
    "xmltable".id_type,
    "xmltable".article_id
   FROM medline24_staging.reference,
    LATERAL XMLTABLE(('//ArticleIdList/ArticleId'::text) PASSING (reference.article_ids) COLUMNS seqnum2 FOR ORDINALITY, id_type text PATH ('@IdType'::text), article_id text PATH ('.'::text));


ALTER VIEW medline24_staging.reference_article_id OWNER TO eichmann;

--
-- Name: space_flight_mission; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.space_flight_mission AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".space_flight_mission
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//SpaceFlightMission'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, space_flight_mission text PATH ('.'::text));


ALTER VIEW medline24_staging.space_flight_mission OWNER TO eichmann;

--
-- Name: suppl_mesh_name; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.suppl_mesh_name AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".type,
    "xmltable".ui,
    "xmltable".suppl_mesh_name
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//SupplMeshList/SupplMeshName'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, type text PATH ('@Type'::text), ui text PATH ('@UI'::text), suppl_mesh_name text PATH ('.'::text));


ALTER VIEW medline24_staging.suppl_mesh_name OWNER TO eichmann;

--
-- Name: vernacular_title; Type: VIEW; Schema: medline24_staging; Owner: eichmann
--

CREATE VIEW medline24_staging.vernacular_title AS
 SELECT xml_staging.pmid,
    "xmltable".seqnum,
    "xmltable".vernacular_title
   FROM medline24_staging.xml_staging,
    LATERAL XMLTABLE(('//Article/VernacularTitle'::text) PASSING (xml_staging.raw) COLUMNS seqnum FOR ORDINALITY, vernacular_title text PATH ('.'::text));


ALTER VIEW medline24_staging.vernacular_title OWNER TO eichmann;

--
-- Name: qpmid; Type: INDEX; Schema: medline24_staging; Owner: eichmann
--

CREATE INDEX qpmid ON medline24_staging.queue USING btree (pmid);


--
-- Name: xpmid; Type: INDEX; Schema: medline24_staging; Owner: eichmann
--

CREATE INDEX xpmid ON 	_staging.xml_staging USING btree (pmid);


--
-- PostgreSQL database dump complete
--

