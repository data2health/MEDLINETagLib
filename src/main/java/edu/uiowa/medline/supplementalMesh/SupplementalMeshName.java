package edu.uiowa.medline.supplementalMesh;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class SupplementalMeshName extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(SupplementalMeshName.class);


	public int doStartTag() throws JspException {
		try {
			SupplementalMesh theSupplementalMesh = (SupplementalMesh)findAncestorWithClass(this, SupplementalMesh.class);
			if (!theSupplementalMesh.commitNeeded) {
				pageContext.getOut().print(theSupplementalMesh.getName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SupplementalMesh for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing SupplementalMesh for name tag ");
		}
		return SKIP_BODY;
	}

	public String getName() throws JspTagException {
		try {
			SupplementalMesh theSupplementalMesh = (SupplementalMesh)findAncestorWithClass(this, SupplementalMesh.class);
			return theSupplementalMesh.getName();
		} catch (Exception e) {
			log.error(" Can't find enclosing SupplementalMesh for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing SupplementalMesh for name tag ");
		}
	}

	public void setName(String name) throws JspTagException {
		try {
			SupplementalMesh theSupplementalMesh = (SupplementalMesh)findAncestorWithClass(this, SupplementalMesh.class);
			theSupplementalMesh.setName(name);
		} catch (Exception e) {
			log.error("Can't find enclosing SupplementalMesh for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing SupplementalMesh for name tag ");
		}
	}

}
