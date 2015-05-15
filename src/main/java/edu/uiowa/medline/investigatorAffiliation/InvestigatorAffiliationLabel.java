package edu.uiowa.medline.investigatorAffiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorAffiliationLabel extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorAffiliationLabel.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			if (!theInvestigatorAffiliation.commitNeeded) {
				pageContext.getOut().print(theInvestigatorAffiliation.getLabel());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliation for label tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for label tag ");
		}
		return SKIP_BODY;
	}

	public String getLabel() throws JspTagException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			return theInvestigatorAffiliation.getLabel();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorAffiliation for label tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for label tag ");
		}
	}

	public void setLabel(String label) throws JspTagException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			theInvestigatorAffiliation.setLabel(label);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliation for label tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for label tag ");
		}
	}

}
