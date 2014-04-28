package edu.uiowa.medline.clusterPattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ClusterPatternOccurrences extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ClusterPatternOccurrences.class);


	public int doStartTag() throws JspException {
		try {
			ClusterPattern theClusterPattern = (ClusterPattern)findAncestorWithClass(this, ClusterPattern.class);
			if (!theClusterPattern.commitNeeded) {
				pageContext.getOut().print(theClusterPattern.getOccurrences());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterPattern for occurrences tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterPattern for occurrences tag ");
		}
		return SKIP_BODY;
	}

	public int getOccurrences() throws JspTagException {
		try {
			ClusterPattern theClusterPattern = (ClusterPattern)findAncestorWithClass(this, ClusterPattern.class);
			return theClusterPattern.getOccurrences();
		} catch (Exception e) {
			log.error(" Can't find enclosing ClusterPattern for occurrences tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterPattern for occurrences tag ");
		}
	}

	public void setOccurrences(int occurrences) throws JspTagException {
		try {
			ClusterPattern theClusterPattern = (ClusterPattern)findAncestorWithClass(this, ClusterPattern.class);
			theClusterPattern.setOccurrences(occurrences);
		} catch (Exception e) {
			log.error("Can't find enclosing ClusterPattern for occurrences tag ", e);
			throw new JspTagException("Error: Can't find enclosing ClusterPattern for occurrences tag ");
		}
	}

}
