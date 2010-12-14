package edu.uiowa.icts.taglib.MEDLINETagLib.keyword;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.icts.taglib.MEDLINETagLib.article.Article;

import edu.uiowa.icts.taglib.MEDLINETagLib.MEDLINETagLibTagSupport;
import edu.uiowa.icts.taglib.MEDLINETagLib.Sequence;

@SuppressWarnings("serial")

public class Keyword extends MEDLINETagLibTagSupport {

	static Keyword currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Keyword.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	String keyword = null;
	boolean major = false;

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

			KeywordIterator theKeywordIterator = (KeywordIterator)findAncestorWithClass(this, KeywordIterator.class);

			if (theKeywordIterator != null) {
				pmid = theKeywordIterator.getPmid();
				seqnum = theKeywordIterator.getSeqnum();
			}

			if (theKeywordIterator == null && theArticle == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new Keyword and to generate a new seqnum
				seqnum = Sequence.generateID();
				log.debug("generating new Keyword " + seqnum);
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a Keyword from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select keyword,major from medline10.keyword where pmid = ? and seqnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (keyword == null)
						keyword = rs.getString(1);
					if (major == false)
						major = rs.getBoolean(2);
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
				PreparedStatement stmt = getConnection().prepareStatement("update medline10.keyword set keyword = ?, major = ? where pmid = ? and seqnum = ?");
				stmt.setString(1,keyword);
				stmt.setBoolean(2,major);
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
				log.debug("generating new Keyword " + seqnum);
			}

			if (keyword == null)
				keyword = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline10.keyword(pmid,seqnum,keyword,major) values (?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setString(3,keyword);
			stmt.setBoolean(4,major);
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

	public String getKeyword () {
		if (commitNeeded)
			return "";
		else
			return keyword;
	}

	public void setKeyword (String keyword) {
		this.keyword = keyword;
		commitNeeded = true;
	}

	public String getActualKeyword () {
		return keyword;
	}

	public boolean getMajor () {
		return major;
	}

	public void setMajor (boolean major) {
		this.major = major;
		commitNeeded = true;
	}

	public boolean getActualMajor () {
		return major;
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

	public static String keywordValue() throws JspException {
		try {
			return currentInstance.getKeyword();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function keywordValue()");
		}
	}

	public static boolean majorValue() throws JspException {
		try {
			return currentInstance.getMajor();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function majorValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		keyword = null;
		major = false;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
