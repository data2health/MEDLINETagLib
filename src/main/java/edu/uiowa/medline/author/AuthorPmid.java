package edu.uiowa.medline.author;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorPmid.class);


	public int doStartTag() throws JspException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			if (!theAuthor.commitNeeded) {
				pageContext.getOut().print(theAuthor.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Author for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			return theAuthor.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing Author for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			theAuthor.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing Author for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for pmid tag ");
		}
	}

}
