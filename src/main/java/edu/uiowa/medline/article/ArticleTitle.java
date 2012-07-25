package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleTitle extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticleTitle.class);


	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (!theArticle.commitNeeded) {
				pageContext.getOut().print(theArticle.getTitle());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Article for title tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for title tag ");
		}
		return SKIP_BODY;
	}

	public String getTitle() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getTitle();
		} catch (Exception e) {
			log.error(" Can't find enclosing Article for title tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for title tag ");
		}
	}

	public void setTitle(String title) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setTitle(title);
		} catch (Exception e) {
			log.error("Can't find enclosing Article for title tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for title tag ");
		}
	}

}
