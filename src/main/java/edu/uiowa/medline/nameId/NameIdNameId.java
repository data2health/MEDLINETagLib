package edu.uiowa.medline.nameId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class NameIdNameId extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			if (!theNameId.commitNeeded) {
				pageContext.getOut().print(theNameId.getNameId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing NameId for nameId tag ");
		}
		return SKIP_BODY;
	}

	public String getNameId() throws JspTagException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			return theNameId.getNameId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing NameId for nameId tag ");
		}
	}

	public void setNameId(String nameId) throws JspTagException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			theNameId.setNameId(nameId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing NameId for nameId tag ");
		}
	}

}
