package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.util.Date;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleDateCreatedToNow extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setDateCreatedToNow( );
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Article for dateCreated tag ");
		}
		return SKIP_BODY;
	}

	public Date getDateCreated() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getDateCreated();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Article for dateCreated tag ");
		}
	}

	public void setDateCreated( ) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setDateCreatedToNow( );
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Article for dateCreated tag ");
		}
	}

}
