package edu.uiowa.medline.citationSubset;


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

@SuppressWarnings("serial")
public class CitationSubsetDeleter extends MEDLINETagLibBodyTagSupport {
    int pmid = 0;
    int seqnum = 0;
    String label = null;
	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	private static final Log log = LogFactory.getLog(CitationSubsetDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Article theArticle = (Article)findAncestorWithClass(this, Article.class);
		if (theArticle!= null)
			parentEntities.addElement(theArticle);

		if (theArticle == null) {
		} else {
			pmid = theArticle.getPmid();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from medline13.citation_subset where 1=1"
                                                        + (pmid == 0 ? "" : " and pmid = ? ")
                                                        + (seqnum == 0 ? "" : " and seqnum = ? "));
            if (pmid != 0) stat.setInt(webapp_keySeq++, pmid);
            if (seqnum != 0) stat.setInt(webapp_keySeq++, seqnum);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating CitationSubset deleter", e);
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating CitationSubset deleter");
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
        pmid = 0;
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
}
