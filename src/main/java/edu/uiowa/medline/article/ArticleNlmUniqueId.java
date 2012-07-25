package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleNlmUniqueId extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticleNlmUniqueId.class);


	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (!theArticle.commitNeeded) {
				pageContext.getOut().print(theArticle.getNlmUniqueId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Article for nlmUniqueId tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for nlmUniqueId tag ");
		}
		return SKIP_BODY;
	}

	public String getNlmUniqueId() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getNlmUniqueId();
		} catch (Exception e) {
			log.error(" Can't find enclosing Article for nlmUniqueId tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for nlmUniqueId tag ");
		}
	}

	public void setNlmUniqueId(String nlmUniqueId) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setNlmUniqueId(nlmUniqueId);
		} catch (Exception e) {
			log.error("Can't find enclosing Article for nlmUniqueId tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for nlmUniqueId tag ");
		}
	}

}
