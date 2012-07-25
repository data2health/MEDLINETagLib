package edu.uiowa.medline.otherAbstract;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class OtherAbstractType extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(OtherAbstractType.class);


	public int doStartTag() throws JspException {
		try {
			OtherAbstract theOtherAbstract = (OtherAbstract)findAncestorWithClass(this, OtherAbstract.class);
			if (!theOtherAbstract.commitNeeded) {
				pageContext.getOut().print(theOtherAbstract.getType());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OtherAbstract for type tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstract for type tag ");
		}
		return SKIP_BODY;
	}

	public String getType() throws JspTagException {
		try {
			OtherAbstract theOtherAbstract = (OtherAbstract)findAncestorWithClass(this, OtherAbstract.class);
			return theOtherAbstract.getType();
		} catch (Exception e) {
			log.error(" Can't find enclosing OtherAbstract for type tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstract for type tag ");
		}
	}

	public void setType(String type) throws JspTagException {
		try {
			OtherAbstract theOtherAbstract = (OtherAbstract)findAncestorWithClass(this, OtherAbstract.class);
			theOtherAbstract.setType(type);
		} catch (Exception e) {
			log.error("Can't find enclosing OtherAbstract for type tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstract for type tag ");
		}
	}

}
