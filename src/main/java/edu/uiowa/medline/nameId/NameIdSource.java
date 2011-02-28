package edu.uiowa.medline.nameId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class NameIdSource extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			if (!theNameId.commitNeeded) {
				pageContext.getOut().print(theNameId.getSource());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing NameId for source tag ");
		}
		return SKIP_BODY;
	}

	public String getSource() throws JspTagException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			return theNameId.getSource();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing NameId for source tag ");
		}
	}

	public void setSource(String source) throws JspTagException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			theNameId.setSource(source);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing NameId for source tag ");
		}
	}

}
