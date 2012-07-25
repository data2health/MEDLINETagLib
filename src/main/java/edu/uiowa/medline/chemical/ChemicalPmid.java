package edu.uiowa.medline.chemical;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ChemicalPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ChemicalPmid.class);


	public int doStartTag() throws JspException {
		try {
			Chemical theChemical = (Chemical)findAncestorWithClass(this, Chemical.class);
			if (!theChemical.commitNeeded) {
				pageContext.getOut().print(theChemical.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Chemical for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Chemical for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			Chemical theChemical = (Chemical)findAncestorWithClass(this, Chemical.class);
			return theChemical.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing Chemical for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Chemical for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			Chemical theChemical = (Chemical)findAncestorWithClass(this, Chemical.class);
			theChemical.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing Chemical for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Chemical for pmid tag ");
		}
	}

}
