package edu.uiowa.medline.investigatorIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorIdentifierSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorIdentifierSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorIdentifier theInvestigatorIdentifier = (InvestigatorIdentifier)findAncestorWithClass(this, InvestigatorIdentifier.class);
			if (!theInvestigatorIdentifier.commitNeeded) {
				pageContext.getOut().print(theInvestigatorIdentifier.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorIdentifier for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorIdentifier for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			InvestigatorIdentifier theInvestigatorIdentifier = (InvestigatorIdentifier)findAncestorWithClass(this, InvestigatorIdentifier.class);
			return theInvestigatorIdentifier.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorIdentifier for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorIdentifier for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			InvestigatorIdentifier theInvestigatorIdentifier = (InvestigatorIdentifier)findAncestorWithClass(this, InvestigatorIdentifier.class);
			theInvestigatorIdentifier.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorIdentifier for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorIdentifier for seqnum tag ");
		}
	}

}
