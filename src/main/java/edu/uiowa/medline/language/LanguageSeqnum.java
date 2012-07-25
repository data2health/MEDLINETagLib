package edu.uiowa.medline.language;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class LanguageSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(LanguageSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			Language theLanguage = (Language)findAncestorWithClass(this, Language.class);
			if (!theLanguage.commitNeeded) {
				pageContext.getOut().print(theLanguage.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Language for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Language for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			Language theLanguage = (Language)findAncestorWithClass(this, Language.class);
			return theLanguage.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing Language for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Language for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			Language theLanguage = (Language)findAncestorWithClass(this, Language.class);
			theLanguage.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing Language for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Language for seqnum tag ");
		}
	}

}
