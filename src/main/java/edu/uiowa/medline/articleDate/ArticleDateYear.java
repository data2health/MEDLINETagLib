package edu.uiowa.medline.articleDate;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleDateYear extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticleDateYear.class);


	public int doStartTag() throws JspException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			if (!theArticleDate.commitNeeded) {
				pageContext.getOut().print(theArticleDate.getYear());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing ArticleDate for year tag ", e);
			throw new JspTagException("Error: Can't find enclosing ArticleDate for year tag ");
		}
		return SKIP_BODY;
	}

	public int getYear() throws JspTagException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			return theArticleDate.getYear();
		} catch (Exception e) {
			log.error(" Can't find enclosing ArticleDate for year tag ", e);
			throw new JspTagException("Error: Can't find enclosing ArticleDate for year tag ");
		}
	}

	public void setYear(int year) throws JspTagException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			theArticleDate.setYear(year);
		} catch (Exception e) {
			log.error("Can't find enclosing ArticleDate for year tag ", e);
			throw new JspTagException("Error: Can't find enclosing ArticleDate for year tag ");
		}
	}

}
