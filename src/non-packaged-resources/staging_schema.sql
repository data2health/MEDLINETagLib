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

SET default_tablespace = '';

SET default_table_access_method = heap;

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
-- Name: xml_staging; Type: TABLE; Schema: medline24_staging; Owner: eichmann
--

CREATE TABLE medline24_staging.xml_staging (
    pmid integer,
    raw xml
);


ALTER TABLE medline24_staging.xml_staging OWNER TO eichmann;

--
-- Name: qpmid; Type: INDEX; Schema: medline24_staging; Owner: eichmann
--

CREATE INDEX qpmid ON medline24_staging.queue USING btree (pmid);


--
-- Name: xpmid; Type: INDEX; Schema: medline24_staging; Owner: eichmann
--

CREATE INDEX xpmid ON medline24_staging.xml_staging USING btree (pmid);


--
-- PostgreSQL database dump complete
--

