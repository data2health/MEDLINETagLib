package edu.uiowa.medline.personalNameSubject;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class PersonalNameSubjectSuffix extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(PersonalNameSubjectSuffix.class);


	public int doStartTag() throws JspException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			if (!thePersonalNameSubject.commitNeeded) {
				pageContext.getOut().print(thePersonalNameSubject.getSuffix());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonalNameSubject for suffix tag ", e);
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for suffix tag ");
		}
		return SKIP_BODY;
	}

	public String getSuffix() throws JspTagException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			return thePersonalNameSubject.getSuffix();
		} catch (Exception e) {
			log.error(" Can't find enclosing PersonalNameSubject for suffix tag ", e);
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for suffix tag ");
		}
	}

	public void setSuffix(String suffix) throws JspTagException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			thePersonalNameSubject.setSuffix(suffix);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonalNameSubject for suffix tag ", e);
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for suffix tag ");
		}
	}

}
