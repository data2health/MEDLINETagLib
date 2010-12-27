package edu.uiowa.medline.clusterDocument;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.medline.article.Article;
import edu.uiowa.medline.documentCluster.DocumentCluster;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.Sequence;

@SuppressWarnings("serial")

public class ClusterDocument extends MEDLINETagLibTagSupport {

	static ClusterDocument currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(ClusterDocument.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int cid = 0;
	int pmid = 0;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (theArticle!= null)
				parentEntities.addElement(theArticle);
			DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
			if (theDocumentCluster!= null)
				parentEntities.addElement(theDocumentCluster);

			if (theArticle == null) {
			} else {
				pmid = theArticle.getPmid();
			}
			if (theDocumentCluster == null) {
			} else {
				cid = theDocumentCluster.getCid();
			}

			ClusterDocumentIterator theClusterDocumentIterator = (ClusterDocumentIterator)findAncestorWithClass(this, ClusterDocumentIterator.class);

			if (theClusterDocumentIterator != null) {
				cid = theClusterDocumentIterator.getCid();
				pmid = theClusterDocumentIterator.getPmid();
			}

			if (theClusterDocumentIterator == null && theArticle == null && theDocumentCluster == null && cid == 0) {
				// no cid was provided - the default is to assume that it is a new ClusterDocument and to generate a new cid
				cid = Sequence.generateID();
				log.debug("generating new ClusterDocument " + cid);
				insertEntity();
			} else if (theClusterDocumentIterator == null && theArticle != null && theDocumentCluster == null) {
				// an cid was provided as an attribute - we need to load a ClusterDocument from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select cid from medline_clustering.cluster_document where pmid = ?");
				stmt.setInt(1,pmid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (cid == 0)
						cid = rs.getInt(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (theClusterDocumentIterator == null && theArticle == null && theDocumentCluster != null) {
				// an cid was provided as an attribute - we need to load a ClusterDocument from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select pmid from medline_clustering.cluster_document where cid = ?");
				stmt.setInt(1,cid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (pmid == 0)
						pmid = rs.getInt(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or cid was provided as an attribute - we need to load a ClusterDocument from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select 1 from medline_clustering.cluster_document where cid = ? and pmid = ?");
				stmt.setInt(1,cid);
				stmt.setInt(2,pmid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error retrieving cid " + cid);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update medline_clustering.cluster_document set where cid = ? and pmid = ?");
				stmt.setInt(1,cid);
				stmt.setInt(2,pmid);
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			clearServiceState();
			freeConnection();
		}
		return super.doEndTag();
	}

	public void insertEntity() throws JspException {
		try {
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline_clustering.cluster_document(cid,pmid) values (?,?)");
			stmt.setInt(1,cid);
			stmt.setInt(2,pmid);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
	}

	public int getCid () {
		return cid;
	}

	public void setCid (int cid) {
		this.cid = cid;
	}

	public int getActualCid () {
		return cid;
	}

	public int getPmid () {
		return pmid;
	}

	public void setPmid (int pmid) {
		this.pmid = pmid;
	}

	public int getActualPmid () {
		return pmid;
	}

	public static int cidValue() throws JspException {
		try {
			return currentInstance.getCid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function cidValue()");
		}
	}

	public static int pmidValue() throws JspException {
		try {
			return currentInstance.getPmid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pmidValue()");
		}
	}

	private void clearServiceState () {
		cid = 0;
		pmid = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
