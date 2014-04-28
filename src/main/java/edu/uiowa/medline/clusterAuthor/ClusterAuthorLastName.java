package edu.uiowa.medline.clusterAuthor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ClusterAuthorLastName extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ClusterAuthorLastName.class);


	public int doStartTag() throws JspException {
		try {
			ClusterAuthor theClusterAuthor = (ClusterAuthor)findAncestorWithClass(this, ClusterAuthor.class);
			if (!theClusterAuthor.commitNeeded) {
				pageContext.getOut().print(theClusterAuthor.getLastName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterAuthor for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterAuthor for lastName tag ");
		}
		return SKIP_BODY;
	}

	public String getLastName() throws JspTagException {
		try {
			ClusterAuthor theClusterAuthor = (ClusterAuthor)findAncestorWithClass(this, ClusterAuthor.class);
			return theClusterAuthor.getLastName();
		} catch (Exception e) {
			log.error(" Can't find enclosing ClusterAuthor for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterAuthor for lastName tag ");
		}
	}

	public void setLastName(String lastName) throws JspTagException {
		try {
			ClusterAuthor theClusterAuthor = (ClusterAuthor)findAncestorWithClass(this, ClusterAuthor.class);
			theClusterAuthor.setLastName(lastName);
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterAuthor for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterAuthor for lastName tag ");
		}
	}

}
