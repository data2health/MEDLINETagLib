package edu.uiowa.medline.otherAbstract;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class OtherAbstractSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(OtherAbstractSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			OtherAbstract theOtherAbstract = (OtherAbstract)findAncestorWithClass(this, OtherAbstract.class);
			if (!theOtherAbstract.commitNeeded) {
				pageContext.getOut().print(theOtherAbstract.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OtherAbstract for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstract for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			OtherAbstract theOtherAbstract = (OtherAbstract)findAncestorWithClass(this, OtherAbstract.class);
			return theOtherAbstract.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing OtherAbstract for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstract for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			OtherAbstract theOtherAbstract = (OtherAbstract)findAncestorWithClass(this, OtherAbstract.class);
			theOtherAbstract.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing OtherAbstract for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstract for seqnum tag ");
		}
	}

}
