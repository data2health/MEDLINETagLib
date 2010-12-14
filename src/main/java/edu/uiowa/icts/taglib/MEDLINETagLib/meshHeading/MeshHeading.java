package edu.uiowa.icts.taglib.MEDLINETagLib.meshHeading;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.icts.taglib.MEDLINETagLib.article.Article;

import edu.uiowa.icts.taglib.MEDLINETagLib.MEDLINETagLibTagSupport;
import edu.uiowa.icts.taglib.MEDLINETagLib.Sequence;

@SuppressWarnings("serial")

public class MeshHeading extends MEDLINETagLibTagSupport {

	static MeshHeading currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(MeshHeading.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	String descriptorName = null;
	boolean major = false;

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
				log.debug("generating new MeshHeading " + seqnum);
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a MeshHeading from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select descriptor_name,major from medline10.mesh_heading where pmid = ? and seqnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (descriptorName == null)
						descriptorName = rs.getString(1);
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
			e.printStackTrace();
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
				PreparedStatement stmt = getConnection().prepareStatement("update medline10.mesh_heading set descriptor_name = ?, major = ? where pmid = ? and seqnum = ?");
				stmt.setString(1,descriptorName);
				stmt.setBoolean(2,major);
				stmt.setInt(3,pmid);
				stmt.setInt(4,seqnum);
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
			if (seqnum == 0) {
				seqnum = Sequence.generateID();
				log.debug("generating new MeshHeading " + seqnum);
			}

			if (descriptorName == null)
				descriptorName = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline10.mesh_heading(pmid,seqnum,descriptor_name,major) values (?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setString(3,descriptorName);
			stmt.setBoolean(4,major);
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

	public static int pmidValue() throws JspException {
		try {
			return currentInstance.getPmid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pmidValue()");
		}
	}

	public static int seqnumValue() throws JspException {
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

	public static boolean majorValue() throws JspException {
		try {
			return currentInstance.getMajor();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function majorValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		descriptorName = null;
		major = false;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
