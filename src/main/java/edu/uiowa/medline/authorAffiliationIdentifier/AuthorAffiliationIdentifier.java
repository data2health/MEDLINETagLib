package edu.uiowa.medline.authorAffiliationIdentifier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.medline.authorAffiliation.AuthorAffiliation;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.Sequence;

@SuppressWarnings("serial")
public class AuthorAffiliationIdentifier extends MEDLINETagLibTagSupport {

	static AuthorAffiliationIdentifier currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(AuthorAffiliationIdentifier.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	int anum = 0;
	int inum = 0;
	String source = null;
	String identifier = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			if (theAuthorAffiliation!= null)
				parentEntities.addElement(theAuthorAffiliation);

			if (theAuthorAffiliation == null) {
			} else {
				pmid = theAuthorAffiliation.getPmid();
				seqnum = theAuthorAffiliation.getSeqnum();
				anum = theAuthorAffiliation.getAnum();
			}

			AuthorAffiliationIdentifierIterator theAuthorAffiliationIdentifierIterator = (AuthorAffiliationIdentifierIterator)findAncestorWithClass(this, AuthorAffiliationIdentifierIterator.class);

			if (theAuthorAffiliationIdentifierIterator != null) {
				pmid = theAuthorAffiliationIdentifierIterator.getPmid();
				seqnum = theAuthorAffiliationIdentifierIterator.getSeqnum();
				anum = theAuthorAffiliationIdentifierIterator.getAnum();
				inum = theAuthorAffiliationIdentifierIterator.getInum();
			}

			if (theAuthorAffiliationIdentifierIterator == null && theAuthorAffiliation == null && inum == 0) {
				// no inum was provided - the default is to assume that it is a new AuthorAffiliationIdentifier and to generate a new inum
				inum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or inum was provided as an attribute - we need to load a AuthorAffiliationIdentifier from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select source,identifier from medline16.author_affiliation_identifier where pmid = ? and seqnum = ? and anum = ? and inum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				stmt.setInt(3,anum);
				stmt.setInt(4,inum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (source == null)
						source = rs.getString(1);
					if (identifier == null)
						identifier = rs.getString(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving inum " + inum, e);
			throw new JspTagException("Error: JDBC error retrieving inum " + inum);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update medline16.author_affiliation_identifier set source = ?, identifier = ? where pmid = ? and seqnum = ? and anum = ? and inum = ?");
				stmt.setString(1,source);
				stmt.setString(2,identifier);
				stmt.setInt(3,pmid);
				stmt.setInt(4,seqnum);
				stmt.setInt(5,anum);
				stmt.setInt(6,inum);
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
			if (inum == 0) {
				inum = Sequence.generateID();
				log.debug("generating new AuthorAffiliationIdentifier " + inum);
			}

			if (source == null)
				source = "";
			if (identifier == null)
				identifier = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline16.author_affiliation_identifier(pmid,seqnum,anum,inum,source,identifier) values (?,?,?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setInt(3,anum);
			stmt.setInt(4,inum);
			stmt.setString(5,source);
			stmt.setString(6,identifier);
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

	public int getInum () {
		return inum;
	}

	public void setInum (int inum) {
		this.inum = inum;
	}

	public int getActualInum () {
		return inum;
	}

	public String getSource () {
		if (commitNeeded)
			return "";
		else
			return source;
	}

	public void setSource (String source) {
		this.source = source;
		commitNeeded = true;
	}

	public String getActualSource () {
		return source;
	}

	public String getIdentifier () {
		if (commitNeeded)
			return "";
		else
			return identifier;
	}

	public void setIdentifier (String identifier) {
		this.identifier = identifier;
		commitNeeded = true;
	}

	public String getActualIdentifier () {
		return identifier;
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

	public static Integer inumValue() throws JspException {
		try {
			return currentInstance.getInum();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function inumValue()");
		}
	}

	public static String sourceValue() throws JspException {
		try {
			return currentInstance.getSource();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function sourceValue()");
		}
	}

	public static String identifierValue() throws JspException {
		try {
			return currentInstance.getIdentifier();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function identifierValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		anum = 0;
		inum = 0;
		source = null;
		identifier = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
