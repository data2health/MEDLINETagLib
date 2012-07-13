package edu.uiowa.medline.commentsCorrections;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class CommentsCorrectionsSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(CommentsCorrectionsSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			if (!theCommentsCorrections.commitNeeded) {
				pageContext.getOut().print(theCommentsCorrections.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing CommentsCorrections for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			return theCommentsCorrections.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing CommentsCorrections for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			theCommentsCorrections.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing CommentsCorrections for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for seqnum tag ");
		}
	}

}
