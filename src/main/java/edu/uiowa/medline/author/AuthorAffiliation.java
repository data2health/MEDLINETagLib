package edu.uiowa.medline.author;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorAffiliation extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorAffiliation.class);


	public int doStartTag() throws JspException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			if (!theAuthor.commitNeeded) {
				pageContext.getOut().print(theAuthor.getAffiliation());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Author for affiliation tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for affiliation tag ");
		}
		return SKIP_BODY;
	}

	public String getAffiliation() throws JspTagException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			return theAuthor.getAffiliation();
		} catch (Exception e) {
			log.error(" Can't find enclosing Author for affiliation tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for affiliation tag ");
		}
	}

	public void setAffiliation(String affiliation) throws JspTagException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			theAuthor.setAffiliation(affiliation);
		} catch (Exception e) {
			log.error("Can't find enclosing Author for affiliation tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for affiliation tag ");
		}
	}

}
