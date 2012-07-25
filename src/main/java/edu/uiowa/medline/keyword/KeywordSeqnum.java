package edu.uiowa.medline.keyword;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class KeywordSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(KeywordSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			Keyword theKeyword = (Keyword)findAncestorWithClass(this, Keyword.class);
			if (!theKeyword.commitNeeded) {
				pageContext.getOut().print(theKeyword.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Keyword for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Keyword for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			Keyword theKeyword = (Keyword)findAncestorWithClass(this, Keyword.class);
			return theKeyword.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing Keyword for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Keyword for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			Keyword theKeyword = (Keyword)findAncestorWithClass(this, Keyword.class);
			theKeyword.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing Keyword for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing Keyword for seqnum tag ");
		}
	}

}
