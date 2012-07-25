package edu.uiowa.medline.meshHeading;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class MeshHeadingSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(MeshHeadingSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			if (!theMeshHeading.commitNeeded) {
				pageContext.getOut().print(theMeshHeading.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing MeshHeading for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshHeading for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			return theMeshHeading.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing MeshHeading for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshHeading for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			MeshHeading theMeshHeading = (MeshHeading)findAncestorWithClass(this, MeshHeading.class);
			theMeshHeading.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing MeshHeading for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing MeshHeading for seqnum tag ");
		}
	}

}
