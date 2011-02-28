package edu.uiowa.medline.citationSubset;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class CitationSubsetSeqnum extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			CitationSubset theCitationSubset = (CitationSubset)findAncestorWithClass(this, CitationSubset.class);
			if (!theCitationSubset.commitNeeded) {
				pageContext.getOut().print(theCitationSubset.getSeqnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing CitationSubset for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			CitationSubset theCitationSubset = (CitationSubset)findAncestorWithClass(this, CitationSubset.class);
			return theCitationSubset.getSeqnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing CitationSubset for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			CitationSubset theCitationSubset = (CitationSubset)findAncestorWithClass(this, CitationSubset.class);
			theCitationSubset.setSeqnum(seqnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing CitationSubset for seqnum tag ");
		}
	}

}
