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
	static Properties prop_file = PropertyLoader.loadProperties("cd2h_hal");

	static boolean initial = false;
	static boolean updateMode = false;

	static int increment = 5000000;

	Connection conn = null;

	static DocumentQueue documentQueue = new DocumentQueue();
	static Thread loaderThread = null;

	int count = 0;
	int recordsAdded = 0;
	int recordsUpdated = 0;
	int recordsDeleted = 0;

	/**
	 * @param args
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure(args[0]);

		if (args[1].equals("-full")) {
			XtableLoader theLoader = new XtableLoader();
			for (int i = 1; i <= 1219; i++) {
				String fileName = "/Volumes/Pegasus0/Corpora/MEDLINE24/ftp.ncbi.nlm.nih.gov/pubmed/baseline/pubmed24n" + formatter.format(i) + ".xml.gz";
				logger.trace("file: " + fileName);
				theLoader.processDocument(theLoader.parseDocument(fileName));
			}
			logger.info("parsing completed.");
		} else if (args[1].equals("-threaded xxx")) { // there are differences in the '21 XML data as text when loaded
														// serially vs. in parallel that need exploring
			for (int i = 1; i <= 1114; i++) {
				String fileName = "/Volumes/Pegasus0/Corpora/MEDLINE24/ftp.ncbi.nlm.nih.gov/pubmed/baseline/pubmed24n" + formatter.format(i) + ".xml.gz";
				logger.info("file: " + fileName);
				documentQueue.queue(fileName, null);
			}

			int maxCrawlerThreads = Runtime.getRuntime().availableProcessors() / 2;
			Thread[] scannerThreads = new Thread[maxCrawlerThreads];

			for (int i = 0; i < maxCrawlerThreads; i++) {
				logger.info("starting thread " + i);
				Thread theThread = new Thread(new XtableThread(documentQueue, XtableThread.Mode.LOAD));
				theThread.setPriority(Math.max(theThread.getPriority() - 2, Thread.MIN_PRIORITY));
				theThread.start();
				scannerThreads[i] = theThread;
			}

			for (int i = 0; i < maxCrawlerThreads; i++) {
				scannerThreads[i].join();
			}
			logger.info("parsing completed.");
		} else if (args[1].equals("-update")) {
			XtableLoader theLoader = new XtableLoader();
			updateMode = true;
			for (int i = 1220; i <= 1256; i++) {
				String fileName = "/Volumes/Pegasus0/Corpora/MEDLINE24/ftp.ncbi.nlm.nih.gov/pubmed/updatefiles/pubmed24n" + formatter.format(i) + ".xml.gz";
				logger.trace("file: " + fileName);
				theLoader.processDocument(theLoader.parseDocument(fileName));
			}
			logger.info("parsing completed.");
		} else if (args[1].equals("-daily")) {
			XtableLoader theLoader = new XtableLoader();
			updateMode = true;
			int count = 0;
			// read files from stdin
			BufferedReader IODesc = new BufferedReader(new InputStreamReader(System.in));
			String current = null;
			while ((current = IODesc.readLine()) != null) {
				theLoader.processDocument(theLoader.parseDocument(current.trim()));
				count++;
			}
			if (count > 0)
				theLoader.rematerialize();
		} else if (args[1].equals("-materialize")) {
			XtableLoader theLoader = new XtableLoader();
			theLoader.materialize();
		} else {
			XtableLoader theLoader = new XtableLoader();
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

	public XtableLoader() throws ClassNotFoundException, SQLException {
		conn = getConnection();
	}

	Element parseDocument(String fileName) throws Exception {
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
	void processDocument(Element root) throws SQLException {
		for (Element citation : (List<Element>) root.selectNodes("PubmedArticle")) {
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
	void deleteCitation(Node deleteNode) throws SQLException {
		// <!ELEMENT DeleteCitation (PMID+)>

		if (deleteNode == null)
			return;
		logger.debug("\ndeleting citations:");
		ListIterator<Element> pmids = deleteNode.selectNodes("MedlineCitation/PMID").listIterator();
		while (pmids.hasNext()) {
			int pmid = Integer.parseInt(pmids.next().getText().trim());
			logger.debug("\t" + pmid);
			PreparedStatement delStmt = conn.prepareStatement("delete from medline24_staging.xml where pmid = ?");
			delStmt.setInt(1, pmid);
			delStmt.execute();
			delStmt.close();

			boolean pmidInQueue = false;
			PreparedStatement checkStmt = conn.prepareStatement("select pmid from medline24_staging.queue where pmid = ?");
			checkStmt.setInt(1, pmid);
			ResultSet rs = checkStmt.executeQuery();
			while (rs.next()) {
				pmidInQueue = true;
			}
			checkStmt.close();

			if (!pmidInQueue) {
				PreparedStatement insStmt = conn.prepareStatement("insert into medline24_staging.queue values(?)");
				insStmt.setInt(1, pmid);
				insStmt.execute();
				insStmt.close();
			}

			parseRequest(pmid);

			recordsDeleted++;
		}
	}

	void medlineCitation(Element citationElement) throws SQLException {
		int pmid = Integer.parseInt(citationElement.selectSingleNode("MedlineCitation/PMID").getText().trim());
		logger.debug("\tcitation pmid: " + pmid);

		boolean pmidInXML = false;
		PreparedStatement checkStmt = conn.prepareStatement("select pmid from medline24_staging.xml_staging where pmid = ?");
		checkStmt.setInt(1, pmid);
		ResultSet rs = checkStmt.executeQuery();
		while (rs.next()) {
			pmidInXML = true;
		}
		checkStmt.close();

		if (pmidInXML) {
			PreparedStatement delStmt = conn.prepareStatement("delete from medline24_staging.xml_staging where pmid = ?");
			delStmt.setInt(1, pmid);
			delStmt.execute();
			delStmt.close();
			recordsUpdated++;
		} else {
			recordsAdded++;
		}

		PreparedStatement insStmt = conn.prepareStatement("insert into medline24_staging.xml_staging values(?,?::xml)");
		insStmt.setInt(1, pmid);
		insStmt.setString(2, citationElement.asXML());
		insStmt.execute();
		insStmt.close();

		boolean pmidInQueue = false;
		checkStmt = conn.prepareStatement("select pmid from medline24_staging.queue where pmid = ?");
		checkStmt.setInt(1, pmid);
		rs = checkStmt.executeQuery();
		while (rs.next()) {
			pmidInQueue = true;
		}
		checkStmt.close();

		if (!pmidInQueue) {
			insStmt = conn.prepareStatement("insert into medline24_staging.queue values(?)");
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

	void materialize() throws Exception {
		MaterializationQueue theQueue = new MaterializationQueue();

		// first materialize the root of everything
		materialize("article", "pmid,issn,volume,issue,pub_date_year,pub_date_month,pub_date_day,pub_date_season,pub_date_medline,start_page,end_page,medline_pgn");

		int maxCrawlerThreads = 12;
		Thread[] scannerThreads = new Thread[maxCrawlerThreads];

		for (int i = 0; i < maxCrawlerThreads; i++) {
			logger.info("starting thread " + i);
			Thread theThread = new Thread(new XtableThread(i, theQueue, XtableThread.Mode.MATERIALIZE));
			theThread.start();
			scannerThreads[i] = theThread;
		}

		for (int i = 0; i < maxCrawlerThreads; i++) {
			scannerThreads[i].join();
		}
		logger.info("materialization completed.");
	}

	void materialize(String table, String attributes) throws SQLException {
		logger.info("materializing: " + table + ": " + attributes);
		PreparedStatement stmt = conn.prepareStatement("insert into medline." + table + " select " + attributes + " from medline24_staging." + table);
		int count = stmt.executeUpdate();
		stmt.close();
		logger.info("\tcount: " + count);
	}

	void materializeByGroup() throws SQLException {
		materializeByGroup("article", "pmid,issn,volume,issue,pub_date_year,pub_date_month,pub_date_day,pub_date_season,pub_date_medline,start_page,end_page,medline_pgn");
		materializeByGroup("article_title", "*");
		materializeByGroup("vernacular_title", "*");
		materializeByGroup("e_location_id", "*");
		materializeByGroup("abstract", "*");
		materializeByGroup("author", "pmid,seqnum,equal_contrib,last_name,fore_name,initials,suffix,collective_name");
		materializeByGroup("author_identifier", "*");
		materializeByGroup("author_affiliation", "*");
		materializeByGroup("language", "*");
		materializeByGroup("data_bank", "pmid,seqnum,data_bank_name");
		materializeByGroup("accession_number", "*");
		materializeByGroup("grant_info", "*");
		materializeByGroup("publication_type", "*");
		materializeByGroup("medline_journal_info", "*");
		materializeByGroup("chemical", "*");
		materializeByGroup("suppl_mesh_name", "*");
		materializeByGroup("citation_subset", "*");
		materializeByGroup("comments_corrections", "*");
		materializeByGroup("gene_symbol", "*");
		materializeByGroup("mesh_heading", "pmid,seqnum,major_topic,type,ui,descriptor_name");
		materializeByGroup("mesh_qualifier", "*");
		materializeByGroup("personal_name_subject", "*");
		materializeByGroup("other_id", "*");
		materializeByGroup("other_abstract", "*");
		materializeByGroup("keyword", "*");
		materializeByGroup("space_flight_mission", "*");
		materializeByGroup("investigator", "pmid,seqnum,last_name,fore_name,initials,suffix");
		materializeByGroup("investigator_identifier", "*");
		materializeByGroup("investigator_affiliation", "*");
		materializeByGroup("general_note", "*");
		materializeByGroup("history", "*");
		materializeByGroup("article_id", "*");
		materializeByGroup("object", "*");
		materializeByGroup("reference", "pmid,seqnum,title,citation");
		materializeByGroup("reference_article_id", "*");
	}

	void materializeByGroup(String table, String attributes) throws SQLException {
		PreparedStatement checkStmt = conn.prepareStatement("select min(pmid), max(pmid) from medline24_staging.xml_staging");
		ResultSet rs = checkStmt.executeQuery();
		while (rs.next()) {
			int min = rs.getInt(1);
			int max = rs.getInt(2);
			logger.info(table + " min: " + min / increment + "\tmax: " + max / increment);
			for (int fence = min / increment; fence <= max / increment; fence++) {
				logger.info("\tfence: " + fence * increment + " : " + (fence + 1) * increment);
				PreparedStatement stmt = conn.prepareStatement("insert into medline." + table + " select " + attributes	+ " from medline24_staging." + table + " where pmid >= ? and pmid < ?");
				stmt.setInt(1, fence * increment);
				stmt.setInt(2, (fence + 1) * increment);
				int count = stmt.executeUpdate();
				stmt.close();
				logger.info("\tcount: " + count);
			}
		}
		checkStmt.close();
	}

	void rematerialize() throws Exception {
		MaterializationQueue theQueue = new MaterializationQueue();
		logger.info("scanning for existing records...");
		PreparedStatement stmt = conn.prepareStatement("delete from medline.article where pmid in (select pmid from medline24_staging.queue)");
		int count = stmt.executeUpdate();
		stmt.close();
		logger.info("\tdeleted " + count + " existing records");

		rematerialize("article", "pmid,issn,volume,issue,pub_date_year,pub_date_month,pub_date_day,pub_date_season,pub_date_medline,start_page,end_page,medline_pgn");

		int maxCrawlerThreads = 12;
		Thread[] scannerThreads = new Thread[maxCrawlerThreads];

		for (int i = 0; i < maxCrawlerThreads; i++) {
			logger.info("starting thread " + i);
			Thread theThread = new Thread(new XtableThread(i, theQueue, XtableThread.Mode.REMATERIALIZE));
			theThread.start();
			scannerThreads[i] = theThread;
		}

		for (int i = 0; i < maxCrawlerThreads; i++) {
			scannerThreads[i].join();
		}

		logger.info("loading indexing queue...");
		stmt = conn.prepareStatement("insert into medline24_staging.queue_indexing select * from medline24_staging.queue");
		count = stmt.executeUpdate();
		stmt.close();
		logger.info("truncating queue...");
		stmt = conn.prepareStatement("truncate medline24_staging.queue");
		count = stmt.executeUpdate();
		stmt.close();
		logger.info("rematerialization completed.");
	}

	void rematerializeByGroup() throws SQLException {
		logger.info("scanning for existing records...");
		PreparedStatement stmt = conn.prepareStatement("delete from medline.article where pmid in (select pmid from medline24_staging.queue)");
		int count = stmt.executeUpdate();
		stmt.close();
		logger.info("\tdeleted " + count + " existing records");

		rematerialize("article", "pmid,issn,volume,issue,pub_date_year,pub_date_month,pub_date_day,pub_date_season,pub_date_medline,start_page,end_page,medline_pgn");
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
		stmt = conn.prepareStatement("truncate medline24_staging.queue");
		count = stmt.executeUpdate();
		stmt.close();
	}

	void rematerialize(String table, String attributes) throws SQLException {
		logger.info("rematerializing " + table + "...");
		PreparedStatement stmt = conn.prepareStatement("insert into medline." + table + " select " + attributes	+ " from medline24_staging." + table + " where pmid in (select pmid from medline24_staging.queue)");
		int count = stmt.executeUpdate();
		stmt.close();
		logger.info("\tcount: " + count);
	}

	void parseRequest(int pmid) throws SQLException {
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
