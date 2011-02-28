package edu.uiowa.medline.meshHeading;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class MeshHeadingDescriptorName extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			if (!theMeshHeading.commitNeeded) {
				pageContext.getOut().print(theMeshHeading.getDescriptorName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing MeshHeading for descriptorName tag ");
		}
		return SKIP_BODY;
	}

	public String getDescriptorName() throws JspTagException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			return theMeshHeading.getDescriptorName();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing MeshHeading for descriptorName tag ");
		}
	}

	public void setDescriptorName(String descriptorName) throws JspTagException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			theMeshHeading.setDescriptorName(descriptorName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing MeshHeading for descriptorName tag ");
		}
	}

}
