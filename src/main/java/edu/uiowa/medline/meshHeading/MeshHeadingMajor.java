package edu.uiowa.medline.meshHeading;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class MeshHeadingMajor extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(MeshHeadingMajor.class);


	public int doStartTag() throws JspException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			if (!theMeshHeading.commitNeeded) {
				pageContext.getOut().print(theMeshHeading.getMajor());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing MeshHeading for major tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshHeading for major tag ");
		}
		return SKIP_BODY;
	}

	public boolean getMajor() throws JspTagException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			return theMeshHeading.getMajor();
		} catch (Exception e) {
			log.error(" Can't find enclosing MeshHeading for major tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshHeading for major tag ");
		}
	}

	public void setMajor(boolean major) throws JspTagException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			theMeshHeading.setMajor(major);
		} catch (Exception e) {
			log.error("Can't find enclosing MeshHeading for major tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshHeading for major tag ");
		}
	}

}
