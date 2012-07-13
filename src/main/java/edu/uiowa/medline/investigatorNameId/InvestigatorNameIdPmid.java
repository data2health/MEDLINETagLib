package edu.uiowa.medline.investigatorNameId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorNameIdPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorNameIdPmid.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			if (!theInvestigatorNameId.commitNeeded) {
				pageContext.getOut().print(theInvestigatorNameId.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorNameId for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			return theInvestigatorNameId.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorNameId for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			theInvestigatorNameId.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorNameId for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for pmid tag ");
		}
	}

}
