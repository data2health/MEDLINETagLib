package edu.uiowa.medline.investigatorNameId;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.medline.investigator.Investigator;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.Sequence;

@SuppressWarnings("serial")
public class InvestigatorNameId extends MEDLINETagLibTagSupport {

	static InvestigatorNameId currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(InvestigatorNameId.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	int nnum = 0;
	String nameId = null;
	String source = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			if (theInvestigator!= null)
				parentEntities.addElement(theInvestigator);

			if (theInvestigator == null) {
			} else {
				pmid = theInvestigator.getPmid();
				seqnum = theInvestigator.getSeqnum();
			}

			InvestigatorNameIdIterator theInvestigatorNameIdIterator = (InvestigatorNameIdIterator)findAncestorWithClass(this, InvestigatorNameIdIterator.class);

			if (theInvestigatorNameIdIterator != null) {
				pmid = theInvestigatorNameIdIterator.getPmid();
				seqnum = theInvestigatorNameIdIterator.getSeqnum();
				nnum = theInvestigatorNameIdIterator.getNnum();
			}

			if (theInvestigatorNameIdIterator == null && theInvestigator == null && nnum == 0) {
				// no nnum was provided - the default is to assume that it is a new InvestigatorNameId and to generate a new nnum
				nnum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or nnum was provided as an attribute - we need to load a InvestigatorNameId from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select name_id,source from medline12.investigator_name_id where pmid = ? and seqnum = ? and nnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				stmt.setInt(3,nnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (nameId == null)
						nameId = rs.getString(1);
					if (source == null)
						source = rs.getString(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving nnum " + nnum, e);
			throw new JspTagException("Error: JDBC error retrieving nnum " + nnum);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update medline12.investigator_name_id set name_id = ?, source = ? where pmid = ? and seqnum = ? and nnum = ?");
				stmt.setString(1,nameId);
				stmt.setString(2,source);
				stmt.setInt(3,pmid);
				stmt.setInt(4,seqnum);
				stmt.setInt(5,nnum);
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
			if (nnum == 0) {
				nnum = Sequence.generateID();
				log.debug("generating new InvestigatorNameId " + nnum);
			}

			if (nameId == null)
				nameId = "";
			if (source == null)
				source = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline12.investigator_name_id(pmid,seqnum,nnum,name_id,source) values (?,?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setInt(3,nnum);
			stmt.setString(4,nameId);
			stmt.setString(5,source);
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

	public int getNnum () {
		return nnum;
	}

	public void setNnum (int nnum) {
		this.nnum = nnum;
	}

	public int getActualNnum () {
		return nnum;
	}

	public String getNameId () {
		if (commitNeeded)
			return "";
		else
			return nameId;
	}

	public void setNameId (String nameId) {
		this.nameId = nameId;
		commitNeeded = true;
	}

	public String getActualNameId () {
		return nameId;
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

	public static Integer nnumValue() throws JspException {
		try {
			return currentInstance.getNnum();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function nnumValue()");
		}
	}

	public static String nameIdValue() throws JspException {
		try {
			return currentInstance.getNameId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function nameIdValue()");
		}
	}

	public static String sourceValue() throws JspException {
		try {
			return currentInstance.getSource();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function sourceValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		nnum = 0;
		nameId = null;
		source = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
