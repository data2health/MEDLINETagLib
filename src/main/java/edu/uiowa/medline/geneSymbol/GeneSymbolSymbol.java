package edu.uiowa.medline.geneSymbol;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class GeneSymbolSymbol extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(GeneSymbolSymbol.class);


	public int doStartTag() throws JspException {
		try {
			GeneSymbol theGeneSymbol = (GeneSymbol)findAncestorWithClass(this, GeneSymbol.class);
			if (!theGeneSymbol.commitNeeded) {
				pageContext.getOut().print(theGeneSymbol.getSymbol());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing GeneSymbol for symbol tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneSymbol for symbol tag ");
		}
		return SKIP_BODY;
	}

	public String getSymbol() throws JspTagException {
		try {
			GeneSymbol theGeneSymbol = (GeneSymbol)findAncestorWithClass(this, GeneSymbol.class);
			return theGeneSymbol.getSymbol();
		} catch (Exception e) {
			log.error(" Can't find enclosing GeneSymbol for symbol tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneSymbol for symbol tag ");
		}
	}

	public void setSymbol(String symbol) throws JspTagException {
		try {
			GeneSymbol theGeneSymbol = (GeneSymbol)findAncestorWithClass(this, GeneSymbol.class);
			theGeneSymbol.setSymbol(symbol);
		} catch (Exception e) {
			log.error("Can't find enclosing GeneSymbol for symbol tag ", e);
			throw new JspTagException("Error: Can't find enclosing GeneSymbol for symbol tag ");
		}
	}

}
