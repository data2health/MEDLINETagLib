package edu.uiowa.medline.article;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.MEDLINETagLibBodyTagSupport;

@SuppressWarnings("serial")

public class ArticleDeleter extends MEDLINETagLibBodyTagSupport {
    int pmid = 0;
    Date dateCreated = null;
    Date dateCompleted = null;
    Date dateRevised = null;
    String title = null;
    int startPage = 0;
    int endPage = 0;
    String medlinePgn = null;
    String abstractText = null;
    String copyright = null;
    String affiliation = null;
    String type = null;
    String vernacularTitle = null;
    String country = null;
    String ta = null;
    String nlmUniqueId = null;
    String issnLinking = null;
    int referenceCount = 0;
    String pubModel = null;
    String status = null;
	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {



        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from medline10.article where 1=1"
                                                        + (pmid == 0 ? "" : " and pmid = ?")
                                                        );
            if (pmid != 0) stat.setInt(webapp_keySeq++, pmid);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating Article deleter");
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
}
