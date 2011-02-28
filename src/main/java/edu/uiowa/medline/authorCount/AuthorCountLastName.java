package edu.uiowa.medline.authorCount;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorCountLastName extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			AuthorCount theAuthorCount = (AuthorCount)findAncestorWithClass(this, AuthorCount.class);
			if (!theAuthorCount.commitNeeded) {
				pageContext.getOut().print(theAuthorCount.getLastName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AuthorCount for lastName tag ");
		}
		return SKIP_BODY;
	}

	public String getLastName() throws JspTagException {
		try {
			AuthorCount theAuthorCount = (AuthorCount)findAncestorWithClass(this, AuthorCount.class);
			return theAuthorCount.getLastName();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AuthorCount for lastName tag ");
		}
	}

	public void setLastName(String lastName) throws JspTagException {
		try {
			AuthorCount theAuthorCount = (AuthorCount)findAncestorWithClass(this, AuthorCount.class);
			theAuthorCount.setLastName(lastName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AuthorCount for lastName tag ");
		}
	}

}
