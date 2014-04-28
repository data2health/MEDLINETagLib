package edu.uiowa.medline.meshQualifier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.medline.meshHeading.MeshHeading;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.Sequence;

@SuppressWarnings("serial")
public class MeshQualifier extends MEDLINETagLibTagSupport {

	static MeshQualifier currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(MeshQualifier.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	int qnum = 0;
	String qualifierName = null;
	boolean major = false;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			if (theMeshHeading!= null)
				parentEntities.addElement(theMeshHeading);

			if (theMeshHeading == null) {
			} else {
				pmid = theMeshHeading.getPmid();
				seqnum = theMeshHeading.getSeqnum();
			}

			MeshQualifierIterator theMeshQualifierIterator = (MeshQualifierIterator)findAncestorWithClass(this, MeshQualifierIterator.class);

			if (theMeshQualifierIterator != null) {
				pmid = theMeshQualifierIterator.getPmid();
				seqnum = theMeshQualifierIterator.getSeqnum();
				qnum = theMeshQualifierIterator.getQnum();
			}

			if (theMeshQualifierIterator == null && theMeshHeading == null && qnum == 0) {
				// no qnum was provided - the default is to assume that it is a new MeshQualifier and to generate a new qnum
				qnum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or qnum was provided as an attribute - we need to load a MeshQualifier from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select qualifier_name,major from medline14.mesh_qualifier where pmid = ? and seqnum = ? and qnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				stmt.setInt(3,qnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (qualifierName == null)
						qualifierName = rs.getString(1);
					if (major == false)
						major = rs.getBoolean(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving qnum " + qnum, e);
			throw new JspTagException("Error: JDBC error retrieving qnum " + qnum);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update medline14.mesh_qualifier set qualifier_name = ?, major = ? where pmid = ? and seqnum = ? and qnum = ?");
				stmt.setString(1,qualifierName);
				stmt.setBoolean(2,major);
				stmt.setInt(3,pmid);
				stmt.setInt(4,seqnum);
				stmt.setInt(5,qnum);
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
			if (qnum == 0) {
				qnum = Sequence.generateID();
				log.debug("generating new MeshQualifier " + qnum);
			}

			if (qualifierName == null)
				qualifierName = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline14.mesh_qualifier(pmid,seqnum,qnum,qualifier_name,major) values (?,?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setInt(3,qnum);
			stmt.setString(4,qualifierName);
			stmt.setBoolean(5,major);
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

	public int getQnum () {
		return qnum;
	}

	public void setQnum (int qnum) {
		this.qnum = qnum;
	}

	public int getActualQnum () {
		return qnum;
	}

	public String getQualifierName () {
		if (commitNeeded)
			return "";
		else
			return qualifierName;
	}

	public void setQualifierName (String qualifierName) {
		this.qualifierName = qualifierName;
		commitNeeded = true;
	}

	public String getActualQualifierName () {
		return qualifierName;
	}

	public boolean getMajor () {
		return major;
	}

	public void setMajor (boolean major) {
		this.major = major;
		commitNeeded = true;
	}

	public boolean getActualMajor () {
		return major;
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

	public static Integer qnumValue() throws JspException {
		try {
			return currentInstance.getQnum();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function qnumValue()");
		}
	}

	public static String qualifierNameValue() throws JspException {
		try {
			return currentInstance.getQualifierName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function qualifierNameValue()");
		}
	}

	public static Boolean majorValue() throws JspException {
		try {
			return currentInstance.getMajor();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function majorValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		qnum = 0;
		qualifierName = null;
		major = false;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
