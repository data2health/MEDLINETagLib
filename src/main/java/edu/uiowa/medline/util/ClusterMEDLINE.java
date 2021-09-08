package edu.uiowa.medline.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
import edu.uiowa.loki.clustering.Clusterer;
import edu.uiowa.loki.clustering.ExternalSource;
import edu.uiowa.loki.clustering.Instance;

public class ClusterMEDLINE extends Clusterer {
	public ClusterMEDLINE() throws Exception {
		super(0);
	}
	public ClusterMEDLINE(int uid) throws Exception {
		super(uid);
		// TODO Auto-generated constructor stub
	}

	final static boolean useFirstInitial = false;
	
	protected static final Log logger = LogFactory.getLog(ExternalSource.class);
	static Connection theConnection = null;
	static LocalProperties prop_file = null;
	ExternalSource source = new ClusteringSource();

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure(args[0]);
		prop_file = PropertyLoader.loadProperties("medline_clustering");

        Class.forName("org.postgresql.Driver");
		Properties props = new Properties();
		props.setProperty("user", prop_file.getProperty("jdbc.user"));
		props.setProperty("password", prop_file.getProperty("jdbc.password"));
//		props.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
//		props.setProperty("ssl", "true");
		theConnection = DriverManager.getConnection(prop_file.getProperty("jdbc.url"), props);
		theConnection.setAutoCommit(false);
		conn = theConnection;
		
		ClusterMEDLINE theClusterer = new ClusterMEDLINE();
		theClusterer.source.setParentCluster(theClusterer);
//		theClusterer.solo();
		if (args.length > 1 && args[1].equals("null"))
				theClusterer.tspace_null();
		else if (args.length > 1 && args[1].equals("aggregate"))
			theClusterer.aggregate();
		else
			theClusterer.tspace();
//		theClusterer.unicodeInitial();
	}
	
	void aggregate() throws SQLException {
		PreparedStatement stmt = theConnection.prepareStatement("select distinct last_name,substring(fore_name from 1 for 1) "
																+ "from medline_clustering.document_cluster "
																+ "where (last_name,substring(fore_name from 1 for 1)) not in (select distinct last_name,substring(fore_name from 1 for 1) "
																															+ "from medline_clustering.document_cluster "
																															+ "where cid in (select super from medline_clustering.supercluster) "
																															+ "   or cid in (select sub from medline_clustering.supercluster)) "
																+ "order by 1,2");
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			logger.info(rs.getString(1) + "\t" + rs.getString(2));
			aggregate(rs.getString(1), rs.getString(2));
		}
		stmt.close();
	}
	
	void aggregate(String lastName, String initial) throws SQLException {
		Vector<edu.uiowa.medline.util.Cluster> clusters = new Vector<edu.uiowa.medline.util.Cluster>();
		
		PreparedStatement stmt = theConnection.prepareStatement("select last_name, fore_name, cid from medline_clustering.document_cluster where last_name = ? and fore_name ~ ?");
		stmt.setString(1, lastName);
		stmt.setString(2, "^"+initial);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			String last_name = rs.getString(1);
			String fore_name = rs.getString(2);
			int cid = rs.getInt(3);
			clusters.add(new edu.uiowa.medline.util.Cluster(last_name, fore_name, cid, theConnection));
		}
		stmt.close();
		
		logger.info("Aggregating " + clusters.size() + " clusters...");
		boolean merged = false;
		do {
			merged = false;
			for (int fenceID = 0; fenceID < clusters.size() - 1; fenceID++) {
				edu.uiowa.medline.util.Cluster fence = clusters.elementAt(fenceID);
				for (int i = fenceID + 1; i < clusters.size(); i++) {
					int grantMatchCount = fence.grantMatchCount(clusters.elementAt(i));
					int orcidMatchCount = fence.orcidMatchCount(clusters.elementAt(i));
					int affiliationMatchCount = fence.affiliationMatchCount(clusters.elementAt(i));

					if (grantMatchCount > 0 || orcidMatchCount > 0 || affiliationMatchCount > 0) {
						logger.info("matches: " + fence.ID + " <-> " + clusters.elementAt(i).ID + " : " + grantMatchCount
								+ "," + orcidMatchCount + "," + affiliationMatchCount);
						fence.addSubcluster(clusters.elementAt(i));
						clusters.remove(i);
						i--;
						merged = true;
					}
				}
			} 
		} while (merged);
		
