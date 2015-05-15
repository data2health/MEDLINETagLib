package edu.uiowa.medline.investigatorAffiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorAffiliationPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorAffiliationPmid.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			if (!theInvestigatorAffiliation.commitNeeded) {
				pageContext.getOut().print(theInvestigatorAffiliation.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliation for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			return theInvestigatorAffiliation.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorAffiliation for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			theInvestigatorAffiliation.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliation for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for pmid tag ");
		}
	}

}
