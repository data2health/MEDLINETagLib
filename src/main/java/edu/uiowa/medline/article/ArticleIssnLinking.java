package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleIssnLinking extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (!theArticle.commitNeeded) {
				pageContext.getOut().print(theArticle.getIssnLinking());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Article for issnLinking tag ");
		}
		return SKIP_BODY;
	}

	public String getIssnLinking() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getIssnLinking();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Article for issnLinking tag ");
		}
	}

	public void setIssnLinking(String issnLinking) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setIssnLinking(issnLinking);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Article for issnLinking tag ");
		}
	}

}
