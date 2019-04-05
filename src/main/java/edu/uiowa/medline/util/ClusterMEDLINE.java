package edu.uiowa.medline.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import com.ibm.tspaces.Field;
import com.ibm.tspaces.Tuple;
import com.ibm.tspaces.TupleSpace;
import com.ibm.tspaces.TupleSpaceException;

import edu.uiowa.loki.clustering.Author;
import edu.uiowa.loki.clustering.Cluster;
import edu.uiowa.loki.clustering.ExternalSource;
import edu.uiowa.loki.clustering.Instance;

public class ClusterMEDLINE {
	final static boolean useFirstInitial = true;
	
	protected static final Log logger = LogFactory.getLog(ExternalSource.class);
	static Connection theConnection = null;
	ExternalSource source = new ClusteringSource();

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @throws TupleSpaceException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, TupleSpaceException {
		PropertyConfigurator.configure(args[0]);

        Class.forName("org.postgresql.Driver");
		Properties props = new Properties();
		LocalProperties prop_file = PropertyLoader.loadProperties("medline");
		props.setProperty("user", prop_file.getProperty("jdbc.user"));
		props.setProperty("password", prop_file.getProperty("jdbc.password"));
//		props.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
//		props.setProperty("ssl", "true");
		theConnection = DriverManager.getConnection("jdbc:postgresql://localhost/loki", props);
		theConnection.setAutoCommit(false);

		ClusterMEDLINE theClusterer = new ClusterMEDLINE();
		
//		theClusterer.solo();
		if (args.length > 1 && args[1].equals("null"))
				theClusterer.tspace_null();
		else
			theClusterer.tspace();
//		theClusterer.unicodeInitial();
	}
	
