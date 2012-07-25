package edu.uiowa.medline.grant;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class GrantAcronym extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(GrantAcronym.class);


	public int doStartTag() throws JspException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			if (!theGrant.commitNeeded) {
				pageContext.getOut().print(theGrant.getAcronym());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Grant for acronym tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for acronym tag ");
		}
		return SKIP_BODY;
	}

	public String getAcronym() throws JspTagException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			return theGrant.getAcronym();
		} catch (Exception e) {
			log.error(" Can't find enclosing Grant for acronym tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for acronym tag ");
		}
	}

	public void setAcronym(String acronym) throws JspTagException {
		try {
			Grant theGrant = (Grant)findAncestorWithClass(this, Grant.class);
			theGrant.setAcronym(acronym);
		} catch (Exception e) {
			log.error("Can't find enclosing Grant for acronym tag ", e);
			throw new JspTagException("Error: Can't find enclosing Grant for acronym tag ");
		}
	}

}
