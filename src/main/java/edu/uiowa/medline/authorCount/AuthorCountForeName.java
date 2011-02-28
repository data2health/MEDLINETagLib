package edu.uiowa.medline.authorCount;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorCountForeName extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			AuthorCount theAuthorCount = (AuthorCount)findAncestorWithClass(this, AuthorCount.class);
			if (!theAuthorCount.commitNeeded) {
				pageContext.getOut().print(theAuthorCount.getForeName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AuthorCount for foreName tag ");
		}
		return SKIP_BODY;
	}

	public String getForeName() throws JspTagException {
		try {
			AuthorCount theAuthorCount = (AuthorCount)findAncestorWithClass(this, AuthorCount.class);
			return theAuthorCount.getForeName();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AuthorCount for foreName tag ");
		}
	}

	public void setForeName(String foreName) throws JspTagException {
		try {
			AuthorCount theAuthorCount = (AuthorCount)findAncestorWithClass(this, AuthorCount.class);
			theAuthorCount.setForeName(foreName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AuthorCount for foreName tag ");
		}
	}

}
