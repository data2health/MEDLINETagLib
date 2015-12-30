package edu.uiowa.medline.investigatorAffiliationIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorAffiliationIdentifierAnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorAffiliationIdentifierAnum.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorAffiliationIdentifier theInvestigatorAffiliationIdentifier = (InvestigatorAffiliationIdentifier)findAncestorWithClass(this, InvestigatorAffiliationIdentifier.class);
			if (!theInvestigatorAffiliationIdentifier.commitNeeded) {
				pageContext.getOut().print(theInvestigatorAffiliationIdentifier.getAnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliationIdentifier for anum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliationIdentifier for anum tag ");
		}
		return SKIP_BODY;
	}

	public int getAnum() throws JspTagException {
		try {
			InvestigatorAffiliationIdentifier theInvestigatorAffiliationIdentifier = (InvestigatorAffiliationIdentifier)findAncestorWithClass(this, InvestigatorAffiliationIdentifier.class);
			return theInvestigatorAffiliationIdentifier.getAnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorAffiliationIdentifier for anum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliationIdentifier for anum tag ");
		}
	}

	public void setAnum(int anum) throws JspTagException {
		try {
			InvestigatorAffiliationIdentifier theInvestigatorAffiliationIdentifier = (InvestigatorAffiliationIdentifier)findAncestorWithClass(this, InvestigatorAffiliationIdentifier.class);
			theInvestigatorAffiliationIdentifier.setAnum(anum);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliationIdentifier for anum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliationIdentifier for anum tag ");
		}
	}

}
