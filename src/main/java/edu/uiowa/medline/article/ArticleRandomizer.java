package edu.uiowa.medline.article;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.MEDLINETagLibBodyTagSupport;

@SuppressWarnings("serial")

public class ArticleRandomizer extends MEDLINETagLibBodyTagSupport {
	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();


    ResultSet rs = null;
    String var = null;

    public int doStartTag() throws JspException {



        PreparedStatement stat;
        try {
            stat = getConnection().prepareStatement("select pmid from medline11.article where pmid > RANDOM()*10000000000 order by pmid limit 1");
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var, rs.getInt(1));
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new JspTagException("Error: JDBC error generating Citation iterator");
        }

        return SKIP_BODY;
    }

    public int doAfterBody() throws JspTagException {
        try {
            if (rs.next()) {
                pageContext.setAttribute(var, rs.getInt(1));
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new JspTagException("Error: JDBC error iterating across Citation");
        }
        return SKIP_BODY;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

}
