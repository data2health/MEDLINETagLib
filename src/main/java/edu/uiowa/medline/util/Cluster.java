package edu.uiowa.medline.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.loki.clustering.ExternalSource;

public class Cluster {
	protected static final Log logger = LogFactory.getLog(ExternalSource.class);
	String last_name = null;
	String fore_name = null;
	int ID = 0;
	int count = 0;
	
	Vector<Grant> grant_ids = new Vector<Grant>();
	Vector<ORCiD> orcids = new Vector<ORCiD>();
	Vector<Affiliation> affiliations = new Vector<Affiliation>();
	Vector<Term> terms = new Vector<Term>();
	
	Hashtable<String, Grant> grantHash = new Hashtable<String, Grant>();
	Hashtable<String, ORCiD> orcidHash = new Hashtable<String, ORCiD>();
	Hashtable<String, Affiliation> affiliationHash = new Hashtable<String, Affiliation>();

	Vector<Cluster> subclusters = new Vector<Cluster>();
	
	public Cluster(String last_name, String fore_name, int ID, Connection conn) throws SQLException {
		logger.info("loading cluster " + ID);
		this.last_name = last_name;
		this.fore_name = fore_name;
		this.ID = ID;
		
		PreparedStatement stmt = conn.prepareStatement("select count(*) from medline_clustering.cluster_document where cid = ?");
		stmt.setInt(1, ID);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			count = rs.getInt(1);
		}
		stmt.close();
		
		stmt = conn.prepareStatement("select grant_id, count from medline_clustering.author_cluster_grant where cid = ?");
		stmt.setInt(1, ID);
		rs = stmt.executeQuery();
		while (rs.next()) {
			Grant grant = new Grant(rs.getString(1), rs.getInt(2));
			grant_ids.add(grant);
			grantHash.put(grant.getGrant_id(), grant);
			logger.info("\tgrant: " + grant.getGrant_id() + " (" + grant.getCount() + ")");
		}
		stmt.close();
		
		stmt = conn.prepareStatement("select orcid, count from medline_clustering.author_cluster_orcid where cid = ?");
		stmt.setInt(1, ID);
		rs = stmt.executeQuery();
		while (rs.next()) {
			ORCiD orcid = new ORCiD(rs.getString(1), rs.getInt(2));
			orcids.add(orcid);
			orcidHash.put(orcid.getOrcid_id(), orcid);
			logger.info("\torcid: " + orcid.getOrcid_id() + " (" + orcid.getCount() + ")");
		}
		stmt.close();
		
		stmt = conn.prepareStatement("select name, id, count from medline_clustering.author_cluster_affiliation where cid = ?");
		stmt.setInt(1, ID);
		rs = stmt.executeQuery();
		while (rs.next()) {
			Affiliation affiliation = new Affiliation(rs.getString(1), rs.getString(2), rs.getInt(3));
			affiliations.add(affiliation);
			affiliationHash.put(affiliation.ror, affiliation);
			logger.info("\taffiliation: " + affiliation.getName() + " (" + affiliation.getCount() + ")");
		}
		stmt.close();
		
		stmt = conn.prepareStatement("select descriptor_name, tfidf from medline_clustering.mesh_tfidf where cid = ? order by descriptor_name");
		stmt.setInt(1, ID);
		rs = stmt.executeQuery();
		while (rs.next()) {
			Term term = new Term(rs.getString(1), rs.getDouble(2));
			terms.add(term);
			logger.info("\tterm: " + term.getTerm() + " (" + term.getFrequency() + ")");
		}
		stmt.close();
	}
	
	public int grantMatchCount(String grant_id) {
		int count = 0;
		
		for (Grant grant : grant_ids) {
			if (grant.getGrant_id().equals(grant_id))
				count += grant.getCount();
		}
		
		return count;
	}
	
	public int orcidMatchCount(String orcid_id) {
		int count = 0;
		
		for (ORCiD orcid : orcids) {
			if (orcid.getOrcid_id().equals(orcid_id))
				count += orcid.getCount();
		}
		
		return count;
	}
	
	public int affiliationMatchCount(String ror) {
		int count = 0;
		
		for (Affiliation affiliation : affiliations) {
			if (affiliation.getRor().equals(ror))
				count += affiliation.getCount();
		}
		
		return count;
	}
	
	public int grantMatchCount(Cluster candidate) {
		int count = 0;
		for (Grant grant : grant_ids) {
			count += candidate.grantMatchCount(grant.getGrant_id());
		}
		return count;
	}
	
	public int orcidMatchCount(Cluster candidate) {
		int count = 0;
		for (ORCiD orcid : orcids) {
			count += candidate.orcidMatchCount(orcid.getOrcid_id());
		}
		return count;
	}
	
	public int affiliationMatchCount(Cluster candidate) {
		int count = 0;
		for (Affiliation affiliation : affiliations) {
			count += candidate.affiliationMatchCount(affiliation.getRor());
		}
		return count;
	}
	
	public void addSubcluster (Cluster subcluster) {
		subclusters.add(subcluster);
		for (Grant grant : subcluster.grant_ids) {
			grantHash.put(grant.getGrant_id(), grant);
			grant_ids.add(grant);
		}
		for (ORCiD orcid : subcluster.orcids) {
			orcidHash.put(orcid.getOrcid_id(), orcid);
			orcids.add(orcid);
		}
		for (Affiliation affiliation : subcluster.affiliations) {
			affiliationHash.put(affiliation.getRor(), affiliation);
			affiliations.add(affiliation);
		}
	}
	
	public int fullCount() {
		int cnt = count;
		for (Cluster cluster : subclusters) {
			cnt += cluster.count;
		}
		return cnt;
	}
	
	public String toString() {
		return last_name + " " + fore_name + " " + ID + " (" + count + (subclusters.size() == 0 ? "" : ", " + fullCount()) + ")";
	}
}
