package edu.uiowa.medline.investigator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorPmid.class);


	public int doStartTag() throws JspException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			if (!theInvestigator.commitNeeded) {
				pageContext.getOut().print(theInvestigator.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Investigator for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Investigator for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			return theInvestigator.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing Investigator for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Investigator for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			theInvestigator.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing Investigator for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Investigator for pmid tag ");
		}
	}

}
