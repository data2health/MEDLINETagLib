package edu.uiowa.medline.generalNote;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class GeneralNotePmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(GeneralNotePmid.class);


	public int doStartTag() throws JspException {
		try {
			GeneralNote theGeneralNote = (GeneralNote)findAncestorWithClass(this, GeneralNote.class);
			if (!theGeneralNote.commitNeeded) {
				pageContext.getOut().print(theGeneralNote.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing GeneralNote for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneralNote for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			GeneralNote theGeneralNote = (GeneralNote)findAncestorWithClass(this, GeneralNote.class);
			return theGeneralNote.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing GeneralNote for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneralNote for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			GeneralNote theGeneralNote = (GeneralNote)findAncestorWithClass(this, GeneralNote.class);
			theGeneralNote.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing GeneralNote for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneralNote for pmid tag ");
		}
	}

}
