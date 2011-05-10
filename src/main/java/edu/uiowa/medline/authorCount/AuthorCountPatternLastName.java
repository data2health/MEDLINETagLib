package edu.uiowa.medline.authorCount;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorCountPatternLastName extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			AuthorCountPattern theAuthorCountPattern = (AuthorCountPattern)findAncestorWithClass(this, AuthorCountPattern.class);
			if (!theAuthorCountPattern.commitNeeded) {
				pageContext.getOut().print(theAuthorCountPattern.getLastName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AuthorCount for lastName tag ");
		}
		return SKIP_BODY;
	}

	public String getLastName() throws JspTagException {
		try {
		    AuthorCountPattern theAuthorCountPattern = (AuthorCountPattern)findAncestorWithClass(this, AuthorCountPattern.class);
			return theAuthorCountPattern.getLastName();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AuthorCount for lastName tag ");
		}
	}

	public void setLastName(String lastName) throws JspTagException {
		try {
		    AuthorCountPattern theAuthorCountPattern = (AuthorCountPattern)findAncestorWithClass(this, AuthorCountPattern.class);
		    theAuthorCountPattern.setLastName(lastName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AuthorCount for lastName tag ");
		}
	}

}
