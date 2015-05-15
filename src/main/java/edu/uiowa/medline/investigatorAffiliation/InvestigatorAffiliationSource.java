package edu.uiowa.medline.investigatorAffiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorAffiliationSource extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorAffiliationSource.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			if (!theInvestigatorAffiliation.commitNeeded) {
				pageContext.getOut().print(theInvestigatorAffiliation.getSource());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliation for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for source tag ");
		}
		return SKIP_BODY;
	}

	public String getSource() throws JspTagException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			return theInvestigatorAffiliation.getSource();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorAffiliation for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for source tag ");
		}
	}

	public void setSource(String source) throws JspTagException {
		try {
			InvestigatorAffiliation theInvestigatorAffiliation = (InvestigatorAffiliation)findAncestorWithClass(this, InvestigatorAffiliation.class);
			theInvestigatorAffiliation.setSource(source);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorAffiliation for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorAffiliation for source tag ");
		}
	}

}
