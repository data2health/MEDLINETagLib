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

	private static final Log log =LogFactory.getLog(Article.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	Date dateCreated = null;
	Date dateCompleted = null;
	Date dateRevised = null;
	String title = null;
	int startPage = 0;
	int endPage = 0;
	String medlinePgn = null;
	String abstractText = null;
	String copyright = null;
	String affiliation = null;
	String type = null;
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
				log.debug("generating new Article " + pmid);
				insertEntity();
			} else {
				// an iterator or pmid was provided as an attribute - we need to load a Article from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select date_created,date_completed,date_revised,title,start_page,end_page,medline_pgn,abstract_text,copyright,affiliation,type,vernacular_title,country,ta,nlm_unique_id,issn_linking,reference_count,pub_model,status from medline10.article where pmid = ?");
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
					if (abstractText == null)
						abstractText = rs.getString(8);
					if (copyright == null)
						copyright = rs.getString(9);
					if (affiliation == null)
						affiliation = rs.getString(10);
					if (type == null)
						type = rs.getString(11);
					if (vernacularTitle == null)
						vernacularTitle = rs.getString(12);
					if (country == null)
						country = rs.getString(13);
					if (ta == null)
						ta = rs.getString(14);
					if (nlmUniqueId == null)
						nlmUniqueId = rs.getString(15);
					if (issnLinking == null)
						issnLinking = rs.getString(16);
					if (referenceCount == 0)
						referenceCount = rs.getInt(17);
					if (pubModel == null)
						pubModel = rs.getString(18);
					if (status == null)
						status = rs.getString(19);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
				PreparedStatement stmt = getConnection().prepareStatement("update medline10.article set date_created = ?, date_completed = ?, date_revised = ?, title = ?, start_page = ?, end_page = ?, medline_pgn = ?, abstract_text = ?, copyright = ?, affiliation = ?, type = ?, vernacular_title = ?, country = ?, ta = ?, nlm_unique_id = ?, issn_linking = ?, reference_count = ?, pub_model = ?, status = ? where pmid = ?");
				stmt.setDate(1,dateCreated == null ? null : new java.sql.Date(dateCreated.getTime()));
				stmt.setDate(2,dateCompleted == null ? null : new java.sql.Date(dateCompleted.getTime()));
				stmt.setDate(3,dateRevised == null ? null : new java.sql.Date(dateRevised.getTime()));
				stmt.setString(4,title);
				stmt.setInt(5,startPage);
				stmt.setInt(6,endPage);
				stmt.setString(7,medlinePgn);
				stmt.setString(8,abstractText);
				stmt.setString(9,copyright);
				stmt.setString(10,affiliation);
				stmt.setString(11,type);
				stmt.setString(12,vernacularTitle);
				stmt.setString(13,country);
				stmt.setString(14,ta);
				stmt.setString(15,nlmUniqueId);
				stmt.setString(16,issnLinking);
				stmt.setInt(17,referenceCount);
				stmt.setString(18,pubModel);
				stmt.setString(19,status);
				stmt.setInt(20,pmid);
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
			if (pmid == 0) {
				pmid = Sequence.generateID();
				log.debug("generating new Article " + pmid);
			}

			if (title == null)
				title = "";
			if (medlinePgn == null)
				medlinePgn = "";
			if (abstractText == null)
				abstractText = "";
			if (copyright == null)
				copyright = "";
			if (affiliation == null)
				affiliation = "";
			if (type == null)
				type = "";
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
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline10.article(pmid,date_created,date_completed,date_revised,title,start_page,end_page,medline_pgn,abstract_text,copyright,affiliation,type,vernacular_title,country,ta,nlm_unique_id,issn_linking,reference_count,pub_model,status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setDate(2,dateCreated == null ? null : new java.sql.Date(dateCreated.getTime()));
			stmt.setDate(3,dateCompleted == null ? null : new java.sql.Date(dateCompleted.getTime()));
			stmt.setDate(4,dateRevised == null ? null : new java.sql.Date(dateRevised.getTime()));
			stmt.setString(5,title);
			stmt.setInt(6,startPage);
			stmt.setInt(7,endPage);
			stmt.setString(8,medlinePgn);
			stmt.setString(9,abstractText);
			stmt.setString(10,copyright);
			stmt.setString(11,affiliation);
			stmt.setString(12,type);
			stmt.setString(13,vernacularTitle);
			stmt.setString(14,country);
			stmt.setString(15,ta);
			stmt.setString(16,nlmUniqueId);
			stmt.setString(17,issnLinking);
			stmt.setInt(18,referenceCount);
			stmt.setString(19,pubModel);
			stmt.setString(20,status);
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

	public String getAbstractText () {
		if (commitNeeded)
			return "";
		else
			return abstractText;
	}

	public void setAbstractText (String abstractText) {
		this.abstractText = abstractText;
		commitNeeded = true;
	}

	public String getActualAbstractText () {
		return abstractText;
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

	public String getAffiliation () {
		if (commitNeeded)
			return "";
		else
			return affiliation;
	}

	public void setAffiliation (String affiliation) {
		this.affiliation = affiliation;
		commitNeeded = true;
	}

	public String getActualAffiliation () {
		return affiliation;
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

	public static int pmidValue() throws JspException {
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

	public static int startPageValue() throws JspException {
		try {
			return currentInstance.getStartPage();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function startPageValue()");
		}
	}

	public static int endPageValue() throws JspException {
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

	public static String abstractTextValue() throws JspException {
		try {
			return currentInstance.getAbstractText();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function abstractTextValue()");
		}
	}

	public static String copyrightValue() throws JspException {
		try {
			return currentInstance.getCopyright();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function copyrightValue()");
		}
	}

	public static String affiliationValue() throws JspException {
		try {
			return currentInstance.getAffiliation();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function affiliationValue()");
		}
	}

	public static String typeValue() throws JspException {
		try {
			return currentInstance.getType();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function typeValue()");
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

	public static int referenceCountValue() throws JspException {
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
		abstractText = null;
		copyright = null;
		affiliation = null;
		type = null;
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
