package edu.uiowa.medline.investigatorAffiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorAffiliationSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorAffiliationSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			if (!theInvestigatorAffiliation.commitNeeded) {
				pageContext.getOut().print(theInvestigatorAffiliation.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliation for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			return theInvestigatorAffiliation.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorAffiliation for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			theInvestigatorAffiliation.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliation for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for seqnum tag ");
		}
	}

}
