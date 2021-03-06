package edu.uiowa.medline.keyword;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class KeywordOwner extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(KeywordOwner.class);


	public int doStartTag() throws JspException {
		try {
			Keyword theKeyword = (Keyword)findAncestorWithClass(this, Keyword.class);
			if (!theKeyword.commitNeeded) {
				pageContext.getOut().print(theKeyword.getOwner());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Keyword for owner tag ", e);
			throw new JspTagException("Error: Can't find enclosing Keyword for owner tag ");
		}
		return SKIP_BODY;
	}

	public String getOwner() throws JspTagException {
		try {
			Keyword theKeyword = (Keyword)findAncestorWithClass(this, Keyword.class);
			return theKeyword.getOwner();
		} catch (Exception e) {
			log.error(" Can't find enclosing Keyword for owner tag ", e);
			throw new JspTagException("Error: Can't find enclosing Keyword for owner tag ");
		}
	}

	public void setOwner(String owner) throws JspTagException {
		try {
			Keyword theKeyword = (Keyword)findAncestorWithClass(this, Keyword.class);
			theKeyword.setOwner(owner);
		} catch (Exception e) {
			log.error("Can't find enclosing Keyword for owner tag ", e);
			throw new JspTagException("Error: Can't find enclosing Keyword for owner tag ");
		}
	}

}
