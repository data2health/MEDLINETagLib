package edu.uiowa.medline.clusterDocument;


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
import edu.uiowa.medline.article.Article;
import edu.uiowa.medline.documentCluster.DocumentCluster;

@SuppressWarnings("serial")
public class ClusterDocumentDeleter extends MEDLINETagLibBodyTagSupport {
    int cid = 0;
    int pmid = 0;
	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	private static final Log log = LogFactory.getLog(ClusterDocumentDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Article theArticle = (Article)findAncestorWithClass(this, Article.class);
		if (theArticle!= null)
			parentEntities.addElement(theArticle);
		DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
		if (theDocumentCluster!= null)
			parentEntities.addElement(theDocumentCluster);

		if (theArticle == null) {
		} else {
			pmid = theArticle.getPmid();
		}
		if (theDocumentCluster == null) {
		} else {
			cid = theDocumentCluster.getCid();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from medline_clustering.cluster_document where 1=1"
                                                        + (cid == 0 ? "" : " and cid = ? ")
                                                        + (pmid == 0 ? "" : " and pmid = ? "));
            if (cid != 0) stat.setInt(webapp_keySeq++, cid);
            if (pmid != 0) stat.setInt(webapp_keySeq++, pmid);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating ClusterDocument deleter", e);
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating ClusterDocument deleter");
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
        pmid = 0;
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

	public int getPmid () {
		return pmid;
	}

	public void setPmid (int pmid) {
		this.pmid = pmid;
	}

	public int getActualPmid () {
		return pmid;
	}
}
