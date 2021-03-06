package edu.uiowa.medline.language;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class LanguageLanguage extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(LanguageLanguage.class);


	public int doStartTag() throws JspException {
		try {
			Language theLanguage = (Language)findAncestorWithClass(this, Language.class);
			if (!theLanguage.commitNeeded) {
				pageContext.getOut().print(theLanguage.getLanguage());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Language for language tag ", e);
			throw new JspTagException("Error: Can't find enclosing Language for language tag ");
		}
		return SKIP_BODY;
	}

	public String getLanguage() throws JspTagException {
		try {
			Language theLanguage = (Language)findAncestorWithClass(this, Language.class);
			return theLanguage.getLanguage();
		} catch (Exception e) {
			log.error(" Can't find enclosing Language for language tag ", e);
			throw new JspTagException("Error: Can't find enclosing Language for language tag ");
		}
	}

	public void setLanguage(String language) throws JspTagException {
		try {
			Language theLanguage = (Language)findAncestorWithClass(this, Language.class);
			theLanguage.setLanguage(language);
		} catch (Exception e) {
			log.error("Can't find enclosing Language for language tag ", e);
			throw new JspTagException("Error: Can't find enclosing Language for language tag ");
		}
	}

}
