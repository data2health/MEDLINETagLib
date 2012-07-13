package edu.uiowa.medline.commentsCorrections;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class CommentsCorrectionsPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(CommentsCorrectionsPmid.class);


	public int doStartTag() throws JspException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			if (!theCommentsCorrections.commitNeeded) {
				pageContext.getOut().print(theCommentsCorrections.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing CommentsCorrections for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			return theCommentsCorrections.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing CommentsCorrections for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			theCommentsCorrections.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing CommentsCorrections for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for pmid tag ");
		}
	}

}
