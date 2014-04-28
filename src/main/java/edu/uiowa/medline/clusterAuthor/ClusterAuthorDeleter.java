package edu.uiowa.medline.clusterAuthor;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.MEDLINETagLibBodyTagSupport;
import edu.uiowa.medline.documentCluster.DocumentCluster;

@SuppressWarnings("serial")
public class ClusterAuthorDeleter extends MEDLINETagLibBodyTagSupport {
    int cid = 0;
    int seqnum = 0;
    String lastName = null;
    String foreName = null;
    int occurrences = 0;
	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	private static final Log log = LogFactory.getLog(ClusterAuthorDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
		if (theDocumentCluster!= null)
			parentEntities.addElement(theDocumentCluster);

		if (theDocumentCluster == null) {
		} else {
			cid = theDocumentCluster.getCid();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from medline_clustering.cluster_author where 1=1"
                                                        + (cid == 0 ? "" : " and cid = ? ")
                                                        + (seqnum == 0 ? "" : " and seqnum = ? "));
            if (cid != 0) stat.setInt(webapp_keySeq++, cid);
            if (seqnum != 0) stat.setInt(webapp_keySeq++, seqnum);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating ClusterAuthor deleter", e);
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating ClusterAuthor deleter");
        } finally {
            freeConnection();
        }

        return SKIP_BODY;
    }

	public int doEndTag() throws JspException {
		clearServiceState();
		return super.doEndTag();
	}

    private void clearServiceState() {
        cid = 0;
        seqnum = 0;
        parentEntities = new Vector<MEDLINETagLibTagSupport>();

        this.rs = null;
        this.var = null;
        this.rsCount = 0;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }



	public int getCid () {
		return cid;
	}

	public void setCid (int cid) {
		this.cid = cid;
	}

	public int getActualCid () {
		return cid;
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
}
