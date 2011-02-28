package edu.uiowa.medline.dataBank;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class DataBankSeqnum extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			DataBank theDataBank = (DataBank)findAncestorWithClass(this, DataBank.class);
			if (!theDataBank.commitNeeded) {
				pageContext.getOut().print(theDataBank.getSeqnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing DataBank for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			DataBank theDataBank = (DataBank)findAncestorWithClass(this, DataBank.class);
			return theDataBank.getSeqnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing DataBank for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			DataBank theDataBank = (DataBank)findAncestorWithClass(this, DataBank.class);
			theDataBank.setSeqnum(seqnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing DataBank for seqnum tag ");
		}
	}

}
