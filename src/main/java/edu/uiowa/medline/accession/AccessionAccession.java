package edu.uiowa.medline.accession;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AccessionAccession extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AccessionAccession.class);


	public int doStartTag() throws JspException {
		try {
			Accession theAccession = (Accession)findAncestorWithClass(this, Accession.class);
			if (!theAccession.commitNeeded) {
				pageContext.getOut().print(theAccession.getAccession());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Accession for accession tag ", e);
			throw new JspTagException("Error: Can't find enclosing Accession for accession tag ");
		}
		return SKIP_BODY;
	}

	public String getAccession() throws JspTagException {
		try {
			Accession theAccession = (Accession)findAncestorWithClass(this, Accession.class);
			return theAccession.getAccession();
		} catch (Exception e) {
			log.error(" Can't find enclosing Accession for accession tag ", e);
			throw new JspTagException("Error: Can't find enclosing Accession for accession tag ");
		}
	}

	public void setAccession(String accession) throws JspTagException {
		try {
			Accession theAccession = (Accession)findAncestorWithClass(this, Accession.class);
			theAccession.setAccession(accession);
		} catch (Exception e) {
			log.error("Can't find enclosing Accession for accession tag ", e);
			throw new JspTagException("Error: Can't find enclosing Accession for accession tag ");
		}
	}

}
