package edu.uiowa.medline.abstr;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AbstrSeqnum extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			if (!theAbstr.commitNeeded) {
				pageContext.getOut().print(theAbstr.getSeqnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Abstr for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			return theAbstr.getSeqnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Abstr for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			theAbstr.setSeqnum(seqnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Abstr for seqnum tag ");
		}
	}

}
