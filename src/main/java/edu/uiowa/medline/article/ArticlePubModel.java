package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticlePubModel extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticlePubModel.class);


	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (!theArticle.commitNeeded) {
				pageContext.getOut().print(theArticle.getPubModel());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Article for pubModel tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for pubModel tag ");
		}
		return SKIP_BODY;
	}

	public String getPubModel() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getPubModel();
		} catch (Exception e) {
			log.error(" Can't find enclosing Article for pubModel tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for pubModel tag ");
		}
	}

	public void setPubModel(String pubModel) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setPubModel(pubModel);
		} catch (Exception e) {
			log.error("Can't find enclosing Article for pubModel tag ", e);
			throw new JspTagException("Error: Can't find enclosing Article for pubModel tag ");
		}
	}

}
