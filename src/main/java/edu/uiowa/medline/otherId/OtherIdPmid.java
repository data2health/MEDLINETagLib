package edu.uiowa.medline.otherId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class OtherIdPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(OtherIdPmid.class);


	public int doStartTag() throws JspException {
		try {
			OtherId theOtherId = (OtherId)findAncestorWithClass(this, OtherId.class);
			if (!theOtherId.commitNeeded) {
				pageContext.getOut().print(theOtherId.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OtherId for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherId for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			OtherId theOtherId = (OtherId)findAncestorWithClass(this, OtherId.class);
			return theOtherId.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing OtherId for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherId for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			OtherId theOtherId = (OtherId)findAncestorWithClass(this, OtherId.class);
			theOtherId.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing OtherId for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherId for pmid tag ");
		}
	}

}
