package edu.uiowa.medline.journal;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class JournalPubSeason extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(JournalPubSeason.class);


	public int doStartTag() throws JspException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			if (!theJournal.commitNeeded) {
				pageContext.getOut().print(theJournal.getPubSeason());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for pubSeason tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for pubSeason tag ");
		}
		return SKIP_BODY;
	}

	public String getPubSeason() throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			return theJournal.getPubSeason();
		} catch (Exception e) {
			log.error(" Can't find enclosing Journal for pubSeason tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for pubSeason tag ");
		}
	}

	public void setPubSeason(String pubSeason) throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			theJournal.setPubSeason(pubSeason);
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for pubSeason tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for pubSeason tag ");
		}
	}

}
