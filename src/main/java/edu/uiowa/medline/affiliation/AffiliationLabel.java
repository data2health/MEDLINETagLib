package edu.uiowa.medline.affiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AffiliationLabel extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AffiliationLabel.class);


	public int doStartTag() throws JspException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			if (!theAffiliation.commitNeeded) {
				pageContext.getOut().print(theAffiliation.getLabel());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Affiliation for label tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for label tag ");
		}
		return SKIP_BODY;
	}

	public String getLabel() throws JspTagException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			return theAffiliation.getLabel();
		} catch (Exception e) {
			log.error(" Can't find enclosing Affiliation for label tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for label tag ");
		}
	}

	public void setLabel(String label) throws JspTagException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			theAffiliation.setLabel(label);
		} catch (Exception e) {
			log.error("Can't find enclosing Affiliation for label tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for label tag ");
		}
	}

}
