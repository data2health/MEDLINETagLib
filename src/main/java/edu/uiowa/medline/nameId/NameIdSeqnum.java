package edu.uiowa.medline.nameId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class NameIdSeqnum extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			if (!theNameId.commitNeeded) {
				pageContext.getOut().print(theNameId.getSeqnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing NameId for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			return theNameId.getSeqnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing NameId for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			theNameId.setSeqnum(seqnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing NameId for seqnum tag ");
		}
	}

}
