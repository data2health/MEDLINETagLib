package edu.uiowa.medline.investigatorNameId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class InvestigatorNameIdSource extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(InvestigatorNameIdSource.class);


	public int doStartTag() throws JspException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			if (!theInvestigatorNameId.commitNeeded) {
				pageContext.getOut().print(theInvestigatorNameId.getSource());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorNameId for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for source tag ");
		}
		return SKIP_BODY;
	}

	public String getSource() throws JspTagException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			return theInvestigatorNameId.getSource();
		} catch (Exception e) {
			log.error(" Can't find enclosing InvestigatorNameId for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for source tag ");
		}
	}

	public void setSource(String source) throws JspTagException {
		try {
			InvestigatorNameId theInvestigatorNameId = (InvestigatorNameId)findAncestorWithClass(this, InvestigatorNameId.class);
			theInvestigatorNameId.setSource(source);
		} catch (Exception e) {
			log.error("Can't find enclosing InvestigatorNameId for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing InvestigatorNameId for source tag ");
		}
	}

}
