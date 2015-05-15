package edu.uiowa.medline.investigatorAffiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorAffiliationAnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorAffiliationAnum.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			if (!theInvestigatorAffiliation.commitNeeded) {
				pageContext.getOut().print(theInvestigatorAffiliation.getAnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliation for anum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for anum tag ");
		}
		return SKIP_BODY;
	}

	public int getAnum() throws JspTagException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			return theInvestigatorAffiliation.getAnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorAffiliation for anum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for anum tag ");
		}
	}

	public void setAnum(int anum) throws JspTagException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			theInvestigatorAffiliation.setAnum(anum);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliation for anum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for anum tag ");
		}
	}

}
