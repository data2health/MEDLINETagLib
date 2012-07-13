package edu.uiowa.medline.citationSubset;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class CitationSubsetPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(CitationSubsetPmid.class);


	public int doStartTag() throws JspException {
		try {
			CitationSubset theCitationSubset = (CitationSubset)findAncestorWithClass(this, CitationSubset.class);
			if (!theCitationSubset.commitNeeded) {
				pageContext.getOut().print(theCitationSubset.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing CitationSubset for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing CitationSubset for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			CitationSubset theCitationSubset = (CitationSubset)findAncestorWithClass(this, CitationSubset.class);
			return theCitationSubset.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing CitationSubset for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing CitationSubset for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			CitationSubset theCitationSubset = (CitationSubset)findAncestorWithClass(this, CitationSubset.class);
			theCitationSubset.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing CitationSubset for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing CitationSubset for pmid tag ");
		}
	}

}
