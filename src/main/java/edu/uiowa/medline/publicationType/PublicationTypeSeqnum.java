package edu.uiowa.medline.publicationType;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class PublicationTypeSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(PublicationTypeSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			PublicationType thePublicationType = (PublicationType)findAncestorWithClass(this, PublicationType.class);
			if (!thePublicationType.commitNeeded) {
				pageContext.getOut().print(thePublicationType.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing PublicationType for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing PublicationType for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			PublicationType thePublicationType = (PublicationType)findAncestorWithClass(this, PublicationType.class);
			return thePublicationType.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing PublicationType for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing PublicationType for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			PublicationType thePublicationType = (PublicationType)findAncestorWithClass(this, PublicationType.class);
			thePublicationType.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing PublicationType for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing PublicationType for seqnum tag ");
		}
	}

}
