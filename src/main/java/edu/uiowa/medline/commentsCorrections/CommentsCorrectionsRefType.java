package edu.uiowa.medline.commentsCorrections;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class CommentsCorrectionsRefType extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(CommentsCorrectionsRefType.class);


	public int doStartTag() throws JspException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			if (!theCommentsCorrections.commitNeeded) {
				pageContext.getOut().print(theCommentsCorrections.getRefType());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing CommentsCorrections for refType tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for refType tag ");
		}
		return SKIP_BODY;
	}

	public String getRefType() throws JspTagException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			return theCommentsCorrections.getRefType();
		} catch (Exception e) {
			log.error(" Can't find enclosing CommentsCorrections for refType tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for refType tag ");
		}
	}

	public void setRefType(String refType) throws JspTagException {
		try {
			CommentsCorrections theCommentsCorrections = (CommentsCorrections)findAncestorWithClass(this, CommentsCorrections.class);
			theCommentsCorrections.setRefType(refType);
		} catch (Exception e) {
			log.error("Can't find enclosing CommentsCorrections for refType tag ", e);
			throw new JspTagException("Error: Can't find enclosing CommentsCorrections for refType tag ");
		}
	}

}
