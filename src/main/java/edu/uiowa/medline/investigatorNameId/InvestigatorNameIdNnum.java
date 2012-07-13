package edu.uiowa.medline.investigatorNameId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorNameIdNnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorNameIdNnum.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			if (!theInvestigatorNameId.commitNeeded) {
				pageContext.getOut().print(theInvestigatorNameId.getNnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorNameId for nnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for nnum tag ");
		}
		return SKIP_BODY;
	}

	public int getNnum() throws JspTagException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			return theInvestigatorNameId.getNnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorNameId for nnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for nnum tag ");
		}
	}

	public void setNnum(int nnum) throws JspTagException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			theInvestigatorNameId.setNnum(nnum);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorNameId for nnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for nnum tag ");
		}
	}

}
