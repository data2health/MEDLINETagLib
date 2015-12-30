package edu.uiowa.medline.authorAffiliationIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorAffiliationIdentifierSource extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorAffiliationIdentifierSource.class);


	public int doStartTag() throws JspException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			if (!theAuthorAffiliationIdentifier.commitNeeded) {
				pageContext.getOut().print(theAuthorAffiliationIdentifier.getSource());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliationIdentifier for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for source tag ");
		}
		return SKIP_BODY;
	}

	public String getSource() throws JspTagException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			return theAuthorAffiliationIdentifier.getSource();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorAffiliationIdentifier for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for source tag ");
		}
	}

	public void setSource(String source) throws JspTagException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			theAuthorAffiliationIdentifier.setSource(source);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliationIdentifier for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for source tag ");
		}
	}

}
