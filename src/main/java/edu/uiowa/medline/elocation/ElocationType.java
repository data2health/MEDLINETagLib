package edu.uiowa.medline.elocation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ElocationType extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ElocationType.class);


	public int doStartTag() throws JspException {
		try {
			Elocation theElocation = (Elocation)findAncestorWithClass(this, Elocation.class);
			if (!theElocation.commitNeeded) {
				pageContext.getOut().print(theElocation.getType());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Elocation for type tag ", e);
			throw new JspTagException("Error: Can't find enclosing Elocation for type tag ");
		}
		return SKIP_BODY;
	}

	public String getType() throws JspTagException {
		try {
			Elocation theElocation = (Elocation)findAncestorWithClass(this, Elocation.class);
			return theElocation.getType();
		} catch (Exception e) {
			log.error(" Can't find enclosing Elocation for type tag ", e);
			throw new JspTagException("Error: Can't find enclosing Elocation for type tag ");
		}
	}

	public void setType(String type) throws JspTagException {
		try {
			Elocation theElocation = (Elocation)findAncestorWithClass(this, Elocation.class);
			theElocation.setType(type);
		} catch (Exception e) {
			log.error("Can't find enclosing Elocation for type tag ", e);
			throw new JspTagException("Error: Can't find enclosing Elocation for type tag ");
		}
	}

}
