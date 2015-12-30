package edu.uiowa.medline.authorCount;


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

@SuppressWarnings("serial")
public class AuthorCountIterator extends MEDLINETagLibBodyTagSupport {
    String lastName = null;
    String foreName = null;
    int count = 0;
	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	private static final Log log = LogFactory.getLog(AuthorCountIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

	public static String authorCountCount() throws JspTagException {
		int count = 0;
		AuthorCountIterator theIterator = new AuthorCountIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from medline16.author_count"
						);

			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating AuthorCount iterator", e);
			throw new JspTagException("Error: JDBC error generating AuthorCount iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean authorCountExists (String lastName, String foreName) throws JspTagException {
		int count = 0;
		AuthorCountIterator theIterator = new AuthorCountIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from medline16.author_count where 1=1"
						+ " and last_name = ?"
						+ " and fore_name = ?"
						);

			stat.setString(1,lastName);
			stat.setString(2,foreName);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating AuthorCount iterator", e);
			throw new JspTagException("Error: JDBC error generating AuthorCount iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {



      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        +  generateLimitCriteria());
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT medline16.author_count.last_name, medline16.author_count.fore_name from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            rs = stat.executeQuery();

            if (rs.next()) {
                lastName = rs.getString(1);
                foreName = rs.getString(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating AuthorCount iterator: " + stat.toString(), e);
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error generating AuthorCount iterator: " + stat.toString());
        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("medline16.author_count");
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
            return "last_name,fore_name";
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
                lastName = rs.getString(1);
                foreName = rs.getString(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across AuthorCount", e);
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error iterating across AuthorCount");
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
            rs.close();
            stat.close();
        } catch (SQLException e) {
            log.error("JDBC error ending AuthorCount iterator",e);
            throw new JspTagException("Error: JDBC error ending AuthorCount iterator");
        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        lastName = null;
        foreName = null;
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



	public String getLastName () {
		return lastName;
	}

	public void setLastName (String lastName) {
		this.lastName = lastName;
	}

	public String getActualLastName () {
		return lastName;
	}

	public String getForeName () {
		return foreName;
	}

	public void setForeName (String foreName) {
		this.foreName = foreName;
	}

	public String getActualForeName () {
		return foreName;
	}
}
