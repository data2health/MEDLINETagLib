package edu.uiowa.medline.geneSymbol;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class GeneSymbolPmid extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			GeneSymbol theGeneSymbol = (GeneSymbol)findAncestorWithClass(this, GeneSymbol.class);
			if (!theGeneSymbol.commitNeeded) {
				pageContext.getOut().print(theGeneSymbol.getPmid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing GeneSymbol for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			GeneSymbol theGeneSymbol = (GeneSymbol)findAncestorWithClass(this, GeneSymbol.class);
			return theGeneSymbol.getPmid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing GeneSymbol for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			GeneSymbol theGeneSymbol = (GeneSymbol)findAncestorWithClass(this, GeneSymbol.class);
			theGeneSymbol.setPmid(pmid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing GeneSymbol for pmid tag ");
		}
	}

}
