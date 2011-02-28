package edu.uiowa.medline.investigatorNameId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorNameIdPmid extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			if (!theInvestigatorNameId.commitNeeded) {
				pageContext.getOut().print(theInvestigatorNameId.getPmid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			return theInvestigatorNameId.getPmid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			theInvestigatorNameId.setPmid(pmid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for pmid tag ");
		}
	}

}
