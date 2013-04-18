/*
 * Created on Feb 26, 2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.uiowa.medline;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;
import java.util.zip.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class Loader extends DefaultHandler {
	
    static Properties prop_file = PropertyLoader.loadProperties("loader");
	
    static enum State { NONE,TITLE,MEDLINEID,ABSTRACT,JOURNAL,LASTNAME,FORENAME,AFFILIATION,
                        PAGENUM,DATE,VOLUME,ISSUE,MESH,MESHdesc,MESHqual,MESHsub,KEYWORD,
                        COLLNAME,PUBDATE,PUBYEAR,PUBMONTH,PUBDAY,PUBSEASON,PUBMEDDATE,
                        CHEMICAL,CHEMNUM,CHEMNAME,PMID,GRANT,COMMENTON, DateCreated, DateCompleted, DateRevised, Journal }

    static Logger logger = Logger.getLogger(Loader.class);

    public static boolean useLocalHost = false;
//    public static final String localHostName = "192.168.2.24";
    public static final String localHostName = "localhost";
//    public static final String networkHostName = "192.168.0.2";
    public static final String networkHostName = "neuromancer.icts.uiowa.edu";
    static Connection conn = null;
    
    static State mode = State.NONE;
    static boolean inPubDate = false;
    static boolean inMesh = false;
    static boolean inChemical = false;
    static boolean inGrant = false;
    static boolean inAuthor = false;
    static boolean inInvestigator = false;
    static boolean inOtherAbstract = false;
    static boolean afterArticle = false;
    static boolean debug = false;
    static boolean verbose = false;
    static boolean gverbose = true; //************
    static boolean terse = false;
    static boolean load = true;
    static boolean reload = false;
    static boolean doThisOne = false;
    
    StringBuffer buffer = new StringBuffer();
    int count = 0;
    int id = 0;
    int pmid = 0;
    int ref_pmid = 0;
    GregorianCalendar dateCreated = null;
    GregorianCalendar dateCompleted = null;
    GregorianCalendar dateRevised = null;
    GregorianCalendar articleDate = null;
    String issn = null;
    String journal = null;
    String isoAbbrev = null;
    String title = null;
    String articleTitle = null;
    String year = null;
    String month = null;
    String day = null;
    String season = null;
    String medlineDate = null;
    String volume = null;
    String issue = null;
    String startPage = null;
    String endPage = null;
    String medlinePgn = null;
    String eLocationID = null;
    String articleAbstr = null;
    String otherAbstr = null;
    String articleCopyright = null;
    String otherCopyright = null;
    String lastName = null;
    String foreName = null;
    String initials = null;
    String suffix = null;
    String collName = null;
    String articleAffiliation = null;
    String authorAffiliation = null;
    String investigatorAffiliation = null;
    String language = null;
    String dataBankName = null;
    String accNumber = null;
    String grantID = null;
    String grantAcronym = null;
    String grantAgency = null;
    String grantCountry = null;
    String publType = null;
    String vernacularTitle = null;
    String medlineTA = null;
    String nlmUniqueID = null;
    String issnLinking = null;
    String citationSubset = null;
    String chemNum = null;
    String chemName = null;
    String refSource = null;
    String note = null;
    String geneSymbol = null;
    String refCount = null;
    String meshName = null;
    String meshDesc = null;
    String meshQual = null;
    boolean majorMESH = false;
    boolean majorMESHqual = false;
    String otherID = null;
    String otherIDSource = null;
    String otherAbstrType = null;
    String spaceFlight = null;
    String generalNote = null;
    String keyword = null;
    boolean majorMESHsub = false;
    boolean majorKeyword = false;
    String collectionTitle = null;
    String publisher = null;
    String refType = null;
    
    int authorSeqNum = 0;
    int dataBankSeqNum = 0;
    int accessionSeqNum = 0;
    int grantSeqNum = 0;
    int geneSeqNum = 0;
    int chemicalSeqNum = 0;
    int keywordSeqNum = 0;
    int meshSeqNum = 0;
    int qualSeqNum = 0;
    int investigatorSeqNum = 0;
    int pnsSeqNum = 0;
    int elocSeqNum = 0;
    int artDateSeqNum = 0;
    int langSeqNum = 0;
    int citeSeqNum = 0;
    int otherIDSeqNum = 0;
    int otherAbstrSeqNum = 0;
    int spaceSeqNum = 0;
    int noteSeqNum = 0;
    int pubTypeSeqNum = 0;
    int commCorrSeqNum = 0;

    static XMLReader xr = null;
    static final String host = networkHostName;
    
    static int recordCount = 0;
    static int replacementCount = 0;

   public static void main (String args[]) throws Exception {
       PropertyConfigurator.configure(args[0]);

       
       if (load || reload) {
            String db_user = prop_file.getProperty("db.user.name", "eichmann");
            logger.debug("Database User Name: " + db_user);
            String db_pass = prop_file.getProperty("db.user.password", "translational");

            String use_ssl = prop_file.getProperty("db.use.ssl", "true");
            logger.debug("Database SSL: " + use_ssl);

            String db_url = prop_file.getProperty("db.url", "jdbc:postgresql://" + (useLocalHost ? localHostName : networkHostName) + "/bioinformatics");
            logger.debug("Database URL: " + db_url);

            Class.forName("org.postgresql.Driver");
            Properties props = new Properties();
            props.setProperty("user", db_user);
            props.setProperty("password", db_pass);
            if (use_ssl.toLowerCase().equals("true")) {
                props.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
                props.setProperty("ssl", use_ssl);
            }
            conn = DriverManager.getConnection(db_url, props);
            conn.setAutoCommit(false);

            PreparedStatement pathStmt = conn.prepareStatement("set search_path to medline10,loki");
            pathStmt.executeUpdate();
            pathStmt.close();

            pathStmt = conn.prepareStatement("set constraints all deferred");
            pathStmt.executeUpdate();
            pathStmt.close();
        }

        xr = XMLReaderFactory.createXMLReader();
        Loader handler = new Loader();
        xr.setContentHandler(handler);
        xr.setErrorHandler(handler);

        // read files from stdin
        BufferedReader IODesc = new BufferedReader(new InputStreamReader(System.in));
        String current = null;
        while ((current = IODesc.readLine()) != null) {
            processFile(current.trim());
        }
        
        handler.materializeAuthorView();
    }
    
    static void processFile(String file) throws Exception {
        logger.info("processing : " + file);
        BufferedReader IODesc = null;
        if (file.endsWith(".z") || file.endsWith(".gz")) {
            IODesc = new BufferedReader(new InputStreamReader(new GZIPInputStream((new URL(file)).openConnection().getInputStream())), 200000);
        } else
            IODesc = new BufferedReader(new InputStreamReader((new URL(file)).openConnection().getInputStream()), 200000);

        //FileReader r = new FileReader(args[i]);
        xr.parse(new InputSource(IODesc));
        IODesc.close();
    }


    public Loader () {
        super();
    }


    ////////////////////////////////////////////////////////////////////
    // Event handlers.
    ////////////////////////////////////////////////////////////////////


    public void startDocument() {
        logger.trace("Start document");
        if (load || reload)
            try {
                PreparedStatement pathStmt = conn.prepareStatement("begin transaction");
                pathStmt.executeUpdate();
                pathStmt.close();

                pathStmt = conn.prepareStatement("set constraints all deferred");
                pathStmt.executeUpdate();
                pathStmt.close();
            } catch (Exception e) {
                System.out.println("Exception: " + e);
                e.printStackTrace();
            }

    }


    public void endDocument () {
        logger.info("Records added: " + (recordCount - replacementCount));
        logger.info("Records replaced: " + replacementCount);
        recordCount = 0;
        replacementCount = 0;
        logger.trace("End document");
        if (load || reload) try {
            PreparedStatement commitStmt = conn.prepareStatement("end transaction");
            commitStmt.executeUpdate();
            commitStmt.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
    }


    public void startElement (String uri, String name, String qName, Attributes atts) {
        logger.trace("Start element: " + (uri.length() > 0 ? "{" + uri + "} " : "") + name);
        if (qName.equals("MedlineCitation")) {
            logger.debug("------------");
        } else if (qName.equals("PMID")) {
            mode = State.PMID;
        } else if (qName.equals("DateCreated")) {
            mode = State.DateCreated;
        } else if (qName.equals("DateCompleted")) {
            mode = State.DateCompleted;
        } else if (qName.equals("DateRevised")) {
            mode = State.DateRevised;
        } else if (qName.equals("Journal")) {
            mode = State.Journal;
// edit point        
        } else if (qName.equals("MedlineID")) {
            mode = State.MEDLINEID;
        } else if (qName.equals("MedlineTA")) {
            mode = State.JOURNAL;
        } else if (qName.equals("AbstractText")) {
            mode = State.ABSTRACT;
        } else if (qName.equals("LastName")) {
            mode = State.LASTNAME;
        } else if (qName.equals("ForeName")) {
            mode = State.FORENAME;
        } else if (qName.equals("CollectiveName")) {
            mode = State.COLLNAME;
        } else if (qName.equals("Affiliation")) {
            mode = State.AFFILIATION;
        } else if (qName.equals("MedlinePgn")) {
            mode = State.PAGENUM;
        } else if (qName.equals("MedlineDate")) {
            mode = State.DATE;
        } else if (qName.equals("MeshHeading")) {
            inMesh = true;
        } else if (qName.equals("Author")) {
            inAuthor = true;
        } else if (qName.equals("Investigator")) {
            inInvestigator = true;
        } else if (qName.equals("OtherID")) {
            otherIDSource = atts.getValue(0);
        } else if (qName.equals("OtherAbstract")) {
            inOtherAbstract = true;
            otherAbstrType = atts.getValue(0);
        } else if (inMesh && qName.equals("Descriptor")) {
            majorMESH = atts.getValue(0).equals("Y");
            logger.trace("Attributes: " + atts.getValue(0));
            mode = State.MESHdesc;
        } else if (inMesh && qName.equals("DescriptorName")) {
            majorMESH = atts.getValue(0).equals("Y");
            logger.trace("Attributes: " + atts.getValue(0));
            mode = State.MESH;
        } else if (inMesh && qName.equals("QualifierName")) {
            majorMESHqual = atts.getValue(0).equals("Y");
            logger.trace("Attributes: " + atts.getValue(0));
            mode = State.MESHqual;
        } else if (inMesh && qName.equals("SubHeading")) {
            majorMESHsub = atts.getValue(0).equals("Y");
            logger.trace("Attributes: " + atts.getValue(0));
            mode = State.MESHsub;
        } else if (qName.equals("Chemical")) {
            inChemical = true;
        } else if (inChemical && qName.equals("RegistryNumber")) {
            mode = State.CHEMNUM;
        } else if (inChemical && qName.equals("NameOfSubstance")) {
            mode = State.CHEMNAME;
        } else if (qName.equals("Keyword")) {
            majorKeyword = atts.getValue(0).equals("Y");
            logger.trace("Attributes: " + atts.getValue(0));
            mode = State.KEYWORD;
        } else if (qName.equals("Grant")) {
            logger.trace("Attributes: " + atts.getValue(0));
            inGrant = true;
            mode = State.GRANT;
        } else if (inGrant && qName.equals("GrantID")) {
            logger.trace("Attributes: " + atts.getValue(0));
            inGrant = true;
            mode = State.GRANT;
        } else if (inGrant && qName.equals("Acronym")) {
            logger.trace("Attributes: " + atts.getValue(0));
            inGrant = true;
            mode = State.GRANT;
        } else if (inGrant && qName.equals("Agency")) {
            logger.trace("Attributes: " + atts.getValue(0));
            inGrant = true;
            mode = State.GRANT;
        } else if (qName.equals("CommentsCorrections")) {
            refType = atts.getValue(0);
            logger.trace("Attributes: " + atts.getValue(0));
        } else
            mode =State. NONE;
    }


    public void endElement (String uri, String name, String qName) {
        logger.trace("End element:  " + (uri.length() > 0 ? "{" + uri + "} " : "") + name);
        if (qName.equals("MedlineCitation")) {
            recordCount++;
            processCitation();
        } else if (qName.equals("PMID")) {
            if (afterArticle)
                ref_pmid = Integer.parseInt(buffer.toString());
            else {
				pmid = Integer.parseInt(buffer.toString());
				if (load || doThisOne)
					try {
						int existingID = 0;
						PreparedStatement citeStmt = null;
						PreparedStatement cntStmt = conn.prepareStatement("select pmid from article where pmid = ?");
						cntStmt.setInt(1, pmid);
						ResultSet rs = cntStmt.executeQuery();
						while (rs.next()) {
							existingID = rs.getInt(1);
							logger.debug("replacing " + pmid + "...");
							replacementCount++;
							deleteCitation(existingID);
						}

						cntStmt.close();
					} catch (Exception e) {
						System.out.println("Exception: " + e);
						e.printStackTrace();
					}
			}
        } else if (qName.equals("Year")) {
            year = buffer.toString();
        } else if (qName.equals("Month")) {
            month = buffer.toString();
        } else if (qName.equals("Day")) {
            day = buffer.toString();
        } else if (qName.equals("DateCreated")) {
            dateCreated = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
            logger.debug("\t\t\tDateCreated: " + new java.sql.Date(dateCreated.getTimeInMillis()));
            year = null;
            month = null;
            day = null;
        } else if (qName.equals("DateCompleted")) {
            dateCompleted = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
            logger.debug("\t\t\tDateCompleted: " + new java.sql.Date(dateCompleted.getTimeInMillis()));
            year = null;
            month = null;
            day = null;
        } else if (qName.equals("DateRevised")) {
            dateRevised = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
            logger.debug("\t\t\tDateRevised: " + new java.sql.Date(dateRevised.getTimeInMillis()));
            year = null;
            month = null;
            day = null;
        } else if (qName.equals("ISSN")) {
            issn = buffer.toString();
        } else if (qName.equals("Volume")) {
            volume = buffer.toString();
        } else if (qName.equals("Issue")) {
            issue = buffer.toString();
        } else if (qName.equals("Season")) {
            season = buffer.toString();
        } else if (qName.equals("Issue")) {
            issue = buffer.toString();
        } else if (qName.equals("MedlineDate")) {
            medlineDate = buffer.toString();
        } else if (qName.equals("Title")) {
            journal = buffer.toString();
        } else if (qName.equals("ISOAbbreviation")) {
            isoAbbrev = buffer.toString();
        } else if (qName.equals("Journal")) {
            logger.debug("\t\t\tJournal issn : " + issn + " volume : " + volume + " issue : " + issue + " year : " + year + " month : " + month + " day : " + day + " season : " + season + " medlineDate : " + medlineDate + " journal : " + journal + " isoAbbrev : " + isoAbbrev);
            processJournal();
        } else if (qName.equals("CollectionTitle")) {
            collectionTitle = buffer.toString();
        } else if (qName.equals("Publisher")) {
            publisher = buffer.toString();
        } else if (qName.equals("ArticleTitle")) {
            articleTitle = buffer.toString();
        } else if (qName.equals("StartPage")) {
            startPage = buffer.toString();
        } else if (qName.equals("EndPage")) {
            endPage = buffer.toString();
        } else if (qName.equals("MedlinePgn")) {
            medlinePgn = buffer.toString();
        } else if (qName.equals("Pagination")) {
            logger.debug("\t\t\tPagination startPage : " + startPage + " endPage : " + endPage + " medlinePgn : " + medlinePgn);
        } else if (qName.equals("ELocationID")) {
            eLocationID = buffer.toString();
            logger.debug("\t\t\tELocationID : " + eLocationID);
            processELocation();
        } else if (qName.equals("AbstractText")) {
            if (inOtherAbstract) {
                otherAbstr = buffer.toString();
            } else {
                articleAbstr = buffer.toString();
            }
        } else if (qName.equals("CopyrightInformation")) {
            if (inOtherAbstract) {
                otherCopyright = buffer.toString();
            } else {
                articleCopyright = buffer.toString();
            }
        } else if (qName.equals("Affiliation")) {
            if (inAuthor) {
                authorAffiliation = buffer.toString();
            } else if (inInvestigator) {
                investigatorAffiliation = buffer.toString();
            } else {
                articleAffiliation = buffer.toString();
                logger.debug("\t\t\tarticleAffiliation : " + articleAffiliation);
            }
        } else if (qName.equals("Abstract")) {
            logger.debug("\t\t\tabstract : " + articleAbstr + "\n\t\t\tcopyright : " + articleCopyright);
        } else if (qName.equals("LastName")) {
            lastName = buffer.toString();
        } else if (qName.equals("ForeName")) {
            foreName = buffer.toString();
        } else if (qName.equals("Initials")) {
            initials = buffer.toString();
        } else if (qName.equals("Suffix")) {
            suffix = buffer.toString();
        } else if (qName.equals("CollectiveName")) {
            collName = buffer.toString();
        } else if (qName.equals("Author")) {
            inAuthor = false;
            logger.debug("\t\t\tlastName : " + lastName + " foreName : " + foreName + " initials : " + initials + " suffix : " + suffix + " collectiveName : " + collName);
            logger.debug("\t\t\tauthorAffiliation : " + authorAffiliation + " dateAssocName : ");
            processAuthor();
        } else if (qName.equals("Language")) {
            language = buffer.toString();
            logger.debug("\t\t\tlanguage : " + language);
            processLanguage();
        } else if (qName.equals("DataBankName")) {
            dataBankName = buffer.toString();
            logger.debug("\t\t\tdataBankName : " + dataBankName);
            processDataBank();
        } else if (qName.equals("AccessionNumber")) {
            accNumber = buffer.toString();
            logger.debug("\t\t\taccNumber : " + accNumber);
            processAccession();
        } else if (qName.equals("GrantID")) {
            grantID = buffer.toString();
        } else if (qName.equals("Acronym")) {
            grantAcronym = buffer.toString();
        } else if (qName.equals("Agency")) {
            grantAgency = buffer.toString();
        } else if (qName.equals("Country")) {
            grantCountry = buffer.toString();
        } else if (qName.equals("Grant")) {
            logger.debug("\t\t\tgrantID : " + grantID + " grantAcronym : " + grantAcronym + " grantAgency : " + grantAgency + " grantCountry : " + grantCountry);
            processGrant();
        } else if (qName.equals("PublicationType")) {
            publType = buffer.toString();
            logger.debug("\t\t\tpublType : " + publType);
            processPublicationType();
        } else if (qName.equals("VernacularTitle")) {
            vernacularTitle = buffer.toString();
            logger.debug("\t\t\tvernacularTitle : " + vernacularTitle);
        } else if (qName.equals("ArticleDate")) {
            articleDate = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
            logger.debug("\t\t\tarticleDate : " + new java.sql.Date(articleDate.getTimeInMillis()));
            processArticleDate();
        } else if (qName.equals("Article")) {
            afterArticle=true;;
        } else if (qName.equals("MedlineTA")) {
            medlineTA = buffer.toString();
        } else if (qName.equals("NlmUniqueID")) {
            nlmUniqueID = buffer.toString();
        } else if (qName.equals("ISSNLinking")) {
            issnLinking = buffer.toString();
        } else if (qName.equals("MedlineJournalInfo")) {
            logger.debug("\t\t\tjournal info - country : " + grantCountry + " medlineTA : " + medlineTA + " nlmUniqueID : " + nlmUniqueID + " issnLinking : " + issnLinking);
        } else if (qName.equals("RegistryNumber")) {
            chemNum = buffer.toString();
        } else if (qName.equals("NameOfSubstance")) {
            chemName = buffer.toString();
        } else if (qName.equals("Chemical")) {
            logger.debug("\t\t\tchemNum: " + chemNum + " chemName: " + chemName);
            processChemical();
        } else if (qName.equals("CitationSubset")) {
            citationSubset = buffer.toString();
            logger.debug("\t\t\tcitationSubset : " + citationSubset);
            processCitationSubset();
        } else if (qName.equals("RefSource")) {
            refSource = buffer.toString();
        } else if (qName.equals("Note")) {
            note = buffer.toString();
        } else if (qName.equals("CommentsCorrections")) {
            logger.debug("\t\t\tcommentsCorrections - refType: " + refType + " refSource: " + refSource + " ref_pmid: " + ref_pmid + " note: " + note);
            processCommentsCorrections();
        } else if (qName.equals("GeneSymbol")) {
            geneSymbol = buffer.toString();
            logger.debug("\t\t\tgeneSymbol : " + geneSymbol);
        } else if (qName.equals("DescriptorName")) {
            meshName = buffer.toString();
            logger.debug("\t\t\tmeshName: " + meshName + " majorMESH: " + majorMESH);
            processMeshHeading();
        } else if (qName.equals("QualifierName")) {
            meshQual = buffer.toString();
            logger.debug("\t\t\tmeshQual: " + meshQual + " majorMESHqual: " + majorMESHqual);
            processMeshQualifier();
        } else if (qName.equals("GeneSymbol")) {
            geneSymbol = buffer.toString();
            logger.debug("\t\t\tgeneSymbol : " + geneSymbol);
            processGeneSymbol();
        } else if (qName.equals("NumberOfReferences")) {
            refCount = buffer.toString();
            logger.debug("\t\t\trefCount : " + refCount);
        } else if (qName.equals("PersonalNameSubject")) {
            logger.debug("\t\t\tpersonalNameSubject - lastName : " + lastName + " foreName : " + foreName + " initials : " + initials + " suffix : " + suffix);
            processPersonalNameSubject();
        } else if (qName.equals("OtherID")) {
            otherID = buffer.toString();
            logger.debug("\t\t\totherID : " + otherID + " source: " + otherIDSource);
            processOtherID();
        } else if (qName.equals("OtherAbstract")) {
            inOtherAbstract = false;
            logger.debug("\t\t\totherAbstract : " + otherAbstr + " copyright: " + otherCopyright + " type: " + otherAbstrType);
            processOtherAbstract();
        } else if (qName.equals("Keyword")) {
            keyword = buffer.toString();
            logger.debug("\t\t\tID: " + pmid + " major : " + majorKeyword  + " Keyword : " + keyword);
            processKeyword();
        } else if (qName.equals("SpaceFlightMission")) {
            spaceFlight = buffer.toString();
            logger.debug("\t\t\tspaceFlight : " + spaceFlight);
            processSpaceFlightMission();
        } else if (qName.equals("Investigator")) {
            inInvestigator = false;
            logger.debug("\t\t\tinvestigator - lastName : " + lastName + " foreName : " + foreName + " initials : " + initials + " suffix : " + suffix + " investigatorAffiliation : " + investigatorAffiliation);
            processInvestigator();
        } else if (qName.equals("GeneralNote")) {
            generalNote = buffer.toString();
            logger.debug("\t\t\tgeneralNote : " + generalNote);
            processGeneralNote();
        }
        buffer = new StringBuffer();
    }
    
    private void processCitation() {
        // logger.info("articleTitle : " + articleTitle);
        // logger.info("pmid : " + pmid);
        // logger.info("abstract : " + articleAbstr);
        // logger.info("journal : " + journal + " date : " + articleDate + "
        // volume : " + volume + " issue : " + issue);

        if (load || reload)
            try {
                int existingCount = 0;
                PreparedStatement citeStmt = null;
                if (reload) {
                    PreparedStatement cntStmt = conn.prepareStatement("select count(*) from article where pmid = ?");
                    cntStmt.setInt(1, pmid);
                    ResultSet rs = cntStmt.executeQuery();
                    while (rs.next()) {
                        existingCount = rs.getInt(1);
                    }

                    cntStmt.close();
                    if (existingCount > 0) {
                        logger.debug("skipping " + pmid + "...");
                        doThisOne = false;
                    } else {
                        doThisOne = true;
                    }
                }

                if (load || doThisOne) {
                    logger.debug("storing " + pmid + "...");
                    citeStmt = conn.prepareStatement("insert into article values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    citeStmt.setInt(1, pmid);
                    citeStmt.setDate(2, new java.sql.Date(dateCreated.getTimeInMillis()));
                    if (dateCompleted == null )
                        citeStmt.setNull(3, java.sql.Types.NULL);
                    else
                        citeStmt.setDate(3, new java.sql.Date(dateCompleted.getTimeInMillis()));
                    if (dateRevised == null )
                        citeStmt.setNull(4, java.sql.Types.NULL);
                    else
                        citeStmt.setDate(4, new java.sql.Date(dateRevised.getTimeInMillis()));
                    citeStmt.setString(5, articleTitle);
                    if (startPage == null )
                        citeStmt.setNull(6, java.sql.Types.NULL);
                    else
                        citeStmt.setInt(6,Integer.parseInt(startPage));
                    if (endPage == null )
                        citeStmt.setNull(7, java.sql.Types.NULL);
                    else
                        citeStmt.setInt(7,Integer.parseInt(endPage));
                    citeStmt.setString(8, medlinePgn);
                    citeStmt.setString(9, articleAbstr);
                    citeStmt.setString(10, articleCopyright);
                    citeStmt.setString(11, articleAffiliation);
                    citeStmt.setString(12, null);
                    citeStmt.setString(13, vernacularTitle);
                    citeStmt.setString(14, null);
                    citeStmt.setString(15, medlineTA);
                    citeStmt.setString(16, nlmUniqueID);
                    citeStmt.setString(17, issnLinking);
                    if (refCount == null )
                        citeStmt.setNull(18, java.sql.Types.NULL);
                    else
                        citeStmt.setInt(18,Integer.parseInt(refCount));
                    citeStmt.setString(19, null);
                    citeStmt.setString(20, null);
                    try {
                        citeStmt.executeUpdate();
                        citeStmt.close();
                    } catch (SQLException se) {
                        System.out.println("Exception: " + se);
                        PreparedStatement commitStmt = conn.prepareStatement("abort transaction");
                        commitStmt.executeUpdate();
                        commitStmt.close();
                    }

                    if ((++count % 100) == 0) {
                        // if (true) {
                        logger.debug("committing transaction for " + pmid + "....");
                        PreparedStatement commitStmt = conn.prepareStatement("commit transaction");
                        commitStmt.executeUpdate();
                        commitStmt.close();

                        // PreparedStatement pathStmt =
                        // conn.prepareStatement("begin transaction");
                        // pathStmt.executeUpdate();
                        // pathStmt.close();

                        PreparedStatement pathStmt = conn.prepareStatement("set constraints all deferred");
                        pathStmt.executeUpdate();
                        pathStmt.close();
                    }
                }
            } catch (Exception e) {
                // System.out.println("Exception: " + e + "\tpubDate: " +
                // pubDate);
                e.printStackTrace();
                System.exit(0);
            }
        pmid=0;
        dateCreated = null;
        dateCompleted = null;
        dateRevised = null;
        articleTitle = null;
        startPage = null;
        endPage = null;
        medlinePgn = null;
        articleAbstr = null;
        articleCopyright = null;
        articleAffiliation = null;
        vernacularTitle = null;
        medlineTA = null;
        nlmUniqueID = null;
        issnLinking = null;
        refCount = null;
        
        afterArticle = false;
        
        authorSeqNum = 0;
        dataBankSeqNum = 0;
        accessionSeqNum = 0;
        grantSeqNum = 0;
        geneSeqNum = 0;
        chemicalSeqNum = 0;
        keywordSeqNum = 0;
        meshSeqNum = 0;
        qualSeqNum = 0;
        investigatorSeqNum = 0;
        pnsSeqNum = 0;
        elocSeqNum = 0;
        artDateSeqNum = 0;
        langSeqNum = 0;
        citeSeqNum = 0;
        otherIDSeqNum = 0;
        otherAbstrSeqNum = 0;
        spaceSeqNum = 0;
        noteSeqNum = 0;
        pubTypeSeqNum = 0;
        commCorrSeqNum = 0;
    }


    /**
     * @param pmid2
     */
    private void deleteCitation(int pmid) {
    	deleteRecord("elocation", pmid);
    	deleteRecord("author", pmid);
    	deleteRecord("language", pmid);
    	deleteRecord("medline10.grant", pmid);
    	deleteRecord("article_date", pmid);
    	deleteRecord("chemical", pmid);
    	deleteRecord("citation_subset", pmid);
    	deleteRecord("comments_corrections", pmid);
    	deleteRecord("gene_symbol", pmid);
    	deleteRecord("personal_name_subject", pmid);
    	deleteRecord("other_id", pmid);
    	deleteRecord("other_abstract", pmid);
    	deleteRecord("keyword", pmid);
    	deleteRecord("spaceflight_mission", pmid);
    	deleteRecord("investigator", pmid);
    	deleteRecord("general_note", pmid);
    	deleteRecord("publication_type", pmid);
    	deleteRecord("journal", pmid);
    	deleteRecord("accession", pmid);
    	deleteRecord("data_bank", pmid);
    	deleteRecord("mesh_qualifier", pmid);
    	deleteRecord("mesh_heading", pmid);
    	deleteRecord("article", pmid);
    }
    
    private void deleteRecord(String table, int pmid) {
        try {
            PreparedStatement stmt = conn.prepareStatement("delete from " + table + " where pmid = ?");
            stmt.setInt(1, pmid);
            stmt.executeUpdate();
            stmt.close();
       } catch (Exception e) {
           e.printStackTrace();
        }
    }

    public void processJournal() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into journal values (?,?,?,?,?,?,?,?,?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setString(2, issn);
            stmt.setString(3, volume);
            stmt.setString(4, issue);
            if (medlineDate == null) {
            	stmt.setInt(5, Integer.parseInt(year));
            	stmt.setString(6, month);
            	if (day != null)
            	    stmt.setInt(7, Integer.parseInt(day));
            	else
                    stmt.setNull(7, java.sql.Types.INTEGER);
            } else {
            	stmt.setNull(5, java.sql.Types.INTEGER);
            	stmt.setNull(6, java.sql.Types.CHAR);
            	stmt.setNull(7, java.sql.Types.INTEGER);
            }
            stmt.setString(8, season);
            stmt.setString(9, medlineDate);
            stmt.setString(10, title);
            stmt.setString(11, isoAbbrev);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        issn = null;
        volume = null;
        issue = null;
        year = null;
        day = null;
        season = null;
        medlineDate = null;
        title = null;
        isoAbbrev = null;

    }
    
    public void processAuthor() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into author values (?,?,?,?,?,?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++authorSeqNum);
            stmt.setString(3, lastName);
            stmt.setString(4, foreName);
            stmt.setString(5, initials);
            stmt.setString(6, suffix);
            stmt.setString(7, collName);
            stmt.setString(8, authorAffiliation);
           stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        lastName = null;
        foreName = null;
        initials = null;
        suffix = null;
        collName = null;
        authorAffiliation = null;
    }
    
    public void processDataBank() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into data_bank values (?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++dataBankSeqNum);
            stmt.setString(3, dataBankName);
           stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        dataBankName = null;
        accessionSeqNum = 0;
    }
    
    public void processAccession() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into accession values (?,?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, dataBankSeqNum);
            stmt.setInt(3, ++accessionSeqNum);
            stmt.setString(4, accNumber);
           stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        dataBankName = null;
    }
    
    public void processGrant() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into medline10.grant values (?,?,?,?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++grantSeqNum);
            stmt.setString(3, grantID);
            stmt.setString(4, grantAcronym);
            stmt.setString(5, grantAgency);
            stmt.setString(6, grantCountry);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        grantID = null;
        grantAcronym = null;
        grantAgency = null;
        grantCountry = null;
    }
    
    public void processGeneSymbol() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into gene_symbol values (?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++geneSeqNum);
            stmt.setString(3, geneSymbol);
           stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        geneSymbol = null;
    }
    
    public void processChemical() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into chemical values (?,?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++chemicalSeqNum);
            stmt.setString(3, chemNum);
            stmt.setString(4, chemName);
           stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        chemNum = null;
        chemName = null;
    }
    
    public void processKeyword() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into keyword values (?,?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++keywordSeqNum);
            stmt.setString(3, keyword);
            stmt.setBoolean(4, majorKeyword);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        keyword = null;
        majorKeyword = false;
    }
    
    public void processMeshHeading() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into mesh_heading values (?,?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++meshSeqNum);
            stmt.setString(3, meshName);
            stmt.setBoolean(4, majorMESH);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        meshName = null;
        majorMESH = false;
        qualSeqNum = 0;
    }
    
    public void processMeshQualifier() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into mesh_qualifier values (?,?,?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, meshSeqNum);
            stmt.setInt(3, ++qualSeqNum);
            stmt.setString(4, meshQual);
            stmt.setBoolean(5, majorMESHqual);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        meshQual = null;
        majorMESHqual = false;
    }
    
    public void processInvestigator() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into investigator values (?,?,?,?,?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++investigatorSeqNum);
            stmt.setString(3, lastName);
            stmt.setString(4, foreName);
            stmt.setString(5, initials);
            stmt.setString(6, suffix);
            stmt.setString(7, investigatorAffiliation);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        lastName = null;
        foreName = null;
        initials = null;
        suffix = null;
        investigatorAffiliation = null;
    }
    
    public void processPersonalNameSubject() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into personal_name_subject values (?,?,?,?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++pnsSeqNum);
            stmt.setString(3, lastName);
            stmt.setString(4, foreName);
            stmt.setString(5, initials);
            stmt.setString(6, suffix);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        lastName = null;
        foreName = null;
        initials = null;
        suffix = null;
    }
    
    public void processBook() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into book values (?,?,?,?,?,?,?,?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, Integer.parseInt(year));
            stmt.setInt(3, Integer.parseInt(month));
            stmt.setInt(4, Integer.parseInt(day));
            stmt.setString(5, season);
            stmt.setString(6, medlineDate);
            stmt.setString(7, publisher);
            stmt.setString(8, journal);
            stmt.setString(9, collectionTitle);
            stmt.setString(10, volume);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        year = null;
        month = null;
        day = null;
        season = null;
        medlineDate = null;
        publisher = null;
        journal = null;
        collectionTitle = null;
        volume = null;
        authorSeqNum = 0;
    }
    
    public void processELocation() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into elocation values (?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++elocSeqNum);
            stmt.setString(3, eLocationID);
           stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        eLocationID = null;
    }
    
    public void processLanguage() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into language values (?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++langSeqNum);
            stmt.setString(3, language);
           stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        language = null;
    }
    
    public void processCitationSubset() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into citation_subset values (?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++citeSeqNum);
            stmt.setString(3, citationSubset);
           stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        citationSubset = null;
    }
    
    public void processArticleDate() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into article_date values (?,?,?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++artDateSeqNum);
            stmt.setInt(3, Integer.parseInt(year));
            stmt.setInt(4, Integer.parseInt(month));
            stmt.setInt(5, Integer.parseInt(day));
           stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        year = null;
        month = null;
        day = null;
    }
    
    public void processOtherID() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into other_id values (?,?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++otherIDSeqNum);
            stmt.setString(3, otherIDSource);
            stmt.setString(4, otherID);
           stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        otherIDSource = null;
        otherID = null;
    }
    
    public void processOtherAbstract() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into other_abstract values (?,?,?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++otherAbstrSeqNum);
            stmt.setString(3, otherAbstrType);
            stmt.setString(4, otherAbstr);
            stmt.setString(5, otherCopyright);
           stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        otherAbstrType = null;
        otherAbstr = null;
        otherCopyright = null;
    }
    
    public void processSpaceFlightMission() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into spaceflight_mission values (?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++spaceSeqNum);
            stmt.setString(3, spaceFlight);
           stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        spaceFlight = null;
    }
    
    public void processGeneralNote() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into general_note values (?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++noteSeqNum);
            stmt.setString(3, generalNote);
           stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        generalNote = null;
    }
    
    public void processPublicationType() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into publication_type values (?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++pubTypeSeqNum);
            stmt.setString(3, publType);
           stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        publType = null;
    }
    
    public void processCommentsCorrections() {
        if (!load)
            return;

        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("insert into comments_corrections values (?,?,?,?,?,?)");
            stmt.setInt(1, pmid);
            stmt.setInt(2, ++commCorrSeqNum);
            stmt.setString(3, refType);
            stmt.setString(4, refSource);
            stmt.setInt(5, ref_pmid);
            stmt.setString(6, note);
           stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        refSource = null;
        ref_pmid = 0;
        note = null;
    }
    
    public void materializeAuthorView() throws SQLException {
        // refresh author uid-pmid cache with new data
        execute("delete from author_cache13");
        execute("analyze medline10.author");
        execute("analyze medline10.journal");
        execute("update journal set pub_day=28 where (pub_month='Feb' or pub_month='02') and pub_day > 28");
        execute("update journal set pub_day=30 where (pub_month='Sep' or pub_month='09' or pub_month='Apr' or pub_month='04' or pub_month='Jun' or pub_month='06' or pub_month='Nov' or pub_month='11') and pub_day > 30");
        execute("insert into author_cache13 select authors.id,(pub_year||'-'||pub_month||'-'||pub_day)::date,medline10.author.pmid"
                + " from loki.authors,medline10.author,medline10.journal"
                + " where authors.lastname=medline10.author.last_name and authors.forename=medline10.author.fore_name and medline10.author.pmid=medline10.journal.pmid");
        execute("analyze author_cache13");
    	
		PreparedStatement cntStmt = conn.prepareStatement("select count(*) from author_cache13");
		ResultSet rs = cntStmt.executeQuery();
		while (rs.next()) {
			logger.info("\nauthor cache instance count: " + rs.getInt(1));
		}
        
        //TODO The following two parameter adjustments make the following query appropriately use their indices in scans, rather than sequentially scanning.  We need to establish a generic means of doing this.
        execute("set session enable_seqscan = off");
        execute("set session random_page_cost = 1");

		// refresh author statistics with new data
        execute("truncate medline10.author_count");
        execute("insert into medline10.author_count select last_name,fore_name,count(*) from medline10.author where fore_name is not null group by 1,2");
        execute("analyze medline10.author_count");

        // refresh MeSH terminology and tf*idf statistics with new data
        execute("delete from loki.mesh");
        execute("insert into loki.mesh select id, descriptor_name as term from loki.author_cache13 natural join medline10.mesh_heading where descriptor_name is not null");

        //TODO Now reset the parameters to their defaults.
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
    
    void execute(String statement) throws SQLException {
        logger.debug("executing " + statement + "...");
        PreparedStatement stmt = conn.prepareStatement(statement);
        stmt.executeUpdate();
        stmt.close();
    }

    public void characters (char ch[], int start, int length) {
        buffer.append(ch,start,length);
        if (logger.isTraceEnabled()) {
            System.out.print("Characters:    \"");
            for (int i = start; i < start + length; i++) {
                switch (ch[i]) {
                    case '\\':
                        System.out.print("\\\\");
                        break;
                    case '"':
                        System.out.print("\\\"");
                        break;
                    case '\n':
                        System.out.print("\\n");
                        break;
                    case '\r':
                        System.out.print("\\r");
                        break;
                    case '\t':
                        System.out.print("\\t");
                        break;
                    default:
                        System.out.print(ch[i]);
                        break;
                }
            }
            System.out.print("\"\n");
        }
    }
    
}
