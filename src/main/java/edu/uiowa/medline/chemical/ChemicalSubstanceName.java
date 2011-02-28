package edu.uiowa.medline.chemical;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ChemicalSubstanceName extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Chemical theChemical = (Chemical)findAncestorWithClass(this, Chemical.class);
			if (!theChemical.commitNeeded) {
				pageContext.getOut().print(theChemical.getSubstanceName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Chemical for substanceName tag ");
		}
		return SKIP_BODY;
	}

	public String getSubstanceName() throws JspTagException {
		try {
			Chemical theChemical = (Chemical)findAncestorWithClass(this, Chemical.class);
			return theChemical.getSubstanceName();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Chemical for substanceName tag ");
		}
	}

	public void setSubstanceName(String substanceName) throws JspTagException {
		try {
			Chemical theChemical = (Chemical)findAncestorWithClass(this, Chemical.class);
			theChemical.setSubstanceName(substanceName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Chemical for substanceName tag ");
		}
	}

}
