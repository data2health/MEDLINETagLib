package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleDateCompletedToNow extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticleDateCompletedToNow.class);


	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setDateCompletedToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing Article for dateCompleted tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for dateCompleted tag ");
		}
		return SKIP_BODY;
	}

	public Date getDateCompleted() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getDateCompleted();
		} catch (Exception e) {
			log.error("Can't find enclosing Article for dateCompleted tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for dateCompleted tag ");
		}
	}

	public void setDateCompleted( ) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setDateCompletedToNow( );
		} catch (Exception e) {
			log.error("Can't find enclosing Article for dateCompleted tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for dateCompleted tag ");
		}
	}

}
