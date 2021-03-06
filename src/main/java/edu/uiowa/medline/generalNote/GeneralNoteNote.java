package edu.uiowa.medline.generalNote;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class GeneralNoteNote extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(GeneralNoteNote.class);


	public int doStartTag() throws JspException {
		try {
			GeneralNote theGeneralNote = (GeneralNote)findAncestorWithClass(this, GeneralNote.class);
			if (!theGeneralNote.commitNeeded) {
				pageContext.getOut().print(theGeneralNote.getNote());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing GeneralNote for note tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneralNote for note tag ");
		}
		return SKIP_BODY;
	}

	public String getNote() throws JspTagException {
		try {
			GeneralNote theGeneralNote = (GeneralNote)findAncestorWithClass(this, GeneralNote.class);
			return theGeneralNote.getNote();
		} catch (Exception e) {
			log.error(" Can't find enclosing GeneralNote for note tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneralNote for note tag ");
		}
	}

	public void setNote(String note) throws JspTagException {
		try {
			GeneralNote theGeneralNote = (GeneralNote)findAncestorWithClass(this, GeneralNote.class);
			theGeneralNote.setNote(note);
		} catch (Exception e) {
			log.error("Can't find enclosing GeneralNote for note tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneralNote for note tag ");
		}
	}

}
