package edu.uiowa.icts.taglib.MEDLINETagLib.accession;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.icts.taglib.MEDLINETagLib.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AccessionAccnum extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Accession theAccession = (Accession)findAncestorWithClass(this, Accession.class);
			if (!theAccession.commitNeeded) {
				pageContext.getOut().print(theAccession.getAccnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Accession for accnum tag ");
		}
		return SKIP_BODY;
	}

	public int getAccnum() throws JspTagException {
		try {
			Accession theAccession = (Accession)findAncestorWithClass(this, Accession.class);
			return theAccession.getAccnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Accession for accnum tag ");
		}
	}

	public void setAccnum(int accnum) throws JspTagException {
		try {
			Accession theAccession = (Accession)findAncestorWithClass(this, Accession.class);
			theAccession.setAccnum(accnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Accession for accnum tag ");
		}
	}

}
