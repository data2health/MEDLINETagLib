package edu.uiowa.medline.affiliation;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AffiliationSource extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AffiliationSource.class);


	public int doStartTag() throws JspException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			if (!theAffiliation.commitNeeded) {
				pageContext.getOut().print(theAffiliation.getSource());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Affiliation for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for source tag ");
		}
		return SKIP_BODY;
	}

	public String getSource() throws JspTagException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			return theAffiliation.getSource();
		} catch (Exception e) {
			log.error(" Can't find enclosing Affiliation for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for source tag ");
		}
	}

	public void setSource(String source) throws JspTagException {
		try {
			Affiliation theAffiliation = (Affiliation)findAncestorWithClass(this, Affiliation.class);
			theAffiliation.setSource(source);
		} catch (Exception e) {
			log.error("Can't find enclosing Affiliation for source tag ", e);
			throw new JspTagException("Error: Can't find enclosing Affiliation for source tag ");
		}
	}

}
