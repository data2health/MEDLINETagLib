package edu.uiowa.medline.journal;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class JournalIsoAbbreviation extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(JournalIsoAbbreviation.class);


	public int doStartTag() throws JspException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			if (!theJournal.commitNeeded) {
				pageContext.getOut().print(theJournal.getIsoAbbreviation());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for isoAbbreviation tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for isoAbbreviation tag ");
		}
		return SKIP_BODY;
	}

	public String getIsoAbbreviation() throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			return theJournal.getIsoAbbreviation();
		} catch (Exception e) {
			log.error(" Can't find enclosing Journal for isoAbbreviation tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for isoAbbreviation tag ");
		}
	}

	public void setIsoAbbreviation(String isoAbbreviation) throws JspTagException {
		try {
			Journal theJournal = (Journal)findAncestorWithClass(this, Journal.class);
			theJournal.setIsoAbbreviation(isoAbbreviation);
		} catch (Exception e) {
			log.error("Can't find enclosing Journal for isoAbbreviation tag ", e);
			throw new JspTagException("Error: Can't find enclosing Journal for isoAbbreviation tag ");
		}
	}

}
