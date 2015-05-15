package edu.uiowa.medline.article;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.Sequence;

@SuppressWarnings("serial")
public class Article extends MEDLINETagLibTagSupport {

	static Article currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Article.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	Date dateCreated = null;
	Date dateCompleted = null;
	Date dateRevised = null;
	String title = null;
	int startPage = 0;
	int endPage = 0;
	String medlinePgn = null;
	String copyright = null;
	String vernacularTitle = null;
	String country = null;
	String ta = null;
	String nlmUniqueId = null;
	String issnLinking = null;
	int referenceCount = 0;
	String pubModel = null;
	String status = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			ArticleIterator theArticleIterator = (ArticleIterator)findAncestorWithClass(this, ArticleIterator.class);

			if (theArticleIterator != null) {
				pmid = theArticleIterator.getPmid();
			}

			if (theArticleIterator == null && pmid == 0) {
				// no pmid was provided - the default is to assume that it is a new Article and to generate a new pmid
				pmid = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or pmid was provided as an attribute - we need to load a Article from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select date_created,date_completed,date_revised,title,start_page,end_page,medline_pgn,copyright,vernacular_title,country,ta,nlm_unique_id,issn_linking,reference_count,pub_model,status from medline15.article where pmid = ?");
				stmt.setInt(1,pmid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (dateCreated == null)
						dateCreated = rs.getDate(1);
					if (dateCompleted == null)
						dateCompleted = rs.getDate(2);
					if (dateRevised == null)
						dateRevised = rs.getDate(3);
					if (title == null)
						title = rs.getString(4);
					if (startPage == 0)
						startPage = rs.getInt(5);
					if (endPage == 0)
						endPage = rs.getInt(6);
					if (medlinePgn == null)
						medlinePgn = rs.getString(7);
					if (copyright == null)
						copyright = rs.getString(8);
					if (vernacularTitle == null)
						vernacularTitle = rs.getString(9);
					if (country == null)
						country = rs.getString(10);
					if (ta == null)
						ta = rs.getString(11);
					if (nlmUniqueId == null)
						nlmUniqueId = rs.getString(12);
					if (issnLinking == null)
						issnLinking = rs.getString(13);
					if (referenceCount == 0)
						referenceCount = rs.getInt(14);
					if (pubModel == null)
						pubModel = rs.getString(15);
					if (status == null)
						status = rs.getString(16);
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
				PreparedStatement stmt = getConnection().prepareStatement("update medline15.article set date_created = ?, date_completed = ?, date_revised = ?, title = ?, start_page = ?, end_page = ?, medline_pgn = ?, copyright = ?, vernacular_title = ?, country = ?, ta = ?, nlm_unique_id = ?, issn_linking = ?, reference_count = ?, pub_model = ?, status = ? where pmid = ?");
				stmt.setDate(1,dateCreated == null ? null : new java.sql.Date(dateCreated.getTime()));
				stmt.setDate(2,dateCompleted == null ? null : new java.sql.Date(dateCompleted.getTime()));
				stmt.setDate(3,dateRevised == null ? null : new java.sql.Date(dateRevised.getTime()));
				stmt.setString(4,title);
				stmt.setInt(5,startPage);
				stmt.setInt(6,endPage);
				stmt.setString(7,medlinePgn);
				stmt.setString(8,copyright);
				stmt.setString(9,vernacularTitle);
				stmt.setString(10,country);
				stmt.setString(11,ta);
				stmt.setString(12,nlmUniqueId);
				stmt.setString(13,issnLinking);
				stmt.setInt(14,referenceCount);
				stmt.setString(15,pubModel);
				stmt.setString(16,status);
				stmt.setInt(17,pmid);
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
			if (pmid == 0) {
				pmid = Sequence.generateID();
				log.debug("generating new Article " + pmid);
			}

			if (title == null)
				title = "";
			if (medlinePgn == null)
				medlinePgn = "";
			if (copyright == null)
				copyright = "";
			if (vernacularTitle == null)
				vernacularTitle = "";
			if (country == null)
				country = "";
			if (ta == null)
				ta = "";
			if (nlmUniqueId == null)
				nlmUniqueId = "";
			if (issnLinking == null)
				issnLinking = "";
			if (pubModel == null)
				pubModel = "";
			if (status == null)
				status = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline15.article(pmid,date_created,date_completed,date_revised,title,start_page,end_page,medline_pgn,copyright,vernacular_title,country,ta,nlm_unique_id,issn_linking,reference_count,pub_model,status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setDate(2,dateCreated == null ? null : new java.sql.Date(dateCreated.getTime()));
			stmt.setDate(3,dateCompleted == null ? null : new java.sql.Date(dateCompleted.getTime()));
			stmt.setDate(4,dateRevised == null ? null : new java.sql.Date(dateRevised.getTime()));
			stmt.setString(5,title);
			stmt.setInt(6,startPage);
			stmt.setInt(7,endPage);
			stmt.setString(8,medlinePgn);
			stmt.setString(9,copyright);
			stmt.setString(10,vernacularTitle);
			stmt.setString(11,country);
			stmt.setString(12,ta);
			stmt.setString(13,nlmUniqueId);
			stmt.setString(14,issnLinking);
			stmt.setInt(15,referenceCount);
			stmt.setString(16,pubModel);
			stmt.setString(17,status);
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

	public Date getDateCreated () {
		return dateCreated;
	}

	public void setDateCreated (Date dateCreated) {
		this.dateCreated = dateCreated;
		commitNeeded = true;
	}

	public Date getActualDateCreated () {
		return dateCreated;
	}

	public void setDateCreatedToNow ( ) {
		this.dateCreated = new java.util.Date();
		commitNeeded = true;
	}

	public Date getDateCompleted () {
		return dateCompleted;
	}

	public void setDateCompleted (Date dateCompleted) {
		this.dateCompleted = dateCompleted;
		commitNeeded = true;
	}

	public Date getActualDateCompleted () {
		return dateCompleted;
	}

	public void setDateCompletedToNow ( ) {
		this.dateCompleted = new java.util.Date();
		commitNeeded = true;
	}

	public Date getDateRevised () {
		return dateRevised;
	}

	public void setDateRevised (Date dateRevised) {
		this.dateRevised = dateRevised;
		commitNeeded = true;
	}

	public Date getActualDateRevised () {
		return dateRevised;
	}

	public void setDateRevisedToNow ( ) {
		this.dateRevised = new java.util.Date();
		commitNeeded = true;
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

	public int getStartPage () {
		return startPage;
	}

	public void setStartPage (int startPage) {
		this.startPage = startPage;
		commitNeeded = true;
	}

	public int getActualStartPage () {
		return startPage;
	}

	public int getEndPage () {
		return endPage;
	}

	public void setEndPage (int endPage) {
		this.endPage = endPage;
		commitNeeded = true;
	}

	public int getActualEndPage () {
		return endPage;
	}

	public String getMedlinePgn () {
		if (commitNeeded)
			return "";
		else
			return medlinePgn;
	}

	public void setMedlinePgn (String medlinePgn) {
		this.medlinePgn = medlinePgn;
		commitNeeded = true;
	}

	public String getActualMedlinePgn () {
		return medlinePgn;
	}

	public String getCopyright () {
		if (commitNeeded)
			return "";
		else
			return copyright;
	}

	public void setCopyright (String copyright) {
		this.copyright = copyright;
		commitNeeded = true;
	}

	public String getActualCopyright () {
		return copyright;
	}

	public String getVernacularTitle () {
		if (commitNeeded)
			return "";
		else
			return vernacularTitle;
	}

	public void setVernacularTitle (String vernacularTitle) {
		this.vernacularTitle = vernacularTitle;
		commitNeeded = true;
	}

	public String getActualVernacularTitle () {
		return vernacularTitle;
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

	public String getTa () {
		if (commitNeeded)
			return "";
		else
			return ta;
	}

	public void setTa (String ta) {
		this.ta = ta;
		commitNeeded = true;
	}

	public String getActualTa () {
		return ta;
	}

	public String getNlmUniqueId () {
		if (commitNeeded)
			return "";
		else
			return nlmUniqueId;
	}

	public void setNlmUniqueId (String nlmUniqueId) {
		this.nlmUniqueId = nlmUniqueId;
		commitNeeded = true;
	}

	public String getActualNlmUniqueId () {
		return nlmUniqueId;
	}

	public String getIssnLinking () {
		if (commitNeeded)
			return "";
		else
			return issnLinking;
	}

	public void setIssnLinking (String issnLinking) {
		this.issnLinking = issnLinking;
		commitNeeded = true;
	}

	public String getActualIssnLinking () {
		return issnLinking;
	}

	public int getReferenceCount () {
		return referenceCount;
	}

	public void setReferenceCount (int referenceCount) {
		this.referenceCount = referenceCount;
		commitNeeded = true;
	}

	public int getActualReferenceCount () {
		return referenceCount;
	}

	public String getPubModel () {
		if (commitNeeded)
			return "";
		else
			return pubModel;
	}

	public void setPubModel (String pubModel) {
		this.pubModel = pubModel;
		commitNeeded = true;
	}

	public String getActualPubModel () {
		return pubModel;
	}

	public String getStatus () {
		if (commitNeeded)
			return "";
		else
			return status;
	}

	public void setStatus (String status) {
		this.status = status;
		commitNeeded = true;
	}

	public String getActualStatus () {
		return status;
	}

	public static Integer pmidValue() throws JspException {
		try {
			return currentInstance.getPmid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pmidValue()");
		}
	}

	public static Date dateCreatedValue() throws JspException {
		try {
			return currentInstance.getDateCreated();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function dateCreatedValue()");
		}
	}

	public static Date dateCompletedValue() throws JspException {
		try {
			return currentInstance.getDateCompleted();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function dateCompletedValue()");
		}
	}

	public static Date dateRevisedValue() throws JspException {
		try {
			return currentInstance.getDateRevised();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function dateRevisedValue()");
		}
	}

	public static String titleValue() throws JspException {
		try {
			return currentInstance.getTitle();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function titleValue()");
		}
	}

	public static Integer startPageValue() throws JspException {
		try {
			return currentInstance.getStartPage();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function startPageValue()");
		}
	}

	public static Integer endPageValue() throws JspException {
		try {
			return currentInstance.getEndPage();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function endPageValue()");
		}
	}

	public static String medlinePgnValue() throws JspException {
		try {
			return currentInstance.getMedlinePgn();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function medlinePgnValue()");
		}
	}

	public static String copyrightValue() throws JspException {
		try {
			return currentInstance.getCopyright();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function copyrightValue()");
		}
	}

	public static String vernacularTitleValue() throws JspException {
		try {
			return currentInstance.getVernacularTitle();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function vernacularTitleValue()");
		}
	}

	public static String countryValue() throws JspException {
		try {
			return currentInstance.getCountry();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function countryValue()");
		}
	}

	public static String taValue() throws JspException {
		try {
			return currentInstance.getTa();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function taValue()");
		}
	}

	public static String nlmUniqueIdValue() throws JspException {
		try {
			return currentInstance.getNlmUniqueId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function nlmUniqueIdValue()");
		}
	}

	public static String issnLinkingValue() throws JspException {
		try {
			return currentInstance.getIssnLinking();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function issnLinkingValue()");
		}
	}

	public static Integer referenceCountValue() throws JspException {
		try {
			return currentInstance.getReferenceCount();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function referenceCountValue()");
		}
	}

	public static String pubModelValue() throws JspException {
		try {
			return currentInstance.getPubModel();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pubModelValue()");
		}
	}

	public static String statusValue() throws JspException {
		try {
			return currentInstance.getStatus();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function statusValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		dateCreated = null;
		dateCompleted = null;
		dateRevised = null;
		title = null;
		startPage = 0;
		endPage = 0;
		medlinePgn = null;
		copyright = null;
		vernacularTitle = null;
		country = null;
		ta = null;
		nlmUniqueId = null;
		issnLinking = null;
		referenceCount = 0;
		pubModel = null;
		status = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
