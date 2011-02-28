package edu.uiowa.medline.articleDate;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleDatePmid extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			if (!theArticleDate.commitNeeded) {
				pageContext.getOut().print(theArticleDate.getPmid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ArticleDate for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			return theArticleDate.getPmid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ArticleDate for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			theArticleDate.setPmid(pmid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ArticleDate for pmid tag ");
		}
	}

}
