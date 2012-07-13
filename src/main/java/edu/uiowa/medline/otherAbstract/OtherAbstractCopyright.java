package edu.uiowa.medline.otherAbstract;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class OtherAbstractCopyright extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(OtherAbstractCopyright.class);


	public int doStartTag() throws JspException {
		try {
			OtherAbstract theOtherAbstract = (OtherAbstract)findAncestorWithClass(this, OtherAbstract.class);
			if (!theOtherAbstract.commitNeeded) {
				pageContext.getOut().print(theOtherAbstract.getCopyright());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OtherAbstract for copyright tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstract for copyright tag ");
		}
		return SKIP_BODY;
	}

	public String getCopyright() throws JspTagException {
		try {
			OtherAbstract theOtherAbstract = (OtherAbstract)findAncestorWithClass(this, OtherAbstract.class);
			return theOtherAbstract.getCopyright();
		} catch (Exception e) {
			log.error(" Can't find enclosing OtherAbstract for copyright tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstract for copyright tag ");
		}
	}

	public void setCopyright(String copyright) throws JspTagException {
		try {
			OtherAbstract theOtherAbstract = (OtherAbstract)findAncestorWithClass(this, OtherAbstract.class);
			theOtherAbstract.setCopyright(copyright);
		} catch (Exception e) {
			log.error("Can't find enclosing OtherAbstract for copyright tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherAbstract for copyright tag ");
		}
	}

}
