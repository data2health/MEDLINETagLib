package edu.uiowa.medline.meshHeading;

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
public class MeshHeading extends MEDLINETagLibTagSupport {

	static MeshHeading currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(MeshHeading.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	String descriptorName = null;
	boolean major = false;
	String type = null;

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

			MeshHeadingIterator theMeshHeadingIterator = (MeshHeadingIterator)findAncestorWithClass(this, MeshHeadingIterator.class);

			if (theMeshHeadingIterator != null) {
				pmid = theMeshHeadingIterator.getPmid();
				seqnum = theMeshHeadingIterator.getSeqnum();
			}

			if (theMeshHeadingIterator == null && theArticle == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new MeshHeading and to generate a new seqnum
				seqnum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a MeshHeading from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select descriptor_name,major,type from medline12.mesh_heading where pmid = ? and seqnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (descriptorName == null)
						descriptorName = rs.getString(1);
					if (major == false)
						major = rs.getBoolean(2);
					if (type == null)
						type = rs.getString(3);
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
				PreparedStatement stmt = getConnection().prepareStatement("update medline12.mesh_heading set descriptor_name = ?, major = ?, type = ? where pmid = ? and seqnum = ?");
				stmt.setString(1,descriptorName);
				stmt.setBoolean(2,major);
				stmt.setString(3,type);
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
				log.debug("generating new MeshHeading " + seqnum);
			}

			if (descriptorName == null)
				descriptorName = "";
			if (type == null)
				type = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline12.mesh_heading(pmid,seqnum,descriptor_name,major,type) values (?,?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setString(3,descriptorName);
			stmt.setBoolean(4,major);
			stmt.setString(5,type);
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

	public String getDescriptorName () {
		if (commitNeeded)
			return "";
		else
			return descriptorName;
	}

	public void setDescriptorName (String descriptorName) {
		this.descriptorName = descriptorName;
		commitNeeded = true;
	}

	public String getActualDescriptorName () {
		return descriptorName;
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

	public static String descriptorNameValue() throws JspException {
		try {
			return currentInstance.getDescriptorName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function descriptorNameValue()");
		}
	}

	public static Boolean majorValue() throws JspException {
		try {
			return currentInstance.getMajor();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function majorValue()");
		}
	}

	public static String typeValue() throws JspException {
		try {
			return currentInstance.getType();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function typeValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		descriptorName = null;
		major = false;
		type = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
