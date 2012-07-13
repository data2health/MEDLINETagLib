package edu.uiowa.medline.keyword;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class KeywordMajor extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(KeywordMajor.class);


	public int doStartTag() throws JspException {
		try {
			Keyword theKeyword = (Keyword)findAncestorWithClass(this, Keyword.class);
			if (!theKeyword.commitNeeded) {
				pageContext.getOut().print(theKeyword.getMajor());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Keyword for major tag ", e);
			throw new JspTagException("Error: Can't find enclosing Keyword for major tag ");
		}
		return SKIP_BODY;
	}

	public boolean getMajor() throws JspTagException {
		try {
			Keyword theKeyword = (Keyword)findAncestorWithClass(this, Keyword.class);
			return theKeyword.getMajor();
		} catch (Exception e) {
			log.error(" Can't find enclosing Keyword for major tag ", e);
			throw new JspTagException("Error: Can't find enclosing Keyword for major tag ");
		}
	}

	public void setMajor(boolean major) throws JspTagException {
		try {
			Keyword theKeyword = (Keyword)findAncestorWithClass(this, Keyword.class);
			theKeyword.setMajor(major);
		} catch (Exception e) {
			log.error("Can't find enclosing Keyword for major tag ", e);
			throw new JspTagException("Error: Can't find enclosing Keyword for major tag ");
		}
	}

}
