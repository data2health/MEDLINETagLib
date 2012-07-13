package edu.uiowa.medline.commentsCorrections;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class CommentsCorrectionsSource extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(CommentsCorrectionsSource.class);


	public int doStartTag() throws JspException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			if (!theCommentsCorrections.commitNeeded) {
				pageContext.getOut().print(theCommentsCorrections.getSource());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing CommentsCorrections for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for source tag ");
		}
		return SKIP_BODY;
	}

	public String getSource() throws JspTagException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			return theCommentsCorrections.getSource();
		} catch (Exception e) {
			log.error(" Can't find enclosing CommentsCorrections for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for source tag ");
		}
	}

	public void setSource(String source) throws JspTagException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			theCommentsCorrections.setSource(source);
		} catch (Exception e) {
			log.error("Can't find enclosing CommentsCorrections for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for source tag ");
		}
	}

}
