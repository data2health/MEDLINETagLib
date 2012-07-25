package edu.uiowa.medline.chemical;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ChemicalRegistryNumber extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ChemicalRegistryNumber.class);


	public int doStartTag() throws JspException {
		try {
			Chemical theChemical = (Chemical)findAncestorWithClass(this, Chemical.class);
			if (!theChemical.commitNeeded) {
				pageContext.getOut().print(theChemical.getRegistryNumber());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Chemical for registryNumber tag ", e);
			throw new JspTagException("Error: Can't find enclosing Chemical for registryNumber tag ");
		}
		return SKIP_BODY;
	}

	public String getRegistryNumber() throws JspTagException {
		try {
			Chemical theChemical = (Chemical)findAncestorWithClass(this, Chemical.class);
			return theChemical.getRegistryNumber();
		} catch (Exception e) {
			log.error(" Can't find enclosing Chemical for registryNumber tag ", e);
			throw new JspTagException("Error: Can't find enclosing Chemical for registryNumber tag ");
		}
	}

	public void setRegistryNumber(String registryNumber) throws JspTagException {
		try {
			Chemical theChemical = (Chemical)findAncestorWithClass(this, Chemical.class);
			theChemical.setRegistryNumber(registryNumber);
		} catch (Exception e) {
			log.error("Can't find enclosing Chemical for registryNumber tag ", e);
			throw new JspTagException("Error: Can't find enclosing Chemical for registryNumber tag ");
		}
	}

}
