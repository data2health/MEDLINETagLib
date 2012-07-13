package edu.uiowa.medline.geneSymbol;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class GeneSymbolPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(GeneSymbolPmid.class);


	public int doStartTag() throws JspException {
		try {
			GeneSymbol theGeneSymbol = (GeneSymbol)findAncestorWithClass(this, GeneSymbol.class);
			if (!theGeneSymbol.commitNeeded) {
				pageContext.getOut().print(theGeneSymbol.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing GeneSymbol for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneSymbol for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			GeneSymbol theGeneSymbol = (GeneSymbol)findAncestorWithClass(this, GeneSymbol.class);
			return theGeneSymbol.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing GeneSymbol for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneSymbol for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			GeneSymbol theGeneSymbol = (GeneSymbol)findAncestorWithClass(this, GeneSymbol.class);
			theGeneSymbol.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing GeneSymbol for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneSymbol for pmid tag ");
		}
	}

}
