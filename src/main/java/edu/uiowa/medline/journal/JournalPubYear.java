package edu.uiowa.medline.journal;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class JournalPubYear extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(JournalPubYear.class);


	public int doStartTag() throws JspException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			if (!theJournal.commitNeeded) {
				pageContext.getOut().print(theJournal.getPubYear());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for pubYear tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for pubYear tag ");
		}
		return SKIP_BODY;
	}

	public int getPubYear() throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			return theJournal.getPubYear();
		} catch (Exception e) {
			log.error(" Can't find enclosing Journal for pubYear tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for pubYear tag ");
		}
	}

	public void setPubYear(int pubYear) throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			theJournal.setPubYear(pubYear);
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for pubYear tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for pubYear tag ");
		}
	}

}
