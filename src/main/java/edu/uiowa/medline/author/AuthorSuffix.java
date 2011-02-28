package edu.uiowa.medline.author;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorSuffix extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			if (!theAuthor.commitNeeded) {
				pageContext.getOut().print(theAuthor.getSuffix());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Author for suffix tag ");
		}
		return SKIP_BODY;
	}

	public String getSuffix() throws JspTagException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			return theAuthor.getSuffix();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Author for suffix tag ");
		}
	}

	public void setSuffix(String suffix) throws JspTagException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			theAuthor.setSuffix(suffix);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Author for suffix tag ");
		}
	}

}
