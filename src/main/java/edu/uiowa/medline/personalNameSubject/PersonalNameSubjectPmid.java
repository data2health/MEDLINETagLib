package edu.uiowa.medline.personalNameSubject;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class PersonalNameSubjectPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(PersonalNameSubjectPmid.class);


	public int doStartTag() throws JspException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			if (!thePersonalNameSubject.commitNeeded) {
				pageContext.getOut().print(thePersonalNameSubject.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PersonalNameSubject for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			return thePersonalNameSubject.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing PersonalNameSubject for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			thePersonalNameSubject.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing PersonalNameSubject for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for pmid tag ");
		}
	}

}
