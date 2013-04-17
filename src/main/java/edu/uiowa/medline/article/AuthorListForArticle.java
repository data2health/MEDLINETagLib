package edu.uiowa.medline.article;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.journal.Journal;

@SuppressWarnings("serial")
public class AuthorListForArticle extends MEDLINETagLibTagSupport {

    int ID = 0;
    Journal theJournal = null;

    public int doStartTag() throws JspException {
        PreparedStatement stat;
        try {
            stat = getConnection().prepareStatement("select last_name, initials from medline13.author where pmid = ? order by seqnum");
            stat.setInt(1, ID);
            ResultSet rs = stat.executeQuery();
            StringBuffer authorString = new StringBuffer();
            while (rs.next()) {
                authorString.append((authorString.length() > 0 ? ", " : "") + rs.getString(1) + " " + rs.getString(2));
            }
            pageContext.getOut().print(authorString.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new JspTagException("Error: JDBC error generating Citation iterator");
        }
		return SKIP_BODY;
	}

    /**
     * @return the iD
     */
    public int getID() {
        return ID;
    }

    /**
     * @param id the iD to set
     */
    public void setID(int id) {
        ID = id;
    }

    private void clearServiceState () {
        ID = 0;
    }

}
