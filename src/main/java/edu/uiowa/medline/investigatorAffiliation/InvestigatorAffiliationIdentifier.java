package edu.uiowa.medline.investigatorAffiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorAffiliationIdentifier extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorAffiliationIdentifier.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			if (!theInvestigatorAffiliation.commitNeeded) {
				pageContext.getOut().print(theInvestigatorAffiliation.getIdentifier());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliation for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for identifier tag ");
		}
		return SKIP_BODY;
	}

	public String getIdentifier() throws JspTagException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			return theInvestigatorAffiliation.getIdentifier();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorAffiliation for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for identifier tag ");
		}
	}

	public void setIdentifier(String identifier) throws JspTagException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			theInvestigatorAffiliation.setIdentifier(identifier);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliation for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for identifier tag ");
		}
	}

}
