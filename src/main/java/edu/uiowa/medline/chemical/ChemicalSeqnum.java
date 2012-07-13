package edu.uiowa.medline.chemical;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ChemicalSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ChemicalSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			Chemical theChemical = (Chemical)findAncestorWithClass(this, Chemical.class);
			if (!theChemical.commitNeeded) {
				pageContext.getOut().print(theChemical.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Chemical for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Chemical for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			Chemical theChemical = (Chemical)findAncestorWithClass(this, Chemical.class);
			return theChemical.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing Chemical for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Chemical for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			Chemical theChemical = (Chemical)findAncestorWithClass(this, Chemical.class);
			theChemical.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing Chemical for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Chemical for seqnum tag ");
		}
	}

}
