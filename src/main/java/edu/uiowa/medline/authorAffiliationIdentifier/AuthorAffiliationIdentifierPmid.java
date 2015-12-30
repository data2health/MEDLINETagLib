package edu.uiowa.medline.authorAffiliationIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorAffiliationIdentifierPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorAffiliationIdentifierPmid.class);


	public int doStartTag() throws JspException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			if (!theAuthorAffiliationIdentifier.commitNeeded) {
				pageContext.getOut().print(theAuthorAffiliationIdentifier.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliationIdentifier for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			return theAuthorAffiliationIdentifier.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorAffiliationIdentifier for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			theAuthorAffiliationIdentifier.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliationIdentifier for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for pmid tag ");
		}
	}

}
