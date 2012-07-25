package edu.uiowa.medline.author;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class AuthorCollectiveName extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(AuthorCollectiveName.class);


	public int doStartTag() throws JspException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			if (!theAuthor.commitNeeded) {
				pageContext.getOut().print(theAuthor.getCollectiveName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Author for collectiveName tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for collectiveName tag ");
		}
		return SKIP_BODY;
	}

	public String getCollectiveName() throws JspTagException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			return theAuthor.getCollectiveName();
		} catch (Exception e) {
			log.error(" Can't find enclosing Author for collectiveName tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for collectiveName tag ");
		}
	}

	public void setCollectiveName(String collectiveName) throws JspTagException {
		try {
			Author theAuthor = (Author)findAncestorWithClass(this, Author.class);
			theAuthor.setCollectiveName(collectiveName);
		} catch (Exception e) {
			log.error("Can't find enclosing Author for collectiveName tag ", e);
			throw new JspTagException("Error: Can't find enclosing Author for collectiveName tag ");
		}
	}

}
