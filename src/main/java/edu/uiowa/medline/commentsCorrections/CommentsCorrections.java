package edu.uiowa.medline.commentsCorrections;

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

public class CommentsCorrections extends MEDLINETagLibTagSupport {

	static CommentsCorrections currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(CommentsCorrections.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	String refType = null;
	String source = null;
	int refPmid = 0;
	String note = null;

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

			CommentsCorrectionsIterator theCommentsCorrectionsIterator = (CommentsCorrectionsIterator)findAncestorWithClass(this, CommentsCorrectionsIterator.class);

			if (theCommentsCorrectionsIterator != null) {
				pmid = theCommentsCorrectionsIterator.getPmid();
				seqnum = theCommentsCorrectionsIterator.getSeqnum();
			}

			if (theCommentsCorrectionsIterator == null && theArticle == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new CommentsCorrections and to generate a new seqnum
				seqnum = Sequence.generateID();
				log.debug("generating new CommentsCorrections " + seqnum);
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a CommentsCorrections from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select ref_type,source,ref_pmid,note from medline10.comments_corrections where pmid = ? and seqnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (refType == null)
						refType = rs.getString(1);
					if (source == null)
						source = rs.getString(2);
					if (refPmid == 0)
						refPmid = rs.getInt(3);
					if (note == null)
						note = rs.getString(4);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
				PreparedStatement stmt = getConnection().prepareStatement("update medline10.comments_corrections set ref_type = ?, source = ?, ref_pmid = ?, note = ? where pmid = ? and seqnum = ?");
				stmt.setString(1,refType);
				stmt.setString(2,source);
				stmt.setInt(3,refPmid);
				stmt.setString(4,note);
				stmt.setInt(5,pmid);
				stmt.setInt(6,seqnum);
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
			if (seqnum == 0) {
				seqnum = Sequence.generateID();
				log.debug("generating new CommentsCorrections " + seqnum);
			}

			if (refType == null)
				refType = "";
			if (source == null)
				source = "";
			if (note == null)
				note = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline10.comments_corrections(pmid,seqnum,ref_type,source,ref_pmid,note) values (?,?,?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setString(3,refType);
			stmt.setString(4,source);
			stmt.setInt(5,refPmid);
			stmt.setString(6,note);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
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

	public String getRefType () {
		if (commitNeeded)
			return "";
		else
			return refType;
	}

	public void setRefType (String refType) {
		this.refType = refType;
		commitNeeded = true;
	}

	public String getActualRefType () {
		return refType;
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

	public int getRefPmid () {
		return refPmid;
	}

	public void setRefPmid (int refPmid) {
		this.refPmid = refPmid;
		commitNeeded = true;
	}

	public int getActualRefPmid () {
		return refPmid;
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

	public static int pmidValue() throws JspException {
		try {
			return currentInstance.getPmid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pmidValue()");
		}
	}

	public static int seqnumValue() throws JspException {
		try {
			return currentInstance.getSeqnum();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function seqnumValue()");
		}
	}

	public static String refTypeValue() throws JspException {
		try {
			return currentInstance.getRefType();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function refTypeValue()");
		}
	}

	public static String sourceValue() throws JspException {
		try {
			return currentInstance.getSource();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function sourceValue()");
		}
	}

	public static int refPmidValue() throws JspException {
		try {
			return currentInstance.getRefPmid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function refPmidValue()");
		}
	}

	public static String noteValue() throws JspException {
		try {
			return currentInstance.getNote();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function noteValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		refType = null;
		source = null;
		refPmid = 0;
		note = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
