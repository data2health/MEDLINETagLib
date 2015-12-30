package edu.uiowa.medline.authorAffiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorAffiliationPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorAffiliationPmid.class);


	public int doStartTag() throws JspException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			if (!theAuthorAffiliation.commitNeeded) {
				pageContext.getOut().print(theAuthorAffiliation.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliation for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			return theAuthorAffiliation.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorAffiliation for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			theAuthorAffiliation.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliation for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for pmid tag ");
		}
	}

}
