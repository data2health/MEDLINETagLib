package edu.uiowa.medline.authorIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorIdentifierIdentifier extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorIdentifierIdentifier.class);


	public int doStartTag() throws JspException {
		try {
			AuthorIdentifier theAuthorIdentifier = (AuthorIdentifier)findAncestorWithClass(this, AuthorIdentifier.class);
			if (!theAuthorIdentifier.commitNeeded) {
				pageContext.getOut().print(theAuthorIdentifier.getIdentifier());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorIdentifier for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorIdentifier for identifier tag ");
		}
		return SKIP_BODY;
	}

	public String getIdentifier() throws JspTagException {
		try {
			AuthorIdentifier theAuthorIdentifier = (AuthorIdentifier)findAncestorWithClass(this, AuthorIdentifier.class);
			return theAuthorIdentifier.getIdentifier();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorIdentifier for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorIdentifier for identifier tag ");
		}
	}

	public void setIdentifier(String identifier) throws JspTagException {
		try {
			AuthorIdentifier theAuthorIdentifier = (AuthorIdentifier)findAncestorWithClass(this, AuthorIdentifier.class);
			theAuthorIdentifier.setIdentifier(identifier);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorIdentifier for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorIdentifier for identifier tag ");
		}
	}

}
