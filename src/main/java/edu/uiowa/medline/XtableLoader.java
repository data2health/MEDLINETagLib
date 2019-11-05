package edu.uiowa.medline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class XtableLoader {
    static Logger logger = Logger.getLogger(XtableLoader.class);
    static DecimalFormat formatter = new DecimalFormat("0000");
    static Properties prop_file = PropertyLoader.loadProperties("cd2h_neuromancer");

    static boolean initial = false;
    static boolean updateMode = false;

    static int increment = 1000000;

    static Connection conn = null;

    static DocumentQueue documentQueue = new DocumentQueue();
    static Thread loaderThread = null;

    static int count = 0;
    static int recordsAdded = 0;
    static int recordsUpdated = 0;
    static int recordsDeleted = 0;

    /**
     * @param args
     * @throws Exception
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception {
	PropertyConfigurator.configure(args[0]);
	conn = getConnection();

	if (args[1].equals("-full")) {
	    for (int i = 1; i <= 972; i++) {
		String fileName = "/Volumes/Pegasus3/Corpora/MEDLINE19/ftp.ncbi.nlm.nih.gov/pubmed/baseline/pubmed19n" + formatter.format(i) + ".xml.gz";
		logger.trace("file: " + fileName);
		processDocument(parseDocument(fileName));
	    }
	    logger.info("parsing completed.");
	} else if (args[1].equals("-threaded")) {
	    for (int i = 1; i <= 972; i++) {
		String fileName = "/Volumes/Pegasus3/Corpora/MEDLINE19/ftp.ncbi.nlm.nih.gov/pubmed/baseline/pubmed19n" + formatter.format(i) + ".xml.gz";
		logger.info("file: " + fileName);
		documentQueue.queue(fileName, null);
	    }

	    int maxCrawlerThreads = Runtime.getRuntime().availableProcessors();
	    Thread[] scannerThreads = new Thread[maxCrawlerThreads];

	    for (int i = 0; i < maxCrawlerThreads; i++) {
		logger.info("starting thread " + i);
		Thread theThread = new Thread(new XpathThread(documentQueue));
		theThread.setPriority(Math.max(theThread.getPriority() - 2, Thread.MIN_PRIORITY));
		theThread.start();
		scannerThreads[i] = theThread;
	    }

	    for (int i = 0; i < maxCrawlerThreads; i++) {
		scannerThreads[i].join();
	    }
	    logger.info("parsing completed.");
	} else if (args[1].equals("-update")) {
	    updateMode = true;
	    for (int i = 973; i <= 983; i++) {
		String fileName = "/Volumes/Pegasus3/Corpora/MEDLINE19/ftp.ncbi.nlm.nih.gov/pubmed/updatefiles/pubmed19n" + formatter.format(i) + ".xml.gz";
		logger.trace("file: " + fileName);
		processDocument(parseDocument(fileName));
	    }
	    logger.info("parsing completed.");
	} else if (args[1].equals("-daily")) {
	    updateMode = true;
	    // read files from stdin
	    BufferedReader IODesc = new BufferedReader(new InputStreamReader(System.in));
	    String current = null;
	    while ((current = IODesc.readLine()) != null) {
		processDocument(parseDocument(current.trim()));
	    }
	    // materializeAuthorView();
	} else if (args[1].equals("-materialize")) {
	    materialize();
	} else {
	    XpathLoader theLoader = new XpathLoader(args[1]);
	}
    }

    static Connection getConnection() throws ClassNotFoundException, SQLException {
	Connection local = null;
	Properties props = new Properties();
	props.setProperty("user", prop_file.getProperty("jdbc.user"));
	props.setProperty("password", prop_file.getProperty("jdbc.password"));

	Class.forName("org.postgresql.Driver");
	local = DriverManager.getConnection(prop_file.getProperty("jdbc.url"), props);
	// local.setAutoCommit(false);
	return local;
    }

    static Element parseDocument(String fileName) throws Exception {
	logger.info("scanning " + fileName + "...");

	File input = new File(fileName);
	InputStream is = new FileInputStream(input);
	CompressorInputStream in = new CompressorStreamFactory().createCompressorInputStream("gz", is);
	SAXReader reader = new SAXReader(false);

	// <!ELEMENT MedlineCitationSet (MedlineCitation*, DeleteCitation?)>

	Document document = reader.read(in);
	Element root = document.getRootElement();
	logger.debug("document root: " + root.getName());
	in.close();

	return root;
    }

    @SuppressWarnings("unchecked")
    static void processDocument(Element root) throws SQLException {
	for (Element citation : (List<Element>) root.selectNodes("PubmedArticle/MedlineCitation")) {
	    medlineCitation(citation);
	}

	deleteCitation(root.selectSingleNode("DeleteCitation"));

	logger.debug("committing final transaction....");
	PreparedStatement commitStmt = conn.prepareStatement("commit transaction");
	commitStmt.executeUpdate();
	commitStmt.close();

	logger.info("records added: " + recordsAdded);
	logger.info("records updated: " + recordsUpdated);
	logger.info("records deleted: " + recordsDeleted);
	logger.info("");

	recordsAdded = 0;
	recordsUpdated = 0;
	recordsDeleted = 0;
    }

    @SuppressWarnings("unchecked")
    static void deleteCitation(Node deleteNode) throws SQLException {
	// <!ELEMENT DeleteCitation (PMID+)>

	if (deleteNode == null)
	    return;
	logger.debug("\ndeleting citations:");
	ListIterator<Element> pmids = deleteNode.selectNodes("PMID").listIterator();
	while (pmids.hasNext()) {
	    int pmid = Integer.parseInt(pmids.next().getText().trim());
	    logger.debug("\t" + pmid);
	    PreparedStatement delStmt = conn.prepareStatement("delete from medline19_staging.xml where pmid = ?");
	    delStmt.setInt(1, pmid);
	    delStmt.execute();
	    delStmt.close();

	    boolean pmidInQueue = false;
	    PreparedStatement checkStmt = conn.prepareStatement("select pmid from medline19_staging.queue where pmid = ?");
	    checkStmt.setInt(1, pmid);
	    ResultSet rs = checkStmt.executeQuery();
	    while (rs.next()) {
		pmidInQueue = true;
	    }
	    checkStmt.close();

	    if (!pmidInQueue) {
		PreparedStatement insStmt = conn.prepareStatement("insert into medline19_staging.queue values(?)");
		insStmt.setInt(1, pmid);
		insStmt.execute();
		insStmt.close();
	    }

	    parseRequest(pmid);

	    recordsDeleted++;
	}
    }

    static void medlineCitation(Element citationElement) throws SQLException {
	int pmid = Integer.parseInt(citationElement.selectSingleNode("PMID").getText().trim());
	logger.debug("\tcitation pmid: " + pmid);

	boolean pmidInXML = false;
	PreparedStatement checkStmt = conn.prepareStatement("select pmid from medline19_staging.xml where pmid = ?");
	checkStmt.setInt(1, pmid);
	ResultSet rs = checkStmt.executeQuery();
	while (rs.next()) {
	    pmidInXML = true;
	}
	checkStmt.close();

	if (pmidInXML) {
	    PreparedStatement delStmt = conn.prepareStatement("delete from medline19_staging.xml where pmid = ?");
	    delStmt.setInt(1, pmid);
	    delStmt.execute();
	    delStmt.close();
	    recordsUpdated++;
	} else {
	    recordsAdded++;
	}

	PreparedStatement insStmt = conn.prepareStatement("insert into medline19_staging.xml values(?,?::xml)");
	insStmt.setInt(1, pmid);
	insStmt.setString(2, citationElement.asXML());
	insStmt.execute();
	insStmt.close();
	
	boolean pmidInQueue = false;
	checkStmt = conn.prepareStatement("select pmid from medline19_staging.queue where pmid = ?");
	checkStmt.setInt(1, pmid);
	rs = checkStmt.executeQuery();
	while (rs.next()) {
	    pmidInQueue = true;
	}
	checkStmt.close();

	if (!pmidInQueue) {
	    insStmt = conn.prepareStatement("insert into medline19_staging.queue values(?)");
	    insStmt.setInt(1, pmid);
	    insStmt.execute();
	    insStmt.close();
	}

	if ((++count % 100) == 0) {
	    logger.debug("committing transaction for " + pmid + "....");
	    PreparedStatement commitStmt = conn.prepareStatement("commit transaction");
	    commitStmt.executeUpdate();
	    commitStmt.close();
	}

    }

    static void materialize() throws SQLException {
	materialize("article",
		"pmid,issn,volume,issue,pub_date_year,pub_date_month,pub_date_day,pub_date_season,pub_date_medline,start_page,end_page,medline_pgn");
	materialize("article_title", "*");
	materialize("vernacular_title", "*");
	materialize("e_location_id", "*");
	materialize("abstract", "*");
	materialize("author", "pmid,seqnum,equal_contrib,last_name,fore_name,initials,suffix,collective_name");
	materialize("author_identifier", "*");
	materialize("author_affiliation", "*");
	materialize("language", "*");
	materialize("data_bank", "pmid,seqnum,data_bank_name");
	materialize("accession_number", "*");
	materialize("grant_info", "*");
	materialize("publication_type", "*");
	materialize("medline_journal_info", "*");
	materialize("chemical", "*");
	materialize("suppl_mesh_name", "*");
	materialize("citation_subset", "*");
	materialize("comments_corrections", "*");
	materialize("gene_symbol", "*");
	materialize("mesh_heading", "pmid,seqnum,major_topic,type,ui,descriptor_name");
	materialize("mesh_qualifier", "*");
	materialize("personal_name_subject", "*");
	materialize("other_id", "*");
	materialize("other_abstract", "*");
	materialize("keyword", "*");
	materialize("space_flight_mission", "*");
	materialize("investigator", "pmid,seqnum,last_name,fore_name,initials,suffix");
	materialize("investigator_identifier", "*");
	materialize("investigator_affiliation", "*");
	materialize("general_note", "*");
	materialize("history", "*");
	materialize("article_id", "*");
	materialize("object", "*");
	materialize("reference", "pmid,seqnum,title,citation");
	materialize("reference_article_id", "*");
    }

    static void materialize(String table, String attributes) throws SQLException {
	PreparedStatement checkStmt = conn.prepareStatement("select min(pmid), max(pmid) from medline19_staging.xml");
	ResultSet rs = checkStmt.executeQuery();
	while (rs.next()) {
	    int min = rs.getInt(1);
	    int max = rs.getInt(2);
	    logger.info(table + " min: " + min / increment + "\tmax: " + max / increment);
	    for (int fence = min / increment; fence <= max / increment; fence++) {
		logger.info("\tfence: " + fence * increment + " : " + (fence + 1) * increment);
		PreparedStatement stmt = conn.prepareStatement(
			"insert into medline." + table + " select " + attributes + " from medline19_staging." + table + " where pmid >= ? and pmid < ?");
		stmt.setInt(1, fence * increment);
		stmt.setInt(2, (fence + 1) * increment);
		int count = stmt.executeUpdate();
		stmt.close();
		logger.info("\tcount: " + count);
	    }
	}
	checkStmt.close();
    }

    static void rematerialize() throws SQLException {
	logger.info("scanning for existing records...");
	PreparedStatement stmt = conn.prepareStatement("delete from medline.article where pmid in (select pmid from medline19_staging.queue)");
	int count = stmt.executeUpdate();
	stmt.close();
	logger.info("\tdeleted " + count + " existing records");

	rematerialize("article",
		"pmid,issn,volume,issue,pub_date_year,pub_date_month,pub_date_day,pub_date_season,pub_date_medline,start_page,end_page,medline_pgn");
	rematerialize("article_title", "*");
	rematerialize("vernacular_title", "*");
	rematerialize("e_location_id", "*");
	rematerialize("abstract", "*");
	rematerialize("author", "pmid,seqnum,equal_contrib,last_name,fore_name,initials,suffix,collective_name");
	rematerialize("author_identifier", "*");
	rematerialize("author_affiliation", "*");
	rematerialize("language", "*");
	rematerialize("data_bank", "pmid,seqnum,data_bank_name");
	rematerialize("accession_number", "*");
	rematerialize("grant_info", "*");
	rematerialize("publication_type", "*");
	rematerialize("medline_journal_info", "*");
	rematerialize("chemical", "*");
	rematerialize("suppl_mesh_name", "*");
	rematerialize("citation_subset", "*");
	rematerialize("comments_corrections", "*");
	rematerialize("gene_symbol", "*");
	rematerialize("mesh_heading", "pmid,seqnum,major_topic,type,ui,descriptor_name");
	rematerialize("mesh_qualifier", "*");
	rematerialize("personal_name_subject", "*");
	rematerialize("other_id", "*");
	rematerialize("other_abstract", "*");
	rematerialize("keyword", "*");
	rematerialize("space_flight_mission", "*");
	rematerialize("investigator", "pmid,seqnum,last_name,fore_name,initials,suffix");
	rematerialize("investigator_identifier", "*");
	rematerialize("investigator_affiliation", "*");
	rematerialize("general_note", "*");
	rematerialize("history", "*");
	rematerialize("article_id", "*");
	rematerialize("object", "*");
	rematerialize("reference", "pmid,seqnum,title,citation");
	rematerialize("reference_article_id", "*");

	logger.info("truncating queue...");
	stmt = conn.prepareStatement("truncate medline19_staging.queue");
	count = stmt.executeUpdate();
	stmt.close();
    }

    static void rematerialize(String table, String attributes) throws SQLException {
	logger.info("rematerializing " + table + "...");
	PreparedStatement stmt = conn.prepareStatement("insert into medline." + table + " select " + attributes + " from medline19_staging." + table
		+ " where pmid in (select pmid from medline19_staging.queue)");
	int count = stmt.executeUpdate();
	stmt.close();
	logger.info("\tcount: " + count);
    }

    static void parseRequest(int pmid) throws SQLException {
	PreparedStatement stmt = conn.prepareStatement("insert into medline_local.parse_request values (?)");
	stmt.setInt(1, pmid);
	stmt.execute();
	stmt.close();

	stmt = conn.prepareStatement("insert into medline_local.concept_request values (?)");
	stmt.setInt(1, pmid);
	stmt.execute();
	stmt.close();
    }

    void execute(String statement) throws SQLException {
	logger.info("executing " + statement + "...");
	PreparedStatement stmt = conn.prepareStatement(statement);
	stmt.executeUpdate();
	stmt.close();
    }

}
