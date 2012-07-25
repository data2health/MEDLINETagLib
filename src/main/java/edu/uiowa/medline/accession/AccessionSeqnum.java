package edu.uiowa.medline.accession;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AccessionSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AccessionSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			Accession theAccession = (Accession)findAncestorWithClass(this, Accession.class);
			if (!theAccession.commitNeeded) {
				pageContext.getOut().print(theAccession.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Accession for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Accession for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			Accession theAccession = (Accession)findAncestorWithClass(this, Accession.class);
			return theAccession.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing Accession for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Accession for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			Accession theAccession = (Accession)findAncestorWithClass(this, Accession.class);
			theAccession.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing Accession for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Accession for seqnum tag ");
		}
	}

}
