package edu.uiowa.medline.meshQualifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class MeshQualifierQnum extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			if (!theMeshQualifier.commitNeeded) {
				pageContext.getOut().print(theMeshQualifier.getQnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for qnum tag ");
		}
		return SKIP_BODY;
	}

	public int getQnum() throws JspTagException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			return theMeshQualifier.getQnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for qnum tag ");
		}
	}

	public void setQnum(int qnum) throws JspTagException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			theMeshQualifier.setQnum(qnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for qnum tag ");
		}
	}

}
