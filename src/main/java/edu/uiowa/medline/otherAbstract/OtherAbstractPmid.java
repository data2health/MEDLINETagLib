package edu.uiowa.medline.otherAbstract;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class OtherAbstractPmid extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			OtherAbstract theOtherAbstract = (OtherAbstract)findAncestorWithClass(this, OtherAbstract.class);
			if (!theOtherAbstract.commitNeeded) {
				pageContext.getOut().print(theOtherAbstract.getPmid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstract for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			OtherAbstract theOtherAbstract = (OtherAbstract)findAncestorWithClass(this, OtherAbstract.class);
			return theOtherAbstract.getPmid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstract for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			OtherAbstract theOtherAbstract = (OtherAbstract)findAncestorWithClass(this, OtherAbstract.class);
			theOtherAbstract.setPmid(pmid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing OtherAbstract for pmid tag ");
		}
	}

}
