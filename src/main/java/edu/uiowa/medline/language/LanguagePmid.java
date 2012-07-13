package edu.uiowa.medline.language;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class LanguagePmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(LanguagePmid.class);


	public int doStartTag() throws JspException {
		try {
			Language theLanguage = (Language)findAncestorWithClass(this, Language.class);
			if (!theLanguage.commitNeeded) {
				pageContext.getOut().print(theLanguage.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Language for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Language for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			Language theLanguage = (Language)findAncestorWithClass(this, Language.class);
			return theLanguage.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing Language for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Language for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			Language theLanguage = (Language)findAncestorWithClass(this, Language.class);
			theLanguage.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing Language for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Language for pmid tag ");
		}
	}

}
