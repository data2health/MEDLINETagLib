package edu.uiowa.medline.meshQualifier;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.MEDLINETagLibBodyTagSupport;
import edu.uiowa.medline.meshHeading.MeshHeading;

@SuppressWarnings("serial")

public class MeshQualifierDeleter extends MEDLINETagLibBodyTagSupport {
    int pmid = 0;
    int seqnum = 0;
    int qnum = 0;
    String qualifierName = null;
    boolean major = false;
	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
		if (theMeshHeading!= null)
			parentEntities.addElement(theMeshHeading);

		if (theMeshHeading == null) {
		} else {
			pmid = theMeshHeading.getPmid();
			seqnum = theMeshHeading.getSeqnum();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from medline11.mesh_qualifier where 1=1"
                                                        + (pmid == 0 ? "" : " and pmid = ?")
                                                        + (seqnum == 0 ? "" : " and seqnum = ?")
                                                        + (qnum == 0 ? "" : " and qnum = ?")
                                                        );
            if (pmid != 0) stat.setInt(webapp_keySeq++, pmid);
            if (seqnum != 0) stat.setInt(webapp_keySeq++, seqnum);
            if (qnum != 0) stat.setInt(webapp_keySeq++, qnum);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating MeshQualifier deleter");
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
        qnum = 0;
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

	public int getQnum () {
		return qnum;
	}

	public void setQnum (int qnum) {
		this.qnum = qnum;
	}

	public int getActualQnum () {
		return qnum;
	}
}
