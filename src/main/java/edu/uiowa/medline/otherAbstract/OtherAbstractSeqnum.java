package edu.uiowa.medline.otherAbstract;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class OtherAbstractSeqnum extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			OtherAbstract theOtherAbstract = (OtherAbstract)findAncestorWithClass(this, OtherAbstract.class);
			if (!theOtherAbstract.commitNeeded) {
				pageContext.getOut().print(theOtherAbstract.getSeqnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstract for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			OtherAbstract theOtherAbstract = (OtherAbstract)findAncestorWithClass(this, OtherAbstract.class);
			return theOtherAbstract.getSeqnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstract for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			OtherAbstract theOtherAbstract = (OtherAbstract)findAncestorWithClass(this, OtherAbstract.class);
			theOtherAbstract.setSeqnum(seqnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstract for seqnum tag ");
		}
	}

}
