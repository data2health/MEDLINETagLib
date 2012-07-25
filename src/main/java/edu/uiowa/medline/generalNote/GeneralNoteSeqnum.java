package edu.uiowa.medline.generalNote;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class GeneralNoteSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(GeneralNoteSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			GeneralNote theGeneralNote = (GeneralNote)findAncestorWithClass(this, GeneralNote.class);
			if (!theGeneralNote.commitNeeded) {
				pageContext.getOut().print(theGeneralNote.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing GeneralNote for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneralNote for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			GeneralNote theGeneralNote = (GeneralNote)findAncestorWithClass(this, GeneralNote.class);
			return theGeneralNote.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing GeneralNote for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneralNote for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			GeneralNote theGeneralNote = (GeneralNote)findAncestorWithClass(this, GeneralNote.class);
			theGeneralNote.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing GeneralNote for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneralNote for seqnum tag ");
		}
	}

}
