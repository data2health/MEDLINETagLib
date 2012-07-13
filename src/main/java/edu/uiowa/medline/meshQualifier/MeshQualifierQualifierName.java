package edu.uiowa.medline.meshQualifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class MeshQualifierQualifierName extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(MeshQualifierQualifierName.class);


	public int doStartTag() throws JspException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			if (!theMeshQualifier.commitNeeded) {
				pageContext.getOut().print(theMeshQualifier.getQualifierName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing MeshQualifier for qualifierName tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for qualifierName tag ");
		}
		return SKIP_BODY;
	}

	public String getQualifierName() throws JspTagException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			return theMeshQualifier.getQualifierName();
		} catch (Exception e) {
			log.error(" Can't find enclosing MeshQualifier for qualifierName tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for qualifierName tag ");
		}
	}

	public void setQualifierName(String qualifierName) throws JspTagException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			theMeshQualifier.setQualifierName(qualifierName);
		} catch (Exception e) {
			log.error("Can't find enclosing MeshQualifier for qualifierName tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for qualifierName tag ");
		}
	}

}
