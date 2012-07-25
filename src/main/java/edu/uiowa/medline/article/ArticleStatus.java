package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleStatus extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticleStatus.class);


	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (!theArticle.commitNeeded) {
				pageContext.getOut().print(theArticle.getStatus());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Article for status tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for status tag ");
		}
		return SKIP_BODY;
	}

	public String getStatus() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getStatus();
		} catch (Exception e) {
			log.error(" Can't find enclosing Article for status tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for status tag ");
		}
	}

	public void setStatus(String status) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setStatus(status);
		} catch (Exception e) {
			log.error("Can't find enclosing Article for status tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for status tag ");
		}
	}

}
