package edu.uiowa.medline.abstr;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AbstrLabel extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			if (!theAbstr.commitNeeded) {
				pageContext.getOut().print(theAbstr.getLabel());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Abstr for label tag ");
		}
		return SKIP_BODY;
	}

	public String getLabel() throws JspTagException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			return theAbstr.getLabel();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Abstr for label tag ");
		}
	}

	public void setLabel(String label) throws JspTagException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			theAbstr.setLabel(label);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Abstr for label tag ");
		}
	}

}
