package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleAffiliation extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticleAffiliation.class);


	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (!theArticle.commitNeeded) {
				pageContext.getOut().print(theArticle.getAffiliation());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Article for affiliation tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for affiliation tag ");
		}
		return SKIP_BODY;
	}

	public String getAffiliation() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getAffiliation();
		} catch (Exception e) {
			log.error(" Can't find enclosing Article for affiliation tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for affiliation tag ");
		}
	}

	public void setAffiliation(String affiliation) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setAffiliation(affiliation);
		} catch (Exception e) {
			log.error("Can't find enclosing Article for affiliation tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for affiliation tag ");
		}
	}

}
