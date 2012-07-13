package edu.uiowa.medline.investigatorNameId;


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
import edu.uiowa.medline.investigator.Investigator;

@SuppressWarnings("serial")
public class InvestigatorNameIdDeleter extends MEDLINETagLibBodyTagSupport {
    int pmid = 0;
    int seqnum = 0;
    int nnum = 0;
    String nameId = null;
    String source = null;
	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	private static final Log log = LogFactory.getLog(InvestigatorNameIdDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
		if (theInvestigator!= null)
			parentEntities.addElement(theInvestigator);

		if (theInvestigator == null) {
		} else {
			pmid = theInvestigator.getPmid();
			seqnum = theInvestigator.getSeqnum();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from medline12.investigator_name_id where 1=1"
                                                        + (pmid == 0 ? "" : " and pmid = ? ")
                                                        + (seqnum == 0 ? "" : " and seqnum = ? ")
                                                        + (nnum == 0 ? "" : " and nnum = ? "));
            if (pmid != 0) stat.setInt(webapp_keySeq++, pmid);
            if (seqnum != 0) stat.setInt(webapp_keySeq++, seqnum);
            if (nnum != 0) stat.setInt(webapp_keySeq++, nnum);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating InvestigatorNameId deleter", e);
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating InvestigatorNameId deleter");
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
        nnum = 0;
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

	public int getNnum () {
		return nnum;
	}

	public void setNnum (int nnum) {
		this.nnum = nnum;
	}

	public int getActualNnum () {
		return nnum;
	}
}
