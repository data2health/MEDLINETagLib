package edu.uiowa.medline.generalNote;

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
public class GeneralNote extends MEDLINETagLibTagSupport {

	static GeneralNote currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(GeneralNote.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	String note = null;
	String owner = null;

	private String var = null;

	private GeneralNote cachedGeneralNote = null;

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

			GeneralNoteIterator theGeneralNoteIterator = (GeneralNoteIterator)findAncestorWithClass(this, GeneralNoteIterator.class);

			if (theGeneralNoteIterator != null) {
				pmid = theGeneralNoteIterator.getPmid();
				seqnum = theGeneralNoteIterator.getSeqnum();
			}

			if (theGeneralNoteIterator == null && theArticle == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new GeneralNote and to generate a new seqnum
				seqnum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a GeneralNote from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select note,owner from medline12.general_note where pmid = ? and seqnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (note == null)
						note = rs.getString(1);
					if (owner == null)
						owner = rs.getString(2);
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

		GeneralNote currentGeneralNote = (GeneralNote) pageContext.getAttribute("tag_generalNote");
		if(currentGeneralNote != null){
			cachedGeneralNote = currentGeneralNote;
		}
		currentGeneralNote = this;
		pageContext.setAttribute((var == null ? "tag_generalNote" : var), currentGeneralNote);

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(this.cachedGeneralNote != null){
			pageContext.setAttribute((var == null ? "tag_generalNote" : var), this.cachedGeneralNote);
		}else{
			pageContext.removeAttribute((var == null ? "tag_generalNote" : var));
			this.cachedGeneralNote = null;
		}

		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update medline12.general_note set note = ?, owner = ? where pmid = ? and seqnum = ?");
				stmt.setString(1,note);
				stmt.setString(2,owner);
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
				log.debug("generating new GeneralNote " + seqnum);
			}

			if (note == null)
				note = "";
			if (owner == null)
				owner = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline12.general_note(pmid,seqnum,note,owner) values (?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setString(3,note);
			stmt.setString(4,owner);
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

	public String getNote () {
		if (commitNeeded)
			return "";
		else
			return note;
	}

	public void setNote (String note) {
		this.note = note;
		commitNeeded = true;
	}

	public String getActualNote () {
		return note;
	}

	public String getOwner () {
		if (commitNeeded)
			return "";
		else
			return owner;
	}

	public void setOwner (String owner) {
		this.owner = owner;
		commitNeeded = true;
	}

	public String getActualOwner () {
		return owner;
	}

	public String getVar () {
		return var;
	}

	public void setVar (String var) {
		this.var = var;
	}

	public String getActualVar () {
		return var;
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

	public static String noteValue() throws JspException {
		try {
			return currentInstance.getNote();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function noteValue()");
		}
	}

	public static String ownerValue() throws JspException {
		try {
			return currentInstance.getOwner();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function ownerValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		note = null;
		owner = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();
		this.var = null;

	}

}
