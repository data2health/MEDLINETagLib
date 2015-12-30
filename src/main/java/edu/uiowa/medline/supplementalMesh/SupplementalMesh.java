package edu.uiowa.medline.supplementalMesh;

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
public class SupplementalMesh extends MEDLINETagLibTagSupport {

	static SupplementalMesh currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(SupplementalMesh.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	String name = null;
	String type = null;
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

			SupplementalMeshIterator theSupplementalMeshIterator = (SupplementalMeshIterator)findAncestorWithClass(this, SupplementalMeshIterator.class);

			if (theSupplementalMeshIterator != null) {
				pmid = theSupplementalMeshIterator.getPmid();
				seqnum = theSupplementalMeshIterator.getSeqnum();
			}

			if (theSupplementalMeshIterator == null && theArticle == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new SupplementalMesh and to generate a new seqnum
				seqnum = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a SupplementalMesh from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select name,type,id from medline16.supplemental_mesh where pmid = ? and seqnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (name == null)
						name = rs.getString(1);
					if (type == null)
						type = rs.getString(2);
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
				PreparedStatement stmt = getConnection().prepareStatement("update medline16.supplemental_mesh set name = ?, type = ?, id = ? where pmid = ? and seqnum = ?");
				stmt.setString(1,name);
				stmt.setString(2,type);
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
				log.debug("generating new SupplementalMesh " + seqnum);
			}

			if (name == null)
				name = "";
			if (type == null)
				type = "";
			if (ID == null)
				ID = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline16.supplemental_mesh(pmid,seqnum,name,type,id) values (?,?,?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setString(3,name);
			stmt.setString(4,type);
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

	public static String nameValue() throws JspException {
		try {
			return currentInstance.getName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function nameValue()");
		}
	}

	public static String typeValue() throws JspException {
		try {
			return currentInstance.getType();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function typeValue()");
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
		name = null;
		type = null;
		ID = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
