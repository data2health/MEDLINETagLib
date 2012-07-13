package edu.uiowa.medline.journal;

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
public class Journal extends MEDLINETagLibTagSupport {

	static Journal currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Journal.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	String issn = null;
	String volume = null;
	String issue = null;
	int pubYear = 0;
	String pubMonth = null;
	int pubDay = 0;
	String pubSeason = null;
	String medlineDate = null;
	String title = null;
	String isoAbbreviation = null;

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

			JournalIterator theJournalIterator = (JournalIterator)findAncestorWithClass(this, JournalIterator.class);

			if (theJournalIterator != null) {
				pmid = theJournalIterator.getPmid();
			}

			if (theJournalIterator == null && theArticle == null && pmid == 0) {
				// no pmid was provided - the default is to assume that it is a new Journal and to generate a new pmid
				pmid = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or pmid was provided as an attribute - we need to load a Journal from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select issn,volume,issue,pub_year,pub_month,pub_day,pub_season,medline_date,title,iso_abbreviation from medline12.journal where pmid = ?");
				stmt.setInt(1,pmid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (issn == null)
						issn = rs.getString(1);
					if (volume == null)
						volume = rs.getString(2);
					if (issue == null)
						issue = rs.getString(3);
					if (pubYear == 0)
						pubYear = rs.getInt(4);
					if (pubMonth == null)
						pubMonth = rs.getString(5);
					if (pubDay == 0)
						pubDay = rs.getInt(6);
					if (pubSeason == null)
						pubSeason = rs.getString(7);
					if (medlineDate == null)
						medlineDate = rs.getString(8);
					if (title == null)
						title = rs.getString(9);
					if (isoAbbreviation == null)
						isoAbbreviation = rs.getString(10);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving pmid " + pmid, e);
			throw new JspTagException("Error: JDBC error retrieving pmid " + pmid);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update medline12.journal set issn = ?, volume = ?, issue = ?, pub_year = ?, pub_month = ?, pub_day = ?, pub_season = ?, medline_date = ?, title = ?, iso_abbreviation = ? where pmid = ?");
				stmt.setString(1,issn);
				stmt.setString(2,volume);
				stmt.setString(3,issue);
				stmt.setInt(4,pubYear);
				stmt.setString(5,pubMonth);
				stmt.setInt(6,pubDay);
				stmt.setString(7,pubSeason);
				stmt.setString(8,medlineDate);
				stmt.setString(9,title);
				stmt.setString(10,isoAbbreviation);
				stmt.setInt(11,pmid);
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
			if (issn == null)
				issn = "";
			if (volume == null)
				volume = "";
			if (issue == null)
				issue = "";
			if (pubMonth == null)
				pubMonth = "";
			if (pubSeason == null)
				pubSeason = "";
			if (medlineDate == null)
				medlineDate = "";
			if (title == null)
				title = "";
			if (isoAbbreviation == null)
				isoAbbreviation = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline12.journal(pmid,issn,volume,issue,pub_year,pub_month,pub_day,pub_season,medline_date,title,iso_abbreviation) values (?,?,?,?,?,?,?,?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setString(2,issn);
			stmt.setString(3,volume);
			stmt.setString(4,issue);
			stmt.setInt(5,pubYear);
			stmt.setString(6,pubMonth);
			stmt.setInt(7,pubDay);
			stmt.setString(8,pubSeason);
			stmt.setString(9,medlineDate);
			stmt.setString(10,title);
			stmt.setString(11,isoAbbreviation);
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

	public String getIssn () {
		if (commitNeeded)
			return "";
		else
			return issn;
	}

	public void setIssn (String issn) {
		this.issn = issn;
		commitNeeded = true;
	}

	public String getActualIssn () {
		return issn;
	}

	public String getVolume () {
		if (commitNeeded)
			return "";
		else
			return volume;
	}

	public void setVolume (String volume) {
		this.volume = volume;
		commitNeeded = true;
	}

	public String getActualVolume () {
		return volume;
	}

	public String getIssue () {
		if (commitNeeded)
			return "";
		else
			return issue;
	}

	public void setIssue (String issue) {
		this.issue = issue;
		commitNeeded = true;
	}

	public String getActualIssue () {
		return issue;
	}

	public int getPubYear () {
		return pubYear;
	}

	public void setPubYear (int pubYear) {
		this.pubYear = pubYear;
		commitNeeded = true;
	}

	public int getActualPubYear () {
		return pubYear;
	}

	public String getPubMonth () {
		if (commitNeeded)
			return "";
		else
			return pubMonth;
	}

	public void setPubMonth (String pubMonth) {
		this.pubMonth = pubMonth;
		commitNeeded = true;
	}

	public String getActualPubMonth () {
		return pubMonth;
	}

	public int getPubDay () {
		return pubDay;
	}

	public void setPubDay (int pubDay) {
		this.pubDay = pubDay;
		commitNeeded = true;
	}

	public int getActualPubDay () {
		return pubDay;
	}

	public String getPubSeason () {
		if (commitNeeded)
			return "";
		else
			return pubSeason;
	}

	public void setPubSeason (String pubSeason) {
		this.pubSeason = pubSeason;
		commitNeeded = true;
	}

	public String getActualPubSeason () {
		return pubSeason;
	}

	public String getMedlineDate () {
		if (commitNeeded)
			return "";
		else
			return medlineDate;
	}

	public void setMedlineDate (String medlineDate) {
		this.medlineDate = medlineDate;
		commitNeeded = true;
	}

	public String getActualMedlineDate () {
		return medlineDate;
	}

	public String getTitle () {
		if (commitNeeded)
			return "";
		else
			return title;
	}

	public void setTitle (String title) {
		this.title = title;
		commitNeeded = true;
	}

	public String getActualTitle () {
		return title;
	}

	public String getIsoAbbreviation () {
		if (commitNeeded)
			return "";
		else
			return isoAbbreviation;
	}

	public void setIsoAbbreviation (String isoAbbreviation) {
		this.isoAbbreviation = isoAbbreviation;
		commitNeeded = true;
	}

	public String getActualIsoAbbreviation () {
		return isoAbbreviation;
	}

	public static Integer pmidValue() throws JspException {
		try {
			return currentInstance.getPmid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pmidValue()");
		}
	}

	public static String issnValue() throws JspException {
		try {
			return currentInstance.getIssn();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function issnValue()");
		}
	}

	public static String volumeValue() throws JspException {
		try {
			return currentInstance.getVolume();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function volumeValue()");
		}
	}

	public static String issueValue() throws JspException {
		try {
			return currentInstance.getIssue();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function issueValue()");
		}
	}

	public static Integer pubYearValue() throws JspException {
		try {
			return currentInstance.getPubYear();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pubYearValue()");
		}
	}

	public static String pubMonthValue() throws JspException {
		try {
			return currentInstance.getPubMonth();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pubMonthValue()");
		}
	}

	public static Integer pubDayValue() throws JspException {
		try {
			return currentInstance.getPubDay();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pubDayValue()");
		}
	}

	public static String pubSeasonValue() throws JspException {
		try {
			return currentInstance.getPubSeason();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pubSeasonValue()");
		}
	}

	public static String medlineDateValue() throws JspException {
		try {
			return currentInstance.getMedlineDate();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function medlineDateValue()");
		}
	}

	public static String titleValue() throws JspException {
		try {
			return currentInstance.getTitle();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function titleValue()");
		}
	}

	public static String isoAbbreviationValue() throws JspException {
		try {
			return currentInstance.getIsoAbbreviation();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function isoAbbreviationValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		issn = null;
		volume = null;
		issue = null;
		pubYear = 0;
		pubMonth = null;
		pubDay = 0;
		pubSeason = null;
		medlineDate = null;
		title = null;
		isoAbbreviation = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
