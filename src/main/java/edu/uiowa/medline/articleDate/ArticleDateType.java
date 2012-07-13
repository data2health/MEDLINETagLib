package edu.uiowa.medline.articleDate;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleDateType extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticleDateType.class);


	public int doStartTag() throws JspException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			if (!theArticleDate.commitNeeded) {
				pageContext.getOut().print(theArticleDate.getType());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing ArticleDate for type tag ", e);
			throw new JspTagException("Error: Can't find enclosing ArticleDate for type tag ");
		}
		return SKIP_BODY;
	}

	public String getType() throws JspTagException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			return theArticleDate.getType();
		} catch (Exception e) {
			log.error(" Can't find enclosing ArticleDate for type tag ", e);
			throw new JspTagException("Error: Can't find enclosing ArticleDate for type tag ");
		}
	}

	public void setType(String type) throws JspTagException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			theArticleDate.setType(type);
		} catch (Exception e) {
			log.error("Can't find enclosing ArticleDate for type tag ", e);
			throw new JspTagException("Error: Can't find enclosing ArticleDate for type tag ");
		}
	}

}
