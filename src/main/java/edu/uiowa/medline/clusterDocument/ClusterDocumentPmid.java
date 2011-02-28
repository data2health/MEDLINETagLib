package edu.uiowa.medline.clusterDocument;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ClusterDocumentPmid extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			ClusterDocument theClusterDocument = (ClusterDocument)findAncestorWithClass(this, ClusterDocument.class);
			if (!theClusterDocument.commitNeeded) {
				pageContext.getOut().print(theClusterDocument.getPmid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ClusterDocument for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			ClusterDocument theClusterDocument = (ClusterDocument)findAncestorWithClass(this, ClusterDocument.class);
			return theClusterDocument.getPmid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ClusterDocument for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			ClusterDocument theClusterDocument = (ClusterDocument)findAncestorWithClass(this, ClusterDocument.class);
			theClusterDocument.setPmid(pmid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ClusterDocument for pmid tag ");
		}
	}

}
