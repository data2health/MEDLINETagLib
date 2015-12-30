package edu.uiowa.medline.authorAffiliationIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorAffiliationIdentifierAnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorAffiliationIdentifierAnum.class);


	public int doStartTag() throws JspException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			if (!theAuthorAffiliationIdentifier.commitNeeded) {
				pageContext.getOut().print(theAuthorAffiliationIdentifier.getAnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliationIdentifier for anum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for anum tag ");
		}
		return SKIP_BODY;
	}

	public int getAnum() throws JspTagException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			return theAuthorAffiliationIdentifier.getAnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorAffiliationIdentifier for anum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for anum tag ");
		}
	}

	public void setAnum(int anum) throws JspTagException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			theAuthorAffiliationIdentifier.setAnum(anum);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliationIdentifier for anum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for anum tag ");
		}
	}

}
