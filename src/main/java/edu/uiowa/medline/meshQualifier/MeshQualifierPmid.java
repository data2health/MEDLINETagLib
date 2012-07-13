package edu.uiowa.medline.meshQualifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class MeshQualifierPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(MeshQualifierPmid.class);


	public int doStartTag() throws JspException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			if (!theMeshQualifier.commitNeeded) {
				pageContext.getOut().print(theMeshQualifier.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing MeshQualifier for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			return theMeshQualifier.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing MeshQualifier for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			theMeshQualifier.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing MeshQualifier for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for pmid tag ");
		}
	}

}
