package edu.uiowa.medline.documentCluster;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class DocumentClusterForeName extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(DocumentClusterForeName.class);


	public int doStartTag() throws JspException {
		try {
			DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
			if (!theDocumentCluster.commitNeeded) {
				pageContext.getOut().print(theDocumentCluster.getForeName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing DocumentCluster for foreName tag ", e);
			throw new JspTagException("Error: Can't find enclosing DocumentCluster for foreName tag ");
		}
		return SKIP_BODY;
	}

	public String getForeName() throws JspTagException {
		try {
			DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
			return theDocumentCluster.getForeName();
		} catch (Exception e) {
			log.error(" Can't find enclosing DocumentCluster for foreName tag ", e);
			throw new JspTagException("Error: Can't find enclosing DocumentCluster for foreName tag ");
		}
	}

	public void setForeName(String foreName) throws JspTagException {
		try {
			DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
			theDocumentCluster.setForeName(foreName);
		} catch (Exception e) {
			log.error("Can't find enclosing DocumentCluster for foreName tag ", e);
			throw new JspTagException("Error: Can't find enclosing DocumentCluster for foreName tag ");
		}
	}

}
