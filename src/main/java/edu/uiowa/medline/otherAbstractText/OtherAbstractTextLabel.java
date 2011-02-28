package edu.uiowa.medline.otherAbstractText;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class OtherAbstractTextLabel extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			if (!theOtherAbstractText.commitNeeded) {
				pageContext.getOut().print(theOtherAbstractText.getLabel());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for label tag ");
		}
		return SKIP_BODY;
	}

	public String getLabel() throws JspTagException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			return theOtherAbstractText.getLabel();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for label tag ");
		}
	}

	public void setLabel(String label) throws JspTagException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			theOtherAbstractText.setLabel(label);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for label tag ");
		}
	}

}
