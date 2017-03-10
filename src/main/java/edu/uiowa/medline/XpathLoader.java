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
import java.util.GregorianCalendar;
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

public class XpathLoader {
    static Logger logger = Logger.getLogger(XpathLoader.class);
    static DecimalFormat formatter = new DecimalFormat("0000");
    static Properties prop_file = PropertyLoader.loadProperties("loader");

    static Connection conn = null;
    
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

	String db_user = prop_file.getProperty("db.user.name", "eichmann");
	logger.debug("Database User Name: " + db_user);
	String db_pass = prop_file.getProperty("db.user.password", "translational");

	String use_ssl = prop_file.getProperty("db.use.ssl", "false");
	logger.debug("Database SSL: " + use_ssl);

	String databaseHost = prop_file.getProperty("db.host", "neuromancer.icts.uiowa.edu");
	logger.debug("Database Host: " + databaseHost);

	String databaseName = prop_file.getProperty("db.name", "bioinformatics");
	logger.debug("Database Name: " + databaseName);

	String db_url = prop_file.getProperty("db.url", "jdbc:postgresql://" + databaseHost + "/" + databaseName);
	logger.debug("Database URL: " + db_url);

	Class.forName("org.postgresql.Driver");
	Properties props = new Properties();
	props.setProperty("user", "eichmann");
	props.setProperty("password", "translational");
	if (use_ssl.equals("true")) {
	    props.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
	    props.setProperty("ssl", "true");
	}

	sessionReset(db_url, props);

