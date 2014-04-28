package edu.uiowa.medline.clusterAuthor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ClusterAuthorOccurrences extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ClusterAuthorOccurrences.class);


	public int doStartTag() throws JspException {
		try {
			ClusterAuthor theClusterAuthor = (ClusterAuthor)findAncestorWithClass(this, ClusterAuthor.class);
			if (!theClusterAuthor.commitNeeded) {
				pageContext.getOut().print(theClusterAuthor.getOccurrences());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterAuthor for occurrences tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterAuthor for occurrences tag ");
		}
		return SKIP_BODY;
	}

	public int getOccurrences() throws JspTagException {
		try {
			ClusterAuthor theClusterAuthor = (ClusterAuthor)findAncestorWithClass(this, ClusterAuthor.class);
			return theClusterAuthor.getOccurrences();
		} catch (Exception e) {
			log.error(" Can't find enclosing ClusterAuthor for occurrences tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterAuthor for occurrences tag ");
		}
	}

	public void setOccurrences(int occurrences) throws JspTagException {
		try {
			ClusterAuthor theClusterAuthor = (ClusterAuthor)findAncestorWithClass(this, ClusterAuthor.class);
			theClusterAuthor.setOccurrences(occurrences);
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterAuthor for occurrences tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterAuthor for occurrences tag ");
		}
	}

}
