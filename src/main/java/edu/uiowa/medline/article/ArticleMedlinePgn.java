package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleMedlinePgn extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticleMedlinePgn.class);


	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (!theArticle.commitNeeded) {
				pageContext.getOut().print(theArticle.getMedlinePgn());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Article for medlinePgn tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for medlinePgn tag ");
		}
		return SKIP_BODY;
	}

	public String getMedlinePgn() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getMedlinePgn();
		} catch (Exception e) {
			log.error(" Can't find enclosing Article for medlinePgn tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for medlinePgn tag ");
		}
	}

	public void setMedlinePgn(String medlinePgn) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setMedlinePgn(medlinePgn);
		} catch (Exception e) {
			log.error("Can't find enclosing Article for medlinePgn tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for medlinePgn tag ");
		}
	}

}
