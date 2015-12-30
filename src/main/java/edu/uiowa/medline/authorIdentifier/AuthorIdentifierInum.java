package edu.uiowa.medline.authorIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorIdentifierInum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorIdentifierInum.class);


	public int doStartTag() throws JspException {
		try {
			AuthorIdentifier theAuthorIdentifier = (AuthorIdentifier)findAncestorWithClass(this, AuthorIdentifier.class);
			if (!theAuthorIdentifier.commitNeeded) {
				pageContext.getOut().print(theAuthorIdentifier.getInum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorIdentifier for inum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorIdentifier for inum tag ");
		}
		return SKIP_BODY;
	}

	public int getInum() throws JspTagException {
		try {
			AuthorIdentifier theAuthorIdentifier = (AuthorIdentifier)findAncestorWithClass(this, AuthorIdentifier.class);
			return theAuthorIdentifier.getInum();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorIdentifier for inum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorIdentifier for inum tag ");
		}
	}

	public void setInum(int inum) throws JspTagException {
		try {
			AuthorIdentifier theAuthorIdentifier = (AuthorIdentifier)findAncestorWithClass(this, AuthorIdentifier.class);
			theAuthorIdentifier.setInum(inum);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorIdentifier for inum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorIdentifier for inum tag ");
		}
	}

}
