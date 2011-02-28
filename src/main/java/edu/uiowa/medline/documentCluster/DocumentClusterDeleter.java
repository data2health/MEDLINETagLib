package edu.uiowa.medline.documentCluster;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.MEDLINETagLibBodyTagSupport;

@SuppressWarnings("serial")

public class DocumentClusterDeleter extends MEDLINETagLibBodyTagSupport {
    int cid = 0;
    String lastName = null;
    String foreName = null;
	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {



        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from medline_clustering.document_cluster where 1=1"
                                                        + (cid == 0 ? "" : " and cid = ?")
                                                        );
            if (cid != 0) stat.setInt(webapp_keySeq++, cid);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating DocumentCluster deleter");
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
}
