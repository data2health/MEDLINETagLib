package edu.uiowa.medline.clusterDocument;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ClusterDocumentPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ClusterDocumentPmid.class);


	public int doStartTag() throws JspException {
		try {
			ClusterDocument theClusterDocument = (ClusterDocument)findAncestorWithClass(this, ClusterDocument.class);
			if (!theClusterDocument.commitNeeded) {
				pageContext.getOut().print(theClusterDocument.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterDocument for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterDocument for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			ClusterDocument theClusterDocument = (ClusterDocument)findAncestorWithClass(this, ClusterDocument.class);
			return theClusterDocument.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing ClusterDocument for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterDocument for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			ClusterDocument theClusterDocument = (ClusterDocument)findAncestorWithClass(this, ClusterDocument.class);
			theClusterDocument.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterDocument for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterDocument for pmid tag ");
		}
	}

}
