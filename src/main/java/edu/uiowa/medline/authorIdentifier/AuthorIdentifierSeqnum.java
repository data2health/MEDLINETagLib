package edu.uiowa.medline.authorIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorIdentifierSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorIdentifierSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			AuthorIdentifier theAuthorIdentifier = (AuthorIdentifier)findAncestorWithClass(this, AuthorIdentifier.class);
			if (!theAuthorIdentifier.commitNeeded) {
				pageContext.getOut().print(theAuthorIdentifier.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorIdentifier for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorIdentifier for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			AuthorIdentifier theAuthorIdentifier = (AuthorIdentifier)findAncestorWithClass(this, AuthorIdentifier.class);
			return theAuthorIdentifier.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorIdentifier for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorIdentifier for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			AuthorIdentifier theAuthorIdentifier = (AuthorIdentifier)findAncestorWithClass(this, AuthorIdentifier.class);
			theAuthorIdentifier.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorIdentifier for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorIdentifier for seqnum tag ");
		}
	}

}
