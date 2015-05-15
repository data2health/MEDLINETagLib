package edu.uiowa.medline.chemical;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ChemicalID extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ChemicalID.class);


	public int doStartTag() throws JspException {
		try {
			Chemical theChemical = (Chemical)findAncestorWithClass(this, Chemical.class);
			if (!theChemical.commitNeeded) {
				pageContext.getOut().print(theChemical.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Chemical for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Chemical for ID tag ");
		}
		return SKIP_BODY;
	}

	public String getID() throws JspTagException {
		try {
			Chemical theChemical = (Chemical)findAncestorWithClass(this, Chemical.class);
			return theChemical.getID();
		} catch (Exception e) {
			log.error(" Can't find enclosing Chemical for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Chemical for ID tag ");
		}
	}

	public void setID(String ID) throws JspTagException {
		try {
			Chemical theChemical = (Chemical)findAncestorWithClass(this, Chemical.class);
			theChemical.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing Chemical for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Chemical for ID tag ");
		}
	}

}
