package edu.uiowa.medline.authorAffiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorAffiliationIdentifier extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorAffiliationIdentifier.class);


	public int doStartTag() throws JspException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			if (!theAuthorAffiliation.commitNeeded) {
				pageContext.getOut().print(theAuthorAffiliation.getIdentifier());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliation for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for identifier tag ");
		}
		return SKIP_BODY;
	}

	public String getIdentifier() throws JspTagException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			return theAuthorAffiliation.getIdentifier();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorAffiliation for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for identifier tag ");
		}
	}

	public void setIdentifier(String identifier) throws JspTagException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			theAuthorAffiliation.setIdentifier(identifier);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliation for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for identifier tag ");
		}
	}

}
