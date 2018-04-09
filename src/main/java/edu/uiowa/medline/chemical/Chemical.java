package edu.uiowa.medline.chemical;

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
public class Chemical extends MEDLINETagLibTagSupport {

	static Chemical currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Chemical.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	String registryNumber = null;
	String substanceName = null;
	String ID = null;

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

			ChemicalIterator theChemicalIterator = (ChemicalIterator)findAncestorWithClass(this, ChemicalIterator.class);

			if (theChemicalIterator != null) {
				pmid = theChemicalIterator.getPmid();
				seqnum = theChemicalIterator.getSeqnum();
			}

			if (theChemicalIterator == null && theArticle == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new Chemical and to generate a new seqnum
				seqnum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a Chemical from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select registry_number,substance_name,id from medline18.chemical where pmid = ? and seqnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (registryNumber == null)
						registryNumber = rs.getString(1);
					if (substanceName == null)
						substanceName = rs.getString(2);
					if (ID == null)
						ID = rs.getString(3);
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
				PreparedStatement stmt = getConnection().prepareStatement("update medline18.chemical set registry_number = ?, substance_name = ?, id = ? where pmid = ? and seqnum = ?");
				stmt.setString(1,registryNumber);
				stmt.setString(2,substanceName);
				stmt.setString(3,ID);
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
				log.debug("generating new Chemical " + seqnum);
			}

			if (registryNumber == null)
				registryNumber = "";
			if (substanceName == null)
				substanceName = "";
			if (ID == null)
				ID = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline18.chemical(pmid,seqnum,registry_number,substance_name,id) values (?,?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setString(3,registryNumber);
			stmt.setString(4,substanceName);
			stmt.setString(5,ID);
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

	public String getRegistryNumber () {
		if (commitNeeded)
			return "";
		else
			return registryNumber;
	}

	public void setRegistryNumber (String registryNumber) {
		this.registryNumber = registryNumber;
		commitNeeded = true;
	}

	public String getActualRegistryNumber () {
		return registryNumber;
	}

	public String getSubstanceName () {
		if (commitNeeded)
			return "";
		else
			return substanceName;
	}

	public void setSubstanceName (String substanceName) {
		this.substanceName = substanceName;
		commitNeeded = true;
	}

	public String getActualSubstanceName () {
		return substanceName;
	}

	public String getID () {
		if (commitNeeded)
			return "";
		else
			return ID;
	}

	public void setID (String ID) {
		this.ID = ID;
		commitNeeded = true;
	}

	public String getActualID () {
		return ID;
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

	public static String registryNumberValue() throws JspException {
		try {
			return currentInstance.getRegistryNumber();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function registryNumberValue()");
		}
	}

	public static String substanceNameValue() throws JspException {
		try {
			return currentInstance.getSubstanceName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function substanceNameValue()");
		}
	}

	public static String IDValue() throws JspException {
		try {
			return currentInstance.getID();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function IDValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		registryNumber = null;
		substanceName = null;
		ID = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
