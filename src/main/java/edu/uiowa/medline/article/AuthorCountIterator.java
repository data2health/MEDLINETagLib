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

public class AuthorCountIterator extends MEDLINETagLibBodyTagSupport {
    int pmid = 0;
    int year = 0;
    String lastName = null;
    String foreName = null;

	private static final Log log =LogFactory.getLog(Article.class);

    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {



      try {
    	  int argCount = 1;
    	  stat = getConnection().prepareStatement("SELECT last_name,fore_name,count(*) from medline12.author"
					+ (year != 0 ? ",medline12.journal" : "")
					+ " where last_name = ?"
					+ (foreName != null && foreName.length() > 0 ? " and fore_name ~ ?" : "")
					+ (year != 0 ? " and author.pmid=journal.pmid and pub_year = ?" : "")
					+ " group by last_name,fore_name order by last_name,fore_name"
					);
			stat.setString(argCount++,lastName);
			if (foreName != null && foreName.length() > 0)
				stat.setString(argCount++, "^" + foreName.charAt(0));
			if (year != 0)
				stat.setInt(argCount++, year);

            rs = stat.executeQuery();

            if (rs.next()) {
                lastName = rs.getString(1);
                foreName = rs.getString(2);
                int count = rs.getInt(3);
                pageContext.setAttribute("authorLastName", lastName);
                pageContext.setAttribute("authorForeName", foreName);
                pageContext.setAttribute(var, count);
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

    public int doAfterBody() throws JspTagException {
        try {
            if (rs.next()) {
                lastName = rs.getString(1);
                foreName = rs.getString(2);
                int count = rs.getInt(3);
                pageContext.setAttribute("authorLastName", lastName);
                pageContext.setAttribute("authorForeName", foreName);
                pageContext.setAttribute(var, count);
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
