package edu.uiowa.medline.authorIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorIdentifierSource extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorIdentifierSource.class);


	public int doStartTag() throws JspException {
		try {
			AuthorIdentifier theAuthorIdentifier = (AuthorIdentifier)findAncestorWithClass(this, AuthorIdentifier.class);
			if (!theAuthorIdentifier.commitNeeded) {
				pageContext.getOut().print(theAuthorIdentifier.getSource());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorIdentifier for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorIdentifier for source tag ");
		}
		return SKIP_BODY;
	}

	public String getSource() throws JspTagException {
		try {
			AuthorIdentifier theAuthorIdentifier = (AuthorIdentifier)findAncestorWithClass(this, AuthorIdentifier.class);
			return theAuthorIdentifier.getSource();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorIdentifier for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorIdentifier for source tag ");
		}
	}

	public void setSource(String source) throws JspTagException {
		try {
			AuthorIdentifier theAuthorIdentifier = (AuthorIdentifier)findAncestorWithClass(this, AuthorIdentifier.class);
			theAuthorIdentifier.setSource(source);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorIdentifier for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorIdentifier for source tag ");
		}
	}

}
