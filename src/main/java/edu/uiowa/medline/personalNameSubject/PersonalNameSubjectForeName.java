package edu.uiowa.medline.personalNameSubject;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class PersonalNameSubjectForeName extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			if (!thePersonalNameSubject.commitNeeded) {
				pageContext.getOut().print(thePersonalNameSubject.getForeName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for foreName tag ");
		}
		return SKIP_BODY;
	}

	public String getForeName() throws JspTagException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			return thePersonalNameSubject.getForeName();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for foreName tag ");
		}
	}

	public void setForeName(String foreName) throws JspTagException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			thePersonalNameSubject.setForeName(foreName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for foreName tag ");
		}
	}

}
