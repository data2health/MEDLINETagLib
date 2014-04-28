package edu.uiowa.medline.clusterAuthor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ClusterAuthorSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ClusterAuthorSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			ClusterAuthor theClusterAuthor = (ClusterAuthor)findAncestorWithClass(this, ClusterAuthor.class);
			if (!theClusterAuthor.commitNeeded) {
				pageContext.getOut().print(theClusterAuthor.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterAuthor for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterAuthor for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			ClusterAuthor theClusterAuthor = (ClusterAuthor)findAncestorWithClass(this, ClusterAuthor.class);
			return theClusterAuthor.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing ClusterAuthor for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterAuthor for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			ClusterAuthor theClusterAuthor = (ClusterAuthor)findAncestorWithClass(this, ClusterAuthor.class);
			theClusterAuthor.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterAuthor for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterAuthor for seqnum tag ");
		}
	}

}
