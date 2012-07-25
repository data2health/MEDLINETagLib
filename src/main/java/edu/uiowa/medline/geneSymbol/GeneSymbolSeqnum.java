package edu.uiowa.medline.geneSymbol;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class GeneSymbolSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(GeneSymbolSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			GeneSymbol theGeneSymbol = (GeneSymbol)findAncestorWithClass(this, GeneSymbol.class);
			if (!theGeneSymbol.commitNeeded) {
				pageContext.getOut().print(theGeneSymbol.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing GeneSymbol for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneSymbol for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			GeneSymbol theGeneSymbol = (GeneSymbol)findAncestorWithClass(this, GeneSymbol.class);
			return theGeneSymbol.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing GeneSymbol for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneSymbol for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			GeneSymbol theGeneSymbol = (GeneSymbol)findAncestorWithClass(this, GeneSymbol.class);
			theGeneSymbol.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing GeneSymbol for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneSymbol for seqnum tag ");
		}
	}

}
