package edu.uiowa.medline.grant;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class GrantGid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(GrantGid.class);


	public int doStartTag() throws JspException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			if (!theGrant.commitNeeded) {
				pageContext.getOut().print(theGrant.getGid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Grant for gid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for gid tag ");
		}
		return SKIP_BODY;
	}

	public String getGid() throws JspTagException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			return theGrant.getGid();
		} catch (Exception e) {
			log.error(" Can't find enclosing Grant for gid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for gid tag ");
		}
	}

	public void setGid(String gid) throws JspTagException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			theGrant.setGid(gid);
		} catch (Exception e) {
			log.error("Can't find enclosing Grant for gid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for gid tag ");
		}
	}

}
