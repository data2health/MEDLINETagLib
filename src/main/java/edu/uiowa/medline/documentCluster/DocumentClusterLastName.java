package edu.uiowa.medline.documentCluster;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class DocumentClusterLastName extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(DocumentClusterLastName.class);


	public int doStartTag() throws JspException {
		try {
			DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
			if (!theDocumentCluster.commitNeeded) {
				pageContext.getOut().print(theDocumentCluster.getLastName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing DocumentCluster for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing DocumentCluster for lastName tag ");
		}
		return SKIP_BODY;
	}

	public String getLastName() throws JspTagException {
		try {
			DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
			return theDocumentCluster.getLastName();
		} catch (Exception e) {
			log.error(" Can't find enclosing DocumentCluster for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing DocumentCluster for lastName tag ");
		}
	}

	public void setLastName(String lastName) throws JspTagException {
		try {
			DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
			theDocumentCluster.setLastName(lastName);
		} catch (Exception e) {
			log.error("Can't find enclosing DocumentCluster for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing DocumentCluster for lastName tag ");
		}
	}

}
