package edu.uiowa.medline.nameId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class NameIdPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(NameIdPmid.class);


	public int doStartTag() throws JspException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			if (!theNameId.commitNeeded) {
				pageContext.getOut().print(theNameId.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing NameId for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing NameId for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			return theNameId.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing NameId for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing NameId for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			NameId theNameId = (NameId)findAncestorWithClass(this, NameId.class);
			theNameId.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing NameId for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing NameId for pmid tag ");
		}
	}

}
