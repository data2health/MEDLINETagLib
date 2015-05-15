package edu.uiowa.medline.meshQualifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class MeshQualifierID extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(MeshQualifierID.class);


	public int doStartTag() throws JspException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			if (!theMeshQualifier.commitNeeded) {
				pageContext.getOut().print(theMeshQualifier.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing MeshQualifier for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for ID tag ");
		}
		return SKIP_BODY;
	}

	public String getID() throws JspTagException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			return theMeshQualifier.getID();
		} catch (Exception e) {
			log.error(" Can't find enclosing MeshQualifier for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for ID tag ");
		}
	}

	public void setID(String ID) throws JspTagException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			theMeshQualifier.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing MeshQualifier for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for ID tag ");
		}
	}

}
