package edu.uiowa.medline.language;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class LanguageLanguage extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Language theLanguage = (Language)findAncestorWithClass(this, Language.class);
			if (!theLanguage.commitNeeded) {
				pageContext.getOut().print(theLanguage.getLanguage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Language for language tag ");
		}
		return SKIP_BODY;
	}

	public String getLanguage() throws JspTagException {
		try {
			Language theLanguage = (Language)findAncestorWithClass(this, Language.class);
			return theLanguage.getLanguage();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Language for language tag ");
		}
	}

	public void setLanguage(String language) throws JspTagException {
		try {
			Language theLanguage = (Language)findAncestorWithClass(this, Language.class);
			theLanguage.setLanguage(language);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Language for language tag ");
		}
	}

}
