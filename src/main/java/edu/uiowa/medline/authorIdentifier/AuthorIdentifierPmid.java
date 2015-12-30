package edu.uiowa.medline.authorIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorIdentifierPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorIdentifierPmid.class);


	public int doStartTag() throws JspException {
		try {
			AuthorIdentifier theAuthorIdentifier = (AuthorIdentifier)findAncestorWithClass(this, AuthorIdentifier.class);
			if (!theAuthorIdentifier.commitNeeded) {
				pageContext.getOut().print(theAuthorIdentifier.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorIdentifier for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorIdentifier for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			AuthorIdentifier theAuthorIdentifier = (AuthorIdentifier)findAncestorWithClass(this, AuthorIdentifier.class);
			return theAuthorIdentifier.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorIdentifier for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorIdentifier for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			AuthorIdentifier theAuthorIdentifier = (AuthorIdentifier)findAncestorWithClass(this, AuthorIdentifier.class);
			theAuthorIdentifier.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorIdentifier for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorIdentifier for pmid tag ");
		}
	}

}
