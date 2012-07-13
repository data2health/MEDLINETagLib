package edu.uiowa.medline.otherAbstractText;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class OtherAbstractTextPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(OtherAbstractTextPmid.class);


	public int doStartTag() throws JspException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			if (!theOtherAbstractText.commitNeeded) {
				pageContext.getOut().print(theOtherAbstractText.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OtherAbstractText for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			return theOtherAbstractText.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing OtherAbstractText for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			theOtherAbstractText.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing OtherAbstractText for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for pmid tag ");
		}
	}

}
