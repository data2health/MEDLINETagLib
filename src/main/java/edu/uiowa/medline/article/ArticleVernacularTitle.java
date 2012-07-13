package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleVernacularTitle extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticleVernacularTitle.class);


	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (!theArticle.commitNeeded) {
				pageContext.getOut().print(theArticle.getVernacularTitle());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Article for vernacularTitle tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for vernacularTitle tag ");
		}
		return SKIP_BODY;
	}

	public String getVernacularTitle() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getVernacularTitle();
		} catch (Exception e) {
			log.error(" Can't find enclosing Article for vernacularTitle tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for vernacularTitle tag ");
		}
	}

	public void setVernacularTitle(String vernacularTitle) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setVernacularTitle(vernacularTitle);
		} catch (Exception e) {
			log.error("Can't find enclosing Article for vernacularTitle tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for vernacularTitle tag ");
		}
	}

}
