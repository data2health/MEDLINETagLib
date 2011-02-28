package edu.uiowa.medline.supplementalMesh;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class SupplementalMeshType extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			SupplementalMesh theSupplementalMesh = (SupplementalMesh)findAncestorWithClass(this, SupplementalMesh.class);
			if (!theSupplementalMesh.commitNeeded) {
				pageContext.getOut().print(theSupplementalMesh.getType());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing SupplementalMesh for type tag ");
		}
		return SKIP_BODY;
	}

	public String getType() throws JspTagException {
		try {
			SupplementalMesh theSupplementalMesh = (SupplementalMesh)findAncestorWithClass(this, SupplementalMesh.class);
			return theSupplementalMesh.getType();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing SupplementalMesh for type tag ");
		}
	}

	public void setType(String type) throws JspTagException {
		try {
			SupplementalMesh theSupplementalMesh = (SupplementalMesh)findAncestorWithClass(this, SupplementalMesh.class);
			theSupplementalMesh.setType(type);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing SupplementalMesh for type tag ");
		}
	}

}
