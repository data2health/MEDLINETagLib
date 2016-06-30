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
            stat = getConnection().prepareStatement("select last_name, initials, collective_name from medline14.author where pmid = ? order by seqnum");
            stat.setInt(1, ID);
            ResultSet rs = stat.executeQuery();
            StringBuffer authorString = new StringBuffer();
            while (rs.next()) {
			    String lastName = rs.getString(1);
			    String foreName = rs.getString(2);
			    String collective = rs.getString(3);
			    if (lastName == null)
			    	authorString.append((authorString.length() > 0 ? ", " : "") + collective);
			    else
			    	authorString.append((authorString.length() > 0 ? ", " : "") + lastName + ", " + foreName);
            }
            pageContext.getOut().print(authorString.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new JspTagException("Error: JDBC error generating Citation iterator");
        }
		return SKIP_BODY;
	}
    
    public int doEndTag() throws JspException {
	clearServiceState();
	return super.doEndTag();
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
