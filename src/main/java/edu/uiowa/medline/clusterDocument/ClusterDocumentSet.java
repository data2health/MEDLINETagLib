package edu.uiowa.medline.clusterDocument;

import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")

public class ClusterDocumentSet extends MEDLINETagLibTagSupport {

	static ClusterDocumentSet currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(ClusterDocumentSet.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int cid = 0;
	int pmid = 0;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
		    if (currentInstance != this)
		        throw new SQLException();
		} catch (SQLException e) {
			log.error("JDBC error in ClusterDocumentSet", e);
			freeConnection();
			throw new JspTagException("JDBC error in ClusterDocumentSet");
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
            if (currentInstance != this)
                throw new SQLException();
		} catch (SQLException e) {
			log.error("JDBC error in ClusterDocumentSet", e);
			freeConnection();
			throw new JspTagException("JDBC error in ClusterDocumentSet");
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
