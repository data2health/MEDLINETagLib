package edu.uiowa.medline.authorAffiliationIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorAffiliationIdentifierInum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorAffiliationIdentifierInum.class);


	public int doStartTag() throws JspException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			if (!theAuthorAffiliationIdentifier.commitNeeded) {
				pageContext.getOut().print(theAuthorAffiliationIdentifier.getInum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliationIdentifier for inum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for inum tag ");
		}
		return SKIP_BODY;
	}

	public int getInum() throws JspTagException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			return theAuthorAffiliationIdentifier.getInum();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorAffiliationIdentifier for inum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for inum tag ");
		}
	}

	public void setInum(int inum) throws JspTagException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			theAuthorAffiliationIdentifier.setInum(inum);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliationIdentifier for inum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for inum tag ");
		}
	}

}
