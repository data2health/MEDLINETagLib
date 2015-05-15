package edu.uiowa.medline.affiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AffiliationPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AffiliationPmid.class);


	public int doStartTag() throws JspException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			if (!theAffiliation.commitNeeded) {
				pageContext.getOut().print(theAffiliation.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Affiliation for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			return theAffiliation.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing Affiliation for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			theAffiliation.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing Affiliation for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for pmid tag ");
		}
	}

}
