package edu.uiowa.medline.citationSubset;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class CitationSubsetLabel extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			CitationSubset theCitationSubset = (CitationSubset)findAncestorWithClass(this, CitationSubset.class);
			if (!theCitationSubset.commitNeeded) {
				pageContext.getOut().print(theCitationSubset.getLabel());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing CitationSubset for label tag ");
		}
		return SKIP_BODY;
	}

	public String getLabel() throws JspTagException {
		try {
			CitationSubset theCitationSubset = (CitationSubset)findAncestorWithClass(this, CitationSubset.class);
			return theCitationSubset.getLabel();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing CitationSubset for label tag ");
		}
	}

	public void setLabel(String label) throws JspTagException {
		try {
			CitationSubset theCitationSubset = (CitationSubset)findAncestorWithClass(this, CitationSubset.class);
			theCitationSubset.setLabel(label);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing CitationSubset for label tag ");
		}
	}

}
