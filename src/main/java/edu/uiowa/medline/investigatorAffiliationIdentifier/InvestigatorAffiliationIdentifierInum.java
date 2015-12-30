package edu.uiowa.medline.investigatorAffiliationIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorAffiliationIdentifierInum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorAffiliationIdentifierInum.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorAffiliationIdentifier theInvestigatorAffiliationIdentifier = (InvestigatorAffiliationIdentifier)findAncestorWithClass(this, InvestigatorAffiliationIdentifier.class);
			if (!theInvestigatorAffiliationIdentifier.commitNeeded) {
				pageContext.getOut().print(theInvestigatorAffiliationIdentifier.getInum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliationIdentifier for inum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliationIdentifier for inum tag ");
		}
		return SKIP_BODY;
	}

	public int getInum() throws JspTagException {
		try {
			InvestigatorAffiliationIdentifier theInvestigatorAffiliationIdentifier = (InvestigatorAffiliationIdentifier)findAncestorWithClass(this, InvestigatorAffiliationIdentifier.class);
			return theInvestigatorAffiliationIdentifier.getInum();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorAffiliationIdentifier for inum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliationIdentifier for inum tag ");
		}
	}

	public void setInum(int inum) throws JspTagException {
		try {
			InvestigatorAffiliationIdentifier theInvestigatorAffiliationIdentifier = (InvestigatorAffiliationIdentifier)findAncestorWithClass(this, InvestigatorAffiliationIdentifier.class);
			theInvestigatorAffiliationIdentifier.setInum(inum);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliationIdentifier for inum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliationIdentifier for inum tag ");
		}
	}

}
