package edu.uiowa.medline.article;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibBodyTagSupport;
import edu.uiowa.medline.commentsCorrections.CommentsCorrectionsIterator;

@SuppressWarnings("serial")

public class ArticleByAuthorIterator extends MEDLINETagLibBodyTagSupport {
    int pmid = 0;
    int year = 0;
    String lastName = null;
    String foreName = null;

	private static final Log log =LogFactory.getLog(Article.class);

	public static String authorArticleCount(String lastName, String foreName, String year) throws JspTagException {
		int count = 0;
		CommentsCorrectionsIterator theIterator = new CommentsCorrectionsIterator();
		try {
			int argCount = 1;
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from medline12.author"
						+ (year != null && year.length() > 0 ? ",medline12.journal" : "")
						+ " where last_name = ?"
						+ (foreName != null && foreName.length() > 0 ? " and fore_name = ?" : "")
						+ (year != null && year.length() > 0 ? " and author.pmid=journal.pmid and pub_year = ?" : "")
						);

			stat.setString(argCount++,lastName);
			if (foreName != null && foreName.length() > 0)
				stat.setString(argCount++, foreName);
			if (year != null && year.length() > 0)
				stat.setInt(argCount++, Integer.parseInt(year));
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

	public static Boolean authorHasArticles(String lastName, String foreName, String year) throws JspTagException {
		return ! authorArticleCount(lastName, foreName, year).equals("0");
	}

    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {



      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where"
                                                        + generateJoinCriteria()
                                                        +  generateLimitCriteria());
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT medline12.article.pmid from " + generateFromClause() + " where"
                                                        + generateJoinCriteria()
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            rs = stat.executeQuery();

            if (rs.next()) {
                pmid = rs.getInt(1);
                pageContext.setAttribute(var, pmid);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error generating Article iterator: " + stat.toString());
        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer(" medline12.article,medline12.journal,medline12.author");
      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer(" article.pmid=journal.pmid and article.pmid=author.pmid");
       theBuffer.append(" and last_name='" + lastName + "'");
       if (foreName != null && foreName.length() > 0)
           theBuffer.append(" and fore_name='" + foreName + "'");
       if (year != 0)
           theBuffer.append(" and pub_year=" + year);
      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "pmid desc";
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
                pageContext.setAttribute(var, pmid);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error iterating across Article");
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new JspTagException("Error: JDBC error ending Article iterator");
        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        pmid = 0;
        year = 0;
        lastName = null;
        foreName = null;

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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getForeName() {
		return foreName;
	}

	public void setForeName(String foreName) {
		this.foreName = foreName;
	}
}
