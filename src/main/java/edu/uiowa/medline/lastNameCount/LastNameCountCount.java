package edu.uiowa.medline.lastNameCount;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class LastNameCountCount extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(LastNameCountCount.class);


	public int doStartTag() throws JspException {
		try {
			LastNameCount theLastNameCount = (LastNameCount)findAncestorWithClass(this, LastNameCount.class);
			if (!theLastNameCount.commitNeeded) {
				pageContext.getOut().print(theLastNameCount.getCount());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing LastNameCount for count tag ", e);
			throw new JspTagException("Error: Can't find enclosing LastNameCount for count tag ");
		}
		return SKIP_BODY;
	}

	public int getCount() throws JspTagException {
		try {
			LastNameCount theLastNameCount = (LastNameCount)findAncestorWithClass(this, LastNameCount.class);
			return theLastNameCount.getCount();
		} catch (Exception e) {
			log.error(" Can't find enclosing LastNameCount for count tag ", e);
			throw new JspTagException("Error: Can't find enclosing LastNameCount for count tag ");
		}
	}

	public void setCount(int count) throws JspTagException {
		try {
			LastNameCount theLastNameCount = (LastNameCount)findAncestorWithClass(this, LastNameCount.class);
			theLastNameCount.setCount(count);
		} catch (Exception e) {
			log.error("Can't find enclosing LastNameCount for count tag ", e);
			throw new JspTagException("Error: Can't find enclosing LastNameCount for count tag ");
		}
	}

}
