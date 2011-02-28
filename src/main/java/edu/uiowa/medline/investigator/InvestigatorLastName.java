package edu.uiowa.medline.investigator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorLastName extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			if (!theInvestigator.commitNeeded) {
				pageContext.getOut().print(theInvestigator.getLastName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Investigator for lastName tag ");
		}
		return SKIP_BODY;
	}

	public String getLastName() throws JspTagException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			return theInvestigator.getLastName();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Investigator for lastName tag ");
		}
	}

	public void setLastName(String lastName) throws JspTagException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			theInvestigator.setLastName(lastName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Investigator for lastName tag ");
		}
	}

}
