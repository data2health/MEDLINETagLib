package edu.uiowa.medline.authorAffiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorAffiliationSource extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorAffiliationSource.class);


	public int doStartTag() throws JspException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			if (!theAuthorAffiliation.commitNeeded) {
				pageContext.getOut().print(theAuthorAffiliation.getSource());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliation for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for source tag ");
		}
		return SKIP_BODY;
	}

	public String getSource() throws JspTagException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			return theAuthorAffiliation.getSource();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorAffiliation for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for source tag ");
		}
	}

	public void setSource(String source) throws JspTagException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			theAuthorAffiliation.setSource(source);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliation for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for source tag ");
		}
	}

}
