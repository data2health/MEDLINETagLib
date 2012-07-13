package edu.uiowa.medline.investigator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorForeName extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorForeName.class);


	public int doStartTag() throws JspException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			if (!theInvestigator.commitNeeded) {
				pageContext.getOut().print(theInvestigator.getForeName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Investigator for foreName tag ", e);
			throw new JspTagException("Error: Can't find enclosing Investigator for foreName tag ");
		}
		return SKIP_BODY;
	}

	public String getForeName() throws JspTagException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			return theInvestigator.getForeName();
		} catch (Exception e) {
			log.error(" Can't find enclosing Investigator for foreName tag ", e);
			throw new JspTagException("Error: Can't find enclosing Investigator for foreName tag ");
		}
	}

	public void setForeName(String foreName) throws JspTagException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			theInvestigator.setForeName(foreName);
		} catch (Exception e) {
			log.error("Can't find enclosing Investigator for foreName tag ", e);
			throw new JspTagException("Error: Can't find enclosing Investigator for foreName tag ");
		}
	}

}
