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

public class InvestigatorNameIdIterator extends MEDLINETagLibBodyTagSupport {
    int pmid = 0;
    int seqnum = 0;
    int nnum = 0;
    String nameId = null;
    String source = null;
	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	private static final Log log =LogFactory.getLog(InvestigatorNameId.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

	public static String investigatorNameIdCountByInvestigator(String pmid, String seqnum) throws JspTagException {
		int count = 0;
		InvestigatorNameIdIterator theIterator = new InvestigatorNameIdIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from medline11.investigator_name_id where 1=1"
						+ " and pmid = ?"
						+ " and seqnum = ?"
						);

			stat.setInt(1,Integer.parseInt(pmid));
			stat.setInt(2,Integer.parseInt(seqnum));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating InvestigatorNameId iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean investigatorHasInvestigatorNameId(String pmid, String seqnum) throws JspTagException {
		return ! investigatorNameIdCountByInvestigator(pmid, seqnum).equals("0");
	}

	public static Boolean investigatorNameIdExists (String pmid, String seqnum, String nnum) throws JspTagException {
		int count = 0;
		InvestigatorNameIdIterator theIterator = new InvestigatorNameIdIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from medline11.investigator_name_id where 1=1"
						+ " and pmid = ?"
						+ " and seqnum = ?"
						+ " and nnum = ?"
						);

			stat.setInt(1,Integer.parseInt(pmid));
			stat.setInt(2,Integer.parseInt(seqnum));
			stat.setInt(3,Integer.parseInt(nnum));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating InvestigatorNameId iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {
		Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
		if (theInvestigator!= null)
			parentEntities.addElement(theInvestigator);

		if (theInvestigator == null) {
		} else {
			pmid = theInvestigator.getPmid();
			seqnum = theInvestigator.getSeqnum();
		}


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (pmid == 0 ? "" : " and pmid = ?")
                                                        + (seqnum == 0 ? "" : " and seqnum = ?")
                                                        +  generateLimitCriteria());
            if (pmid != 0) stat.setInt(webapp_keySeq++, pmid);
            if (seqnum != 0) stat.setInt(webapp_keySeq++, seqnum);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT medline11.investigator_name_id.pmid, medline11.investigator_name_id.seqnum, medline11.investigator_name_id.nnum from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (pmid == 0 ? "" : " and pmid = ?")
                                                        + (seqnum == 0 ? "" : " and seqnum = ?")
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            if (pmid != 0) stat.setInt(webapp_keySeq++, pmid);
            if (seqnum != 0) stat.setInt(webapp_keySeq++, seqnum);
            rs = stat.executeQuery();

            if (rs.next()) {
                pmid = rs.getInt(1);
                seqnum = rs.getInt(2);
                nnum = rs.getInt(3);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error generating InvestigatorNameId iterator: " + stat.toString());
        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("medline11.investigator_name_id");
      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "pmid,seqnum,nnum";
        }
    }

    private String generateLimitCriteria() {
        if (limitCriteria > 0) {
            return " limit " + limitCriteria;
        } else {
            return "";
        }
    }

    public int doAfterBody() throws JspTagException {
        try {
            if (rs.next()) {
                pmid = rs.getInt(1);
                seqnum = rs.getInt(2);
                nnum = rs.getInt(3);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error iterating across InvestigatorNameId");
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new JspTagException("Error: JDBC error ending InvestigatorNameId iterator");
        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        pmid = 0;
        seqnum = 0;
        nnum = 0;
        parentEntities = new Vector<MEDLINETagLibTagSupport>();

        this.rs = null;
        this.stat = null;
        this.sortCriteria = null;
        this.var = null;
        this.rsCount = 0;
    }

    public String getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(String sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public int getLimitCriteria() {
        return limitCriteria;
    }

    public void setLimitCriteria(int limitCriteria) {
        this.limitCriteria = limitCriteria;
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
