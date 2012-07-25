package edu.uiowa.medline.otherAbstractText;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class OtherAbstractTextTnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(OtherAbstractTextTnum.class);


	public int doStartTag() throws JspException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			if (!theOtherAbstractText.commitNeeded) {
				pageContext.getOut().print(theOtherAbstractText.getTnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OtherAbstractText for tnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for tnum tag ");
		}
		return SKIP_BODY;
	}

	public int getTnum() throws JspTagException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			return theOtherAbstractText.getTnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing OtherAbstractText for tnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for tnum tag ");
		}
	}

	public void setTnum(int tnum) throws JspTagException {
		try {
			OtherAbstractText theOtherAbstractText = (OtherAbstractText)findAncestorWithClass(this, OtherAbstractText.class);
			theOtherAbstractText.setTnum(tnum);
		} catch (Exception e) {
			log.error("Can't find enclosing OtherAbstractText for tnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstractText for tnum tag ");
		}
	}

}
