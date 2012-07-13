package edu.uiowa.medline.nameId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class NameIdNameId extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(NameIdNameId.class);


	public int doStartTag() throws JspException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			if (!theNameId.commitNeeded) {
				pageContext.getOut().print(theNameId.getNameId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing NameId for nameId tag ", e);
			throw new JspTagException("Error: Can't find enclosing NameId for nameId tag ");
		}
		return SKIP_BODY;
	}

	public String getNameId() throws JspTagException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			return theNameId.getNameId();
		} catch (Exception e) {
			log.error(" Can't find enclosing NameId for nameId tag ", e);
			throw new JspTagException("Error: Can't find enclosing NameId for nameId tag ");
		}
	}

	public void setNameId(String nameId) throws JspTagException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			theNameId.setNameId(nameId);
		} catch (Exception e) {
			log.error("Can't find enclosing NameId for nameId tag ", e);
			throw new JspTagException("Error: Can't find enclosing NameId for nameId tag ");
		}
	}

}
