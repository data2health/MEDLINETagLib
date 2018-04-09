package edu.uiowa.medline.otherAbstractText;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.medline.otherAbstract.OtherAbstract;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.Sequence;

@SuppressWarnings("serial")
public class OtherAbstractText extends MEDLINETagLibTagSupport {

	static OtherAbstractText currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(OtherAbstractText.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	int tnum = 0;
	String abstractText = null;
	String label = null;
	String category = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			OtherAbstract theOtherAbstract = (OtherAbstract)findAncestorWithClass(this, OtherAbstract.class);
			if (theOtherAbstract!= null)
				parentEntities.addElement(theOtherAbstract);

			if (theOtherAbstract == null) {
			} else {
				pmid = theOtherAbstract.getPmid();
				seqnum = theOtherAbstract.getSeqnum();
			}

			OtherAbstractTextIterator theOtherAbstractTextIterator = (OtherAbstractTextIterator)findAncestorWithClass(this, OtherAbstractTextIterator.class);

			if (theOtherAbstractTextIterator != null) {
				pmid = theOtherAbstractTextIterator.getPmid();
				seqnum = theOtherAbstractTextIterator.getSeqnum();
				tnum = theOtherAbstractTextIterator.getTnum();
			}

			if (theOtherAbstractTextIterator == null && theOtherAbstract == null && tnum == 0) {
				// no tnum was provided - the default is to assume that it is a new OtherAbstractText and to generate a new tnum
				tnum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or tnum was provided as an attribute - we need to load a OtherAbstractText from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select abstract_text,label,category from medline18.other_abstract_text where pmid = ? and seqnum = ? and tnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				stmt.setInt(3,tnum);
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
			log.error("JDBC error retrieving tnum " + tnum, e);
			throw new JspTagException("Error: JDBC error retrieving tnum " + tnum);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update medline18.other_abstract_text set abstract_text = ?, label = ?, category = ? where pmid = ? and seqnum = ? and tnum = ?");
				stmt.setString(1,abstractText);
				stmt.setString(2,label);
				stmt.setString(3,category);
				stmt.setInt(4,pmid);
				stmt.setInt(5,seqnum);
				stmt.setInt(6,tnum);
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
			if (tnum == 0) {
				tnum = Sequence.generateID();
				log.debug("generating new OtherAbstractText " + tnum);
			}

			if (abstractText == null)
				abstractText = "";
			if (label == null)
				label = "";
			if (category == null)
				category = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline18.other_abstract_text(pmid,seqnum,tnum,abstract_text,label,category) values (?,?,?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setInt(3,tnum);
			stmt.setString(4,abstractText);
			stmt.setString(5,label);
			stmt.setString(6,category);
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

	public int getTnum () {
		return tnum;
	}

	public void setTnum (int tnum) {
		this.tnum = tnum;
	}

	public int getActualTnum () {
		return tnum;
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

	public static Integer tnumValue() throws JspException {
		try {
			return currentInstance.getTnum();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function tnumValue()");
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
		tnum = 0;
		abstractText = null;
		label = null;
		category = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
