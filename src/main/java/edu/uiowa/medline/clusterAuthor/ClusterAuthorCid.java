package edu.uiowa.medline.clusterAuthor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ClusterAuthorCid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ClusterAuthorCid.class);


	public int doStartTag() throws JspException {
		try {
			ClusterAuthor theClusterAuthor = (ClusterAuthor)findAncestorWithClass(this, ClusterAuthor.class);
			if (!theClusterAuthor.commitNeeded) {
				pageContext.getOut().print(theClusterAuthor.getCid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterAuthor for cid tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterAuthor for cid tag ");
		}
		return SKIP_BODY;
	}

	public int getCid() throws JspTagException {
		try {
			ClusterAuthor theClusterAuthor = (ClusterAuthor)findAncestorWithClass(this, ClusterAuthor.class);
			return theClusterAuthor.getCid();
		} catch (Exception e) {
			log.error(" Can't find enclosing ClusterAuthor for cid tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterAuthor for cid tag ");
		}
	}

	public void setCid(int cid) throws JspTagException {
		try {
			ClusterAuthor theClusterAuthor = (ClusterAuthor)findAncestorWithClass(this, ClusterAuthor.class);
			theClusterAuthor.setCid(cid);
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterAuthor for cid tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterAuthor for cid tag ");
		}
	}

}
