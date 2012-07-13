package edu.uiowa.medline.otherAbstractText;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class OtherAbstractTextAbstractText extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(OtherAbstractTextAbstractText.class);


	public int doStartTag() throws JspException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			if (!theOtherAbstractText.commitNeeded) {
				pageContext.getOut().print(theOtherAbstractText.getAbstractText());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OtherAbstractText for abstractText tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for abstractText tag ");
		}
		return SKIP_BODY;
	}

	public String getAbstractText() throws JspTagException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			return theOtherAbstractText.getAbstractText();
		} catch (Exception e) {
			log.error(" Can't find enclosing OtherAbstractText for abstractText tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for abstractText tag ");
		}
	}

	public void setAbstractText(String abstractText) throws JspTagException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			theOtherAbstractText.setAbstractText(abstractText);
		} catch (Exception e) {
			log.error("Can't find enclosing OtherAbstractText for abstractText tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for abstractText tag ");
		}
	}

}
