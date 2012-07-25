package edu.uiowa.medline.publicationType;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class PublicationTypePmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(PublicationTypePmid.class);


	public int doStartTag() throws JspException {
		try {
			PublicationType thePublicationType = (PublicationType)findAncestorWithClass(this, PublicationType.class);
			if (!thePublicationType.commitNeeded) {
				pageContext.getOut().print(thePublicationType.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PublicationType for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing PublicationType for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			PublicationType thePublicationType = (PublicationType)findAncestorWithClass(this, PublicationType.class);
			return thePublicationType.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing PublicationType for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing PublicationType for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			PublicationType thePublicationType = (PublicationType)findAncestorWithClass(this, PublicationType.class);
			thePublicationType.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing PublicationType for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing PublicationType for pmid tag ");
		}
	}

}
