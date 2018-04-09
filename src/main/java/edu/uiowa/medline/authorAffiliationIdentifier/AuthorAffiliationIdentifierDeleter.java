package edu.uiowa.medline.authorAffiliationIdentifier;


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
import edu.uiowa.medline.authorAffiliation.AuthorAffiliation;

@SuppressWarnings("serial")
public class AuthorAffiliationIdentifierDeleter extends MEDLINETagLibBodyTagSupport {
    int pmid = 0;
    int seqnum = 0;
    int anum = 0;
    int inum = 0;
    String source = null;
    String identifier = null;
	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	private static final Log log = LogFactory.getLog(AuthorAffiliationIdentifierDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
		if (theAuthorAffiliation!= null)
			parentEntities.addElement(theAuthorAffiliation);

		if (theAuthorAffiliation == null) {
		} else {
			pmid = theAuthorAffiliation.getPmid();
			seqnum = theAuthorAffiliation.getSeqnum();
			anum = theAuthorAffiliation.getAnum();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from medline18.author_affiliation_identifier where 1=1"
                                                        + (pmid == 0 ? "" : " and pmid = ? ")
                                                        + (seqnum == 0 ? "" : " and seqnum = ? ")
                                                        + (anum == 0 ? "" : " and anum = ? ")
                                                        + (inum == 0 ? "" : " and inum = ? "));
            if (pmid != 0) stat.setInt(webapp_keySeq++, pmid);
            if (seqnum != 0) stat.setInt(webapp_keySeq++, seqnum);
            if (anum != 0) stat.setInt(webapp_keySeq++, anum);
            if (inum != 0) stat.setInt(webapp_keySeq++, inum);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating AuthorAffiliationIdentifier deleter", e);
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating AuthorAffiliationIdentifier deleter");
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
        anum = 0;
        inum = 0;
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

	public int getAnum () {
		return anum;
	}

	public void setAnum (int anum) {
		this.anum = anum;
	}

	public int getActualAnum () {
		return anum;
	}

	public int getInum () {
		return inum;
	}

	public void setInum (int inum) {
		this.inum = inum;
	}

	public int getActualInum () {
		return inum;
	}
}
