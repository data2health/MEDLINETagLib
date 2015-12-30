package edu.uiowa.medline.investigatorIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorIdentifierInum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorIdentifierInum.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorIdentifier theInvestigatorIdentifier = (InvestigatorIdentifier)findAncestorWithClass(this, InvestigatorIdentifier.class);
			if (!theInvestigatorIdentifier.commitNeeded) {
				pageContext.getOut().print(theInvestigatorIdentifier.getInum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorIdentifier for inum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorIdentifier for inum tag ");
		}
		return SKIP_BODY;
	}

	public int getInum() throws JspTagException {
		try {
			InvestigatorIdentifier theInvestigatorIdentifier = (InvestigatorIdentifier)findAncestorWithClass(this, InvestigatorIdentifier.class);
			return theInvestigatorIdentifier.getInum();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorIdentifier for inum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorIdentifier for inum tag ");
		}
	}

	public void setInum(int inum) throws JspTagException {
		try {
			InvestigatorIdentifier theInvestigatorIdentifier = (InvestigatorIdentifier)findAncestorWithClass(this, InvestigatorIdentifier.class);
			theInvestigatorIdentifier.setInum(inum);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorIdentifier for inum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorIdentifier for inum tag ");
		}
	}

}
