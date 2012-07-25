package edu.uiowa.medline.keyword;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class KeywordKeyword extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(KeywordKeyword.class);


	public int doStartTag() throws JspException {
		try {
			Keyword theKeyword = (Keyword)findAncestorWithClass(this, Keyword.class);
			if (!theKeyword.commitNeeded) {
				pageContext.getOut().print(theKeyword.getKeyword());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Keyword for keyword tag ", e);
			throw new JspTagException("Error: Can't find enclosing Keyword for keyword tag ");
		}
		return SKIP_BODY;
	}

	public String getKeyword() throws JspTagException {
		try {
			Keyword theKeyword = (Keyword)findAncestorWithClass(this, Keyword.class);
			return theKeyword.getKeyword();
		} catch (Exception e) {
			log.error(" Can't find enclosing Keyword for keyword tag ", e);
			throw new JspTagException("Error: Can't find enclosing Keyword for keyword tag ");
		}
	}

	public void setKeyword(String keyword) throws JspTagException {
		try {
			Keyword theKeyword = (Keyword)findAncestorWithClass(this, Keyword.class);
			theKeyword.setKeyword(keyword);
		} catch (Exception e) {
			log.error("Can't find enclosing Keyword for keyword tag ", e);
			throw new JspTagException("Error: Can't find enclosing Keyword for keyword tag ");
		}
	}

}
