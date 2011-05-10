package edu.uiowa.medline.clusterDocument;

import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")

public class ClusterDocumentSet extends MEDLINETagLibTagSupport {

	static ClusterDocumentSet currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int cid = 0;
	int pmid = 0;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
		    if (false)
		        throw new SQLException();
		} catch (SQLException e) {
			e.printStackTrace();
			freeConnection();
			throw new JspTagException("Error: JDBC error retrieving cid " + cid);
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
            if (false)
                throw new SQLException();
		} catch (SQLException e) {
			e.printStackTrace();
			freeConnection();
			throw new JspTagException("Error: IOException while writing to the user");
		}
		clearServiceState();
		return super.doEndTag();
	}

	private void clearServiceState () {
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
