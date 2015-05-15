package edu.uiowa.medline.affiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AffiliationSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AffiliationSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			if (!theAffiliation.commitNeeded) {
				pageContext.getOut().print(theAffiliation.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Affiliation for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			return theAffiliation.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing Affiliation for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			theAffiliation.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing Affiliation for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for seqnum tag ");
		}
	}

}
