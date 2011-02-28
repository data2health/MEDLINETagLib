package edu.uiowa.medline.article;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticlePubModel extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (!theArticle.commitNeeded) {
				pageContext.getOut().print(theArticle.getPubModel());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Article for pubModel tag ");
		}
		return SKIP_BODY;
	}

	public String getPubModel() throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			return theArticle.getPubModel();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Article for pubModel tag ");
		}
	}

	public void setPubModel(String pubModel) throws JspTagException {
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			theArticle.setPubModel(pubModel);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Article for pubModel tag ");
		}
	}

}
