package edu.uiowa.medline.journal;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class JournalPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(JournalPmid.class);


	public int doStartTag() throws JspException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			if (!theJournal.commitNeeded) {
				pageContext.getOut().print(theJournal.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			return theJournal.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing Journal for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			theJournal.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for pmid tag ");
		}
	}

}
