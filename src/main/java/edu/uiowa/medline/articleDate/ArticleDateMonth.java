package edu.uiowa.medline.articleDate;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleDateMonth extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticleDateMonth.class);


	public int doStartTag() throws JspException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			if (!theArticleDate.commitNeeded) {
				pageContext.getOut().print(theArticleDate.getMonth());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing ArticleDate for month tag ", e);
			throw new JspTagException("Error: Can't find enclosing ArticleDate for month tag ");
		}
		return SKIP_BODY;
	}

	public int getMonth() throws JspTagException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			return theArticleDate.getMonth();
		} catch (Exception e) {
			log.error(" Can't find enclosing ArticleDate for month tag ", e);
			throw new JspTagException("Error: Can't find enclosing ArticleDate for month tag ");
		}
	}

	public void setMonth(int month) throws JspTagException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			theArticleDate.setMonth(month);
		} catch (Exception e) {
			log.error("Can't find enclosing ArticleDate for month tag ", e);
			throw new JspTagException("Error: Can't find enclosing ArticleDate for month tag ");
		}
	}

}
