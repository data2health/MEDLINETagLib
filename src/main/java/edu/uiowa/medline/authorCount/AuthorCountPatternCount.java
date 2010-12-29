package edu.uiowa.medline.authorCount;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorCountPatternCount extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			AuthorCountPattern theAuthorCountPattern = (AuthorCountPattern)findAncestorWithClass(this, AuthorCountPattern.class);
			if (!theAuthorCountPattern.commitNeeded) {
				pageContext.getOut().print(theAuthorCountPattern.getCount());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AuthorCount for count tag ");
		}
		return SKIP_BODY;
	}

	public int getCount() throws JspTagException {
		try {
		    AuthorCountPattern theAuthorCountPattern = (AuthorCountPattern)findAncestorWithClass(this, AuthorCountPattern.class);
			return theAuthorCountPattern.getCount();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AuthorCount for count tag ");
		}
	}

	public void setCount(int count) throws JspTagException {
		try {
		    AuthorCountPattern theAuthorCountPattern = (AuthorCountPattern)findAncestorWithClass(this, AuthorCountPattern.class);
			theAuthorCountPattern.setCount(count);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AuthorCount for count tag ");
		}
	}

}
