package edu.uiowa.medline.elocation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ElocationEid extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Elocation theElocation = (Elocation)findAncestorWithClass(this, Elocation.class);
			if (!theElocation.commitNeeded) {
				pageContext.getOut().print(theElocation.getEid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Elocation for eid tag ");
		}
		return SKIP_BODY;
	}

	public String getEid() throws JspTagException {
		try {
			Elocation theElocation = (Elocation)findAncestorWithClass(this, Elocation.class);
			return theElocation.getEid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Elocation for eid tag ");
		}
	}

	public void setEid(String eid) throws JspTagException {
		try {
			Elocation theElocation = (Elocation)findAncestorWithClass(this, Elocation.class);
			theElocation.setEid(eid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Elocation for eid tag ");
		}
	}

}
