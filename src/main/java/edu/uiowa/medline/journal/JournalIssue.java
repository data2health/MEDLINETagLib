package edu.uiowa.medline.journal;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class JournalIssue extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(JournalIssue.class);


	public int doStartTag() throws JspException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			if (!theJournal.commitNeeded) {
				pageContext.getOut().print(theJournal.getIssue());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for issue tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for issue tag ");
		}
		return SKIP_BODY;
	}

	public String getIssue() throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			return theJournal.getIssue();
		} catch (Exception e) {
			log.error(" Can't find enclosing Journal for issue tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for issue tag ");
		}
	}

	public void setIssue(String issue) throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			theJournal.setIssue(issue);
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for issue tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for issue tag ");
		}
	}

}
