package edu.uiowa.medline.investigatorNameId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorNameIdNameId extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			if (!theInvestigatorNameId.commitNeeded) {
				pageContext.getOut().print(theInvestigatorNameId.getNameId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for nameId tag ");
		}
		return SKIP_BODY;
	}

	public String getNameId() throws JspTagException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			return theInvestigatorNameId.getNameId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for nameId tag ");
		}
	}

	public void setNameId(String nameId) throws JspTagException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			theInvestigatorNameId.setNameId(nameId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for nameId tag ");
		}
	}

}
