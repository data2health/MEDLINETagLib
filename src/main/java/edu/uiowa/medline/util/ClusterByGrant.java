package edu.uiowa.medline.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import edu.uiowa.loki.clustering.ExternalSource;
import edu.uiowa.tagUtil.grantParser.nih;

public class ClusterByGrant {
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
		props.setProperty("user", "eichmann");
		props.setProperty("password", "translational");
//		props.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
//		props.setProperty("ssl", "true");
		theConnection = DriverManager.getConnection("jdbc:postgresql://localhost/loki", props);
		theConnection.setAutoCommit(false);

		ClusterByGrant theClusterer = new ClusterByGrant();
		
//		theClusterer.solo();
		theClusterer.tspace();
	}
	
	void solo() throws SQLException {
//		cluster(new Author("Bickenbach", "J R"));
		cluster(new Author("Engelhardt", "John F"));
		theConnection.commit();
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
			theConnection.commit();

			theTuple = ts.waitToTake("cluster_request", new Field(String.class), new Field(String.class));
        }
	}

	void cluster(Author theAuthor) throws SQLException {
		logger.info("");
		logger.info("author: " + theAuthor.getLastName() + "\t" + theAuthor.getForeName());
		logger.info("");
		Vector<Cluster> clusters = new Vector<Cluster>();
		
		PreparedStatement stmt = theConnection.prepareStatement("select cid from medline_clustering.document_cluster_2 where last_name=? and fore_name=? order by cid");
		stmt.setString(1, theAuthor.getLastName());
		stmt.setString(2, theAuthor.getForeName());
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			int cid = rs.getInt(1);
			logger.info("cluster: " + cid);
			Cluster theCluster = new Cluster(cid);
			clusters.add(theCluster);
			
			PreparedStatement grantStmt = theConnection.prepareStatement("select gid from medline13.grant,medline_clustering.document_cluster_2 as cluster,medline_clustering.cluster_document_2 as document where cluster.cid=? and medline13.grant.pmid=document.pmid and document.cid=cluster.cid");
			grantStmt.setInt(1, cid);
			ResultSet grs = grantStmt.executeQuery();
			while (grs.next()) {
				String gid = grs.getString(1);
				logger.info("\tgrant: " + gid);
				theCluster.grants.add(gid);
			}
			grantStmt.close();
		}
		stmt.close();
		
		logger.info("======= cluster merge =======");
		for (Cluster current : clusters) {
			if (current.merged) {
				logger.info("<< skipping already merged cluster " + current.cid);
				continue;
			}
			logger.info("cluster: " + current.cid);
			fence: for (Cluster fence : clusters) {
				if (fence.cid <= current.cid)
					continue;
				if (fence.merged) {
					logger.info("<< skipping already merged cluster " + fence.cid);
					continue;
				}
				logger.info("\tchecking cluster: " + fence.cid);
				for (String current_grant : current.grants) {
					for (String fence_grant : fence.grants) {
						if (nih.matches(current_grant,fence_grant,1)) {
							logger.info("\t\tgrant match:");
							logger.info("\t\t\t" + current.cid + "\t" + current_grant);
							logger.info("\t\t\t" + fence.cid + "\t" + fence_grant);
							fence.merged = true;
							current.grants.addAll(fence.grants);
							current.mergedClusters.add(fence);
							continue fence;
						}
					}
				}
			}
		}
		dumpClusters(clusters);
		remapClusters(clusters);
	}
	
	void dumpClusters(Vector<Cluster> clusters) {
		logger.info("======= cluster list =======");
        for (int i=0; i<clusters.size(); i++) {
            Cluster theCluster = clusters.elementAt(i);
            if (theCluster.merged)
            	continue;
            logger.info("cluster " + theCluster.cid);
            for (String gid : theCluster.grants) {
            	logger.info("\t" + gid);
            }
            logger.info("\tmerged clusters:");
            for (Cluster merged : theCluster.mergedClusters) {
            	logger.info("\t\t" + merged.cid);
            }
        }
	}

	void remapClusters(Vector<Cluster> clusters) throws SQLException {
		for (Cluster cluster : clusters) {
			for (Cluster merged : cluster.mergedClusters) {
	            PreparedStatement docStat = theConnection.prepareStatement("update medline_clustering.cluster_document_2 set cid = ? where cid = ?");
	            docStat.setInt(1, cluster.cid);
	            docStat.setInt(2, merged.cid);
	            docStat.execute();
	            docStat.close();
				
	            PreparedStatement clusterStat = theConnection.prepareStatement("delete from medline_clustering.document_cluster_2 where cid = ?");
	            clusterStat.setInt(1, merged.cid);
	            clusterStat.execute();
	            clusterStat.close();
			}
			
		}
	}
	
	class Cluster {
		boolean merged = false;
		int cid = 0;
		Vector<String> grants = new Vector<String>();
		Vector<Cluster> mergedClusters = new Vector<Cluster>();
		
		public Cluster(int cid) {
			this.cid = cid;
		}
	}
}
