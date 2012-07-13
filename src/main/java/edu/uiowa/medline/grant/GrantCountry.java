package edu.uiowa.medline.grant;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class GrantCountry extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(GrantCountry.class);


	public int doStartTag() throws JspException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			if (!theGrant.commitNeeded) {
				pageContext.getOut().print(theGrant.getCountry());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Grant for country tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for country tag ");
		}
		return SKIP_BODY;
	}

	public String getCountry() throws JspTagException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			return theGrant.getCountry();
		} catch (Exception e) {
			log.error(" Can't find enclosing Grant for country tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for country tag ");
		}
	}

	public void setCountry(String country) throws JspTagException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			theGrant.setCountry(country);
		} catch (Exception e) {
			log.error("Can't find enclosing Grant for country tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for country tag ");
		}
	}

}
