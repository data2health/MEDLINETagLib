package edu.uiowa.medline.abstr;

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
public class Abstr extends MEDLINETagLibTagSupport {

	static Abstr currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Abstr.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	String abstractText = null;
	String label = null;
	String category = null;

	private String var = null;

	private Abstr cachedAbstr = null;

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

			AbstrIterator theAbstrIterator = (AbstrIterator)findAncestorWithClass(this, AbstrIterator.class);

			if (theAbstrIterator != null) {
				pmid = theAbstrIterator.getPmid();
				seqnum = theAbstrIterator.getSeqnum();
			}

			if (theAbstrIterator == null && theArticle == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new Abstr and to generate a new seqnum
				seqnum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a Abstr from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select abstract_text,label,category from medline13.abstr where pmid = ? and seqnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (abstractText == null)
						abstractText = rs.getString(1);
					if (label == null)
						label = rs.getString(2);
					if (category == null)
						category = rs.getString(3);
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

		Abstr currentAbstr = (Abstr) pageContext.getAttribute("tag_abstr");
		if(currentAbstr != null){
			cachedAbstr = currentAbstr;
		}
		currentAbstr = this;
		pageContext.setAttribute((var == null ? "tag_abstr" : var), currentAbstr);

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(this.cachedAbstr != null){
			pageContext.setAttribute((var == null ? "tag_abstr" : var), this.cachedAbstr);
		}else{
			pageContext.removeAttribute((var == null ? "tag_abstr" : var));
			this.cachedAbstr = null;
		}

		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update medline13.abstr set abstract_text = ?, label = ?, category = ? where pmid = ? and seqnum = ?");
				stmt.setString(1,abstractText);
				stmt.setString(2,label);
				stmt.setString(3,category);
				stmt.setInt(4,pmid);
				stmt.setInt(5,seqnum);
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
				log.debug("generating new Abstr " + seqnum);
			}

			if (abstractText == null)
				abstractText = "";
			if (label == null)
				label = "";
			if (category == null)
				category = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline13.abstr(pmid,seqnum,abstract_text,label,category) values (?,?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setString(3,abstractText);
			stmt.setString(4,label);
			stmt.setString(5,category);
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

	public String getLabel () {
		if (commitNeeded)
			return "";
		else
			return label;
	}

	public void setLabel (String label) {
		this.label = label;
		commitNeeded = true;
	}

	public String getActualLabel () {
		return label;
	}

	public String getCategory () {
		if (commitNeeded)
			return "";
		else
			return category;
	}

	public void setCategory (String category) {
		this.category = category;
		commitNeeded = true;
	}

	public String getActualCategory () {
		return category;
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

	public static String abstractTextValue() throws JspException {
		try {
			return currentInstance.getAbstractText();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function abstractTextValue()");
		}
	}

	public static String labelValue() throws JspException {
		try {
			return currentInstance.getLabel();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function labelValue()");
		}
	}

	public static String categoryValue() throws JspException {
		try {
			return currentInstance.getCategory();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function categoryValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		abstractText = null;
		label = null;
		category = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();
		this.var = null;

	}

}
