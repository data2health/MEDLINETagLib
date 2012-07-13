package edu.uiowa.medline.grant;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class GrantPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(GrantPmid.class);


	public int doStartTag() throws JspException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			if (!theGrant.commitNeeded) {
				pageContext.getOut().print(theGrant.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Grant for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			return theGrant.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing Grant for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			theGrant.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing Grant for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for pmid tag ");
		}
	}

}
