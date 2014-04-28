package edu.uiowa.medline.clusterPattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ClusterPatternLastName extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ClusterPatternLastName.class);


	public int doStartTag() throws JspException {
		try {
			ClusterPattern theClusterPattern = (ClusterPattern)findAncestorWithClass(this, ClusterPattern.class);
			if (!theClusterPattern.commitNeeded) {
				pageContext.getOut().print(theClusterPattern.getLastName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterPattern for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterPattern for lastName tag ");
		}
		return SKIP_BODY;
	}

	public String getLastName() throws JspTagException {
		try {
			ClusterPattern theClusterPattern = (ClusterPattern)findAncestorWithClass(this, ClusterPattern.class);
			return theClusterPattern.getLastName();
		} catch (Exception e) {
			log.error(" Can't find enclosing ClusterPattern for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterPattern for lastName tag ");
		}
	}

	public void setLastName(String lastName) throws JspTagException {
		try {
			ClusterPattern theClusterPattern = (ClusterPattern)findAncestorWithClass(this, ClusterPattern.class);
			theClusterPattern.setLastName(lastName);
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterPattern for lastName tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterPattern for lastName tag ");
		}
	}

}
