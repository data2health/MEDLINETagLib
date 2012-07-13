package edu.uiowa.medline.supplementalMesh;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class SupplementalMeshSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(SupplementalMeshSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			SupplementalMesh theSupplementalMesh = (SupplementalMesh)findAncestorWithClass(this, SupplementalMesh.class);
			if (!theSupplementalMesh.commitNeeded) {
				pageContext.getOut().print(theSupplementalMesh.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SupplementalMesh for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing SupplementalMesh for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			SupplementalMesh theSupplementalMesh = (SupplementalMesh)findAncestorWithClass(this, SupplementalMesh.class);
			return theSupplementalMesh.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing SupplementalMesh for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing SupplementalMesh for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			SupplementalMesh theSupplementalMesh = (SupplementalMesh)findAncestorWithClass(this, SupplementalMesh.class);
			theSupplementalMesh.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing SupplementalMesh for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing SupplementalMesh for seqnum tag ");
		}
	}

}
