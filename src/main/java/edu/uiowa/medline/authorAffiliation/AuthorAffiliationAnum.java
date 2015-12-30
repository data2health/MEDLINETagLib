package edu.uiowa.medline.authorAffiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorAffiliationAnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorAffiliationAnum.class);


	public int doStartTag() throws JspException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			if (!theAuthorAffiliation.commitNeeded) {
				pageContext.getOut().print(theAuthorAffiliation.getAnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliation for anum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for anum tag ");
		}
		return SKIP_BODY;
	}

	public int getAnum() throws JspTagException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			return theAuthorAffiliation.getAnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorAffiliation for anum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for anum tag ");
		}
	}

	public void setAnum(int anum) throws JspTagException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			theAuthorAffiliation.setAnum(anum);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliation for anum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for anum tag ");
		}
	}

}
