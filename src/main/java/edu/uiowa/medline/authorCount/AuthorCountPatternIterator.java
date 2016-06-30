package edu.uiowa.medline.authorCount;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.MEDLINETagLibBodyTagSupport;

@SuppressWarnings("serial")

public class AuthorCountPatternIterator extends MEDLINETagLibBodyTagSupport {
    String lastName = null;
    String foreName = null;
    int count = 0;
	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();


    ResultSet rs = null;
    String sortCriteria = null;
    String var = null;
    int rsCount = 0;

	public static String authorCountCount() throws JspTagException {
		int count = 0;
		AuthorCountIterator theIterator = new AuthorCountIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from medline14.author_count"
						);

			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
			theIterator.freeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			theIterator.freeConnection();
			throw new JspTagException("Error: JDBC error generating AuthorCount iterator");
		}
		return "" + count;
	}

	public static Boolean authorCountExists (String lastName, String foreName) throws JspTagException {
		int count = 0;
		AuthorCountIterator theIterator = new AuthorCountIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from medline14.author_count where 1=1"
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
			theIterator.freeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			theIterator.freeConnection();
			throw new JspTagException("Error: JDBC error generating AuthorCount iterator");
		}
		return count > 0;
	}

    @SuppressWarnings("unused")
    public int doStartTag() throws JspException {



        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT last_name, fore_name from medline14.author_count where last_name = ? and fore_name ~ ?"
                                                        + " order by " + generateSortCriteria());
            stat.setString(1, lastName.substring(0, 1).toUpperCase() + lastName.substring(1));
            stat.setString(2, "^" + foreName.substring(0, 1).toUpperCase() + foreName.substring(1));
            rs = stat.executeQuery();

            if (rs.next()) {
                lastName = rs.getString(1);
                foreName = rs.getString(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            freeConnection();
            throw new JspTagException("Error: JDBC error generating AuthorCount iterator");
        }

        return SKIP_BODY;
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "last_name,fore_name";
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
            e.printStackTrace();
            freeConnection();
            throw new JspTagException("Error: JDBC error iterating across AuthorCount");
        }
        clearServiceState();
        freeConnection();
        return SKIP_BODY;
    }

    private void clearServiceState() {
        lastName = null;
        foreName = null;
        parentEntities = new Vector<MEDLINETagLibTagSupport>();

        this.rs = null;
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

	public String getForeName () {
		return foreName;
	}

	public void setForeName (String foreName) {
		this.foreName = foreName;
	}
}
