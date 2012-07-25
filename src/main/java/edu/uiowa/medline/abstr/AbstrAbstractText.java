package edu.uiowa.medline.abstr;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AbstrAbstractText extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AbstrAbstractText.class);


	public int doStartTag() throws JspException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			if (!theAbstr.commitNeeded) {
				pageContext.getOut().print(theAbstr.getAbstractText());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Abstr for abstractText tag ", e);
			throw new JspTagException("Error: Can't find enclosing Abstr for abstractText tag ");
		}
		return SKIP_BODY;
	}

	public String getAbstractText() throws JspTagException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			return theAbstr.getAbstractText();
		} catch (Exception e) {
			log.error(" Can't find enclosing Abstr for abstractText tag ", e);
			throw new JspTagException("Error: Can't find enclosing Abstr for abstractText tag ");
		}
	}

	public void setAbstractText(String abstractText) throws JspTagException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			theAbstr.setAbstractText(abstractText);
		} catch (Exception e) {
			log.error("Can't find enclosing Abstr for abstractText tag ", e);
			throw new JspTagException("Error: Can't find enclosing Abstr for abstractText tag ");
		}
	}

}
