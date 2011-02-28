package edu.uiowa.medline.keyword;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class KeywordPmid extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Keyword theKeyword = (Keyword)findAncestorWithClass(this, Keyword.class);
			if (!theKeyword.commitNeeded) {
				pageContext.getOut().print(theKeyword.getPmid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Keyword for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			Keyword theKeyword = (Keyword)findAncestorWithClass(this, Keyword.class);
			return theKeyword.getPmid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Keyword for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			Keyword theKeyword = (Keyword)findAncestorWithClass(this, Keyword.class);
			theKeyword.setPmid(pmid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Keyword for pmid tag ");
		}
	}

}
