package edu.uiowa.medline.abstr;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AbstrSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AbstrSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			if (!theAbstr.commitNeeded) {
				pageContext.getOut().print(theAbstr.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Abstr for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Abstr for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			return theAbstr.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing Abstr for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Abstr for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			theAbstr.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing Abstr for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Abstr for seqnum tag ");
		}
	}

}
