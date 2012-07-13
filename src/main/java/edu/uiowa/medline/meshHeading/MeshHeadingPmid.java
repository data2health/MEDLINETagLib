package edu.uiowa.medline.meshHeading;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class MeshHeadingPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(MeshHeadingPmid.class);


	public int doStartTag() throws JspException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			if (!theMeshHeading.commitNeeded) {
				pageContext.getOut().print(theMeshHeading.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing MeshHeading for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshHeading for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			return theMeshHeading.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing MeshHeading for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshHeading for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			theMeshHeading.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing MeshHeading for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshHeading for pmid tag ");
		}
	}

}
