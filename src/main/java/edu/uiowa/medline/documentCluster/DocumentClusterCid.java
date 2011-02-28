package edu.uiowa.medline.documentCluster;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class DocumentClusterCid extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
			if (!theDocumentCluster.commitNeeded) {
				pageContext.getOut().print(theDocumentCluster.getCid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing DocumentCluster for cid tag ");
		}
		return SKIP_BODY;
	}

	public int getCid() throws JspTagException {
		try {
			DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
			return theDocumentCluster.getCid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing DocumentCluster for cid tag ");
		}
	}

	public void setCid(int cid) throws JspTagException {
		try {
			DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
			theDocumentCluster.setCid(cid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing DocumentCluster for cid tag ");
		}
	}

}
