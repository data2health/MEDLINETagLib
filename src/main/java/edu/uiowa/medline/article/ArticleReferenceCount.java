package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleReferenceCount extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticleReferenceCount.class);


	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (!theArticle.commitNeeded) {
				pageContext.getOut().print(theArticle.getReferenceCount());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Article for referenceCount tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for referenceCount tag ");
		}
		return SKIP_BODY;
	}

	public int getReferenceCount() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getReferenceCount();
		} catch (Exception e) {
			log.error(" Can't find enclosing Article for referenceCount tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for referenceCount tag ");
		}
	}

	public void setReferenceCount(int referenceCount) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setReferenceCount(referenceCount);
		} catch (Exception e) {
			log.error("Can't find enclosing Article for referenceCount tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for referenceCount tag ");
		}
	}

}
