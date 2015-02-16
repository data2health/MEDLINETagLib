package edu.uiowa.medline.util;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

public class LuceneIndex {
    static Logger logger = Logger.getLogger(LuceneIndex.class);
	static boolean tomcat = false;
	static boolean local = true;
	static String lucenePath = "MEDLINEIndex";
	static boolean truncate = true;

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws NamingException 
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws CorruptIndexException 
	 */
	public static void main(String[] args) throws SQLException, NamingException, ClassNotFoundException, CorruptIndexException, IOException {
		PropertyConfigurator.configure(args[0]);
		IndexWriter theWriter = initializeWriter();
		
		logger.info("Scanning pmids:");
		Connection conn = getConnection();
		PreparedStatement articleStmt = conn.prepareStatement("select pmid from medline14.article order by pmid");
		ResultSet articleRS = articleStmt.executeQuery();
		while (articleRS.next()) {
			Document theDocument = new Document();
			int pmid = articleRS.getInt(1);
            theDocument.add(new Field("pmid", pmid+"", Field.Store.YES, Field.Index.NOT_ANALYZED));
			
			PreparedStatement journalStmt = conn.prepareStatement("select article.title,pub_year from medline14.article,medline14.journal where article.pmid=journal.pmid and article.pmid = ?");
			journalStmt.setInt(1, pmid);
			ResultSet journalRS = journalStmt.executeQuery();
			while (journalRS.next()) {
				String title = journalRS.getString(1);
				int year = journalRS.getInt(2);
				logger.info("pmid: " + pmid + "  year: " + year + " - " + title);
	            theDocument.add(new Field("year", year+"", Field.Store.YES, Field.Index.NOT_ANALYZED));
	            theDocument.add(new Field("content", pmid+"", Field.Store.NO, Field.Index.ANALYZED));
	            theDocument.add(new Field("content", year+"", Field.Store.NO, Field.Index.ANALYZED));
	            theDocument.add(new Field("content", title, Field.Store.NO, Field.Index.ANALYZED));
				
			}
			
			PreparedStatement authorStmt = conn.prepareStatement("select last_name,fore_name,initials,collective_name from medline14.author where author.pmid = ?");
			authorStmt.setInt(1, pmid);
			ResultSet authorRS = authorStmt.executeQuery();
			while (authorRS.next()) {
				String lastName = authorRS.getString(1);
				String foreName = authorRS.getString(2);
				String initials = authorRS.getString(3);
				String collectiveName = authorRS.getString(4);

				logger.debug("\tlastName: " + lastName + "  foreName: " + foreName + " initials: " + initials + "  collectiveName: " + collectiveName);
				if (lastName != null)
	            	theDocument.add(new Field("content", lastName, Field.Store.NO, Field.Index.ANALYZED));
				if (foreName != null)
	            	theDocument.add(new Field("content", foreName, Field.Store.NO, Field.Index.ANALYZED));
				if (initials != null)
	            	theDocument.add(new Field("content", initials, Field.Store.NO, Field.Index.ANALYZED));
				if (collectiveName != null)
	            	theDocument.add(new Field("content", collectiveName, Field.Store.NO, Field.Index.ANALYZED));
			}

			PreparedStatement abstrStmt = conn.prepareStatement("select abstract_text,label,category from medline14.abstr where abstr.pmid = ?");
			abstrStmt.setInt(1, pmid);
			ResultSet absgtrRS = abstrStmt.executeQuery();
			while (absgtrRS.next()) {
				String abstr = absgtrRS.getString(1);
				String label = absgtrRS.getString(2);
				String category = absgtrRS.getString(3);

				logger.debug("\tlabel: " + label + "  category: " + category + " text: " + (abstr.length() >= 100 ? abstr.substring(0,100) : abstr));
	            if (abstr != null)
	            	theDocument.add(new Field("content", abstr, Field.Store.NO, Field.Index.ANALYZED));
	            if (label != null)
	            	theDocument.add(new Field("content", label, Field.Store.NO, Field.Index.ANALYZED));
	            if (category != null)
	            	theDocument.add(new Field("content", category, Field.Store.NO, Field.Index.ANALYZED));
			}

			theWriter.addDocument(theDocument);
		}
		
		theWriter.optimize();
		theWriter.close();
		conn.close();
		
	}
	
	static IndexWriter initializeWriter() {
		IndexWriter theWriter = null;
		logger.debug("Starting Index Operation");
		    
		try {
			if (truncate) {

		        //theWriter = new IndexWriter(FSDirectory.open(new File(lucenePath), _LockFactory),
				theWriter = new IndexWriter(FSDirectory.open(new File(lucenePath)),
		        		new StandardAnalyzer(org.apache.lucene.util.Version.LUCENE_30), true, 
		        		IndexWriter.MaxFieldLength.LIMITED);
		        for (int i = 0; i < theWriter.maxDoc(); i++)
		            theWriter.deleteAll();
		        theWriter.close();
		    }
			//theWriter = new IndexWriter(FSDirectory.open(new File(lucenePath), _LockFactory),
		    theWriter = new IndexWriter(FSDirectory.open(new File(lucenePath)), 
		    		new StandardAnalyzer(org.apache.lucene.util.Version.LUCENE_30), true, 
		    		IndexWriter.MaxFieldLength.LIMITED);
		    
		} catch (CorruptIndexException e) {
			logger.error("Corruption Exception", e);
		} catch (LockObtainFailedException e) {
			logger.error("Failed to Obtain Lock", e);
		} catch (IOException e) {
			logger.error("IO Exception", e);
		}

		return theWriter;

	}

    static Connection getConnection() throws NamingException, SQLException, ClassNotFoundException {
		Connection theConnection = null;

		if (tomcat) {
	    	DataSource theDataSource = (DataSource)new InitialContext().lookup("java:/comp/env/jdbc/MEDLINETagLib");
	    	theConnection = theDataSource.getConnection();
	    } else if (local) {
	        Class.forName("org.postgresql.Driver");
			Properties props = new Properties();
			props.setProperty("user", "eichmann");
			props.setProperty("password", "translational");
			theConnection = DriverManager.getConnection("jdbc:postgresql://localhost/loki", props);	    	
	    } else {
	        Class.forName("org.postgresql.Driver");
			Properties props = new Properties();
			props.setProperty("user", "eichmann");
			props.setProperty("password", "translational");
			props.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
			props.setProperty("ssl", "true");
			theConnection = DriverManager.getConnection("jdbc:postgresql://neuromancer.icts.uiowa.edu/bioinformatics", props);
	    }
		
		return theConnection;
    }
}
