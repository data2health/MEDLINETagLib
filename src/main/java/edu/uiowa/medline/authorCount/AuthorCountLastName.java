package edu.uiowa.medline.authorCount;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorCountLastName extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorCountLastName.class);


	public int doStartTag() throws JspException {
		try {
			AuthorCount theAuthorCount = (AuthorCount)findAncestorWithClass(this, AuthorCount.class);
			if (!theAuthorCount.commitNeeded) {
				pageContext.getOut().print(theAuthorCount.getLastName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorCount for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorCount for lastName tag ");
		}
		return SKIP_BODY;
	}

	public String getLastName() throws JspTagException {
		try {
			AuthorCount theAuthorCount = (AuthorCount)findAncestorWithClass(this, AuthorCount.class);
			return theAuthorCount.getLastName();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorCount for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorCount for lastName tag ");
		}
	}

	public void setLastName(String lastName) throws JspTagException {
		try {
			AuthorCount theAuthorCount = (AuthorCount)findAncestorWithClass(this, AuthorCount.class);
			theAuthorCount.setLastName(lastName);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorCount for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorCount for lastName tag ");
		}
	}

}
