package edu.uiowa.medline.documentCluster;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.Sequence;

@SuppressWarnings("serial")
public class DocumentCluster extends MEDLINETagLibTagSupport {

	static DocumentCluster currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(DocumentCluster.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int cid = 0;
	String lastName = null;
	String foreName = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			DocumentClusterIterator theDocumentClusterIterator = (DocumentClusterIterator)findAncestorWithClass(this, DocumentClusterIterator.class);

			if (theDocumentClusterIterator != null) {
				cid = theDocumentClusterIterator.getCid();
			}

			if (theDocumentClusterIterator == null && cid == 0) {
				// no cid was provided - the default is to assume that it is a new DocumentCluster and to generate a new cid
				cid = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or cid was provided as an attribute - we need to load a DocumentCluster from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select last_name,fore_name from medline_clustering.document_cluster where cid = ?");
				stmt.setInt(1,cid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (lastName == null)
						lastName = rs.getString(1);
					if (foreName == null)
						foreName = rs.getString(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving cid " + cid, e);
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
				PreparedStatement stmt = getConnection().prepareStatement("update medline_clustering.document_cluster set last_name = ?, fore_name = ? where cid = ?");
				stmt.setString(1,lastName);
				stmt.setString(2,foreName);
				stmt.setInt(3,cid);
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
			if (cid == 0) {
				cid = Sequence.generateID();
				log.debug("generating new DocumentCluster " + cid);
			}

			if (lastName == null)
				lastName = "";
			if (foreName == null)
				foreName = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline_clustering.document_cluster(cid,last_name,fore_name) values (?,?,?)");
			stmt.setInt(1,cid);
			stmt.setString(2,lastName);
			stmt.setString(3,foreName);
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

	public static Integer cidValue() throws JspException {
		try {
			return currentInstance.getCid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function cidValue()");
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

	private void clearServiceState () {
		cid = 0;
		lastName = null;
		foreName = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
