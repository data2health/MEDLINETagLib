package edu.uiowa.medline.otherId;

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

public class OtherId extends MEDLINETagLibTagSupport {

	static OtherId currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(OtherId.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	String source = null;
	String otherId = null;

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

			OtherIdIterator theOtherIdIterator = (OtherIdIterator)findAncestorWithClass(this, OtherIdIterator.class);

			if (theOtherIdIterator != null) {
				pmid = theOtherIdIterator.getPmid();
				seqnum = theOtherIdIterator.getSeqnum();
			}

			if (theOtherIdIterator == null && theArticle == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new OtherId and to generate a new seqnum
				seqnum = Sequence.generateID();
				log.debug("generating new OtherId " + seqnum);
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a OtherId from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select source,other_id from medline12.other_id where pmid = ? and seqnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (source == null)
						source = rs.getString(1);
					if (otherId == null)
						otherId = rs.getString(2);
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
				PreparedStatement stmt = getConnection().prepareStatement("update medline12.other_id set source = ?, other_id = ? where pmid = ? and seqnum = ?");
				stmt.setString(1,source);
				stmt.setString(2,otherId);
				stmt.setInt(3,pmid);
				stmt.setInt(4,seqnum);
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
				log.debug("generating new OtherId " + seqnum);
			}

			if (source == null)
				source = "";
			if (otherId == null)
				otherId = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline12.other_id(pmid,seqnum,source,other_id) values (?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setString(3,source);
			stmt.setString(4,otherId);
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

	public String getOtherId () {
		if (commitNeeded)
			return "";
		else
			return otherId;
	}

	public void setOtherId (String otherId) {
		this.otherId = otherId;
		commitNeeded = true;
	}

	public String getActualOtherId () {
		return otherId;
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

	public static String sourceValue() throws JspException {
		try {
			return currentInstance.getSource();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function sourceValue()");
		}
	}

	public static String otherIdValue() throws JspException {
		try {
			return currentInstance.getOtherId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function otherIdValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		source = null;
		otherId = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
