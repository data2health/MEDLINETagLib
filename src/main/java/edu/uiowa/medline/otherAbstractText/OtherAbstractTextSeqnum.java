package edu.uiowa.medline.otherAbstractText;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class OtherAbstractTextSeqnum extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			if (!theOtherAbstractText.commitNeeded) {
				pageContext.getOut().print(theOtherAbstractText.getSeqnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			return theOtherAbstractText.getSeqnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			theOtherAbstractText.setSeqnum(seqnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for seqnum tag ");
		}
	}

}
