package edu.uiowa.medline.abstr;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AbstrPmid extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			if (!theAbstr.commitNeeded) {
				pageContext.getOut().print(theAbstr.getPmid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Abstr for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			return theAbstr.getPmid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Abstr for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			Abstr theAbstr = (Abstr)findAncestorWithClass(this, Abstr.class);
			theAbstr.setPmid(pmid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Abstr for pmid tag ");
		}
	}

}
