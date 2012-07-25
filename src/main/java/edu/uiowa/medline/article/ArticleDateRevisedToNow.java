package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleDateRevisedToNow extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticleDateRevisedToNow.class);


	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setDateRevisedToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing Article for dateRevised tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for dateRevised tag ");
		}
		return SKIP_BODY;
	}

	public Date getDateRevised() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getDateRevised();
		} catch (Exception e) {
			log.error("Can't find enclosing Article for dateRevised tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for dateRevised tag ");
		}
	}

	public void setDateRevised( ) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setDateRevisedToNow( );
		} catch (Exception e) {
			log.error("Can't find enclosing Article for dateRevised tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for dateRevised tag ");
		}
	}

}
