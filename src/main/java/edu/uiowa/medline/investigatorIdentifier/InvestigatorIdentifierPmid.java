package edu.uiowa.medline.investigatorIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorIdentifierPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorIdentifierPmid.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorIdentifier theInvestigatorIdentifier = (InvestigatorIdentifier)findAncestorWithClass(this, InvestigatorIdentifier.class);
			if (!theInvestigatorIdentifier.commitNeeded) {
				pageContext.getOut().print(theInvestigatorIdentifier.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorIdentifier for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorIdentifier for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			InvestigatorIdentifier theInvestigatorIdentifier = (InvestigatorIdentifier)findAncestorWithClass(this, InvestigatorIdentifier.class);
			return theInvestigatorIdentifier.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorIdentifier for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorIdentifier for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			InvestigatorIdentifier theInvestigatorIdentifier = (InvestigatorIdentifier)findAncestorWithClass(this, InvestigatorIdentifier.class);
			theInvestigatorIdentifier.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorIdentifier for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorIdentifier for pmid tag ");
		}
	}

}
