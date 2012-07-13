package edu.uiowa.medline.grant;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class GrantSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(GrantSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			if (!theGrant.commitNeeded) {
				pageContext.getOut().print(theGrant.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Grant for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			return theGrant.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing Grant for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			theGrant.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing Grant for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for seqnum tag ");
		}
	}

}
