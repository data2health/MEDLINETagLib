package edu.uiowa.medline.documentCluster;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class DocumentClusterCid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(DocumentClusterCid.class);


	public int doStartTag() throws JspException {
		try {
			DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
			if (!theDocumentCluster.commitNeeded) {
				pageContext.getOut().print(theDocumentCluster.getCid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing DocumentCluster for cid tag ", e);
			throw new JspTagException("Error: Can't find enclosing DocumentCluster for cid tag ");
		}
		return SKIP_BODY;
	}

	public int getCid() throws JspTagException {
		try {
			DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
			return theDocumentCluster.getCid();
		} catch (Exception e) {
			log.error(" Can't find enclosing DocumentCluster for cid tag ", e);
			throw new JspTagException("Error: Can't find enclosing DocumentCluster for cid tag ");
		}
	}

	public void setCid(int cid) throws JspTagException {
		try {
			DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
			theDocumentCluster.setCid(cid);
		} catch (Exception e) {
			log.error("Can't find enclosing DocumentCluster for cid tag ", e);
			throw new JspTagException("Error: Can't find enclosing DocumentCluster for cid tag ");
		}
	}

}
