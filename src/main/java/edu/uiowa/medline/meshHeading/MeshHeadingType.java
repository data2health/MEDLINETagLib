package edu.uiowa.medline.meshHeading;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class MeshHeadingType extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(MeshHeadingType.class);


	public int doStartTag() throws JspException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			if (!theMeshHeading.commitNeeded) {
				pageContext.getOut().print(theMeshHeading.getType());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing MeshHeading for type tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshHeading for type tag ");
		}
		return SKIP_BODY;
	}

	public String getType() throws JspTagException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			return theMeshHeading.getType();
		} catch (Exception e) {
			log.error(" Can't find enclosing MeshHeading for type tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshHeading for type tag ");
		}
	}

	public void setType(String type) throws JspTagException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			theMeshHeading.setType(type);
		} catch (Exception e) {
			log.error("Can't find enclosing MeshHeading for type tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshHeading for type tag ");
		}
	}

}
