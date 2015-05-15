package edu.uiowa.medline.affiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AffiliationAnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AffiliationAnum.class);


	public int doStartTag() throws JspException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			if (!theAffiliation.commitNeeded) {
				pageContext.getOut().print(theAffiliation.getAnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Affiliation for anum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for anum tag ");
		}
		return SKIP_BODY;
	}

	public int getAnum() throws JspTagException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			return theAffiliation.getAnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing Affiliation for anum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for anum tag ");
		}
	}

	public void setAnum(int anum) throws JspTagException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			theAffiliation.setAnum(anum);
		} catch (Exception e) {
			log.error("Can't find enclosing Affiliation for anum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for anum tag ");
		}
	}

}
