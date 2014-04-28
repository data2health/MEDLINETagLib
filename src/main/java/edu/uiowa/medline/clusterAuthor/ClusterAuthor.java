package edu.uiowa.medline.clusterAuthor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.medline.documentCluster.DocumentCluster;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.Sequence;

@SuppressWarnings("serial")
public class ClusterAuthor extends MEDLINETagLibTagSupport {

	static ClusterAuthor currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(ClusterAuthor.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int cid = 0;
	int seqnum = 0;
	String lastName = null;
	String foreName = null;
	int occurrences = 0;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
			if (theDocumentCluster!= null)
				parentEntities.addElement(theDocumentCluster);

			if (theDocumentCluster == null) {
			} else {
				cid = theDocumentCluster.getCid();
			}

			ClusterAuthorIterator theClusterAuthorIterator = (ClusterAuthorIterator)findAncestorWithClass(this, ClusterAuthorIterator.class);

			if (theClusterAuthorIterator != null) {
				cid = theClusterAuthorIterator.getCid();
				seqnum = theClusterAuthorIterator.getSeqnum();
			}

			if (theClusterAuthorIterator == null && theDocumentCluster == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new ClusterAuthor and to generate a new seqnum
				seqnum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a ClusterAuthor from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select last_name,fore_name,occurrences from medline_clustering.cluster_author where cid = ? and seqnum = ?");
				stmt.setInt(1,cid);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (lastName == null)
						lastName = rs.getString(1);
					if (foreName == null)
						foreName = rs.getString(2);
					if (occurrences == 0)
						occurrences = rs.getInt(3);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving seqnum " + seqnum, e);
			throw new JspTagException("Error: JDBC error retrieving seqnum " + seqnum);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update medline_clustering.cluster_author set last_name = ?, fore_name = ?, occurrences = ? where cid = ? and seqnum = ?");
				stmt.setString(1,lastName);
				stmt.setString(2,foreName);
				stmt.setInt(3,occurrences);
				stmt.setInt(4,cid);
				stmt.setInt(5,seqnum);
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			clearServiceState();
			freeConnection();
		}
		return super.doEndTag();
	}

	public void insertEntity() throws JspException {
		try {
			if (seqnum == 0) {
				seqnum = Sequence.generateID();
				log.debug("generating new ClusterAuthor " + seqnum);
			}

			if (lastName == null)
				lastName = "";
			if (foreName == null)
				foreName = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline_clustering.cluster_author(cid,seqnum,last_name,fore_name,occurrences) values (?,?,?,?,?)");
			stmt.setInt(1,cid);
			stmt.setInt(2,seqnum);
			stmt.setString(3,lastName);
			stmt.setString(4,foreName);
			stmt.setInt(5,occurrences);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);
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

	public int getSeqnum () {
		return seqnum;
	}

	public void setSeqnum (int seqnum) {
		this.seqnum = seqnum;
	}

	public int getActualSeqnum () {
		return seqnum;
	}

	public String getLastName () {
		if (commitNeeded)
			return "";
		else
			return lastName;
	}

	public void setLastName (String lastName) {
		this.lastName = lastName;
		commitNeeded = true;
	}

	public String getActualLastName () {
		return lastName;
	}

	public String getForeName () {
		if (commitNeeded)
			return "";
		else
			return foreName;
	}

	public void setForeName (String foreName) {
		this.foreName = foreName;
		commitNeeded = true;
	}

	public String getActualForeName () {
		return foreName;
	}

	public int getOccurrences () {
		return occurrences;
	}

	public void setOccurrences (int occurrences) {
		this.occurrences = occurrences;
		commitNeeded = true;
	}

	public int getActualOccurrences () {
		return occurrences;
	}

	public static Integer cidValue() throws JspException {
		try {
			return currentInstance.getCid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function cidValue()");
		}
	}

	public static Integer seqnumValue() throws JspException {
		try {
			return currentInstance.getSeqnum();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function seqnumValue()");
		}
	}

	public static String lastNameValue() throws JspException {
		try {
			return currentInstance.getLastName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function lastNameValue()");
		}
	}

	public static String foreNameValue() throws JspException {
		try {
			return currentInstance.getForeName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function foreNameValue()");
		}
	}

	public static Integer occurrencesValue() throws JspException {
		try {
			return currentInstance.getOccurrences();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function occurrencesValue()");
		}
	}

	private void clearServiceState () {
		cid = 0;
		seqnum = 0;
		lastName = null;
		foreName = null;
		occurrences = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
