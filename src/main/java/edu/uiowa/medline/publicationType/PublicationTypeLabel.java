package edu.uiowa.medline.publicationType;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class PublicationTypeLabel extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			PublicationType thePublicationType = (PublicationType)findAncestorWithClass(this, PublicationType.class);
			if (!thePublicationType.commitNeeded) {
				pageContext.getOut().print(thePublicationType.getLabel());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing PublicationType for label tag ");
		}
		return SKIP_BODY;
	}

	public String getLabel() throws JspTagException {
		try {
			PublicationType thePublicationType = (PublicationType)findAncestorWithClass(this, PublicationType.class);
			return thePublicationType.getLabel();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing PublicationType for label tag ");
		}
	}

	public void setLabel(String label) throws JspTagException {
		try {
			PublicationType thePublicationType = (PublicationType)findAncestorWithClass(this, PublicationType.class);
			thePublicationType.setLabel(label);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing PublicationType for label tag ");
		}
	}

}
