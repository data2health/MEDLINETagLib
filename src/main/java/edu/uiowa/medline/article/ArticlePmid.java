package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticlePmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticlePmid.class);


	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (!theArticle.commitNeeded) {
				pageContext.getOut().print(theArticle.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Article for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing Article for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing Article for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for pmid tag ");
		}
	}

}
