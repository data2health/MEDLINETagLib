package edu.uiowa.medline.investigator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorAffiliation extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorAffiliation.class);


	public int doStartTag() throws JspException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			if (!theInvestigator.commitNeeded) {
				pageContext.getOut().print(theInvestigator.getAffiliation());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Investigator for affiliation tag ", e);
			throw new JspTagException("Error: Can't find enclosing Investigator for affiliation tag ");
		}
		return SKIP_BODY;
	}

	public String getAffiliation() throws JspTagException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			return theInvestigator.getAffiliation();
		} catch (Exception e) {
			log.error(" Can't find enclosing Investigator for affiliation tag ", e);
			throw new JspTagException("Error: Can't find enclosing Investigator for affiliation tag ");
		}
	}

	public void setAffiliation(String affiliation) throws JspTagException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			theInvestigator.setAffiliation(affiliation);
		} catch (Exception e) {
			log.error("Can't find enclosing Investigator for affiliation tag ", e);
			throw new JspTagException("Error: Can't find enclosing Investigator for affiliation tag ");
		}
	}

}
