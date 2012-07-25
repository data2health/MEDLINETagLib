package edu.uiowa.medline.author;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorForeName extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorForeName.class);


	public int doStartTag() throws JspException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			if (!theAuthor.commitNeeded) {
				pageContext.getOut().print(theAuthor.getForeName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Author for foreName tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for foreName tag ");
		}
		return SKIP_BODY;
	}

	public String getForeName() throws JspTagException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			return theAuthor.getForeName();
		} catch (Exception e) {
			log.error(" Can't find enclosing Author for foreName tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for foreName tag ");
		}
	}

	public void setForeName(String foreName) throws JspTagException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			theAuthor.setForeName(foreName);
		} catch (Exception e) {
			log.error("Can't find enclosing Author for foreName tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for foreName tag ");
		}
	}

}
