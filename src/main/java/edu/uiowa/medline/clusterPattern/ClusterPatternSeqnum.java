package edu.uiowa.medline.clusterPattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ClusterPatternSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ClusterPatternSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			ClusterPattern theClusterPattern = (ClusterPattern)findAncestorWithClass(this, ClusterPattern.class);
			if (!theClusterPattern.commitNeeded) {
				pageContext.getOut().print(theClusterPattern.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterPattern for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterPattern for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			ClusterPattern theClusterPattern = (ClusterPattern)findAncestorWithClass(this, ClusterPattern.class);
			return theClusterPattern.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing ClusterPattern for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterPattern for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			ClusterPattern theClusterPattern = (ClusterPattern)findAncestorWithClass(this, ClusterPattern.class);
			theClusterPattern.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterPattern for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterPattern for seqnum tag ");
		}
	}

}
