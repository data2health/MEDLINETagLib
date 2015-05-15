package edu.uiowa.medline.supplementalMesh;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class SupplementalMeshID extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(SupplementalMeshID.class);


	public int doStartTag() throws JspException {
		try {
			SupplementalMesh theSupplementalMesh = (SupplementalMesh)findAncestorWithClass(this, SupplementalMesh.class);
			if (!theSupplementalMesh.commitNeeded) {
				pageContext.getOut().print(theSupplementalMesh.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SupplementalMesh for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing SupplementalMesh for ID tag ");
		}
		return SKIP_BODY;
	}

	public String getID() throws JspTagException {
		try {
			SupplementalMesh theSupplementalMesh = (SupplementalMesh)findAncestorWithClass(this, SupplementalMesh.class);
			return theSupplementalMesh.getID();
		} catch (Exception e) {
			log.error(" Can't find enclosing SupplementalMesh for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing SupplementalMesh for ID tag ");
		}
	}

	public void setID(String ID) throws JspTagException {
		try {
			SupplementalMesh theSupplementalMesh = (SupplementalMesh)findAncestorWithClass(this, SupplementalMesh.class);
			theSupplementalMesh.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing SupplementalMesh for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing SupplementalMesh for ID tag ");
		}
	}

}
