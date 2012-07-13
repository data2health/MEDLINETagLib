package edu.uiowa.medline.articleDate;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleDateDay extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticleDateDay.class);


	public int doStartTag() throws JspException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			if (!theArticleDate.commitNeeded) {
				pageContext.getOut().print(theArticleDate.getDay());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing ArticleDate for day tag ", e);
			throw new JspTagException("Error: Can't find enclosing ArticleDate for day tag ");
		}
		return SKIP_BODY;
	}

	public int getDay() throws JspTagException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			return theArticleDate.getDay();
		} catch (Exception e) {
			log.error(" Can't find enclosing ArticleDate for day tag ", e);
			throw new JspTagException("Error: Can't find enclosing ArticleDate for day tag ");
		}
	}

	public void setDay(int day) throws JspTagException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			theArticleDate.setDay(day);
		} catch (Exception e) {
			log.error("Can't find enclosing ArticleDate for day tag ", e);
			throw new JspTagException("Error: Can't find enclosing ArticleDate for day tag ");
		}
	}

}
