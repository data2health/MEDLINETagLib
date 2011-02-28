package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleTa extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (!theArticle.commitNeeded) {
				pageContext.getOut().print(theArticle.getTa());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Article for ta tag ");
		}
		return SKIP_BODY;
	}

	public String getTa() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getTa();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Article for ta tag ");
		}
	}

	public void setTa(String ta) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setTa(ta);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Article for ta tag ");
		}
	}

}
