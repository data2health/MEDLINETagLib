package edu.uiowa.medline.otherId;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class OtherIdSource extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(OtherIdSource.class);


	public int doStartTag() throws JspException {
		try {
			OtherId theOtherId = (OtherId)findAncestorWithClass(this, OtherId.class);
			if (!theOtherId.commitNeeded) {
				pageContext.getOut().print(theOtherId.getSource());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OtherId for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherId for source tag ");
		}
		return SKIP_BODY;
	}

	public String getSource() throws JspTagException {
		try {
			OtherId theOtherId = (OtherId)findAncestorWithClass(this, OtherId.class);
			return theOtherId.getSource();
		} catch (Exception e) {
			log.error(" Can't find enclosing OtherId for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherId for source tag ");
		}
	}

	public void setSource(String source) throws JspTagException {
		try {
			OtherId theOtherId = (OtherId)findAncestorWithClass(this, OtherId.class);
			theOtherId.setSource(source);
		} catch (Exception e) {
			log.error("Can't find enclosing OtherId for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherId for source tag ");
		}
	}

}
