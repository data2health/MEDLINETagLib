package edu.uiowa.medline.affiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AffiliationIdentifier extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AffiliationIdentifier.class);


	public int doStartTag() throws JspException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			if (!theAffiliation.commitNeeded) {
				pageContext.getOut().print(theAffiliation.getIdentifier());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Affiliation for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for identifier tag ");
		}
		return SKIP_BODY;
	}

	public String getIdentifier() throws JspTagException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			return theAffiliation.getIdentifier();
		} catch (Exception e) {
			log.error(" Can't find enclosing Affiliation for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for identifier tag ");
		}
	}

	public void setIdentifier(String identifier) throws JspTagException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			theAffiliation.setIdentifier(identifier);
		} catch (Exception e) {
			log.error("Can't find enclosing Affiliation for identifier tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for identifier tag ");
		}
	}

}
