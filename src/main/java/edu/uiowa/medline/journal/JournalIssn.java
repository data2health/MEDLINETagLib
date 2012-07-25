package edu.uiowa.medline.journal;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class JournalIssn extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(JournalIssn.class);


	public int doStartTag() throws JspException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			if (!theJournal.commitNeeded) {
				pageContext.getOut().print(theJournal.getIssn());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for issn tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for issn tag ");
		}
		return SKIP_BODY;
	}

	public String getIssn() throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			return theJournal.getIssn();
		} catch (Exception e) {
			log.error(" Can't find enclosing Journal for issn tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for issn tag ");
		}
	}

	public void setIssn(String issn) throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			theJournal.setIssn(issn);
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for issn tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for issn tag ");
		}
	}

}
