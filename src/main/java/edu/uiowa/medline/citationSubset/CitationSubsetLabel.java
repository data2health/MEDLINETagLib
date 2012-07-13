package edu.uiowa.medline.citationSubset;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class CitationSubsetLabel extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(CitationSubsetLabel.class);


	public int doStartTag() throws JspException {
		try {
			CitationSubset theCitationSubset = (CitationSubset)findAncestorWithClass(this, CitationSubset.class);
			if (!theCitationSubset.commitNeeded) {
				pageContext.getOut().print(theCitationSubset.getLabel());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing CitationSubset for label tag ", e);
			throw new JspTagException("Error: Can't find enclosing CitationSubset for label tag ");
		}
		return SKIP_BODY;
	}

	public String getLabel() throws JspTagException {
		try {
			CitationSubset theCitationSubset = (CitationSubset)findAncestorWithClass(this, CitationSubset.class);
			return theCitationSubset.getLabel();
		} catch (Exception e) {
			log.error(" Can't find enclosing CitationSubset for label tag ", e);
			throw new JspTagException("Error: Can't find enclosing CitationSubset for label tag ");
		}
	}

	public void setLabel(String label) throws JspTagException {
		try {
			CitationSubset theCitationSubset = (CitationSubset)findAncestorWithClass(this, CitationSubset.class);
			theCitationSubset.setLabel(label);
		} catch (Exception e) {
			log.error("Can't find enclosing CitationSubset for label tag ", e);
			throw new JspTagException("Error: Can't find enclosing CitationSubset for label tag ");
		}
	}

}
