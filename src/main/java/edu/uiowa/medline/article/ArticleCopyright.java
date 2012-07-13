package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleCopyright extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticleCopyright.class);


	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (!theArticle.commitNeeded) {
				pageContext.getOut().print(theArticle.getCopyright());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Article for copyright tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for copyright tag ");
		}
		return SKIP_BODY;
	}

	public String getCopyright() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getCopyright();
		} catch (Exception e) {
			log.error(" Can't find enclosing Article for copyright tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for copyright tag ");
		}
	}

	public void setCopyright(String copyright) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setCopyright(copyright);
		} catch (Exception e) {
			log.error("Can't find enclosing Article for copyright tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for copyright tag ");
		}
	}

}
