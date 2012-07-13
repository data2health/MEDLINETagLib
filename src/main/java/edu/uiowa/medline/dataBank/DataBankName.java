package edu.uiowa.medline.dataBank;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class DataBankName extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(DataBankName.class);


	public int doStartTag() throws JspException {
		try {
			DataBank theDataBank = (DataBank)findAncestorWithClass(this, DataBank.class);
			if (!theDataBank.commitNeeded) {
				pageContext.getOut().print(theDataBank.getName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing DataBank for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing DataBank for name tag ");
		}
		return SKIP_BODY;
	}

	public String getName() throws JspTagException {
		try {
			DataBank theDataBank = (DataBank)findAncestorWithClass(this, DataBank.class);
			return theDataBank.getName();
		} catch (Exception e) {
			log.error(" Can't find enclosing DataBank for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing DataBank for name tag ");
		}
	}

	public void setName(String name) throws JspTagException {
		try {
			DataBank theDataBank = (DataBank)findAncestorWithClass(this, DataBank.class);
			theDataBank.setName(name);
		} catch (Exception e) {
			log.error("Can't find enclosing DataBank for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing DataBank for name tag ");
		}
	}

}
