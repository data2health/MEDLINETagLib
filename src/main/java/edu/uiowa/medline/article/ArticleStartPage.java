package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleStartPage extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticleStartPage.class);


	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (!theArticle.commitNeeded) {
				pageContext.getOut().print(theArticle.getStartPage());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Article for startPage tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for startPage tag ");
		}
		return SKIP_BODY;
	}

	public int getStartPage() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getStartPage();
		} catch (Exception e) {
			log.error(" Can't find enclosing Article for startPage tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for startPage tag ");
		}
	}

	public void setStartPage(int startPage) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setStartPage(startPage);
		} catch (Exception e) {
			log.error("Can't find enclosing Article for startPage tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for startPage tag ");
		}
	}

}
