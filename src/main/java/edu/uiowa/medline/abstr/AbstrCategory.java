package edu.uiowa.medline.abstr;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AbstrCategory extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AbstrCategory.class);


	public int doStartTag() throws JspException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			if (!theAbstr.commitNeeded) {
				pageContext.getOut().print(theAbstr.getCategory());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Abstr for category tag ", e);
			throw new JspTagException("Error: Can't find enclosing Abstr for category tag ");
		}
		return SKIP_BODY;
	}

	public String getCategory() throws JspTagException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			return theAbstr.getCategory();
		} catch (Exception e) {
			log.error(" Can't find enclosing Abstr for category tag ", e);
			throw new JspTagException("Error: Can't find enclosing Abstr for category tag ");
		}
	}

	public void setCategory(String category) throws JspTagException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			theAbstr.setCategory(category);
		} catch (Exception e) {
			log.error("Can't find enclosing Abstr for category tag ", e);
			throw new JspTagException("Error: Can't find enclosing Abstr for category tag ");
		}
	}

}
