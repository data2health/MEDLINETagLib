package edu.uiowa.medline.author;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			if (!theAuthor.commitNeeded) {
				pageContext.getOut().print(theAuthor.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Author for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			return theAuthor.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing Author for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			theAuthor.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing Author for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for seqnum tag ");
		}
	}

}
