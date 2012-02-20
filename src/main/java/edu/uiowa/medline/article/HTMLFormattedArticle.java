package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.journal.Journal;

@SuppressWarnings("serial")
public class HTMLFormattedArticle extends MEDLINETagLibTagSupport {

    int ID = 0;
    String defaultTargetPage = "../publications/browsePublication.jsp";
    String targetPage = defaultTargetPage;
    Journal theJournal = null;

    public int doStartTag() throws JspException {
		try {
            Article theArticle = (Article)findAncestorWithClass(this, Article.class);
            theJournal = new Journal();
            theJournal.setParent(theArticle);
            theJournal.doStartTag();
            
            if (theArticle != null && ID == 0)
                ID = theArticle.getPmid();
                
            pageContext.getOut().print(
            		" <a href=\"" + targetPage + "?id=" + theArticle.getPmid() + "\">" + theArticle.getTitle()
                            + "</a> <i>" + theArticle.getTa() + "</i> " + theJournal.getVolume() + (theJournal.getIssue() == null ? "" : "(" + theJournal.getIssue() + ")")
                            + ":" + theArticle.getMedlinePgn() + ", " + (theJournal.getMedlineDate() == null ? theJournal.getPubYear() : theJournal.getMedlineDate()) + ".");
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Article for abstract tag ");
		}
		return SKIP_BODY;
	}

    public int doEndTag() throws JspException {
    	theJournal.doEndTag();
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

    /**
     * @return the targetPage
     */
    public String getTargetPage() {
        return targetPage;
    }

    /**
     * @param targetPage the target page to set
     */
    public void setTargetPage(String targetPage) {
        this.targetPage = targetPage;
    }

    private void clearServiceState () {
        ID = 0;
        targetPage = defaultTargetPage;
    }

}
