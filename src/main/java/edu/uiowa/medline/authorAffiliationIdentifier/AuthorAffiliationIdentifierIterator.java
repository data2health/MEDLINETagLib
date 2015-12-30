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
public class AuthorAffiliationIdentifierIterator extends MEDLINETagLibBodyTagSupport {
    int pmid = 0;
    int seqnum = 0;
    int anum = 0;
    int inum = 0;
    String source = null;
    String identifier = null;
	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	private static final Log log = LogFactory.getLog(AuthorAffiliationIdentifierIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

	public static String authorAffiliationIdentifierCountByAuthorAffiliation(String pmid, String seqnum, String anum) throws JspTagException {
		int count = 0;
		AuthorAffiliationIdentifierIterator theIterator = new AuthorAffiliationIdentifierIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from medline16.author_affiliation_identifier where 1=1"
						+ " and pmid = ?"
						+ " and seqnum = ?"
						+ " and anum = ?"
						);

			stat.setInt(1,Integer.parseInt(pmid));
			stat.setInt(2,Integer.parseInt(seqnum));
			stat.setInt(3,Integer.parseInt(anum));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating AuthorAffiliationIdentifier iterator", e);
			throw new JspTagException("Error: JDBC error generating AuthorAffiliationIdentifier iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean authorAffiliationHasAuthorAffiliationIdentifier(String pmid, String seqnum, String anum) throws JspTagException {
		return ! authorAffiliationIdentifierCountByAuthorAffiliation(pmid, seqnum, anum).equals("0");
	}

	public static Boolean authorAffiliationIdentifierExists (String pmid, String seqnum, String anum, String inum) throws JspTagException {
		int count = 0;
		AuthorAffiliationIdentifierIterator theIterator = new AuthorAffiliationIdentifierIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from medline16.author_affiliation_identifier where 1=1"
						+ " and pmid = ?"
						+ " and seqnum = ?"
						+ " and anum = ?"
						+ " and inum = ?"
						);

			stat.setInt(1,Integer.parseInt(pmid));
			stat.setInt(2,Integer.parseInt(seqnum));
			stat.setInt(3,Integer.parseInt(anum));
			stat.setInt(4,Integer.parseInt(inum));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating AuthorAffiliationIdentifier iterator", e);
			throw new JspTagException("Error: JDBC error generating AuthorAffiliationIdentifier iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

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


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (pmid == 0 ? "" : " and pmid = ?")
                                                        + (seqnum == 0 ? "" : " and seqnum = ?")
                                                        + (anum == 0 ? "" : " and anum = ?")
                                                        +  generateLimitCriteria());
            if (pmid != 0) stat.setInt(webapp_keySeq++, pmid);
            if (seqnum != 0) stat.setInt(webapp_keySeq++, seqnum);
            if (anum != 0) stat.setInt(webapp_keySeq++, anum);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT medline16.author_affiliation_identifier.pmid, medline16.author_affiliation_identifier.seqnum, medline16.author_affiliation_identifier.anum, medline16.author_affiliation_identifier.inum from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (pmid == 0 ? "" : " and pmid = ?")
                                                        + (seqnum == 0 ? "" : " and seqnum = ?")
                                                        + (anum == 0 ? "" : " and anum = ?")
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            if (pmid != 0) stat.setInt(webapp_keySeq++, pmid);
            if (seqnum != 0) stat.setInt(webapp_keySeq++, seqnum);
            if (anum != 0) stat.setInt(webapp_keySeq++, anum);
            rs = stat.executeQuery();

            if (rs.next()) {
                pmid = rs.getInt(1);
                seqnum = rs.getInt(2);
                anum = rs.getInt(3);
                inum = rs.getInt(4);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating AuthorAffiliationIdentifier iterator: " + stat.toString(), e);
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error generating AuthorAffiliationIdentifier iterator: " + stat.toString());
        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("medline16.author_affiliation_identifier");
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
            return "pmid,seqnum,anum,inum";
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
                anum = rs.getInt(3);
                inum = rs.getInt(4);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across AuthorAffiliationIdentifier", e);
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error iterating across AuthorAffiliationIdentifier");
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
            rs.close();
            stat.close();
        } catch (SQLException e) {
            log.error("JDBC error ending AuthorAffiliationIdentifier iterator",e);
            throw new JspTagException("Error: JDBC error ending AuthorAffiliationIdentifier iterator");
        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        pmid = 0;
        seqnum = 0;
        anum = 0;
        inum = 0;
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
