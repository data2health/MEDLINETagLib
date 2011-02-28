package edu.uiowa.medline.otherAbstractText;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class OtherAbstractTextCategory extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			if (!theOtherAbstractText.commitNeeded) {
				pageContext.getOut().print(theOtherAbstractText.getCategory());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for category tag ");
		}
		return SKIP_BODY;
	}

	public String getCategory() throws JspTagException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			return theOtherAbstractText.getCategory();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for category tag ");
		}
	}

	public void setCategory(String category) throws JspTagException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			theOtherAbstractText.setCategory(category);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for category tag ");
		}
	}

}
