package edu.uiowa.medline.supplementalMesh;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class SupplementalMeshPmid extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			SupplementalMesh theSupplementalMesh = (SupplementalMesh)findAncestorWithClass(this, SupplementalMesh.class);
			if (!theSupplementalMesh.commitNeeded) {
				pageContext.getOut().print(theSupplementalMesh.getPmid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing SupplementalMesh for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			SupplementalMesh theSupplementalMesh = (SupplementalMesh)findAncestorWithClass(this, SupplementalMesh.class);
			return theSupplementalMesh.getPmid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing SupplementalMesh for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			SupplementalMesh theSupplementalMesh = (SupplementalMesh)findAncestorWithClass(this, SupplementalMesh.class);
			theSupplementalMesh.setPmid(pmid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing SupplementalMesh for pmid tag ");
		}
	}

}
