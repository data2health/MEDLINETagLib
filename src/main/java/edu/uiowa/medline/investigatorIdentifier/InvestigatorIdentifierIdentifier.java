package edu.uiowa.medline.investigatorIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorIdentifierIdentifier extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorIdentifierIdentifier.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorIdentifier theInvestigatorIdentifier = (InvestigatorIdentifier)findAncestorWithClass(this, InvestigatorIdentifier.class);
			if (!theInvestigatorIdentifier.commitNeeded) {
				pageContext.getOut().print(theInvestigatorIdentifier.getIdentifier());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorIdentifier for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorIdentifier for identifier tag ");
		}
		return SKIP_BODY;
	}

	public String getIdentifier() throws JspTagException {
		try {
			InvestigatorIdentifier theInvestigatorIdentifier = (InvestigatorIdentifier)findAncestorWithClass(this, InvestigatorIdentifier.class);
			return theInvestigatorIdentifier.getIdentifier();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorIdentifier for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorIdentifier for identifier tag ");
		}
	}

	public void setIdentifier(String identifier) throws JspTagException {
		try {
			InvestigatorIdentifier theInvestigatorIdentifier = (InvestigatorIdentifier)findAncestorWithClass(this, InvestigatorIdentifier.class);
			theInvestigatorIdentifier.setIdentifier(identifier);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorIdentifier for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorIdentifier for identifier tag ");
		}
	}

}
