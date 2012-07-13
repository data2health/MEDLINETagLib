package edu.uiowa.medline.investigator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorSuffix extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorSuffix.class);


	public int doStartTag() throws JspException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			if (!theInvestigator.commitNeeded) {
				pageContext.getOut().print(theInvestigator.getSuffix());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Investigator for suffix tag ", e);
			throw new JspTagException("Error: Can't find enclosing Investigator for suffix tag ");
		}
		return SKIP_BODY;
	}

	public String getSuffix() throws JspTagException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			return theInvestigator.getSuffix();
		} catch (Exception e) {
			log.error(" Can't find enclosing Investigator for suffix tag ", e);
			throw new JspTagException("Error: Can't find enclosing Investigator for suffix tag ");
		}
	}

	public void setSuffix(String suffix) throws JspTagException {
		try {
			Investigator theInvestigator = (Investigator)findAncestorWithClass(this, Investigator.class);
			theInvestigator.setSuffix(suffix);
		} catch (Exception e) {
			log.error("Can't find enclosing Investigator for suffix tag ", e);
			throw new JspTagException("Error: Can't find enclosing Investigator for suffix tag ");
		}
	}

}
