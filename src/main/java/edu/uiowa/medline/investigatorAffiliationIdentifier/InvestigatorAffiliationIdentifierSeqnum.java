package edu.uiowa.medline.investigatorAffiliationIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorAffiliationIdentifierSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorAffiliationIdentifierSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorAffiliationIdentifier theInvestigatorAffiliationIdentifier = (InvestigatorAffiliationIdentifier)findAncestorWithClass(this, InvestigatorAffiliationIdentifier.class);
			if (!theInvestigatorAffiliationIdentifier.commitNeeded) {
				pageContext.getOut().print(theInvestigatorAffiliationIdentifier.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliationIdentifier for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliationIdentifier for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			InvestigatorAffiliationIdentifier theInvestigatorAffiliationIdentifier = (InvestigatorAffiliationIdentifier)findAncestorWithClass(this, InvestigatorAffiliationIdentifier.class);
			return theInvestigatorAffiliationIdentifier.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorAffiliationIdentifier for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliationIdentifier for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			InvestigatorAffiliationIdentifier theInvestigatorAffiliationIdentifier = (InvestigatorAffiliationIdentifier)findAncestorWithClass(this, InvestigatorAffiliationIdentifier.class);
			theInvestigatorAffiliationIdentifier.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliationIdentifier for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliationIdentifier for seqnum tag ");
		}
	}

}
