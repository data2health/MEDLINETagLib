package edu.uiowa.medline.commentsCorrections;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class CommentsCorrectionsNote extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(CommentsCorrectionsNote.class);


	public int doStartTag() throws JspException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			if (!theCommentsCorrections.commitNeeded) {
				pageContext.getOut().print(theCommentsCorrections.getNote());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing CommentsCorrections for note tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for note tag ");
		}
		return SKIP_BODY;
	}

	public String getNote() throws JspTagException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			return theCommentsCorrections.getNote();
		} catch (Exception e) {
			log.error(" Can't find enclosing CommentsCorrections for note tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for note tag ");
		}
	}

	public void setNote(String note) throws JspTagException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			theCommentsCorrections.setNote(note);
		} catch (Exception e) {
			log.error("Can't find enclosing CommentsCorrections for note tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for note tag ");
		}
	}

}