	if (args[1].equals("-full")) {
//	    loaderThread = new Thread(new XpathThread(documentQueue));
//	    loaderThread.start();
	    for (int i = 640; i <= 892; i++) {
		String fileName = "/Volumes/SLIS_SAN_01/Corpora/MEDLINE17/ftp.ncbi.nlm.nih.gov/pubmed/baseline/medline17n" + formatter.format(i) + ".xml.gz";
		logger.trace("file: " + fileName);
		XpathLoader theLoader = new XpathLoader(fileName);
//		Element root = parseDocument(fileName);
//		theLoader.processDocument(root);
//		while (documentQueue.atCapacity()) {
//		    logger.info("parser thread sleeping...");
//		    Thread.sleep(2* 60 * 1000);
//		}
//		documentQueue.queue(fileName, root);

//		if (i % 5 == 0) {
//		    sessionReset(db_url, props);
//		}
	    }
	    logger.info("parsing completed.");
//	    documentQueue.completed();
//	    loaderThread.join();
	} else if (args[1].equals("-update")) {
//	    loaderThread = new Thread(new XpathThread(documentQueue));
//	    loaderThread.start();
	    for (int i = 893; i <= 1070; i++) {
		String fileName = "/Volumes/SLIS_SAN_01/Corpora/MEDLINE17/ftp.ncbi.nlm.nih.gov/pubmed/updatefiles/medline17n" + formatter.format(i) + ".xml.gz";
		logger.trace("file: " + fileName);
		XpathLoader theLoader = new XpathLoader(fileName);
//		Element root = parseDocument(fileName);
//		
//		while (documentQueue.atCapacity()) {
//		    logger.info("parser thread sleeping...");
//		    Thread.sleep(2* 60 * 1000);
//		}
//		documentQueue.queue(fileName, root);

//		if (i % 5 == 0) {
//		    sessionReset(db_url, props);
//		}
	    }
	    logger.info("parsing completed.");
//	    documentQueue.completed();
//	    loaderThread.join();
	} else if (args[1].equals("-daily")) {
	    // read files from stdin
	    BufferedReader IODesc = new BufferedReader(new InputStreamReader(System.in));
	    String current = null;
	    while ((current = IODesc.readLine()) != null) {
		XpathLoader theLoader = new XpathLoader(current.trim());
	    }
//	    materializeAuthorView();
	} else if (args[1].equals("-materialize")) {
	    materializeAuthorView();
	} else {
	    XpathLoader theLoader = new XpathLoader(args[1]);
	}
    }

    static void sessionReset(String db_url, Properties props) throws SQLException {
	if (conn != null)
	    conn.close();

	conn = DriverManager.getConnection(db_url, props);
	conn.setAutoCommit(false);

	PreparedStatement pathStmt = conn.prepareStatement("set search_path to medline17,loki");
	pathStmt.executeUpdate();
	pathStmt.close();

	pathStmt = conn.prepareStatement("set constraints all deferred");
	pathStmt.executeUpdate();
	pathStmt.close();

    }
    
    public XpathLoader() {
	
    }

    public XpathLoader(String fileName) throws Exception {
	processDocument(parseDocument(fileName));
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
    void processDocument(Element root) throws SQLException {
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
    void deleteCitation(Node deleteNode) throws SQLException {
	// <!ELEMENT DeleteCitation (PMID+)>

	if (deleteNode == null)
	    return;
	logger.debug("\ndeleting citations:");
	ListIterator<Element> pmids = deleteNode.selectNodes("PMID").listIterator();
	while (pmids.hasNext()) {
	    int pmid = Integer.parseInt(pmids.next().getText());
	    logger.debug("\t" + pmid);
	    PreparedStatement delStmt = conn.prepareStatement("delete from article where pmid = ?");
	    delStmt.setInt(1, pmid);
	    delStmt.execute();
	    delStmt.close();

	    recordsDeleted++;
	}
    }

    void medlineCitation(Element citationElement) throws SQLException {
	// <!ELEMENT MedlineCitation (PMID, DateCreated, DateCompleted?,
	// DateRevised?,
	// Article, MedlineJournalInfo, ChemicalList?,SupplMeshList?,
	// CitationSubset*, CommentsCorrectionsList?, GeneSymbolList?,
	// MeshHeadingList?,NumberOfReferences?, PersonalNameSubjectList?,
	// OtherID*, OtherAbstract*, KeywordList*, SpaceFlightMission*,
	// InvestigatorList?, GeneralNote*)>
	// <!ATTLIST MedlineCitation
	// Owner (NLM | NASA | PIP | KIE | HSR | HMD | NOTNLM) "NLM"
	// Status (Completed | In-Process | PubMed-not-MEDLINE |
	// In-Data-Review | Publisher | MEDLINE |
	// OLDMEDLINE) #REQUIRED
	// VersionID CDATA #IMPLIED
	// VersionDate CDATA #IMPLIED>
	// <!ELEMENT MedlineJournalInfo (Country?, MedlineTA,
	// NlmUniqueID?,ISSNLinking?)>
	// <!ELEMENT Article (Journal,ArticleTitle,((Pagination, ELocationID*) |
	// ELocationID+),Abstract?, Affiliation?, AuthorList?,
	// Language+, DataBankList?, GrantList?,PublicationTypeList,
	// VernacularTitle?, ArticleDate*)>
	// <!ATTLIST Article
	// PubModel (Print | Print-Electronic | Electronic |
	// Electronic-Print) #REQUIRED>

	// MedlineCitation elements
	int pmid = Integer.parseInt(citationElement.selectSingleNode("PMID").getText());
	logger.debug("\tcitation pmid: " + pmid);
	
	int found = 0;
	PreparedStatement cntStmt = conn.prepareStatement("select pmid from article where pmid = ?");
	cntStmt.setInt(1, pmid);
	ResultSet rs = cntStmt.executeQuery();
	if (rs.next()) {
	    found++;
	}
	cntStmt.close();
	if (found != 0)
	    return;

	PreparedStatement delStmt = conn.prepareStatement("delete from author where pmid = ?");
	delStmt.setInt(1, pmid);
	delStmt.execute();
	delStmt.close();

	delStmt = conn.prepareStatement("delete from investigator where pmid = ?");
	delStmt.setInt(1, pmid);
	delStmt.execute();
	delStmt.close();

	
	article(pmid, citationElement);

	journal(pmid, citationElement.selectSingleNode("Article/Journal"));
	elocationID(pmid, citationElement.selectNodes("Article/ELocationID"));
	abstr(pmid, citationElement.selectSingleNode("Article/Abstract"));
	author(pmid, citationElement.selectSingleNode("Article/AuthorList"));
	language(pmid, citationElement.selectNodes("Article/Language"));
	dataBank(pmid, citationElement.selectSingleNode("Article/DataBankList"));
	grant(pmid, citationElement.selectSingleNode("Article/GrantList"));
	publicationType(pmid, citationElement.selectSingleNode("Article/PublicationTypeList"));
	articleDate(pmid, citationElement.selectNodes("Article/ArticleDate"));

	chemical(pmid, citationElement.selectSingleNode("ChemicalList"));
	supplMesh(pmid, citationElement.selectSingleNode("SupplMeshList"));
	citationSubset(pmid, citationElement.selectNodes("CitationSubset"));
	commentsCorrections(pmid, citationElement.selectSingleNode("CommentsCorrectionsList"));
	geneSymbol(pmid, citationElement.selectSingleNode("GeneSymbolList"));
	meshHeading(pmid, citationElement.selectSingleNode("MeshHeadingList"));
	personalName(pmid, citationElement.selectSingleNode("PersonalNameSubjectList"));
	otherID(pmid, citationElement.selectNodes("OtherID"));
	otherAbstract(pmid, citationElement.selectNodes("OtherAbstract"));
	keyword(pmid, citationElement.selectSingleNode("KeywordList"));
	spaceFlightMission(pmid, citationElement.selectNodes("SpaceFlightMission"));
	investigator(pmid, citationElement.selectSingleNode("InvestigatorList"));
	generalNote(pmid, citationElement.selectNodes("GeneralNote"));

	if ((++count % 100) == 0) {
	    logger.debug("committing transaction for " + pmid + "....");
	    PreparedStatement commitStmt = conn.prepareStatement("commit transaction");
	    commitStmt.executeUpdate();
	    commitStmt.close();
	}

    }
    
    @SuppressWarnings("unused")
    void article(int pmid, Element citationElement) throws SQLException {
	GregorianCalendar dateCreated = new GregorianCalendar(Integer.parseInt(citationElement.selectSingleNode("DateCreated/Year").getText()),
		Integer.parseInt(citationElement.selectSingleNode("DateCreated/Month").getText()) - 1, Integer.parseInt(citationElement.selectSingleNode(
			"DateCreated/Day").getText()));
	logger.trace("\tDateCreated: " + new java.sql.Date(dateCreated.getTimeInMillis()));
	GregorianCalendar dateCompleted = citationElement.selectSingleNode("DateCompleted") == null ? null : new GregorianCalendar(
		Integer.parseInt(citationElement.selectSingleNode("DateCompleted/Year").getText()), Integer.parseInt(citationElement.selectSingleNode(
			"DateCompleted/Month").getText()) - 1, Integer.parseInt(citationElement.selectSingleNode("DateCompleted/Day").getText()));
	if (dateCompleted != null)
	    logger.trace("\tDateCompleted: " + new java.sql.Date(dateCompleted.getTimeInMillis()));
	GregorianCalendar dateRevised = citationElement.selectSingleNode("DateRevised") == null ? null : new GregorianCalendar(Integer.parseInt(citationElement
		.selectSingleNode("DateRevised/Year").getText()), Integer.parseInt(citationElement.selectSingleNode("DateRevised/Month").getText()) - 1,
		Integer.parseInt(citationElement.selectSingleNode("DateRevised/Day").getText()));
	if (dateRevised != null)
	    logger.trace("\tDateRevised: " + new java.sql.Date(dateRevised.getTimeInMillis()));
	String country = citationElement.selectSingleNode("MedlineJournalInfo/Country") == null ? null : citationElement.selectSingleNode(
		"MedlineJournalInfo/Country").getText();
	logger.trace("\tcountry: " + country);
	String medlineTA = citationElement.selectSingleNode("MedlineJournalInfo/MedlineTA") == null ? null : citationElement.selectSingleNode(
		"MedlineJournalInfo/MedlineTA").getText();
	logger.trace("\tmedlineTA: " + medlineTA);
	String nlmID = citationElement.selectSingleNode("MedlineJournalInfo/NlmUniqueID") == null ? null : citationElement.selectSingleNode(
		"MedlineJournalInfo/NlmUniqueID").getText();
	logger.trace("\tnlmID: " + nlmID);
	String ISSNlinking = citationElement.selectSingleNode("MedlineJournalInfo/ISSNLinking") == null ? null : citationElement.selectSingleNode(
		"MedlineJournalInfo/ISSNLinking").getText();
	logger.trace("\tISSNlinking: " + ISSNlinking);
	int numRefs = citationElement.selectSingleNode("NumberOfReferences") == null ? 0 : Integer.parseInt(citationElement.selectSingleNode(
		"NumberOfReferences").getText());
	logger.trace("\tnumRefs: " + numRefs);
	String status = citationElement.attributeValue("Status");
	logger.trace("\tstatus: " + status);

	// Article elements
	String title = citationElement.selectSingleNode("Article/ArticleTitle") == null ? null : citationElement.selectSingleNode("Article/ArticleTitle")
		.getText();
	logger.trace("\ttitle: " + title);
	int startPage = citationElement.selectSingleNode("Article/Pagination/StartPage") == null ? 0 : Integer.parseInt(citationElement.selectSingleNode(
		"Article/Pagination/StartPage").getText());
	logger.trace("\tstart page: " + startPage);
	int endPage = citationElement.selectSingleNode("Article/Pagination/EndPage") == null ? 0 : Integer.parseInt(citationElement.selectSingleNode(
		"Article/Pagination/EndPage").getText());
	logger.trace("\tend page: " + endPage);
	String pagination = citationElement.selectSingleNode("Article/Pagination/MedlinePgn") == null ? null : citationElement.selectSingleNode(
		"Article/Pagination/MedlinePgn").getText();
	logger.trace("\tpagination: " + pagination);
	String copyright = citationElement.selectSingleNode("Article/Abstract/CopyrightInformation") == null ? null : citationElement.selectSingleNode(
		"Article/Abstract/CopyrightInformation").getText();
	logger.trace("\tcopyright: " + copyright);
	String vernacularTitle = citationElement.selectSingleNode("Article/VernacularTitle") == null ? null : citationElement.selectSingleNode(
		"Article/VernacularTitle").getText();
	logger.trace("\tvernacularTitle: " + vernacularTitle);
	String pubModel = ((Element) citationElement.selectSingleNode("Article")).attributeValue("PubModel");
	logger.trace("\tpubModel: " + pubModel);

	PreparedStatement cntStmt = conn.prepareStatement("select pmid from article where pmid = ?");
	cntStmt.setInt(1, pmid);
	ResultSet rs = cntStmt.executeQuery();
	if (rs.next()) {
	    int thePMID = rs.getInt(1);
	    logger.debug("replacing " + pmid + "...");
	    recordsUpdated++;
	    PreparedStatement delStmt = conn.prepareStatement("delete from article where pmid = ?");
	    delStmt.setInt(1, pmid);
	    delStmt.execute();
	    delStmt.close();
	} else {
	    recordsAdded++;
	}
	cntStmt.close();

	PreparedStatement citeStmt = conn.prepareStatement("insert into article values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	citeStmt.setInt(1, pmid);
	citeStmt.setDate(2, new java.sql.Date(dateCreated.getTimeInMillis()));
	if (dateCompleted == null)
	    citeStmt.setNull(3, java.sql.Types.NULL);
	else
	    citeStmt.setDate(3, new java.sql.Date(dateCompleted.getTimeInMillis()));
	if (dateRevised == null)
	    citeStmt.setNull(4, java.sql.Types.NULL);
	else
	    citeStmt.setDate(4, new java.sql.Date(dateRevised.getTimeInMillis()));
	citeStmt.setString(5, title);
	if (startPage == 0)
	    citeStmt.setNull(6, java.sql.Types.NULL);
	else
	    citeStmt.setInt(6, startPage);
	if (endPage == 0)
	    citeStmt.setNull(7, java.sql.Types.NULL);
	else
	    citeStmt.setInt(7, endPage);
	citeStmt.setString(8, pagination);
	citeStmt.setString(9, copyright);
	citeStmt.setString(10, vernacularTitle);
	citeStmt.setString(11, country);
	citeStmt.setString(12, medlineTA);
	citeStmt.setString(13, nlmID);
	citeStmt.setString(14, ISSNlinking);
	citeStmt.setInt(15, numRefs);
	citeStmt.setString(16, pubModel);
	citeStmt.setString(17, status);
	citeStmt.executeUpdate();
	citeStmt.close();
    }

    void journal(int pmid, Node journalNode) throws SQLException {
	// <!ELEMENT Journal (ISSN?, JournalIssue, Title?, ISOAbbreviation?)>
	// <!ELEMENT JournalIssue (Volume?, Issue?, PubDate)>
	// <!ATTLIST JournalIssue CitedMedium (Internet | Print) #REQUIRED>

	logger.trace("\tjournal: ");
	String ISSN = journalNode.selectSingleNode("ISSN") == null ? null : journalNode.selectSingleNode("ISSN").getText();
	logger.trace("\t\tISSN: " + ISSN);
	String volume = journalNode.selectSingleNode("JournalIssue/Volume") == null ? null : journalNode.selectSingleNode("JournalIssue/Volume").getText();
	logger.trace("\t\tvolume: " + volume);
	String issue = journalNode.selectSingleNode("JournalIssue/Issue") == null ? null : journalNode.selectSingleNode("JournalIssue/Issue").getText();
	logger.trace("\t\tissue: " + issue);
	int pubYear = journalNode.selectSingleNode("JournalIssue/PubDate/Year") == null ? 0 : Integer.parseInt(journalNode.selectSingleNode(
		"JournalIssue/PubDate/Year").getText());
	logger.trace("\t\tpubYear: " + pubYear);
	String pubMonth = journalNode.selectSingleNode("JournalIssue/PubDate/Month") == null ? null : journalNode
		.selectSingleNode("JournalIssue/PubDate/Month").getText();
	logger.trace("\t\tpubMonth: " + pubMonth);
	int pubDay = journalNode.selectSingleNode("JournalIssue/PubDate/Day") == null ? 0 : Integer.parseInt(journalNode.selectSingleNode(
		"JournalIssue/PubDate/Day").getText());
	logger.trace("\t\tpubDay: " + pubDay);
	String pubSeason = journalNode.selectSingleNode("JournalIssue/PubDate/Season") == null ? null : journalNode.selectSingleNode(
		"JournalIssue/PubDate/Season").getText();
	logger.trace("\t\tpubSeason: " + pubSeason);
	String pubMedlineDate = journalNode.selectSingleNode("JournalIssue/PubDate/MedlineDate") == null ? null : journalNode.selectSingleNode(
		"JournalIssue/PubDate/MedlineDate").getText();
	logger.trace("\t\tpubMedlineDate: " + pubMedlineDate);
	String title = journalNode.selectSingleNode("Title") == null ? null : journalNode.selectSingleNode("Title").getText();
	logger.trace("\t\ttitle: " + title);
	String iso = journalNode.selectSingleNode("ISOAbbreviation") == null ? null : journalNode.selectSingleNode("ISOAbbreviation").getText();
	logger.trace("\t\tiso: " + iso);

	PreparedStatement stmt = conn.prepareStatement("insert into journal values (?,?,?,?,?,?,?,?,?,?,?)");
	stmt.setInt(1, pmid);
	stmt.setString(2, ISSN);
	stmt.setString(3, volume);
	stmt.setString(4, issue);
	if (pubMedlineDate == null) {
	    stmt.setInt(5, pubYear);
	    stmt.setString(6, pubMonth);
	    if (pubDay != 0)
		stmt.setInt(7, pubDay);
	    else
		stmt.setNull(7, java.sql.Types.INTEGER);
	} else {
	    stmt.setNull(5, java.sql.Types.INTEGER);
	    stmt.setNull(6, java.sql.Types.CHAR);
	    stmt.setNull(7, java.sql.Types.INTEGER);
	}
	stmt.setString(8, pubSeason);
	stmt.setString(9, pubMedlineDate);
	stmt.setString(10, title);
	stmt.setString(11, iso);
	stmt.executeUpdate();
	stmt.close();
    }

    @SuppressWarnings("unchecked")
    void author(int pmid, Node authorListNode) throws SQLException {
	// <!ELEMENT Author (((LastName, ForeName?, Initials?, Suffix?) |
	// CollectiveName),NameID*)>
	// <!ATTLIST Author ValidYN (Y | N) "Y">
	// <!ELEMENT AuthorList (Author+)>
	// <!ATTLIST AuthorList CompleteYN (Y | N) "Y">
	int seqnum = 1;

	if (authorListNode == null)
	    return;
	ListIterator<Node> authors = authorListNode.selectNodes("Author").listIterator();
	while (authors.hasNext()) {
	    logger.trace("\tauthor " + seqnum + ":");
	    Node authorNode = authors.next();
	    String lastName = authorNode.selectSingleNode("LastName") == null ? null : authorNode.selectSingleNode("LastName").getText();
	    logger.trace("\t\tlast name: " + lastName);
	    String foreName = authorNode.selectSingleNode("ForeName") == null ? null : authorNode.selectSingleNode("ForeName").getText();
	    logger.trace("\t\tfore name: " + foreName);
	    String initials = authorNode.selectSingleNode("Initials") == null ? null : authorNode.selectSingleNode("Initials").getText();
	    logger.trace("\t\tinitals: " + initials);
	    String suffix = authorNode.selectSingleNode("Suffix") == null ? null : authorNode.selectSingleNode("Suffix").getText();
	    logger.trace("\t\tsuffix: " + suffix);
	    String collectiveName = authorNode.selectSingleNode("CollectiveName") == null ? null : authorNode.selectSingleNode("CollectiveName").getText();
	    logger.trace("\t\tcollectiveName: " + collectiveName);
	    String affiliation = authorNode.selectSingleNode("Affiliation") == null ? null : authorNode.selectSingleNode("Affiliation").getText();
	    logger.trace("\taffiliation: " + affiliation);

	    PreparedStatement stmt = conn.prepareStatement("insert into author values (?,?,?,?,?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, lastName);
	    stmt.setString(4, foreName);
	    stmt.setString(5, initials);
	    stmt.setString(6, suffix);
	    stmt.setString(7, collectiveName);
	    stmt.setString(8, affiliation);
	    stmt.executeUpdate();
	    stmt.close();

	    identifier(pmid, seqnum, "author_identifier", authorNode.selectNodes("Identifier"));
	    affiliation(pmid, seqnum, "author_affiliation", authorNode.selectNodes("AffiliationInfo"));

	    seqnum++;
	}
    }

    @SuppressWarnings("unchecked")
    void chemical(int pmid, Node chemicalListNode) throws SQLException {
	// <!ELEMENT Chemical (RegistryNumber,NameOfSubstance)>
	// <!ELEMENT ChemicalList (Chemical+)>
	int seqnum = 1;

	if (chemicalListNode == null)
	    return;
	ListIterator<Node> authors = chemicalListNode.selectNodes("Chemical").listIterator();
	while (authors.hasNext()) {
	    logger.trace("\tchemical " + seqnum + ":");
	    Node chemicalNode = authors.next();
	    String registryNumber = chemicalNode.selectSingleNode("RegistryNumber") == null ? null : chemicalNode.selectSingleNode("RegistryNumber").getText();
	    logger.trace("\t\tregistryNumber: " + registryNumber);
	    Node substanceNode = chemicalNode.selectSingleNode("NameOfSubstance") == null ? null : chemicalNode.selectSingleNode("NameOfSubstance");
	    String substance = substanceNode.getText();
	    logger.trace("\t\tsubstance: " + substance);
	    String identifier = ((Element) substanceNode).attributeValue("Identifier");
	    logger.trace("\t\tsubstance: " + substance);

	    PreparedStatement stmt = conn.prepareStatement("insert into chemical values (?,?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, registryNumber);
	    stmt.setString(4, substance);
	    stmt.setString(4, substance);
	    stmt.setString(5, identifier);
	    stmt.executeUpdate();
	    stmt.close();

	    seqnum++;
	}
    }

    @SuppressWarnings("unchecked")
    void supplMesh(int pmid, Node supplMeshListNode) throws SQLException {
	// <!ELEMENT SupplMeshList (SupplMeshName+)>
	// <!ELEMENT SupplMeshName (#PCDATA)>
	// <!ATTLIST SupplMeshName Type (Disease | Protocol) #REQUIRED>
	int seqnum = 1;

	if (supplMeshListNode == null)
	    return;
	ListIterator<Node> supplMeshs = supplMeshListNode.selectNodes("SupplMeshName").listIterator();
	while (supplMeshs.hasNext()) {
	    logger.trace("\tsupplMesh " + seqnum + ":");
	    Node supplNode = supplMeshs.next();
	    String supplMeshName = supplNode.getText();
	    logger.trace("\t\tsupplMeshName: " + supplMeshName);
	    String type = ((Element) supplNode).attributeValue("Type");
	    logger.trace("\t\ttype: " + type);
	    String identifier = ((Element) supplNode).attributeValue("Identifier");
	    logger.trace("\t\tidentifier: " + identifier);

	    PreparedStatement stmt = conn.prepareStatement("insert into supplemental_mesh values (?,?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, supplMeshName);
	    stmt.setString(4, type);
	    stmt.setString(5, identifier);
	    stmt.executeUpdate();
	    stmt.close();

	    seqnum++;
	}
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    void citationSubset(int pmid, List list) throws SQLException {
	// <!ELEMENT CitationSubset (#PCDATA)>
	int seqnum = 1;

	if (list == null)
	    return;
	ListIterator<Node> subset = list.listIterator();
	while (subset.hasNext()) {
	    logger.trace("\tcitationSubset " + seqnum + ":");
	    Node subsetNode = subset.next();
	    String citationSubset = subsetNode.getText();
	    logger.trace("\t\tcitationSubset: " + citationSubset);

	    PreparedStatement stmt = conn.prepareStatement("insert into citation_subset values (?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, citationSubset);
	    stmt.executeUpdate();
	    stmt.close();

	    seqnum++;
	}
    }

    @SuppressWarnings("unchecked")
    void commentsCorrections(int pmid, Node commentsCorrectionsListNode) throws SQLException {
	// <!ELEMENT CommentsCorrections (RefSource,PMID?,Note?)>
	// <!ATTLIST CommentsCorrections
	// RefType (CommentOn | CommentIn | ErratumIn | ErratumFor |
	// PartialRetractionIn | PartialRetractionOf | RepublishedFrom |
	// RepublishedIn | RetractionOf | RetractionIn | UpdateIn |
	// UpdateOf | SummaryForPatientsIn | OriginalReportIn |
	// ReprintOf | ReprintIn | Cites) #REQUIRED >
	// <!ELEMENT CommentsCorrectionsList (CommentsCorrections+)>
	int seqnum = 1;

	if (commentsCorrectionsListNode == null)
	    return;
	ListIterator<Node> commCorrs = commentsCorrectionsListNode.selectNodes("CommentsCorrections").listIterator();
	while (commCorrs.hasNext()) {
	    logger.trace("\tcommentsCorrection " + seqnum + ":");
	    Node commentNode = commCorrs.next();
	    String refSource = commentNode.selectSingleNode("RefSource") == null ? null : commentNode.selectSingleNode("RefSource").getText();
	    logger.trace("\t\trefSource: " + refSource);
	    int pmid2 = commentNode.selectSingleNode("PMID") == null ? 0 : Integer.parseInt(commentNode.selectSingleNode("PMID").getText().trim());
	    logger.trace("\t\tpmid: " + pmid2);
	    String note = commentNode.selectSingleNode("Note") == null ? null : commentNode.selectSingleNode("Note").getText();
	    logger.trace("\t\tnote: " + note);
	    String type = ((Element) commentNode).attributeValue("RefType");
	    logger.trace("\t\ttype: " + type);

	    PreparedStatement stmt = conn.prepareStatement("insert into comments_corrections values (?,?,?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, type);
	    stmt.setString(4, refSource);
	    if (pmid2 != 0)
		stmt.setInt(5, pmid2);
	    else
		stmt.setNull(5, java.sql.Types.INTEGER);
	    stmt.setString(6, note);
	    stmt.executeUpdate();
	    stmt.close();

	    seqnum++;
	}
    }

    @SuppressWarnings("unchecked")
    void geneSymbol(int pmid, Node geneSymbolListNode) throws SQLException {
	// <!ELEMENT GeneSymbol (#PCDATA)>
	// <!ELEMENT GeneSymbolList (GeneSymbol+)>
	int seqnum = 1;

	if (geneSymbolListNode == null)
	    return;
	ListIterator<Node> supplMeshs = geneSymbolListNode.selectNodes("GeneSymbol").listIterator();
	while (supplMeshs.hasNext()) {
	    logger.trace("\tgene symbol " + seqnum + ":");
	    Node symbolNode = supplMeshs.next();
	    String geneSymbol = symbolNode.getText();
	    logger.trace("\t\tgeneSymbol: " + geneSymbol);

	    PreparedStatement stmt = conn.prepareStatement("insert into gene_symbol values (?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, geneSymbol);
	    stmt.executeUpdate();
	    stmt.close();

	    seqnum++;
	}
    }

    @SuppressWarnings("unchecked")
    void meshHeading(int pmid, Node meshHeadingListNode) throws SQLException {
	// <!ELEMENT MeshHeading (DescriptorName, QualifierName*)>
	// <!ELEMENT MeshHeadingList (MeshHeading+)>
	// <!ELEMENT DescriptorName (#PCDATA)>
	// <!ATTLIST DescriptorName
	// MajorTopicYN (Y | N) "N"
	// Type (Geographic) #IMPLIED>
	// <!ELEMENT QualifierName (#PCDATA)>
	// <!ATTLIST QualifierName MajorTopicYN (Y | N) "N">
	int seqnum = 1;

	if (meshHeadingListNode == null)
	    return;
	ListIterator<Node> meshs = meshHeadingListNode.selectNodes("MeshHeading").listIterator();
	while (meshs.hasNext()) {
	    logger.trace("\tmesh heading " + seqnum + ":");
	    Node meshNode = meshs.next();
	    Node descriptorNode = meshNode.selectSingleNode("DescriptorName");
	    String descriptorName = descriptorNode.getText();
	    logger.trace("\t\tdescriptorName: " + descriptorName);
	    String majorTopic = ((Element) descriptorNode).attributeValue("MajorTopicYN");
	    logger.trace("\t\tmajorTopic: " + majorTopic);
	    String type = ((Element) descriptorNode).attributeValue("Type");
	    logger.trace("\t\ttype: " + type);
	    String identifier = ((Element) descriptorNode).attributeValue("UI");
	    logger.trace("\t\tidentifier: " + identifier);

	    PreparedStatement stmt = conn.prepareStatement("insert into mesh_heading values (?,?,?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, descriptorName);
	    stmt.setBoolean(4, majorTopic.equals('Y'));
	    stmt.setString(5, type);
	    stmt.setString(6, identifier);
	    stmt.executeUpdate();
	    stmt.close();

	    int qnum = 1;
	    ListIterator<Node> qualifiers = meshNode.selectNodes("QualifierName").listIterator();
	    while (qualifiers.hasNext()) {
		Node qualifierNode = qualifiers.next();
		String qualifierName = qualifierNode.getText();
		logger.trace("\t\t\tqualifierName: " + qualifierName);
		String majorQTopic = ((Element) qualifierNode).attributeValue("MajorTopicYN");
		logger.trace("\t\t\tmajorTopic: " + majorQTopic);
		String qIdentifier = ((Element) qualifierNode).attributeValue("Identifier");
		logger.trace("\t\t\tidentifier: " + qIdentifier);

		PreparedStatement qstmt = conn.prepareStatement("insert into mesh_qualifier values (?,?,?,?,?,?)");
		qstmt.setInt(1, pmid);
		qstmt.setInt(2, seqnum);
		qstmt.setInt(3, qnum);
		qstmt.setString(4, qualifierName);
		qstmt.setBoolean(5, majorQTopic.equals('Y'));
		qstmt.setString(6, qIdentifier);
		qstmt.executeUpdate();
		qstmt.close();

		qnum++;
	    }

	    seqnum++;
	}
    }

    @SuppressWarnings("unchecked")
    void personalName(int pmid, Node personalNameListNode) throws SQLException {
	// <!ELEMENT PersonalNameSubject (LastName,ForeName?,
	// Initials?,Suffix?)>
	// <!ELEMENT PersonalNameSubjectList (PersonalNameSubject+)>
	int seqnum = 1;

	if (personalNameListNode == null)
	    return;
	ListIterator<Node> personals = personalNameListNode.selectNodes("PersonalNameSubject").listIterator();
	while (personals.hasNext()) {
	    logger.trace("\tpersonal Name " + seqnum + ":");
	    Node personalNode = personals.next();
	    String lastName = personalNode.selectSingleNode("LastName") == null ? null : personalNode.selectSingleNode("LastName").getText();
	    logger.trace("\t\tlast name: " + lastName);
	    String foreName = personalNode.selectSingleNode("ForeName") == null ? null : personalNode.selectSingleNode("ForeName").getText();
	    logger.trace("\t\tfore name: " + foreName);
	    String initials = personalNode.selectSingleNode("Initials") == null ? null : personalNode.selectSingleNode("Initials").getText();
	    logger.trace("\t\tinitals: " + initials);
	    String suffix = personalNode.selectSingleNode("Suffix") == null ? null : personalNode.selectSingleNode("Suffix").getText();
	    logger.trace("\t\tsuffix: " + suffix);

	    PreparedStatement stmt = conn.prepareStatement("insert into personal_name_subject values (?,?,?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, lastName);
	    stmt.setString(4, foreName);
	    stmt.setString(5, initials);
	    stmt.setString(6, suffix);
	    stmt.executeUpdate();
	    stmt.close();

	    seqnum++;
	}
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    void otherID(int pmid, List list) throws SQLException {
	// <!ELEMENT OtherID (#PCDATA)>
	// <!ATTLIST OtherID Source (NASA | KIE | PIP | POP | ARPL | CPC |
	// IND | CPFH | CLML | NRCBL | NLM) #REQUIRED>
	int seqnum = 1;

	if (list == null)
	    return;
	ListIterator<Node> others = list.listIterator();
	while (others.hasNext()) {
	    logger.trace("\totherID " + seqnum + ":");
	    Node otherNode = others.next();
	    String otherID = otherNode.getText();
	    logger.trace("\t\totherID: " + otherID);
	    String source = ((Element) otherNode).attributeValue("Source");
	    logger.trace("\t\tsource: " + source);

	    PreparedStatement stmt = conn.prepareStatement("insert into other_id values (?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, otherID);
	    stmt.setString(4, source);
	    stmt.executeUpdate();
	    stmt.close();

	    seqnum++;
	}
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    void otherAbstract(int pmid, List list) throws SQLException {
	// <!ELEMENT OtherAbstract (AbstractText+,CopyrightInformation?)>
	// <!ATTLIST OtherAbstract Type (AAMC | AIDS | KIE | PIP |
	// NASA | Publisher) #REQUIRED>
	int seqnum = 1;

	if (list == null)
	    return;
	ListIterator<Node> otherAbstrs = list.listIterator();
	while (otherAbstrs.hasNext()) {
	    logger.trace("\tother abstract " + seqnum + ":");
	    Node otherAbstractNode = otherAbstrs.next();
	    String copyright = otherAbstractNode.selectSingleNode("CopyrightInformation") == null ? null : otherAbstractNode.selectSingleNode(
		    "CopyrightInformation").getText();
	    logger.trace("\t\tcopyright: " + copyright);
	    String type = ((Element) otherAbstractNode).attributeValue("Type");
	    logger.trace("\t\ttype: " + type);

	    PreparedStatement stmt = conn.prepareStatement("insert into other_abstract values (?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, copyright);
	    stmt.setString(4, type);
	    stmt.executeUpdate();
	    stmt.close();

	    otherAbstractText(pmid, seqnum, otherAbstractNode);

	    seqnum++;
	}
    }

    @SuppressWarnings("unchecked")
    void otherAbstractText(int pmid, int seqnum, Node abstractNode) throws SQLException {
	// <!ELEMENT AbstractText (#PCDATA)>
	// <!ATTLIST AbstractText
	// Label CDATA #IMPLIED
	// NlmCategory (UNLABELLED | BACKGROUND | OBJECTIVE | METHODS |
	// RESULTS | CONCLUSIONS) #IMPLIED>
	int tnum = 1;

	if (abstractNode == null)
	    return;
	ListIterator<Node> texts = abstractNode.selectNodes("AbstractText").listIterator();
	while (texts.hasNext()) {
	    logger.trace("\t\tabstractText:");
	    Node textNode = texts.next();
	    String abstractText = textNode.getText();
	    logger.trace("\t\t\tabstractText: " + abstractText);
	    String label = ((Element) textNode).attributeValue("Label");
	    logger.trace("\t\t\tlabel: " + label);
	    String category = ((Element) textNode).attributeValue("NlmCategory");
	    logger.trace("\t\t\tcategory: " + category);

	    PreparedStatement stmt = conn.prepareStatement("insert into other_abstract_text values (?,?,?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setInt(3, tnum);
	    stmt.setString(4, abstractText);
	    stmt.setString(5, label);
	    stmt.setString(6, category);
	    stmt.executeUpdate();
	    stmt.close();

	    tnum++;
	}

    }

    @SuppressWarnings("unchecked")
    void keyword(int pmid, Node keywordListNode) throws SQLException {
	// <!ELEMENT Keyword (#PCDATA)>
	// <!ATTLIST Keyword MajorTopicYN (Y | N) "N">
	// <!ELEMENT KeywordList (Keyword+)>
	// <!ATTLIST KeywordList Owner (NLM | NLM-AUTO | NASA | PIP | KIE |
	// NOTNLM) "NLM">
	int seqnum = 1;

	if (keywordListNode == null)
	    return;
	String owner = ((Element) keywordListNode).attributeValue("Owner");
	logger.trace("\tkeyword owner" + owner);
	ListIterator<Node> keywords = keywordListNode.selectNodes("Keyword").listIterator();
	while (keywords.hasNext()) {
	    logger.trace("\tkeyword " + seqnum + ":");
	    Node keywordNode = keywords.next();
	    String keyword = keywordNode.getText();
	    logger.trace("\t\tkeyword: " + keyword);
	    String majorTopic = ((Element) keywordNode).attributeValue("MajorTopicYN");
	    logger.trace("\t\tmajorTopic: " + majorTopic);

	    PreparedStatement stmt = conn.prepareStatement("insert into keyword values (?,?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, keyword);
	    stmt.setBoolean(4, majorTopic == null ? false : majorTopic.equals('Y'));
	    stmt.setString(5, owner);
	    stmt.executeUpdate();
	    stmt.close();

	    seqnum++;
	}
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    void spaceFlightMission(int pmid, List list) throws SQLException {
	// <!ELEMENT SpaceFlightMission (#PCDATA)>
	int seqnum = 1;

	if (list == null)
	    return;
	ListIterator<Node> flights = list.listIterator();
	while (flights.hasNext()) {
	    logger.trace("\tspaceFlight " + seqnum + ":");
	    Node flightNode = flights.next();
	    String flight = flightNode.getText();
	    logger.trace("\t\tspaceFlight: " + flight);

	    PreparedStatement stmt = conn.prepareStatement("insert into spaceflight_mission values (?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, flight);
	    stmt.executeUpdate();
	    stmt.close();

	    seqnum++;
	}
    }

    @SuppressWarnings("unchecked")
    void investigator(int pmid, Node investigatorListNode) throws SQLException {
	// <!ELEMENT Investigator (LastName,ForeName?,
	// Initials?,Suffix?,NameID*,Affiliation?)>
	// <!ATTLIST Investigator ValidYN (Y | N) "Y">
	// <!ELEMENT InvestigatorList (Investigator+)>
	int seqnum = 1;

	if (investigatorListNode == null)
	    return;
	ListIterator<Node> investigators = investigatorListNode.selectNodes("Investigator").listIterator();
	while (investigators.hasNext()) {
	    logger.trace("\tinvestigator " + seqnum + ":");
	    Node investigatorNode = investigators.next();
	    String lastName = investigatorNode.selectSingleNode("LastName") == null ? null : investigatorNode.selectSingleNode("LastName").getText();
	    logger.trace("\t\tlast name: " + lastName);
	    String foreName = investigatorNode.selectSingleNode("ForeName") == null ? null : investigatorNode.selectSingleNode("ForeName").getText();
	    logger.trace("\t\tfore name: " + foreName);
	    String initials = investigatorNode.selectSingleNode("Initials") == null ? null : investigatorNode.selectSingleNode("Initials").getText();
	    logger.trace("\t\tinitals: " + initials);
	    String suffix = investigatorNode.selectSingleNode("Suffix") == null ? null : investigatorNode.selectSingleNode("Suffix").getText();
	    logger.trace("\t\tsuffix: " + suffix);
	    String affiliation = investigatorNode.selectSingleNode("Affiliation") == null ? null : investigatorNode.selectSingleNode("Affiliation").getText();
	    logger.trace("\t\taffiliation: " + affiliation);

	    PreparedStatement stmt = conn.prepareStatement("insert into investigator values (?,?,?,?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, lastName);
	    stmt.setString(4, foreName);
	    stmt.setString(5, initials);
	    stmt.setString(6, suffix);
	    stmt.setString(7, affiliation);
	    stmt.executeUpdate();
	    stmt.close();

	    identifier(pmid, seqnum, "investigator_identifier", investigatorNode.selectNodes("Identifier"));
	    affiliation(pmid, seqnum, "investigator_affiliation", investigatorNode.selectNodes("AffiliationInfo"));

	    seqnum++;
	}
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    void generalNote(int pmid, List list) throws SQLException {
	// <!ELEMENT GeneralNote (#PCDATA)>
	// <!ATTLIST GeneralNote Owner (NLM | NASA | PIP | KIE | HSR | HMD)
	// "NLM">
	int seqnum = 1;

	if (list == null)
	    return;
	ListIterator<Node> notes = list.listIterator();
	while (notes.hasNext()) {
	    logger.trace("\tgeneral note " + seqnum + ":");
	    Node noteNode = notes.next();
	    String note = noteNode.getText();
	    logger.trace("\t\tnote: " + note);
	    String owner = ((Element) noteNode).attributeValue("Owner");
	    logger.trace("\t\towner: " + owner);

	    PreparedStatement stmt = conn.prepareStatement("insert into general_note values (?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, note);
	    stmt.setString(4, owner);
	    stmt.executeUpdate();
	    stmt.close();

	    seqnum++;
	}
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    void elocationID(int pmid, List list) throws SQLException {
	// <!ELEMENT ELocationID (#PCDATA)>
	// <!ATTLIST ELocationID EIdType (doi | pii) #REQUIRED
	// ValidYN (Y | N) "Y">
	int seqnum = 1;

	if (list == null)
	    return;
	ListIterator<Node> elocs = list.listIterator();
	while (elocs.hasNext()) {
	    logger.trace("\telocation " + seqnum + ":");
	    Node elocNode = elocs.next();
	    String eloc = elocNode.getText();
	    logger.trace("\t\teloc: " + eloc);
	    String type = ((Element) elocNode).attributeValue("EIdType");
	    logger.trace("\t\ttype: " + type);

	    PreparedStatement stmt = conn.prepareStatement("insert into elocation values (?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, eloc);
	    stmt.setString(4, type);
	    stmt.executeUpdate();
	    stmt.close();

	    seqnum++;
	}
    }

    @SuppressWarnings("unchecked")
    void abstr(int pmid, Node abstrNode) throws SQLException {
	// <!ELEMENT Abstract (AbstractText+,CopyrightInformation?)>
	int seqnum = 1;

	if (abstrNode == null)
	    return;
	ListIterator<Node> abstrs = abstrNode.selectNodes("AbstractText").listIterator();
	while (abstrs.hasNext()) {
	    logger.trace("\tabstract " + seqnum + ":");
	    Node abstractNode = abstrs.next();
	    abstractText(pmid, seqnum, abstractNode);
	    seqnum++;
	}
    }

    void abstractText(int pmid, int seqnum, Node abstractNode) throws SQLException {
	// <!ELEMENT AbstractText (#PCDATA)>
	// <!ATTLIST AbstractText
	// Label CDATA #IMPLIED
	// NlmCategory (UNLABELLED | BACKGROUND | OBJECTIVE | METHODS |
	// RESULTS | CONCLUSIONS) #IMPLIED>

	logger.trace("\tabstractText:");
	String abstractText = abstractNode.getText();
	logger.trace("\t\tabstractText: " + abstractText);
	String label = ((Element) abstractNode).attributeValue("Label");
	logger.trace("\t\tlabel: " + label);
	String category = ((Element) abstractNode).attributeValue("NlmCategory");
	logger.trace("\t\tcategory: " + category);

	PreparedStatement stmt = conn.prepareStatement("insert into abstr values (?,?,?,?,?)");
	stmt.setInt(1, pmid);
	stmt.setInt(2, seqnum);
	stmt.setString(3, abstractText);
	stmt.setString(4, label);
	stmt.setString(5, category);
	stmt.executeUpdate();
	stmt.close();

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    void language(int pmid, List list) throws SQLException {
	// <!ELEMENT Language (#PCDATA)>
	int seqnum = 1;

	if (list == null)
	    return;
	ListIterator<Node> langs = list.listIterator();
	while (langs.hasNext()) {
	    logger.trace("\tlanguage " + seqnum + ":");
	    Node langNode = langs.next();
	    String language = langNode.getText();
	    logger.trace("\t\tlanguage: " + language);

	    PreparedStatement stmt = conn.prepareStatement("insert into language values (?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, language);
	    stmt.executeUpdate();
	    stmt.close();

	    seqnum++;
	}
    }

    @SuppressWarnings("unchecked")
    void dataBank(int pmid, Node dataBankListNode) throws SQLException {
	// <!ELEMENT DataBank (DataBankName, AccessionNumberList?)>
	// <!ELEMENT DataBankList (DataBank+)>
	// <!ATTLIST DataBankList CompleteYN (Y | N) "Y">
	// <!ELEMENT DataBankName (#PCDATA)>
	// <!ELEMENT AccessionNumber (#PCDATA)>
	// <!ELEMENT AccessionNumberList (AccessionNumber+)>
	int seqnum = 1;

	if (dataBankListNode == null)
	    return;
	ListIterator<Node> banks = dataBankListNode.selectNodes("DataBank").listIterator();
	while (banks.hasNext()) {
	    logger.trace("\tdata bank " + seqnum + ":");
	    Node dataBankNode = banks.next();
	    String dataBankName = dataBankNode.selectSingleNode("DataBankName") == null ? null : dataBankNode.selectSingleNode("DataBankName").getText();
	    logger.trace("\t\tdataBankName: " + dataBankName);

	    PreparedStatement stmt = conn.prepareStatement("insert into data_bank values (?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, dataBankName);
	    stmt.executeUpdate();
	    stmt.close();

	    int accSeq = 1;
	    ListIterator<Element> acs = dataBankNode.selectNodes("AccessionNumberList/AccessionNumber").listIterator();
	    while (acs.hasNext()) {
		String accNum = acs.next().getText();
		logger.trace("\t\t\taccNum: " + accNum);

		PreparedStatement astmt = conn.prepareStatement("insert into accession values (?,?,?,?)");
		astmt.setInt(1, pmid);
		astmt.setInt(2, seqnum);
		astmt.setInt(3, accSeq);
		astmt.setString(4, accNum);
		astmt.executeUpdate();
		astmt.close();

		accSeq++;
	    }
	    seqnum++;
	}
    }

    @SuppressWarnings("unchecked")
    void grant(int pmid, Node grantListNode) throws SQLException {
	// <!ELEMENT Grant (GrantID?, Acronym?, Agency, Country)>
	// <!ELEMENT GrantID (#PCDATA)>
	// <!ELEMENT GrantList (Grant+)>
	// <!ATTLIST GrantList CompleteYN (Y | N) "Y">
	int seqnum = 1;

	if (grantListNode == null)
	    return;
	ListIterator<Node> grants = grantListNode.selectNodes("Grant").listIterator();
	while (grants.hasNext()) {
	    logger.trace("\tgrant " + seqnum + ":");
	    Node grantNode = grants.next();
	    String grantID = grantNode.selectSingleNode("GrantID") == null ? null : grantNode.selectSingleNode("GrantID").getText();
	    logger.trace("\t\tgrantID: " + grantID);
	    String acronym = grantNode.selectSingleNode("Acronym") == null ? null : grantNode.selectSingleNode("Acronym").getText();
	    logger.trace("\t\tacronym: " + acronym);
	    String agency = grantNode.selectSingleNode("Agency") == null ? null : grantNode.selectSingleNode("Agency").getText();
	    logger.trace("\t\tagency: " + agency);
	    String country = grantNode.selectSingleNode("Country") == null ? null : grantNode.selectSingleNode("Country").getText();
	    logger.trace("\t\tcountry: " + country);

	    PreparedStatement stmt = conn.prepareStatement("insert into medline17.grant values (?,?,?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, grantID);
	    stmt.setString(4, acronym);
	    stmt.setString(5, agency);
	    stmt.setString(6, country);
	    stmt.executeUpdate();
	    stmt.close();

	    seqnum++;
	}
    }

    @SuppressWarnings("unchecked")
    void publicationType(int pmid, Node publicationTypeListNode) throws SQLException {
	// <!ELEMENT PublicationType (#PCDATA)>
	// <!ELEMENT PublicationTypeList (PublicationType+)>
	int seqnum = 1;

	if (publicationTypeListNode == null)
	    return;
	ListIterator<Node> pubTypes = publicationTypeListNode.selectNodes("PublicationType").listIterator();
	while (pubTypes.hasNext()) {
	    logger.trace("\tpublication type " + seqnum + ":");
	    Node grantNode = pubTypes.next();
	    String pubType = grantNode.getText();
	    logger.trace("\t\tpubType: " + pubType);
	    String identifier = ((Element) grantNode).attributeValue("UI");
	    logger.trace("\t\tidentifer: " + identifier);

	    PreparedStatement stmt = conn.prepareStatement("insert into publication_type values (?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setString(3, pubType);
	    stmt.setString(4, identifier);
	    stmt.executeUpdate();
	    stmt.close();

	    seqnum++;
	}
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    void identifier(int pmid, int seqnum, String tableName, List list) throws SQLException {
	// <!ELEMENT Identifier (#PCDATA)>
	// <!ATTLIST Identifier
	// Source CDATA #REQUIRED >
	int inum = 1;

	if (list == null)
	    return;
	ListIterator<Node> names = list.listIterator();
	while (names.hasNext()) {
	    logger.debug("\tcitation pmid: " + pmid);
	    logger.debug("\t\tidentifier " + seqnum + ":");
	    Node nameNode = names.next();
	    String identifier = nameNode.getText();
	    logger.debug("\t\t\tidentifier: " + identifier);
	    String source = ((Element) nameNode).attributeValue("Source");
	    logger.debug("\t\t\tsource: " + source);

	    PreparedStatement stmt = conn.prepareStatement("insert into " + tableName + " values (?,?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setInt(3, inum);
	    stmt.setString(4, source);
	    stmt.setString(5, identifier);
	    stmt.executeUpdate();
	    stmt.close();

	    inum++;
	}
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    void affiliation(int pmid, int seqnum, String tableName, List list) throws SQLException {
	// <!ELEMENT NameID (#PCDATA)>
	// <!ATTLIST NameID
	// Source CDATA #REQUIRED >
	int anum = 1;

	if (list == null)
	    return;
	ListIterator<Node> affiliations = list.listIterator();
	while (affiliations.hasNext()) {
	    logger.debug("\tcitation pmid: " + pmid);
	    logger.debug("\t\taffiliation " + seqnum + ":");
	    Node affiliationListNode = affiliations.next();
	    Node affiliationNode = affiliationListNode.selectSingleNode("Affiliation");
	    String affiliation = affiliationNode.getText();
	    logger.debug("\t\t\taffiliation: " + affiliation);

	    PreparedStatement stmt = conn.prepareStatement("insert into " + tableName + " values (?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setInt(3, anum);
	    stmt.setString(4, affiliation);
	    stmt.executeUpdate();
	    stmt.close();
	    
	    affiliationIdentifier(pmid, seqnum, anum, tableName+"_identifier", affiliationListNode.selectNodes("Identifier"));

	    anum++;
	}
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    void affiliationIdentifier(int pmid, int seqnum, int anum, String tableName, List list) throws SQLException {
	// <!ELEMENT Identifier (#PCDATA)>
	// <!ATTLIST Identifier
	// Source CDATA #REQUIRED >
	int inum = 1;

	if (list == null)
	    return;
	ListIterator<Node> names = list.listIterator();
	while (names.hasNext()) {
	    logger.debug("\t\tidentifier " + seqnum + ":");
	    Node nameNode = names.next();
	    String identifier = nameNode.getText();
	    logger.debug("\t\t\tidentifier: " + identifier);
	    String source = ((Element) nameNode).attributeValue("Source");
	    logger.debug("\t\t\tsource: " + source);

	    PreparedStatement stmt = conn.prepareStatement("insert into " + tableName + " values (?,?,?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setInt(3, anum);
	    stmt.setInt(4, inum);
	    stmt.setString(5, source);
	    stmt.setString(6, identifier);
	    stmt.executeUpdate();
	    stmt.close();

	    inum++;
	}
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    void articleDate(int pmid, List list) throws SQLException {
	// <!ELEMENT ArticleDate (Year,Month,Day)>
	// <!ATTLIST ArticleDate DateType CDATA #FIXED "Electronic">
	int seqnum = 1;

	if (list == null)
	    return;
	ListIterator<Node> dates = list.listIterator();
	while (dates.hasNext()) {
	    logger.trace("\tarticleDate " + seqnum + ":");
	    Node articleDateNode = dates.next();
	    int year = articleDateNode.selectSingleNode("Year") == null ? 0 : Integer.parseInt(articleDateNode.selectSingleNode("Year").getText());
	    logger.trace("\t\tyear: " + year);
	    int month = articleDateNode.selectSingleNode("Month") == null ? 0 : Integer.parseInt(articleDateNode.selectSingleNode("Month").getText());
	    logger.trace("\t\tmonth: " + month);
	    int day = articleDateNode.selectSingleNode("Day") == null ? 0 : Integer.parseInt(articleDateNode.selectSingleNode("Day").getText());
	    logger.trace("\t\tday: " + day);
	    String dateType = ((Element) articleDateNode).attributeValue("DateType");
	    logger.trace("\t\tdateType: " + dateType);

	    PreparedStatement stmt = conn.prepareStatement("insert into article_date values (?,?,?,?,?,?)");
	    stmt.setInt(1, pmid);
	    stmt.setInt(2, seqnum);
	    stmt.setInt(3, year);
	    stmt.setInt(4, month);
	    stmt.setInt(5, day);
	    stmt.setString(6, dateType);
	    stmt.executeUpdate();
	    stmt.close();

	    seqnum++;
	}
    }

    static void materializeAuthorView() throws SQLException {
	// refresh author uid-pmid cache with new data
	execute("delete from author_cache10");
	execute("analyze medline17.author");
	execute("analyze medline17.journal");
	execute("update journal set pub_day=28 where (pub_month='Feb' or pub_month='02') and pub_day > 28");
	execute("update journal set pub_day=30 where (pub_month='Sep' or pub_month='09' or pub_month='Apr' or pub_month='04' or pub_month='Jun' or pub_month='06' or pub_month='Nov' or pub_month='11') and pub_day > 30");
	execute("insert into author_cache10 select authors.id,(pub_year||'-'||pub_month||'-'||pub_day)::date,medline17.author.pmid"
		+ " from loki.authors,medline17.author,medline17.journal"
		+ " where authors.lastname=medline17.author.last_name and authors.forename=medline17.author.fore_name and medline17.author.pmid=medline17.journal.pmid");
	execute("update author_cache10 set pubdate = (pub_month||' 01 '||pub_year)::date from medline17.journal where journal.pmid=author_cache10.pmid and pubdate is null and pub_month is not null");
	execute("update author_cache10 set pubdate = ('Jan 01 '||pub_year)::date from medline17.journal where journal.pmid=author_cache10.pmid and pubdate is null");
	execute("analyze author_cache10");

	PreparedStatement cntStmt = conn.prepareStatement("select count(*) from author_cache10");
	ResultSet rs = cntStmt.executeQuery();
	while (rs.next()) {
	    logger.info("\nauthor cache instance count: " + rs.getInt(1));
	}

	// TODO The following two parameter adjustments make the following query
	// appropriately use their indices in scans, rather than sequentially
	// scanning. We need to establish a generic means of doing this.
	execute("set session enable_seqscan = off");
	execute("set session random_page_cost = 1");

	// refresh author statistics with new data
	execute("truncate medline17.author_count");
	execute("insert into medline17.author_count select last_name,fore_name,count(*) from medline17.author where fore_name is not null group by 1,2");
	execute("analyze medline17.author_count");

	// refresh MeSH terminology and tf*idf statistics with new data
	execute("delete from loki.mesh");
	execute("insert into loki.mesh select id, descriptor_name as term from loki.publication natural join medline17.mesh_heading where descriptor_name is not null");
	execute("insert into loki.mesh select id, descriptor_name as term from loki.author_cache10 natural join medline17.mesh_heading where descriptor_name is not null and not exists (select id from publication where author_cache10.id=publication.id and pmid > 0)");

	// TODO Now reset the parameters to their defaults.
	execute("set session enable_seqscan = on");
	execute("set session random_page_cost = 4");

	execute("analyze loki.mesh");
	execute("delete from loki.mesh_lexicon");
	execute("insert into loki.mesh_lexicon select term, count(distinct id), count(*) from loki.mesh where term is not null group by 1");
	execute("analyze loki.mesh_lexicon");
	execute("delete from loki.mesh_frequency");
	execute("insert into loki.mesh_frequency select id, term, count(*) from loki.mesh where term is not null group by 1,2");
	execute("analyze loki.mesh_frequency");

	execute("end transaction");
    }

    static void execute(String statement) throws SQLException {
	logger.info("executing " + statement + "...");
	PreparedStatement stmt = conn.prepareStatement(statement);
	stmt.executeUpdate();
	stmt.close();
    }

}
