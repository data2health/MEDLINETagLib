package edu.uiowa.medline.publicationType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.medline.article.Article;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.Sequence;

@SuppressWarnings("serial")
public class PublicationType extends MEDLINETagLibTagSupport {

	static PublicationType currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(PublicationType.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	String label = null;
	String ID = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (theArticle!= null)
				parentEntities.addElement(theArticle);

			if (theArticle == null) {
			} else {
				pmid = theArticle.getPmid();
			}

			PublicationTypeIterator thePublicationTypeIterator = (PublicationTypeIterator)findAncestorWithClass(this, PublicationTypeIterator.class);

			if (thePublicationTypeIterator != null) {
				pmid = thePublicationTypeIterator.getPmid();
				seqnum = thePublicationTypeIterator.getSeqnum();
			}

			if (thePublicationTypeIterator == null && theArticle == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new PublicationType and to generate a new seqnum
				seqnum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a PublicationType from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select label,id from medline16.publication_type where pmid = ? and seqnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (label == null)
						label = rs.getString(1);
					if (ID == null)
						ID = rs.getString(2);
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
				PreparedStatement stmt = getConnection().prepareStatement("update medline16.publication_type set label = ?, id = ? where pmid = ? and seqnum = ?");
				stmt.setString(1,label);
				stmt.setString(2,ID);
				stmt.setInt(3,pmid);
				stmt.setInt(4,seqnum);
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
				log.debug("generating new PublicationType " + seqnum);
			}

			if (label == null)
				label = "";
			if (ID == null)
				ID = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline16.publication_type(pmid,seqnum,label,id) values (?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setString(3,label);
			stmt.setString(4,ID);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
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

	public int getSeqnum () {
		return seqnum;
	}

	public void setSeqnum (int seqnum) {
		this.seqnum = seqnum;
	}

	public int getActualSeqnum () {
		return seqnum;
	}

	public String getLabel () {
		if (commitNeeded)
			return "";
		else
			return label;
	}

	public void setLabel (String label) {
		this.label = label;
		commitNeeded = true;
	}

	public String getActualLabel () {
		return label;
	}

	public String getID () {
		if (commitNeeded)
			return "";
		else
			return ID;
	}

	public void setID (String ID) {
		this.ID = ID;
		commitNeeded = true;
	}

	public String getActualID () {
		return ID;
	}

	public static Integer pmidValue() throws JspException {
		try {
			return currentInstance.getPmid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pmidValue()");
		}
	}

	public static Integer seqnumValue() throws JspException {
		try {
			return currentInstance.getSeqnum();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function seqnumValue()");
		}
	}

	public static String labelValue() throws JspException {
		try {
			return currentInstance.getLabel();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function labelValue()");
		}
	}

	public static String IDValue() throws JspException {
		try {
			return currentInstance.getID();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function IDValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		label = null;
		ID = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
