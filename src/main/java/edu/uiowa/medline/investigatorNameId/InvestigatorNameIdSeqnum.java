package edu.uiowa.medline.investigatorNameId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorNameIdSeqnum extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			if (!theInvestigatorNameId.commitNeeded) {
				pageContext.getOut().print(theInvestigatorNameId.getSeqnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			return theInvestigatorNameId.getSeqnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			theInvestigatorNameId.setSeqnum(seqnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for seqnum tag ");
		}
	}

}
