package edu.uiowa.medline.accession;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.medline.dataBank.DataBank;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.Sequence;

@SuppressWarnings("serial")
public class Accession extends MEDLINETagLibTagSupport {

	static Accession currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Accession.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	int accnum = 0;
	String accession = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			DataBank theDataBank = (DataBank)findAncestorWithClass(this, DataBank.class);
			if (theDataBank!= null)
				parentEntities.addElement(theDataBank);

			if (theDataBank == null) {
			} else {
				pmid = theDataBank.getPmid();
				seqnum = theDataBank.getSeqnum();
			}

			AccessionIterator theAccessionIterator = (AccessionIterator)findAncestorWithClass(this, AccessionIterator.class);

			if (theAccessionIterator != null) {
				pmid = theAccessionIterator.getPmid();
				seqnum = theAccessionIterator.getSeqnum();
				accnum = theAccessionIterator.getAccnum();
			}

			if (theAccessionIterator == null && theDataBank == null && accnum == 0) {
				// no accnum was provided - the default is to assume that it is a new Accession and to generate a new accnum
				accnum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or accnum was provided as an attribute - we need to load a Accession from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select accession from medline14.accession where pmid = ? and seqnum = ? and accnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				stmt.setInt(3,accnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (accession == null)
						accession = rs.getString(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving accnum " + accnum, e);
			throw new JspTagException("Error: JDBC error retrieving accnum " + accnum);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update medline14.accession set accession = ? where pmid = ? and seqnum = ? and accnum = ?");
				stmt.setString(1,accession);
				stmt.setInt(2,pmid);
				stmt.setInt(3,seqnum);
				stmt.setInt(4,accnum);
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
			if (accnum == 0) {
				accnum = Sequence.generateID();
				log.debug("generating new Accession " + accnum);
			}

			if (accession == null)
				accession = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline14.accession(pmid,seqnum,accnum,accession) values (?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setInt(3,accnum);
			stmt.setString(4,accession);
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

	public int getAccnum () {
		return accnum;
	}

	public void setAccnum (int accnum) {
		this.accnum = accnum;
	}

	public int getActualAccnum () {
		return accnum;
	}

	public String getAccession () {
		if (commitNeeded)
			return "";
		else
			return accession;
	}

	public void setAccession (String accession) {
		this.accession = accession;
		commitNeeded = true;
	}

	public String getActualAccession () {
		return accession;
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

	public static Integer accnumValue() throws JspException {
		try {
			return currentInstance.getAccnum();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function accnumValue()");
		}
	}

	public static String accessionValue() throws JspException {
		try {
			return currentInstance.getAccession();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function accessionValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		accnum = 0;
		accession = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
