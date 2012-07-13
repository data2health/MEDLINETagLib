package edu.uiowa.medline.commentsCorrections;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class CommentsCorrectionsRefPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(CommentsCorrectionsRefPmid.class);


	public int doStartTag() throws JspException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			if (!theCommentsCorrections.commitNeeded) {
				pageContext.getOut().print(theCommentsCorrections.getRefPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing CommentsCorrections for refPmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for refPmid tag ");
		}
		return SKIP_BODY;
	}

	public int getRefPmid() throws JspTagException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			return theCommentsCorrections.getRefPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing CommentsCorrections for refPmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for refPmid tag ");
		}
	}

	public void setRefPmid(int refPmid) throws JspTagException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			theCommentsCorrections.setRefPmid(refPmid);
		} catch (Exception e) {
			log.error("Can't find enclosing CommentsCorrections for refPmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for refPmid tag ");
		}
	}

}
