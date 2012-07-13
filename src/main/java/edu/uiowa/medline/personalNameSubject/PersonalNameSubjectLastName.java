package edu.uiowa.medline.personalNameSubject;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class PersonalNameSubjectLastName extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(PersonalNameSubjectLastName.class);


	public int doStartTag() throws JspException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			if (!thePersonalNameSubject.commitNeeded) {
				pageContext.getOut().print(thePersonalNameSubject.getLastName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonalNameSubject for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for lastName tag ");
		}
		return SKIP_BODY;
	}

	public String getLastName() throws JspTagException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			return thePersonalNameSubject.getLastName();
		} catch (Exception e) {
			log.error(" Can't find enclosing PersonalNameSubject for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for lastName tag ");
		}
	}

	public void setLastName(String lastName) throws JspTagException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			thePersonalNameSubject.setLastName(lastName);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonalNameSubject for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for lastName tag ");
		}
	}

}
