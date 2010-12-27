package edu.uiowa.medline.clusterDocument;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ClusterDocumentCid extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			ClusterDocument theClusterDocument = (ClusterDocument)findAncestorWithClass(this, ClusterDocument.class);
			if (!theClusterDocument.commitNeeded) {
				pageContext.getOut().print(theClusterDocument.getCid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ClusterDocument for cid tag ");
		}
		return SKIP_BODY;
	}

	public int getCid() throws JspTagException {
		try {
			ClusterDocument theClusterDocument = (ClusterDocument)findAncestorWithClass(this, ClusterDocument.class);
			return theClusterDocument.getCid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ClusterDocument for cid tag ");
		}
	}

	public void setCid(int cid) throws JspTagException {
		try {
			ClusterDocument theClusterDocument = (ClusterDocument)findAncestorWithClass(this, ClusterDocument.class);
			theClusterDocument.setCid(cid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing ClusterDocument for cid tag ");
		}
	}

}
