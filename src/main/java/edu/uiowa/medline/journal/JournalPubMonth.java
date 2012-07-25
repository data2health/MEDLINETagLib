package edu.uiowa.medline.journal;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class JournalPubMonth extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(JournalPubMonth.class);


	public int doStartTag() throws JspException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			if (!theJournal.commitNeeded) {
				pageContext.getOut().print(theJournal.getPubMonth());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for pubMonth tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for pubMonth tag ");
		}
		return SKIP_BODY;
	}

	public String getPubMonth() throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			return theJournal.getPubMonth();
		} catch (Exception e) {
			log.error(" Can't find enclosing Journal for pubMonth tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for pubMonth tag ");
		}
	}

	public void setPubMonth(String pubMonth) throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			theJournal.setPubMonth(pubMonth);
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for pubMonth tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for pubMonth tag ");
		}
	}

}
