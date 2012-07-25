package edu.uiowa.medline.journal;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class JournalVolume extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(JournalVolume.class);


	public int doStartTag() throws JspException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			if (!theJournal.commitNeeded) {
				pageContext.getOut().print(theJournal.getVolume());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for volume tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for volume tag ");
		}
		return SKIP_BODY;
	}

	public String getVolume() throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			return theJournal.getVolume();
		} catch (Exception e) {
			log.error(" Can't find enclosing Journal for volume tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for volume tag ");
		}
	}

	public void setVolume(String volume) throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			theJournal.setVolume(volume);
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for volume tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for volume tag ");
		}
	}

}
