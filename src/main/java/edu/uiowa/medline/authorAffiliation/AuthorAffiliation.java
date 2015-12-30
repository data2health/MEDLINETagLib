package edu.uiowa.medline.authorAffiliation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.medline.author.Author;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.Sequence;

@SuppressWarnings("serial")
public class AuthorAffiliation extends MEDLINETagLibTagSupport {

	static AuthorAffiliation currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(AuthorAffiliation.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	int anum = 0;
	String label = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			if (theAuthor!= null)
				parentEntities.addElement(theAuthor);

			if (theAuthor == null) {
			} else {
				pmid = theAuthor.getPmid();
				seqnum = theAuthor.getSeqnum();
			}

			AuthorAffiliationIterator theAuthorAffiliationIterator = (AuthorAffiliationIterator)findAncestorWithClass(this, AuthorAffiliationIterator.class);

			if (theAuthorAffiliationIterator != null) {
				pmid = theAuthorAffiliationIterator.getPmid();
				seqnum = theAuthorAffiliationIterator.getSeqnum();
				anum = theAuthorAffiliationIterator.getAnum();
			}

			if (theAuthorAffiliationIterator == null && theAuthor == null && anum == 0) {
				// no anum was provided - the default is to assume that it is a new AuthorAffiliation and to generate a new anum
				anum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or anum was provided as an attribute - we need to load a AuthorAffiliation from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select label from medline16.author_affiliation where pmid = ? and seqnum = ? and anum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				stmt.setInt(3,anum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (label == null)
						label = rs.getString(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving anum " + anum, e);
			throw new JspTagException("Error: JDBC error retrieving anum " + anum);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update medline16.author_affiliation set label = ? where pmid = ? and seqnum = ? and anum = ?");
				stmt.setString(1,label);
				stmt.setInt(2,pmid);
				stmt.setInt(3,seqnum);
				stmt.setInt(4,anum);
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
			if (anum == 0) {
				anum = Sequence.generateID();
				log.debug("generating new AuthorAffiliation " + anum);
			}

			if (label == null)
				label = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline16.author_affiliation(pmid,seqnum,anum,label) values (?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setInt(3,anum);
			stmt.setString(4,label);
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

	public int getAnum () {
		return anum;
	}

	public void setAnum (int anum) {
		this.anum = anum;
	}

	public int getActualAnum () {
		return anum;
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

	public static Integer anumValue() throws JspException {
		try {
			return currentInstance.getAnum();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function anumValue()");
		}
	}

	public static String labelValue() throws JspException {
		try {
			return currentInstance.getLabel();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function labelValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		anum = 0;
		label = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
