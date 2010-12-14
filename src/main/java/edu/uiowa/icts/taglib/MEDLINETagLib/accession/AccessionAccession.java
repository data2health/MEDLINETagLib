package edu.uiowa.icts.taglib.MEDLINETagLib.accession;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.icts.taglib.MEDLINETagLib.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AccessionAccession extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Accession theAccession = (Accession)findAncestorWithClass(this, Accession.class);
			if (!theAccession.commitNeeded) {
				pageContext.getOut().print(theAccession.getAccession());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Accession for accession tag ");
		}
		return SKIP_BODY;
	}

	public String getAccession() throws JspTagException {
		try {
			Accession theAccession = (Accession)findAncestorWithClass(this, Accession.class);
			return theAccession.getAccession();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Accession for accession tag ");
		}
	}

	public void setAccession(String accession) throws JspTagException {
		try {
			Accession theAccession = (Accession)findAncestorWithClass(this, Accession.class);
			theAccession.setAccession(accession);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Accession for accession tag ");
		}
	}

}
