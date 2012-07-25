package edu.uiowa.medline.meshQualifier;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class MeshQualifierSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(MeshQualifierSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			if (!theMeshQualifier.commitNeeded) {
				pageContext.getOut().print(theMeshQualifier.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing MeshQualifier for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			return theMeshQualifier.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing MeshQualifier for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			MeshQualifier theMeshQualifier = (MeshQualifier)findAncestorWithClass(this, MeshQualifier.class);
			theMeshQualifier.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing MeshQualifier for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshQualifier for seqnum tag ");
		}
	}

}
