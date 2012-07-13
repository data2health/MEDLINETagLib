package edu.uiowa.medline.elocation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ElocationSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ElocationSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			Elocation theElocation = (Elocation)findAncestorWithClass(this, Elocation.class);
			if (!theElocation.commitNeeded) {
				pageContext.getOut().print(theElocation.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Elocation for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Elocation for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			Elocation theElocation = (Elocation)findAncestorWithClass(this, Elocation.class);
			return theElocation.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing Elocation for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Elocation for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			Elocation theElocation = (Elocation)findAncestorWithClass(this, Elocation.class);
			theElocation.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing Elocation for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Elocation for seqnum tag ");
		}
	}

}
