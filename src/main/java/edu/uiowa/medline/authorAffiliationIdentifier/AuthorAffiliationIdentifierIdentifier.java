package edu.uiowa.medline.authorAffiliationIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorAffiliationIdentifierIdentifier extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorAffiliationIdentifierIdentifier.class);


	public int doStartTag() throws JspException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			if (!theAuthorAffiliationIdentifier.commitNeeded) {
				pageContext.getOut().print(theAuthorAffiliationIdentifier.getIdentifier());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliationIdentifier for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for identifier tag ");
		}
		return SKIP_BODY;
	}

	public String getIdentifier() throws JspTagException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			return theAuthorAffiliationIdentifier.getIdentifier();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorAffiliationIdentifier for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for identifier tag ");
		}
	}

	public void setIdentifier(String identifier) throws JspTagException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			theAuthorAffiliationIdentifier.setIdentifier(identifier);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliationIdentifier for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for identifier tag ");
		}
	}

}
