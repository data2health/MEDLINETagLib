package edu.uiowa.medline.otherAbstractText;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class OtherAbstractTextPmid extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			if (!theOtherAbstractText.commitNeeded) {
				pageContext.getOut().print(theOtherAbstractText.getPmid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			return theOtherAbstractText.getPmid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			theOtherAbstractText.setPmid(pmid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for pmid tag ");
		}
	}

}
