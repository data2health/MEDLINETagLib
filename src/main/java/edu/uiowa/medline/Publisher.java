/*
 * Created on Feb 26, 2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.uiowa.medline;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.sql.*;
import java.util.*;

public class Publisher {

    static Properties prop_file = PropertyLoader.loadProperties("loader");

    static Logger logger = Logger.getLogger(Loader.class);

    public static boolean useLocalHost = false;

    // public static final String localHostName = "192.168.2.24";
    public static final String localHostName = "localhost";

    // public static final String networkHostName = "192.168.0.2";
    public static final String networkHostName = "neuromancer.icts.uiowa.edu";

    static Connection conn = null;

    static final String host = networkHostName;
    
    Hashtable<String,String> authorHash = null;

    public static void main(String args[]) throws Exception {
        PropertyConfigurator.configure(args[0]);
        connect();

        Publisher thePublisher = new Publisher();
//        thePublisher.materializeAuthorView();
        thePublisher.identifyResearchers();
    }
    
    static void connect() throws SQLException, ClassNotFoundException {
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

    void materializeAuthorView() throws SQLException {
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

        // TODO The following two parameter adjustments make the following query
        // appropriately use their indices in scans, rather than sequentially
        // scanning. We need to establish a generic means of doing this.
        execute("set session enable_seqscan = off");
        execute("set session random_page_cost = 1");

        // refresh author statistics with new data
        execute("truncate medline10.author_count");
        execute("insert into medline10.author_count select last_name,fore_name,count(*) from medline10.author where fore_name is not null group by 1,2");
        execute("analyze medline10.author_count");

        // refresh MeSH terminology and tf*idf statistics with new data
        execute("delete from loki.mesh");
        execute("insert into loki.mesh select id, descriptor_name as term from loki.author_cache13 natural join medline10.mesh_heading");

        // TOTO Now reset the parameters to their defaults.
        execute("set session enable_seqscan = on");
        execute("set session random_page_cost = 4");

        execute("analyze loki.mesh");
        execute("delete from loki.mesh_frequency");
        execute("insert into loki.mesh_frequency select id, term, count(*) from loki.mesh group by 1,2");
        execute("analyze loki.mesh_frequency");
        execute("delete from loki.mesh_lexicon");
        execute("insert into loki.mesh_lexicon select term, count(*), sum(count) from loki.mesh_frequency group by 1");
        execute("analyze loki.mesh_lexicon");

        execute("end transaction");
    }
    
    void identifyResearchers() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select distinct id from author_cache13 where id in (select id from author_cluster) and pmid not in (select pmid from cluster_document where author_cache13.id=cluster_document.id)");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt(1);
            logger.info("researcher with new publication(s): " + id);
            authorHash = loadAuthorHash(id);
            incrementResearcherClusters(id);
        }
        stmt.close();
        execute("end transaction");
    }
    
    void incrementResearcherClusters(int id) throws SQLException {
        Vector<DocumentCluster> theClusters = loadClusters(id);

        PreparedStatement stmt = conn.prepareStatement("select pmid from author_cache13 where id = ? and pmid not in (select pmid from cluster_document where author_cache13.id=cluster_document.id)");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int pmid = rs.getInt(1);
            logger.info("\tnew publication: " + pmid);
            ClusterDocument newDocument = new ClusterDocument(authorHash, pmid);
            newDocument.recent = true;

            DocumentCluster bestMatch = null;
            for (int i = 0; i<theClusters.size() && bestMatch == null; i++) {
                if (authorMatch(newDocument, theClusters.elementAt(i)) > 0)
                    bestMatch = theClusters.elementAt(i);
            }
            if (bestMatch == null) {
                bestMatch = new DocumentCluster();
                bestMatch.id = id;
                bestMatch.seqNum = theClusters.size() == 0 ? 1 : theClusters.lastElement().seqNum + 1;
                bestMatch.recent = true;
                theClusters.addElement(bestMatch);
                logger.info("\t\tcreating new cluster with " + pmid);
            } else {
                logger.info("\t\tadding " + pmid + " to " + bestMatch.seqNum);                
            }
            bestMatch.theDocuments.addElement(newDocument);
        }
        stmt.close();
        
        clusterMerge(theClusters);
        storeUpdates(id, theClusters);
        notifyResearcher(id, theClusters);
    }
    
    void notifyResearcher(int id, Vector<DocumentCluster> theClusters) {
        logger.info("email to: " + id);
        for (int i = 0; i < theClusters.size(); i++) {
            DocumentCluster current = theClusters.elementAt(i);
            if (current.recent) {
                logger.info("\tnew cluster " + current.seqNum + " " + current.valid + " " + current.recent);
                for (int j = 0; j < current.theDocuments.size(); j++) {
                    ClusterDocument theDoc = current.theDocuments.elementAt(j);
                    logger.info("\t\t" + theDoc.pmid + " : " + theDoc.title);
                }
            }
        }
        
        for (int i = 0; i < theClusters.size(); i++) {
            DocumentCluster current = theClusters.elementAt(i);
            for (int j = 0; j < current.theDocuments.size(); j++) {
                ClusterDocument theDoc = current.theDocuments.elementAt(j);
                if (theDoc.recent && current.valid) {
                    logger.info("\tnew document " + theDoc.pmid + " : " + theDoc.title);
                }
            }
        }
    }
    
    void storeUpdates(int id, Vector<DocumentCluster> theClusters) throws SQLException {
        for (int i = 0; i < theClusters.size(); i++) {
            DocumentCluster current = theClusters.elementAt(i);
            if (current.recent) {
                logger.info("\tstoring new cluster " + current.seqNum + " " + current.valid + " " + current.recent);
                PreparedStatement clusterStmt = conn.prepareStatement("insert into author_cluster values (?,?,?,?)");
                clusterStmt.setInt(1, id);
                clusterStmt.setInt(2, current.seqNum);
                clusterStmt.setBoolean(3, current.valid);
                clusterStmt.setBoolean(4, current.recent);
                clusterStmt.execute();
                clusterStmt.close();
            }
            for (int j = 0; j < current.theDocuments.size(); j++) {
                ClusterDocument theDoc = current.theDocuments.elementAt(j);
                if (theDoc.recent) {
                    logger.info("\tstoring new document " + theDoc.pmid + " in cluster " + current.seqNum);
                    PreparedStatement docStmt = conn.prepareStatement("insert into cluster_document values (?,?,?)");
                    docStmt.setInt(1, id);
                    docStmt.setInt(2, current.seqNum);
                    docStmt.setInt(3, theDoc.pmid);
                    docStmt.execute();
                    docStmt.close();
                    
                    if (current.valid){
                        logger.info("\tstoring new document " + theDoc.pmid + " to publications for " + id);
                        String citationString = null;
                        
                        PreparedStatement loadStmt = conn.prepareStatement("select article.title, ta, volume, issue, medline_pgn, pub_year from medline10.article, medline10.journal where article.pmid=journal.pmid and article.pmid = ?");
                        loadStmt.setInt(1,theDoc.pmid);
                        ResultSet lrs = loadStmt.executeQuery();
                        while (lrs.next()) {
                            citationString = authorString(theDoc.pmid) + " <a href=\"../publications/browsePublication.jsp?id=" + theDoc.pmid + "\">" + lrs.getString(1) + "</a> <i>" + lrs.getString(2) + "</i> " + lrs.getString(3) + "(" + lrs.getString(4) + "):" + lrs.getString(5) + ", " + lrs.getInt(6) + ".";
                        }
                        loadStmt.close();

                        int sequenceNumber = 1;
                        PreparedStatement countStmt = conn.prepareStatement("select max(sequence_number) from loki.publication where true  and id = ? having max(sequence_number) is not null;");
                        countStmt.setInt(1, id);
                        ResultSet rs = countStmt.executeQuery();
                        while (rs.next()) {
                            sequenceNumber = rs.getInt(1) + 1;
                        }
                        countStmt.close();

                        PreparedStatement pubStmt = conn.prepareStatement("insert into publication values (?,?,?,?)");
                        pubStmt.setInt(1, id);
                        pubStmt.setInt(2, sequenceNumber);
                        pubStmt.setString(3, citationString);
                        pubStmt.setInt(4, theDoc.pmid);
                        pubStmt.execute();
                        pubStmt.close();
                    }
                }
            }
        }
    }
    
    String authorString(int pmid) throws SQLException {
        StringBuffer authorBuffer = new StringBuffer();

        int count = 0;
        PreparedStatement loadStmt = conn.prepareStatement("select last_name,fore_name from medline10.author where pmid = ? order by seqnum");
        loadStmt.setInt(1,pmid);
        ResultSet lrs = loadStmt.executeQuery();
        while (lrs.next()) {
            String lastName = lrs.getString(1);
            String foreName = lrs.getString(2);
            authorBuffer.append((count++ > 0 ? ", " : "") + lastName + ", " + foreName);
        }
        loadStmt.close();
        authorBuffer.append(".");

        return authorBuffer.toString();
    }

    void clusterMerge(Vector<DocumentCluster> theClusters) {
        boolean matched = false;
        for (int i = 0; i < theClusters.size(); i = (!matched ? i+1 : i)) {
            DocumentCluster current = theClusters.elementAt(i);
            matched = false;
            for (int j = i + 1; j < theClusters.size() && !matched; j++) {
                DocumentCluster fence = theClusters.elementAt(j);
                for (int k = 0; k < fence.theDocuments.size() && !matched; k++) {
                    ClusterDocument theInstance = fence.theDocuments.elementAt(k);
                    if (authorMatch(theInstance, current) > 0) {
                        logger.info("merging " + i + " with " +j);
                        matched = true;
                        theClusters.removeElementAt(j);
                        clusterMerge(current, fence);
                    }
                }
            }
        }
    }
    
    void clusterMerge(DocumentCluster current, DocumentCluster fence) {
        for (int i = 0; i < current.theDocuments.size() && fence.theDocuments.size() > 0; i++) {
            int currPMID = current.theDocuments.elementAt(i).pmid;
            int fencePMID = fence.theDocuments.elementAt(0).pmid;
            if (fencePMID > currPMID) {
                current.theDocuments.insertElementAt(fence.theDocuments.elementAt(0), i);
                fence.theDocuments.removeElementAt(0);
            }
        }
        
        for (int i = 0; i < fence.theDocuments.size(); i++) {
            current.theDocuments.addElement(fence.theDocuments.elementAt(0));
            fence.theDocuments.removeElementAt(0);
        }
    }
    
    int authorMatch(ClusterDocument theInstance, DocumentCluster theCluster) {
        int score = 0;
        
        for (int i = 0; i < theCluster.theDocuments.size(); i++) {
            ClusterDocument currentInstance = theCluster.theDocuments.elementAt(i);
            for (int j = 0; j < currentInstance.authors.size(); j++) {
                for (int k = 0; k < theInstance.authors.size(); k++) {
                    if (currentInstance.authors.elementAt(j).equals(theInstance.authors.elementAt(k)))
                        score++;
                }
                
            }
        }
        logger.debug("score: " + score);
        return score;
    }
    
    Vector<DocumentCluster> loadClusters(int id) throws SQLException {
        Vector<DocumentCluster> theClusters = new Vector<DocumentCluster>();
        
        PreparedStatement stmt = conn.prepareStatement("select id,seqnum,valid,recent from author_cluster where id = ? order by seqnum");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            DocumentCluster theCluster = new DocumentCluster();
            theCluster.id = rs.getInt(1);
            theCluster.seqNum = rs.getInt(2);
            theCluster.valid = rs.getBoolean(3);
            theCluster.recent = rs.getBoolean(4);
            theClusters.add(theCluster);
            logger.info("\tcluster: " + theCluster.seqNum + " " + theCluster.valid + " " + theCluster.recent);

            PreparedStatement docStmt = conn.prepareStatement("select pmid from cluster_document where id = ? and seqnum = ? order by pmid");
            docStmt.setInt(1, id);
            docStmt.setInt(2, theCluster.seqNum);
            ResultSet drs = docStmt.executeQuery();
            while (drs.next()) {
                ClusterDocument theDocument = new ClusterDocument(authorHash, drs.getInt(1));
                theDocument.recent = false;
                theCluster.theDocuments.add(theDocument);
                logger.debug("\t\tDocument: " + theDocument.pmid + " " + theDocument.recent);
            }
            docStmt.close();
        }
        stmt.close();

        return theClusters;
    }
    
    Hashtable<String,String> loadAuthorHash(int id) throws SQLException {
        Hashtable<String,String> authorHash = new Hashtable<String,String>();
        
        PreparedStatement stmt = conn.prepareStatement("select lastname,forename from authors where id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String lastName = rs.getString(1);
            String foreName = rs.getString(2);
            authorHash.put(lastName + " " + foreName, lastName + " " + foreName);
        }
        stmt.close();

        return authorHash;
    }

    static void execute(String statement) throws SQLException {
        logger.debug("executing " + statement + "...");
        PreparedStatement stmt = conn.prepareStatement(statement);
        stmt.executeUpdate();
        stmt.close();
    }
    
    class DocumentCluster {
        int id = 0;
        int seqNum = 0;
        boolean valid = false;
        boolean recent = false;
        Vector<ClusterDocument> theDocuments = new Vector<ClusterDocument>();
    }
    
    class ClusterDocument {
        String title = null;
        int pmid = 0;
        Vector<String> authors = new Vector<String>();
        boolean recent = false;
        
        ClusterDocument(Hashtable<String,String> authorHash, int pmid) {
            this.pmid = pmid;
            
            try {
                PreparedStatement loadStmt = conn.prepareStatement("select title from medline10.article where pmid = ?");
                loadStmt.setInt(1,pmid);
                ResultSet lrs = loadStmt.executeQuery();
                while (lrs.next()) {
                    title = lrs.getString(1);
                }
                loadStmt.close();

                PreparedStatement authStmt = conn.prepareStatement("select last_name, fore_name, initials from medline10.author where pmid = ? order by 1,2");
                authStmt.setInt(1, pmid);
                ResultSet ars = authStmt.executeQuery();
                while (ars.next()) {
                    String lname = ars.getString(1);
                    String fname = ars.getString(2);
                    String initials = ars.getString(3);
                    if (!authorHash.containsKey(lname + " " + fname)) {
                        logger.debug("\t\t\tauthor: " + lname + " " + fname);
                        authors.addElement(lname + " " + initials);
                    }
                }
                authStmt.close();

            } catch (SQLException e) { }
        }
    }

}
