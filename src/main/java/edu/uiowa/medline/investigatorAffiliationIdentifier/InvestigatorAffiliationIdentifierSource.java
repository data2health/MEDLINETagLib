package edu.uiowa.medline.investigatorAffiliationIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorAffiliationIdentifierSource extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorAffiliationIdentifierSource.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorAffiliationIdentifier theInvestigatorAffiliationIdentifier = (InvestigatorAffiliationIdentifier)findAncestorWithClass(this, InvestigatorAffiliationIdentifier.class);
			if (!theInvestigatorAffiliationIdentifier.commitNeeded) {
				pageContext.getOut().print(theInvestigatorAffiliationIdentifier.getSource());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliationIdentifier for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliationIdentifier for source tag ");
		}
		return SKIP_BODY;
	}

	public String getSource() throws JspTagException {
		try {
			InvestigatorAffiliationIdentifier theInvestigatorAffiliationIdentifier = (InvestigatorAffiliationIdentifier)findAncestorWithClass(this, InvestigatorAffiliationIdentifier.class);
			return theInvestigatorAffiliationIdentifier.getSource();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorAffiliationIdentifier for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliationIdentifier for source tag ");
		}
	}

	public void setSource(String source) throws JspTagException {
		try {
			InvestigatorAffiliationIdentifier theInvestigatorAffiliationIdentifier = (InvestigatorAffiliationIdentifier)findAncestorWithClass(this, InvestigatorAffiliationIdentifier.class);
			theInvestigatorAffiliationIdentifier.setSource(source);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliationIdentifier for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliationIdentifier for source tag ");
		}
	}

}