	void unicodeInitial() throws SQLException {
			PreparedStatement stmt = theConnection.prepareStatement("select last_name from medline_clustering.author_prefix where not completed");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String lastName = rs.getString(1);
				
				PreparedStatement countStmt = theConnection.prepareStatement("select fore_name from medline_clustering.author_count where last_name = ? order by fore_name");
				countStmt.setString(1, lastName);
				ResultSet crs = countStmt.executeQuery();
				while (crs.next()) {
					String foreName = crs.getString(1);
					if (foreName == null || foreName.length() == 0)
						continue;
					String initial = "" + foreName.charAt(0);

					PreparedStatement compStmt = theConnection.prepareStatement("insert into medline_clustering.author_patch values (?,?)");
					compStmt.setString(1, lastName);
					compStmt.setString(2, initial);
					compStmt.execute();
					compStmt.close();
				}
				countStmt.close();
				theConnection.commit();
			}
			stmt.close();
	}
	
	void solo() throws SQLException {
		
		boolean foundSome = true;
		
		while (foundSome) {
			foundSome = false;

			PreparedStatement stmt = theConnection.prepareStatement("select last_name,fore_name from medline_clustering.author_count where last_name='Bickenbach' limit 10");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String lastName = rs.getString(1);
				String foreName = rs.getString(2);
				cluster(new Author(lastName, foreName));
				foundSome = true;
				
				PreparedStatement compStmt = theConnection.prepareStatement("update medline_clustering.author_count set completed = true where last_name = ? and fore_name = ?");
				compStmt.setString(1, lastName);
				compStmt.setString(2, foreName);
				compStmt.execute();
				compStmt.close();
				theConnection.commit();
			}
			stmt.close();
			break;
		}
	}
	
	void tspace() throws TupleSpaceException, SQLException {
		TupleSpace ts = null;
		logger.debug("initializing tspace...");
		try {
			ts = new TupleSpace("MEDLINE", "localhost");
		} catch (TupleSpaceException tse) {
			logger.error("TSpace error: " + tse);
		}
		
        Tuple theTuple = null;

        theTuple = ts.waitToTake("cluster_request", new Field(String.class), new Field(String.class));

        while (theTuple != null) {
            String lastName = (String) theTuple.getField(1).getValue();
            String foreName = (String) theTuple.getField(2).getValue();
            logger.info("consuming " + lastName + ", " + foreName);

            cluster(new Author(lastName, foreName));

			PreparedStatement compStmt = useFirstInitial ? theConnection.prepareStatement("update medline_clustering.author_prefix set completed = true where last_name = ? and initial = ?")
														 : theConnection.prepareStatement("update medline_clustering.author_count set completed = true where last_name = ? and fore_name = ?");
			compStmt.setString(1, lastName);
			compStmt.setString(2, foreName);
			compStmt.execute();
			compStmt.close();
			theConnection.commit();

			theTuple = ts.waitToTake("cluster_request", new Field(String.class), new Field(String.class));
        }
	}

	void tspace_null() throws TupleSpaceException, SQLException {
		TupleSpace ts = null;
		logger.debug("initializing tspace...");
		try {
			ts = new TupleSpace("MEDLINE", "localhost");
		} catch (TupleSpaceException tse) {
			logger.error("TSpace error: " + tse);
		}
		
        Tuple theTuple = null;

        theTuple = ts.waitToTake("cluster_request", new Field(String.class));

        while (theTuple != null) {
            String lastName = (String) theTuple.getField(1).getValue();
            String foreName = null;
            logger.info("consuming " + lastName + ", " + foreName);

            cluster(new Author(lastName, foreName));

			PreparedStatement compStmt = useFirstInitial ? theConnection.prepareStatement("update medline_clustering.author_prefix set completed = true where last_name = ? and initial is null")
														 : theConnection.prepareStatement("update medline_clustering.author_count set completed = true where last_name = ? and fore_name is null");
			compStmt.setString(1, lastName);
			compStmt.execute();
			compStmt.close();
			theConnection.commit();

			theTuple = ts.waitToTake("cluster_request", new Field(String.class));
        }
	}

	void cluster(Author theAuthor) throws SQLException {
		Vector<Cluster> clusters = new Vector<Cluster>();

		logger.info("clustering: " + theAuthor.getLastName() + ", " + theAuthor.getForeName());
		source.generateClusters(clusters,theAuthor);
		
		dumpClusters(clusters);
		storeClusters(theAuthor,clusters);
		logger.info("");
	}

	void dumpClusters(Vector<Cluster> clusters) {
        for (int i=0; i<clusters.size(); i++) {
            Cluster theCluster = clusters.elementAt(i);
            logger.info("cluster " + i + ":\tvalid: " + theCluster.isValid() + "\trecent: " + theCluster.isRecent());
            for (int j = 0; j < theCluster.getInstances().size(); j++) {
                Instance theInstance = theCluster.getInstances().elementAt(j);
                logger.info("\tinstance: " + theInstance.getTitle());
                logger.info("\t\t" + theInstance.getAuthors());
                for (int k = 0; k < theInstance.getLinkages().size(); k++)
                	logger.info("\t\tLinkage: " + theInstance.getLinkages().elementAt(k));
                logger.info("");
            }
            logger.info("authors:");
            Enumeration<Author> authEnum = theCluster.authors.elements();
            while (authEnum.hasMoreElements()) {
            	Author author = authEnum.nextElement();
            	logger.info("\tauthor: " + author);
            }
        }
	}

	void storeClusters(Author theAuthor, Vector<Cluster> clusters) throws SQLException {
		int nextInt = 0;
		
        for (int i=0; i<clusters.size(); i++) {
            PreparedStatement stat = theConnection.prepareStatement("SELECT nextval ('medline_clustering.seqnum')");
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                nextInt = rs.getInt(1);
            }
            stat.close();
            
            Cluster theCluster = clusters.elementAt(i);
            logger.info("cluster " + i + ":\tvalid: " + theCluster.isValid() + "\trecent: " + theCluster.isRecent());
            
            PreparedStatement authStat = useFirstInitial ? theConnection.prepareStatement("insert into medline_clustering.document_cluster_prefix values (?,?,?)")
            											 : theConnection.prepareStatement("insert into medline_clustering.document_cluster values (?,?,?)");
            authStat.setInt(1, nextInt);
            authStat.setString(2, theAuthor.getLastName());
            authStat.setString(3, theAuthor.getForeName());
            authStat.execute();
            authStat.close();
            
            for (int j = 0; j < theCluster.getInstances().size(); j++) {
                Instance theInstance = theCluster.getInstances().elementAt(j);
                logger.info("\tinstance: " + theInstance.getTitle());
                logger.info("\t\t" + theInstance.getAuthors());
                for (int k = 0; k < theInstance.getLinkages().size(); k++)
                	logger.info("\t\tLinkage: " + theInstance.getLinkages().elementAt(k));
                
                PreparedStatement docStat = useFirstInitial ? theConnection.prepareStatement("insert into medline_clustering.cluster_document_prefix values (?,?)")
                											: theConnection.prepareStatement("insert into medline_clustering.cluster_document values (?,?)");
                docStat.setInt(1, nextInt);
                docStat.setInt(2, theInstance.getLinkages().firstElement().getPub_id());
                docStat.execute();
                docStat.close();
                
                logger.info("");
            }
        }
	}
}
