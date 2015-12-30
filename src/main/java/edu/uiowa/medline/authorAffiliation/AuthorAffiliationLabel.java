package edu.uiowa.medline.authorAffiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorAffiliationLabel extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorAffiliationLabel.class);


	public int doStartTag() throws JspException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			if (!theAuthorAffiliation.commitNeeded) {
				pageContext.getOut().print(theAuthorAffiliation.getLabel());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliation for label tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for label tag ");
		}
		return SKIP_BODY;
	}

	public String getLabel() throws JspTagException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			return theAuthorAffiliation.getLabel();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorAffiliation for label tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for label tag ");
		}
	}

	public void setLabel(String label) throws JspTagException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			theAuthorAffiliation.setLabel(label);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliation for label tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for label tag ");
		}
	}

}
