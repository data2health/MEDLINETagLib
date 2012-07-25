package edu.uiowa.medline.investigator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorInitials extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorInitials.class);


	public int doStartTag() throws JspException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			if (!theInvestigator.commitNeeded) {
				pageContext.getOut().print(theInvestigator.getInitials());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Investigator for initials tag ", e);
			throw new JspTagException("Error: Can't find enclosing Investigator for initials tag ");
		}
		return SKIP_BODY;
	}

	public String getInitials() throws JspTagException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			return theInvestigator.getInitials();
		} catch (Exception e) {
			log.error(" Can't find enclosing Investigator for initials tag ", e);
			throw new JspTagException("Error: Can't find enclosing Investigator for initials tag ");
		}
	}

	public void setInitials(String initials) throws JspTagException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			theInvestigator.setInitials(initials);
		} catch (Exception e) {
			log.error("Can't find enclosing Investigator for initials tag ", e);
			throw new JspTagException("Error: Can't find enclosing Investigator for initials tag ");
		}
	}

}
