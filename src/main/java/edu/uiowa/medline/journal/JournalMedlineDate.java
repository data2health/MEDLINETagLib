package edu.uiowa.medline.journal;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class JournalMedlineDate extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(JournalMedlineDate.class);


	public int doStartTag() throws JspException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			if (!theJournal.commitNeeded) {
				pageContext.getOut().print(theJournal.getMedlineDate());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for medlineDate tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for medlineDate tag ");
		}
		return SKIP_BODY;
	}

	public String getMedlineDate() throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			return theJournal.getMedlineDate();
		} catch (Exception e) {
			log.error(" Can't find enclosing Journal for medlineDate tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for medlineDate tag ");
		}
	}

	public void setMedlineDate(String medlineDate) throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			theJournal.setMedlineDate(medlineDate);
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for medlineDate tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for medlineDate tag ");
		}
	}

}
