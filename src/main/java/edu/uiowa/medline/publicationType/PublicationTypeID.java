package edu.uiowa.medline.publicationType;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class PublicationTypeID extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(PublicationTypeID.class);


	public int doStartTag() throws JspException {
		try {
			PublicationType thePublicationType = (PublicationType)findAncestorWithClass(this, PublicationType.class);
			if (!thePublicationType.commitNeeded) {
				pageContext.getOut().print(thePublicationType.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PublicationType for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing PublicationType for ID tag ");
		}
		return SKIP_BODY;
	}

	public String getID() throws JspTagException {
		try {
			PublicationType thePublicationType = (PublicationType)findAncestorWithClass(this, PublicationType.class);
			return thePublicationType.getID();
		} catch (Exception e) {
			log.error(" Can't find enclosing PublicationType for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing PublicationType for ID tag ");
		}
	}

	public void setID(String ID) throws JspTagException {
		try {
			PublicationType thePublicationType = (PublicationType)findAncestorWithClass(this, PublicationType.class);
			thePublicationType.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing PublicationType for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing PublicationType for ID tag ");
		}
	}

}
