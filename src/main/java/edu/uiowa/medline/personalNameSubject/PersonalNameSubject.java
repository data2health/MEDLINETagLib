package edu.uiowa.medline.personalNameSubject;

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
public class PersonalNameSubject extends MEDLINETagLibTagSupport {

	static PersonalNameSubject currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(PersonalNameSubject.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	String lastName = null;
	String foreName = null;
	String initials = null;
	String suffix = null;

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

			PersonalNameSubjectIterator thePersonalNameSubjectIterator = (PersonalNameSubjectIterator)findAncestorWithClass(this, PersonalNameSubjectIterator.class);

			if (thePersonalNameSubjectIterator != null) {
				pmid = thePersonalNameSubjectIterator.getPmid();
				seqnum = thePersonalNameSubjectIterator.getSeqnum();
			}

			if (thePersonalNameSubjectIterator == null && theArticle == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new PersonalNameSubject and to generate a new seqnum
				seqnum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a PersonalNameSubject from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select last_name,fore_name,initials,suffix from medline18.personal_name_subject where pmid = ? and seqnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (lastName == null)
						lastName = rs.getString(1);
					if (foreName == null)
						foreName = rs.getString(2);
					if (initials == null)
						initials = rs.getString(3);
					if (suffix == null)
						suffix = rs.getString(4);
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
				PreparedStatement stmt = getConnection().prepareStatement("update medline18.personal_name_subject set last_name = ?, fore_name = ?, initials = ?, suffix = ? where pmid = ? and seqnum = ?");
				stmt.setString(1,lastName);
				stmt.setString(2,foreName);
				stmt.setString(3,initials);
				stmt.setString(4,suffix);
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
				log.debug("generating new PersonalNameSubject " + seqnum);
			}

			if (lastName == null)
				lastName = "";
			if (foreName == null)
				foreName = "";
			if (initials == null)
				initials = "";
			if (suffix == null)
				suffix = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline18.personal_name_subject(pmid,seqnum,last_name,fore_name,initials,suffix) values (?,?,?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setString(3,lastName);
			stmt.setString(4,foreName);
			stmt.setString(5,initials);
			stmt.setString(6,suffix);
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

	public String getInitials () {
		if (commitNeeded)
			return "";
		else
			return initials;
	}

	public void setInitials (String initials) {
		this.initials = initials;
		commitNeeded = true;
	}

	public String getActualInitials () {
		return initials;
	}

	public String getSuffix () {
		if (commitNeeded)
			return "";
		else
			return suffix;
	}

	public void setSuffix (String suffix) {
		this.suffix = suffix;
		commitNeeded = true;
	}

	public String getActualSuffix () {
		return suffix;
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

	public static String initialsValue() throws JspException {
		try {
			return currentInstance.getInitials();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function initialsValue()");
		}
	}

	public static String suffixValue() throws JspException {
		try {
			return currentInstance.getSuffix();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function suffixValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		lastName = null;
		foreName = null;
		initials = null;
		suffix = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
