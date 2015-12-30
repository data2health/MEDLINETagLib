package edu.uiowa.medline.authorAffiliationIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorAffiliationIdentifierSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorAffiliationIdentifierSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			if (!theAuthorAffiliationIdentifier.commitNeeded) {
				pageContext.getOut().print(theAuthorAffiliationIdentifier.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliationIdentifier for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			return theAuthorAffiliationIdentifier.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorAffiliationIdentifier for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			AuthorAffiliationIdentifier theAuthorAffiliationIdentifier = (AuthorAffiliationIdentifier)findAncestorWithClass(this, AuthorAffiliationIdentifier.class);
			theAuthorAffiliationIdentifier.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliationIdentifier for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliationIdentifier for seqnum tag ");
		}
	}

}
