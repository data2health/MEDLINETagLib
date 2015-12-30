package edu.uiowa.medline.investigatorAffiliationIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorAffiliationIdentifierPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorAffiliationIdentifierPmid.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorAffiliationIdentifier theInvestigatorAffiliationIdentifier = (InvestigatorAffiliationIdentifier)findAncestorWithClass(this, InvestigatorAffiliationIdentifier.class);
			if (!theInvestigatorAffiliationIdentifier.commitNeeded) {
				pageContext.getOut().print(theInvestigatorAffiliationIdentifier.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliationIdentifier for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliationIdentifier for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			InvestigatorAffiliationIdentifier theInvestigatorAffiliationIdentifier = (InvestigatorAffiliationIdentifier)findAncestorWithClass(this, InvestigatorAffiliationIdentifier.class);
			return theInvestigatorAffiliationIdentifier.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorAffiliationIdentifier for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliationIdentifier for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			InvestigatorAffiliationIdentifier theInvestigatorAffiliationIdentifier = (InvestigatorAffiliationIdentifier)findAncestorWithClass(this, InvestigatorAffiliationIdentifier.class);
			theInvestigatorAffiliationIdentifier.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliationIdentifier for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliationIdentifier for pmid tag ");
		}
	}

}
