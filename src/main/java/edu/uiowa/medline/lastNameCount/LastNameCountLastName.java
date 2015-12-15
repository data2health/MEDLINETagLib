package edu.uiowa.medline.lastNameCount;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class LastNameCountLastName extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(LastNameCountLastName.class);


	public int doStartTag() throws JspException {
		try {
			LastNameCount theLastNameCount = (LastNameCount)findAncestorWithClass(this, LastNameCount.class);
			if (!theLastNameCount.commitNeeded) {
				pageContext.getOut().print(theLastNameCount.getLastName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing LastNameCount for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing LastNameCount for lastName tag ");
		}
		return SKIP_BODY;
	}

	public String getLastName() throws JspTagException {
		try {
			LastNameCount theLastNameCount = (LastNameCount)findAncestorWithClass(this, LastNameCount.class);
			return theLastNameCount.getLastName();
		} catch (Exception e) {
			log.error(" Can't find enclosing LastNameCount for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing LastNameCount for lastName tag ");
		}
	}

	public void setLastName(String lastName) throws JspTagException {
		try {
			LastNameCount theLastNameCount = (LastNameCount)findAncestorWithClass(this, LastNameCount.class);
			theLastNameCount.setLastName(lastName);
		} catch (Exception e) {
			log.error("Can't find enclosing LastNameCount for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing LastNameCount for lastName tag ");
		}
	}

}
