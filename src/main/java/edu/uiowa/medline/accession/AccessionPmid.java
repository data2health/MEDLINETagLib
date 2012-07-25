package edu.uiowa.medline.accession;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AccessionPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AccessionPmid.class);


	public int doStartTag() throws JspException {
		try {
			Accession theAccession = (Accession)findAncestorWithClass(this, Accession.class);
			if (!theAccession.commitNeeded) {
				pageContext.getOut().print(theAccession.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Accession for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Accession for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			Accession theAccession = (Accession)findAncestorWithClass(this, Accession.class);
			return theAccession.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing Accession for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Accession for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			Accession theAccession = (Accession)findAncestorWithClass(this, Accession.class);
			theAccession.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing Accession for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Accession for pmid tag ");
		}
	}

}
