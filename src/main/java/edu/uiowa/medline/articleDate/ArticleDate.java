package edu.uiowa.medline.articleDate;

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
public class ArticleDate extends MEDLINETagLibTagSupport {

	static ArticleDate currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(ArticleDate.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	int year = 0;
	int month = 0;
	int day = 0;
	String type = null;

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

			ArticleDateIterator theArticleDateIterator = (ArticleDateIterator)findAncestorWithClass(this, ArticleDateIterator.class);

			if (theArticleDateIterator != null) {
				pmid = theArticleDateIterator.getPmid();
				seqnum = theArticleDateIterator.getSeqnum();
			}

			if (theArticleDateIterator == null && theArticle == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new ArticleDate and to generate a new seqnum
				seqnum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a ArticleDate from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select year,month,day,type from medline16.article_date where pmid = ? and seqnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (year == 0)
						year = rs.getInt(1);
					if (month == 0)
						month = rs.getInt(2);
					if (day == 0)
						day = rs.getInt(3);
					if (type == null)
						type = rs.getString(4);
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
				PreparedStatement stmt = getConnection().prepareStatement("update medline16.article_date set year = ?, month = ?, day = ?, type = ? where pmid = ? and seqnum = ?");
				stmt.setInt(1,year);
				stmt.setInt(2,month);
				stmt.setInt(3,day);
				stmt.setString(4,type);
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
				log.debug("generating new ArticleDate " + seqnum);
			}

			if (type == null)
				type = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline16.article_date(pmid,seqnum,year,month,day,type) values (?,?,?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setInt(3,year);
			stmt.setInt(4,month);
			stmt.setInt(5,day);
			stmt.setString(6,type);
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

	public int getYear () {
		return year;
	}

	public void setYear (int year) {
		this.year = year;
		commitNeeded = true;
	}

	public int getActualYear () {
		return year;
	}

	public int getMonth () {
		return month;
	}

	public void setMonth (int month) {
		this.month = month;
		commitNeeded = true;
	}

	public int getActualMonth () {
		return month;
	}

	public int getDay () {
		return day;
	}

	public void setDay (int day) {
		this.day = day;
		commitNeeded = true;
	}

	public int getActualDay () {
		return day;
	}

	public String getType () {
		if (commitNeeded)
			return "";
		else
			return type;
	}

	public void setType (String type) {
		this.type = type;
		commitNeeded = true;
	}

	public String getActualType () {
		return type;
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

	public static Integer yearValue() throws JspException {
		try {
			return currentInstance.getYear();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function yearValue()");
		}
	}

	public static Integer monthValue() throws JspException {
		try {
			return currentInstance.getMonth();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function monthValue()");
		}
	}

	public static Integer dayValue() throws JspException {
		try {
			return currentInstance.getDay();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function dayValue()");
		}
	}

	public static String typeValue() throws JspException {
		try {
			return currentInstance.getType();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function typeValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		year = 0;
		month = 0;
		day = 0;
		type = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
