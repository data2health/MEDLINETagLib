package edu.uiowa.medline.elocation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ElocationPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ElocationPmid.class);


	public int doStartTag() throws JspException {
		try {
			Elocation theElocation = (Elocation)findAncestorWithClass(this, Elocation.class);
			if (!theElocation.commitNeeded) {
				pageContext.getOut().print(theElocation.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Elocation for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Elocation for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			Elocation theElocation = (Elocation)findAncestorWithClass(this, Elocation.class);
			return theElocation.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing Elocation for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Elocation for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			Elocation theElocation = (Elocation)findAncestorWithClass(this, Elocation.class);
			theElocation.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing Elocation for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Elocation for pmid tag ");
		}
	}

}
