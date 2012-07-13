package edu.uiowa.medline.personalNameSubject;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class PersonalNameSubjectInitials extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(PersonalNameSubjectInitials.class);


	public int doStartTag() throws JspException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			if (!thePersonalNameSubject.commitNeeded) {
				pageContext.getOut().print(thePersonalNameSubject.getInitials());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonalNameSubject for initials tag ", e);
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for initials tag ");
		}
		return SKIP_BODY;
	}

	public String getInitials() throws JspTagException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			return thePersonalNameSubject.getInitials();
		} catch (Exception e) {
			log.error(" Can't find enclosing PersonalNameSubject for initials tag ", e);
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for initials tag ");
		}
	}

	public void setInitials(String initials) throws JspTagException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			thePersonalNameSubject.setInitials(initials);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonalNameSubject for initials tag ", e);
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for initials tag ");
		}
	}

}
