package edu.uiowa.medline.personalNameSubject;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class PersonalNameSubjectSeqnum extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			if (!thePersonalNameSubject.commitNeeded) {
				pageContext.getOut().print(thePersonalNameSubject.getSeqnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			return thePersonalNameSubject.getSeqnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			PersonalNameSubject thePersonalNameSubject = (PersonalNameSubject)findAncestorWithClass(this, PersonalNameSubject.class);
			thePersonalNameSubject.setSeqnum(seqnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing PersonalNameSubject for seqnum tag ");
		}
	}

}
