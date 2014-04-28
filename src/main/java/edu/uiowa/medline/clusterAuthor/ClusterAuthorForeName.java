package edu.uiowa.medline.clusterAuthor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ClusterAuthorForeName extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ClusterAuthorForeName.class);


	public int doStartTag() throws JspException {
		try {
			ClusterAuthor theClusterAuthor = (ClusterAuthor)findAncestorWithClass(this, ClusterAuthor.class);
			if (!theClusterAuthor.commitNeeded) {
				pageContext.getOut().print(theClusterAuthor.getForeName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterAuthor for foreName tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterAuthor for foreName tag ");
		}
		return SKIP_BODY;
	}

	public String getForeName() throws JspTagException {
		try {
			ClusterAuthor theClusterAuthor = (ClusterAuthor)findAncestorWithClass(this, ClusterAuthor.class);
			return theClusterAuthor.getForeName();
		} catch (Exception e) {
			log.error(" Can't find enclosing ClusterAuthor for foreName tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterAuthor for foreName tag ");
		}
	}

	public void setForeName(String foreName) throws JspTagException {
		try {
			ClusterAuthor theClusterAuthor = (ClusterAuthor)findAncestorWithClass(this, ClusterAuthor.class);
			theClusterAuthor.setForeName(foreName);
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterAuthor for foreName tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterAuthor for foreName tag ");
		}
	}

}
