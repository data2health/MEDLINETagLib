package edu.uiowa.medline.generalNote;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class GeneralNoteOwner extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(GeneralNoteOwner.class);


	public int doStartTag() throws JspException {
		try {
			GeneralNote theGeneralNote = (GeneralNote)findAncestorWithClass(this, GeneralNote.class);
			if (!theGeneralNote.commitNeeded) {
				pageContext.getOut().print(theGeneralNote.getOwner());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing GeneralNote for owner tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneralNote for owner tag ");
		}
		return SKIP_BODY;
	}

	public String getOwner() throws JspTagException {
		try {
			GeneralNote theGeneralNote = (GeneralNote)findAncestorWithClass(this, GeneralNote.class);
			return theGeneralNote.getOwner();
		} catch (Exception e) {
			log.error(" Can't find enclosing GeneralNote for owner tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneralNote for owner tag ");
		}
	}

	public void setOwner(String owner) throws JspTagException {
		try {
			GeneralNote theGeneralNote = (GeneralNote)findAncestorWithClass(this, GeneralNote.class);
			theGeneralNote.setOwner(owner);
		} catch (Exception e) {
			log.error("Can't find enclosing GeneralNote for owner tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneralNote for owner tag ");
		}
	}

}
