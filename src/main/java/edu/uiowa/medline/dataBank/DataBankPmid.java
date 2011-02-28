package edu.uiowa.medline.dataBank;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class DataBankPmid extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			DataBank theDataBank = (DataBank)findAncestorWithClass(this, DataBank.class);
			if (!theDataBank.commitNeeded) {
				pageContext.getOut().print(theDataBank.getPmid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing DataBank for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			DataBank theDataBank = (DataBank)findAncestorWithClass(this, DataBank.class);
			return theDataBank.getPmid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing DataBank for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			DataBank theDataBank = (DataBank)findAncestorWithClass(this, DataBank.class);
			theDataBank.setPmid(pmid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing DataBank for pmid tag ");
		}
	}

}
