package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleCountry extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (!theArticle.commitNeeded) {
				pageContext.getOut().print(theArticle.getCountry());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Article for country tag ");
		}
		return SKIP_BODY;
	}

	public String getCountry() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getCountry();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Article for country tag ");
		}
	}

	public void setCountry(String country) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setCountry(country);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Article for country tag ");
		}
	}

}