//		simpleStmt("truncate medline_clustering.supercluster");
		for (edu.uiowa.medline.util.Cluster cluster : clusters) {
			if (cluster.subclusters.size() == 0) {
				PreparedStatement insStmt = theConnection.prepareStatement("insert into medline_clustering.supercluster values(?,?)");
				insStmt.setInt(1, cluster.ID);
				insStmt.setNull(2, Types.INTEGER);
				insStmt.execute();
				insStmt.close();			
			}
			for (edu.uiowa.medline.util.Cluster subcluster : cluster.subclusters) {
				PreparedStatement insStmt = theConnection.prepareStatement("insert into medline_clustering.supercluster values(?,?)");
				insStmt.setInt(1, cluster.ID);
				insStmt.setInt(2, subcluster.ID);
				insStmt.execute();
				insStmt.close();
			}
		}
		
		logger.info("Aggregated clusters:");
		dumpSuperClusters(clusters);
		
		similarityMerge(clusters);
		
//		simpleStmt("truncate medline_clustering.supercluster_similarity");
		for (edu.uiowa.medline.util.Cluster cluster : clusters) {
			for (edu.uiowa.medline.util.Cluster subcluster : cluster.subclusters) {
				PreparedStatement insStmt = theConnection.prepareStatement("insert into medline_clustering.supercluster_similarity values(?,?)");
				insStmt.setInt(1, cluster.ID);
				insStmt.setInt(2, subcluster.ID);
				insStmt.execute();
				insStmt.close();
			}
		}
		
		theConnection.commit();

		logger.info("Similarity Aggregated clusters:");
		dumpSuperClusters(clusters);
	}
	
	void dumpSuperClusters(Vector<edu.uiowa.medline.util.Cluster> clusters) throws SQLException {
		for (edu.uiowa.medline.util.Cluster cluster : clusters) {
			logger.info("primary: " + cluster);
			
			PreparedStatement stmt = theConnection.prepareStatement("select article_title from medline.article_title natural join medline_clustering.cluster_document where cid = ?");
			stmt.setInt(1, cluster.ID);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				logger.info("\t" + rs.getString(1));
			}
			stmt.close();
			logger.debug("");
			
			for (edu.uiowa.medline.util.Cluster subcluster : cluster.subclusters) {
				logger.info("\tsecondary: " + subcluster);
				
				stmt = theConnection.prepareStatement("select article_title from medline.article_title natural join medline_clustering.cluster_document where cid = ?");
				stmt.setInt(1, subcluster.ID);
				rs = stmt.executeQuery();
				while (rs.next()) {
					logger.info("\t\t" + rs.getString(1));
				}
				stmt.close();
			}
			
		}
	}
	
	MeshSimilarity comparator = new MeshSimilarity();
	double similarityThreshold = 0.10;

	void similarityMerge(Vector<edu.uiowa.medline.util.Cluster> clusters) {
		logger.info("merging by similarity:");
		for (int fenceID = 1; fenceID < clusters.size(); fenceID++) {
			MatchMetadata match = new MatchMetadata();
			similarityMerge(clusters, fenceID, match);
			logger.info(clusters.elementAt(fenceID) + " best match: " + match.bestSuperCluster + " : " + match.bestCluster + " : " + match.bestMatch);
			
			if (match.bestMatch > similarityThreshold) {
				match.bestSuperCluster.subclusters.add(clusters.elementAt(fenceID));
				for (edu.uiowa.medline.util.Cluster cluster : clusters.elementAt(fenceID).subclusters) {
					match.bestSuperCluster.subclusters.add(cluster);
				}
				clusters.elementAt(fenceID).subclusters.clear();
				clusters.remove(fenceID);
				fenceID--;
			}
		}
	}
	
	void similarityMerge(Vector<edu.uiowa.medline.util.Cluster> clusters, int fenceID, MatchMetadata match) {
		// do the supercluster
		similarityMerge(clusters, fenceID, clusters.elementAt(fenceID), match);
		
		// now do the supercluster's subclusters
		for (edu.uiowa.medline.util.Cluster current : clusters.elementAt(fenceID).subclusters) {
			similarityMerge(clusters, fenceID, current, match);
		}
	}
	
	void similarityMerge(Vector<edu.uiowa.medline.util.Cluster> clusters, int fenceID, edu.uiowa.medline.util.Cluster current, MatchMetadata match) {
		for (int i = 0; i < fenceID; i++) {
			double similarity = comparator.similarity(clusters.elementAt(i).terms, current.terms);
			logger.debug("\tsimilarity " + current.ID + ", " + clusters.elementAt(i).ID + " : " + similarity);
			if (similarity > match.bestMatch && nameMatch(current, clusters.elementAt(i))) {
				logger.info("\tcurrent: " + current.fore_name + "\tsuper: " + clusters.elementAt(i).fore_name);
				match.bestSuperCluster = clusters.elementAt(i);
				match.bestCluster = clusters.elementAt(i);
				match.bestMatch = similarity;
			}
			
			for (edu.uiowa.medline.util.Cluster cluster : clusters.elementAt(i).subclusters) {
				double sim = comparator.similarity(cluster.terms, current.terms);
				logger.debug("\t\tsimilarity " + current.ID + ", " + cluster.ID + " : " + similarity);
				if (sim > match.bestMatch && nameMatch(current, clusters.elementAt(i))) {
					logger.info("\tcurrent: " + current.fore_name + "\tsub: " + clusters.elementAt(i).fore_name);
					match.bestSuperCluster = clusters.elementAt(i);
					match.bestCluster = cluster;
					match.bestMatch = sim;
				}
			}
		}		
	}
	
	boolean nameMatch(edu.uiowa.medline.util.Cluster current, edu.uiowa.medline.util.Cluster candidate) {
		if (current.fore_name.equals(candidate.fore_name))
			return true;
		
		String[] currentElements = current.fore_name.split(" ");
		String[] candidateElements = candidate.fore_name.split(" ");
		
		if (currentElements.length == candidateElements.length) {
			if (currentElements[0].equals(candidateElements[0])) {
				if (currentElements.length > 1 && !currentElements[1].equals(candidateElements[1]))
					return false;
				if (currentElements.length > 2 && !currentElements[2].equals(candidateElements[2]))
					return false;
				return true;
			}
			if (currentElements[0].length() > 0 && candidateElements[0].length() == 1 && currentElements[0].charAt(0) == candidateElements[0].charAt(0)) {
				if (currentElements.length > 1 && !currentElements[1].equals(candidateElements[1]))
					return false;
				if (currentElements.length > 2 && !currentElements[2].equals(candidateElements[2]))
					return false;
				return true;
			}
			if (currentElements[0].length() == 1 && candidateElements[0].length() > 0 && currentElements[0].charAt(0) == candidateElements[0].charAt(0)) {
				if (currentElements.length > 1 && !currentElements[1].equals(candidateElements[1]))
					return false;
				if (currentElements.length > 2 && !currentElements[2].equals(candidateElements[2]))
					return false;
				return true;
			}
		}
		
		return false;
	}
	
	class MatchMetadata {
		edu.uiowa.medline.util.Cluster bestSuperCluster = null;
		edu.uiowa.medline.util.Cluster bestCluster = null;
		double bestMatch = 0.0;
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
			ts = new TupleSpace(prop_file.getProperty("tspace.space"), prop_file.getProperty("tspace.server"));
		} catch (TupleSpaceException tse) {
			logger.error("TSpace error: " + tse);
		}
		
        Tuple theTuple = null;

        theTuple = ts.take("cluster_request", new Field(String.class), new Field(String.class));

        while (theTuple != null) {
            String lastName = (String) theTuple.getField(1).getValue();
            String foreName = (String) theTuple.getField(2).getValue();
            logger.info("consuming " + lastName + ", " + foreName);

            sourceIDHash.clear();
            cluster(new Author(lastName, foreName));

			PreparedStatement compStmt = useFirstInitial ? theConnection.prepareStatement("update medline_clustering.author_prefix set completed = true where last_name = ? and initial = ?")
														 : theConnection.prepareStatement("update medline_clustering.author_count set completed = true where last_name = ? and fore_name = ?");
			compStmt.setString(1, lastName);
			compStmt.setString(2, foreName);
			compStmt.execute();
			compStmt.close();
			theConnection.commit();

			theTuple = ts.take("cluster_request", new Field(String.class), new Field(String.class));
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

    void simpleStmt(String queryString) {
	try {
	    logger.debug("executing " + queryString + "...");
	    PreparedStatement beginStmt = theConnection.prepareStatement(queryString);
	    beginStmt.executeUpdate();
	    beginStmt.close();
	} catch (Exception e) {
	    logger.error("Error in database initialization: " + e);
	    e.printStackTrace();
	}
    }

}
