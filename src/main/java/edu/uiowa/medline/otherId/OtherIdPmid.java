package edu.uiowa.medline.otherId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class OtherIdPmid extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			OtherId theOtherId = (OtherId)findAncestorWithClass(this, OtherId.class);
			if (!theOtherId.commitNeeded) {
				pageContext.getOut().print(theOtherId.getPmid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherId for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			OtherId theOtherId = (OtherId)findAncestorWithClass(this, OtherId.class);
			return theOtherId.getPmid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherId for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			OtherId theOtherId = (OtherId)findAncestorWithClass(this, OtherId.class);
			theOtherId.setPmid(pmid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherId for pmid tag ");
		}
	}

}
