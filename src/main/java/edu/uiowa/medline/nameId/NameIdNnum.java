package edu.uiowa.medline.nameId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class NameIdNnum extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			if (!theNameId.commitNeeded) {
				pageContext.getOut().print(theNameId.getNnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing NameId for nnum tag ");
		}
		return SKIP_BODY;
	}

	public int getNnum() throws JspTagException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			return theNameId.getNnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing NameId for nnum tag ");
		}
	}

	public void setNnum(int nnum) throws JspTagException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			theNameId.setNnum(nnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing NameId for nnum tag ");
		}
	}

}
