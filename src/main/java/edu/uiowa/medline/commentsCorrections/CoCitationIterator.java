package edu.uiowa.medline.commentsCorrections;


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

public class CoCitationIterator extends MEDLINETagLibBodyTagSupport {
    int pmid = 0;
    int count = 0;
    String refType = null;
    String source = null;
    int ref_pmid = 0;
    String note = null;
	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	private static final Log log =LogFactory.getLog(CommentsCorrections.class);

	public static String coCitationCount(String pmid) throws JspTagException {
		int count = 0;
		CommentsCorrectionsIterator theIterator = new CommentsCorrectionsIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("select count(distinct co2.pmid) from medline11.comments_corrections as co1, medline11.comments_corrections as co2"
					+ " where co1.ref_type='Cites' and co2.ref_type='Cites' and co1.ref_pmid=co2.ref_pmid and co1.pmid!=co2.pmid and co1.pmid = ?"
						);

			stat.setInt(1,Integer.parseInt(pmid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating CommentsCorrections iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean articleHasCoCitingArticles(String pmid) throws JspTagException {
		return ! coCitationCount(pmid).equals("0");
	}

    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
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


      try {
            stat = getConnection().prepareStatement("select co2.pmid,count(*) from medline11.comments_corrections as co1, medline11.comments_corrections as co2 "
            		+ "where co1.ref_type='Cites' and co2.ref_type='Cites' and co1.ref_pmid=co2.ref_pmid and co1.pmid!=co2.pmid and co1.pmid=? group by 1 order by 2 desc, 1 desc" +  generateLimitCriteria());
            stat.setInt(1, pmid);
            rs = stat.executeQuery();

            if (rs.next()) {
                ref_pmid = rs.getInt(1);
                count = rs.getInt(2);
                pageContext.setAttribute(var, ref_pmid);
                pageContext.setAttribute("coCitationCount", count);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error generating CommentsCorrections iterator: " + stat.toString());
        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("medline11.comments_corrections");
      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer(" and ref_type='Cites'");
      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "pmid,seqnum";
        }
    }

    private String generateLimitCriteria() {
        if (limitCriteria > 0) {
            return " limit " + limitCriteria;
        } else {
            return " limit 25";
        }
    }

    public int doAfterBody() throws JspTagException {
        try {
            if (rs.next()) {
                ref_pmid = rs.getInt(1);
                count = rs.getInt(2);
                pageContext.setAttribute(var, ref_pmid);
                pageContext.setAttribute("coCitationCount", count);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error iterating across CommentsCorrections");
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new JspTagException("Error: JDBC error ending CommentsCorrections iterator");
        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        pmid = 0;
        count = 0;
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

}
