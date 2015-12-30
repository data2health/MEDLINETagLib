package edu.uiowa.medline.authorAffiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorAffiliationSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorAffiliationSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			if (!theAuthorAffiliation.commitNeeded) {
				pageContext.getOut().print(theAuthorAffiliation.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliation for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			return theAuthorAffiliation.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorAffiliation for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			AuthorAffiliation theAuthorAffiliation = (AuthorAffiliation)findAncestorWithClass(this, AuthorAffiliation.class);
			theAuthorAffiliation.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorAffiliation for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorAffiliation for seqnum tag ");
		}
	}

}
