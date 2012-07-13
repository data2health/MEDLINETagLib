package edu.uiowa.medline.journal;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class JournalPubDay extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(JournalPubDay.class);


	public int doStartTag() throws JspException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			if (!theJournal.commitNeeded) {
				pageContext.getOut().print(theJournal.getPubDay());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for pubDay tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for pubDay tag ");
		}
		return SKIP_BODY;
	}

	public int getPubDay() throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			return theJournal.getPubDay();
		} catch (Exception e) {
			log.error(" Can't find enclosing Journal for pubDay tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for pubDay tag ");
		}
	}

	public void setPubDay(int pubDay) throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			theJournal.setPubDay(pubDay);
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for pubDay tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for pubDay tag ");
		}
	}

}
