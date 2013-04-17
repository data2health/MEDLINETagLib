package edu.uiowa.medline.dataBank;

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
public class DataBank extends MEDLINETagLibTagSupport {

	static DataBank currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(DataBank.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	String name = null;

	private String var = null;

	private DataBank cachedDataBank = null;

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

			DataBankIterator theDataBankIterator = (DataBankIterator)findAncestorWithClass(this, DataBankIterator.class);

			if (theDataBankIterator != null) {
				pmid = theDataBankIterator.getPmid();
				seqnum = theDataBankIterator.getSeqnum();
			}

			if (theDataBankIterator == null && theArticle == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new DataBank and to generate a new seqnum
				seqnum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a DataBank from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select name from medline13.data_bank where pmid = ? and seqnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (name == null)
						name = rs.getString(1);
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

		DataBank currentDataBank = (DataBank) pageContext.getAttribute("tag_dataBank");
		if(currentDataBank != null){
			cachedDataBank = currentDataBank;
		}
		currentDataBank = this;
		pageContext.setAttribute((var == null ? "tag_dataBank" : var), currentDataBank);

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(this.cachedDataBank != null){
			pageContext.setAttribute((var == null ? "tag_dataBank" : var), this.cachedDataBank);
		}else{
			pageContext.removeAttribute((var == null ? "tag_dataBank" : var));
			this.cachedDataBank = null;
		}

		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update medline13.data_bank set name = ? where pmid = ? and seqnum = ?");
				stmt.setString(1,name);
				stmt.setInt(2,pmid);
				stmt.setInt(3,seqnum);
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
				log.debug("generating new DataBank " + seqnum);
			}

			if (name == null)
				name = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline13.data_bank(pmid,seqnum,name) values (?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setString(3,name);
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

	public String getName () {
		if (commitNeeded)
			return "";
		else
			return name;
	}

	public void setName (String name) {
		this.name = name;
		commitNeeded = true;
	}

	public String getActualName () {
		return name;
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

	public static String nameValue() throws JspException {
		try {
			return currentInstance.getName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function nameValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		name = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();
		this.var = null;

	}

}
