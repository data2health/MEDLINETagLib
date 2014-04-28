package edu.uiowa.medline.clusterPattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ClusterPatternCid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ClusterPatternCid.class);


	public int doStartTag() throws JspException {
		try {
			ClusterPattern theClusterPattern = (ClusterPattern)findAncestorWithClass(this, ClusterPattern.class);
			if (!theClusterPattern.commitNeeded) {
				pageContext.getOut().print(theClusterPattern.getCid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterPattern for cid tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterPattern for cid tag ");
		}
		return SKIP_BODY;
	}

	public int getCid() throws JspTagException {
		try {
			ClusterPattern theClusterPattern = (ClusterPattern)findAncestorWithClass(this, ClusterPattern.class);
			return theClusterPattern.getCid();
		} catch (Exception e) {
			log.error(" Can't find enclosing ClusterPattern for cid tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterPattern for cid tag ");
		}
	}

	public void setCid(int cid) throws JspTagException {
		try {
			ClusterPattern theClusterPattern = (ClusterPattern)findAncestorWithClass(this, ClusterPattern.class);
			theClusterPattern.setCid(cid);
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterPattern for cid tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterPattern for cid tag ");
		}
	}

}
