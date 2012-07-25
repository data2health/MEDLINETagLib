package edu.uiowa.medline.articleDate;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class ArticleDateSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(ArticleDateSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			if (!theArticleDate.commitNeeded) {
				pageContext.getOut().print(theArticleDate.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing ArticleDate for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing ArticleDate for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			return theArticleDate.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing ArticleDate for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing ArticleDate for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			ArticleDate theArticleDate = (ArticleDate)findAncestorWithClass(this, ArticleDate.class);
			theArticleDate.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing ArticleDate for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing ArticleDate for seqnum tag ");
		}
	}

}
