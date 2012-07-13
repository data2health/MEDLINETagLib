package edu.uiowa.medline.author;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorInitials extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorInitials.class);


	public int doStartTag() throws JspException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			if (!theAuthor.commitNeeded) {
				pageContext.getOut().print(theAuthor.getInitials());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Author for initials tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for initials tag ");
		}
		return SKIP_BODY;
	}

	public String getInitials() throws JspTagException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			return theAuthor.getInitials();
		} catch (Exception e) {
			log.error(" Can't find enclosing Author for initials tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for initials tag ");
		}
	}

	public void setInitials(String initials) throws JspTagException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			theAuthor.setInitials(initials);
		} catch (Exception e) {
			log.error("Can't find enclosing Author for initials tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for initials tag ");
		}
	}

}
