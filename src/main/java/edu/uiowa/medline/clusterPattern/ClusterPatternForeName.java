package edu.uiowa.medline.clusterPattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ClusterPatternForeName extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ClusterPatternForeName.class);


	public int doStartTag() throws JspException {
		try {
			ClusterPattern theClusterPattern = (ClusterPattern)findAncestorWithClass(this, ClusterPattern.class);
			if (!theClusterPattern.commitNeeded) {
				pageContext.getOut().print(theClusterPattern.getForeName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterPattern for foreName tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterPattern for foreName tag ");
		}
		return SKIP_BODY;
	}

	public String getForeName() throws JspTagException {
		try {
			ClusterPattern theClusterPattern = (ClusterPattern)findAncestorWithClass(this, ClusterPattern.class);
			return theClusterPattern.getForeName();
		} catch (Exception e) {
			log.error(" Can't find enclosing ClusterPattern for foreName tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterPattern for foreName tag ");
		}
	}

	public void setForeName(String foreName) throws JspTagException {
		try {
			ClusterPattern theClusterPattern = (ClusterPattern)findAncestorWithClass(this, ClusterPattern.class);
			theClusterPattern.setForeName(foreName);
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterPattern for foreName tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterPattern for foreName tag ");
		}
	}

}
