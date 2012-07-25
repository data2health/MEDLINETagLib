package edu.uiowa.medline.grant;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class GrantAgency extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(GrantAgency.class);


	public int doStartTag() throws JspException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			if (!theGrant.commitNeeded) {
				pageContext.getOut().print(theGrant.getAgency());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Grant for agency tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for agency tag ");
		}
		return SKIP_BODY;
	}

	public String getAgency() throws JspTagException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			return theGrant.getAgency();
		} catch (Exception e) {
			log.error(" Can't find enclosing Grant for agency tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for agency tag ");
		}
	}

	public void setAgency(String agency) throws JspTagException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			theGrant.setAgency(agency);
		} catch (Exception e) {
			log.error("Can't find enclosing Grant for agency tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for agency tag ");
		}
	}

}
