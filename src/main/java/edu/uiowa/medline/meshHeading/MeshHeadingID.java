package edu.uiowa.medline.meshHeading;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class MeshHeadingID extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(MeshHeadingID.class);


	public int doStartTag() throws JspException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			if (!theMeshHeading.commitNeeded) {
				pageContext.getOut().print(theMeshHeading.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing MeshHeading for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshHeading for ID tag ");
		}
		return SKIP_BODY;
	}

	public String getID() throws JspTagException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			return theMeshHeading.getID();
		} catch (Exception e) {
			log.error(" Can't find enclosing MeshHeading for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshHeading for ID tag ");
		}
	}

	public void setID(String ID) throws JspTagException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			theMeshHeading.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing MeshHeading for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshHeading for ID tag ");
		}
	}

}
