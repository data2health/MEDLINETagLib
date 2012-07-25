package edu.uiowa.medline.meshQualifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class MeshQualifierMajor extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(MeshQualifierMajor.class);


	public int doStartTag() throws JspException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			if (!theMeshQualifier.commitNeeded) {
				pageContext.getOut().print(theMeshQualifier.getMajor());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing MeshQualifier for major tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for major tag ");
		}
		return SKIP_BODY;
	}

	public boolean getMajor() throws JspTagException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			return theMeshQualifier.getMajor();
		} catch (Exception e) {
			log.error(" Can't find enclosing MeshQualifier for major tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for major tag ");
		}
	}

	public void setMajor(boolean major) throws JspTagException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			theMeshQualifier.setMajor(major);
		} catch (Exception e) {
			log.error("Can't find enclosing MeshQualifier for major tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for major tag ");
		}
	}

}
