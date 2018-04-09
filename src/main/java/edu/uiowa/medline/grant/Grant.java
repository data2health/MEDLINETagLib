package edu.uiowa.medline.grant;

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
public class Grant extends MEDLINETagLibTagSupport {

	static Grant currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Grant.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	String gid = null;
	String acronym = null;
	String agency = null;
	String country = null;

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

			GrantIterator theGrantIterator = (GrantIterator)findAncestorWithClass(this, GrantIterator.class);

			if (theGrantIterator != null) {
				pmid = theGrantIterator.getPmid();
				seqnum = theGrantIterator.getSeqnum();
			}

			if (theGrantIterator == null && theArticle == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new Grant and to generate a new seqnum
				seqnum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a Grant from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select gid,acronym,agency,country from medline18.grant where pmid = ? and seqnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (gid == null)
						gid = rs.getString(1);
					if (acronym == null)
						acronym = rs.getString(2);
					if (agency == null)
						agency = rs.getString(3);
					if (country == null)
						country = rs.getString(4);
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
				PreparedStatement stmt = getConnection().prepareStatement("update medline18.grant set gid = ?, acronym = ?, agency = ?, country = ? where pmid = ? and seqnum = ?");
				stmt.setString(1,gid);
				stmt.setString(2,acronym);
				stmt.setString(3,agency);
				stmt.setString(4,country);
				stmt.setInt(5,pmid);
				stmt.setInt(6,seqnum);
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
				log.debug("generating new Grant " + seqnum);
			}

			if (gid == null)
				gid = "";
			if (acronym == null)
				acronym = "";
			if (agency == null)
				agency = "";
			if (country == null)
				country = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline18.grant(pmid,seqnum,gid,acronym,agency,country) values (?,?,?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setString(3,gid);
			stmt.setString(4,acronym);
			stmt.setString(5,agency);
			stmt.setString(6,country);
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

	public String getGid () {
		if (commitNeeded)
			return "";
		else
			return gid;
	}

	public void setGid (String gid) {
		this.gid = gid;
		commitNeeded = true;
	}

	public String getActualGid () {
		return gid;
	}

	public String getAcronym () {
		if (commitNeeded)
			return "";
		else
			return acronym;
	}

	public void setAcronym (String acronym) {
		this.acronym = acronym;
		commitNeeded = true;
	}

	public String getActualAcronym () {
		return acronym;
	}

	public String getAgency () {
		if (commitNeeded)
			return "";
		else
			return agency;
	}

	public void setAgency (String agency) {
		this.agency = agency;
		commitNeeded = true;
	}

	public String getActualAgency () {
		return agency;
	}

	public String getCountry () {
		if (commitNeeded)
			return "";
		else
			return country;
	}

	public void setCountry (String country) {
		this.country = country;
		commitNeeded = true;
	}

	public String getActualCountry () {
		return country;
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

	public static String gidValue() throws JspException {
		try {
			return currentInstance.getGid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function gidValue()");
		}
	}

	public static String acronymValue() throws JspException {
		try {
			return currentInstance.getAcronym();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function acronymValue()");
		}
	}

	public static String agencyValue() throws JspException {
		try {
			return currentInstance.getAgency();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function agencyValue()");
		}
	}

	public static String countryValue() throws JspException {
		try {
			return currentInstance.getCountry();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function countryValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		gid = null;
		acronym = null;
		agency = null;
		country = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
