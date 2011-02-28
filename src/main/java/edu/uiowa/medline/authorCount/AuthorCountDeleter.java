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

public class AuthorCountDeleter extends MEDLINETagLibBodyTagSupport {
    String lastName = null;
    String foreName = null;
    int count = 0;
	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {



        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from medline11.author_count where 1=1"
                                                        + (lastName == null ? "" : " and last_name = ?")
                                                        + (foreName == null ? "" : " and fore_name = ?")
                                                        );
            if (lastName != null) stat.setString(webapp_keySeq++, lastName);
            if (foreName != null) stat.setString(webapp_keySeq++, foreName);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating AuthorCount deleter");
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
        lastName = null;
        foreName = null;
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
