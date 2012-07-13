package edu.uiowa.medline.abstr;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AbstrLabel extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AbstrLabel.class);


	public int doStartTag() throws JspException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			if (!theAbstr.commitNeeded) {
				pageContext.getOut().print(theAbstr.getLabel());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Abstr for label tag ", e);
			throw new JspTagException("Error: Can't find enclosing Abstr for label tag ");
		}
		return SKIP_BODY;
	}

	public String getLabel() throws JspTagException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			return theAbstr.getLabel();
		} catch (Exception e) {
			log.error(" Can't find enclosing Abstr for label tag ", e);
			throw new JspTagException("Error: Can't find enclosing Abstr for label tag ");
		}
	}

	public void setLabel(String label) throws JspTagException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			theAbstr.setLabel(label);
		} catch (Exception e) {
			log.error("Can't find enclosing Abstr for label tag ", e);
			throw new JspTagException("Error: Can't find enclosing Abstr for label tag ");
		}
	}

}
