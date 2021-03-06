package edu.uiowa.medline.authorCount;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorCountCount extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorCountCount.class);


	public int doStartTag() throws JspException {
		try {
			AuthorCount theAuthorCount = (AuthorCount)findAncestorWithClass(this, AuthorCount.class);
			if (!theAuthorCount.commitNeeded) {
				pageContext.getOut().print(theAuthorCount.getCount());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorCount for count tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorCount for count tag ");
		}
		return SKIP_BODY;
	}

	public int getCount() throws JspTagException {
		try {
			AuthorCount theAuthorCount = (AuthorCount)findAncestorWithClass(this, AuthorCount.class);
			return theAuthorCount.getCount();
		} catch (Exception e) {
			log.error(" Can't find enclosing AuthorCount for count tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorCount for count tag ");
		}
	}

	public void setCount(int count) throws JspTagException {
		try {
			AuthorCount theAuthorCount = (AuthorCount)findAncestorWithClass(this, AuthorCount.class);
			theAuthorCount.setCount(count);
		} catch (Exception e) {
			log.error("Can't find enclosing AuthorCount for count tag ", e);
			throw new JspTagException("Error: Can't find enclosing AuthorCount for count tag ");
		}
	}

}
