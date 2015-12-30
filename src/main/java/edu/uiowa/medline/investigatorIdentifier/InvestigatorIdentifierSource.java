package edu.uiowa.medline.investigatorIdentifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorIdentifierSource extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorIdentifierSource.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorIdentifier theInvestigatorIdentifier = (InvestigatorIdentifier)findAncestorWithClass(this, InvestigatorIdentifier.class);
			if (!theInvestigatorIdentifier.commitNeeded) {
				pageContext.getOut().print(theInvestigatorIdentifier.getSource());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorIdentifier for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorIdentifier for source tag ");
		}
		return SKIP_BODY;
	}

	public String getSource() throws JspTagException {
		try {
			InvestigatorIdentifier theInvestigatorIdentifier = (InvestigatorIdentifier)findAncestorWithClass(this, InvestigatorIdentifier.class);
			return theInvestigatorIdentifier.getSource();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorIdentifier for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorIdentifier for source tag ");
		}
	}

	public void setSource(String source) throws JspTagException {
		try {
			InvestigatorIdentifier theInvestigatorIdentifier = (InvestigatorIdentifier)findAncestorWithClass(this, InvestigatorIdentifier.class);
			theInvestigatorIdentifier.setSource(source);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorIdentifier for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorIdentifier for source tag ");
		}
	}

}
