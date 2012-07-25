package edu.uiowa.medline.spaceflightMission;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class SpaceflightMissionPmid extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(SpaceflightMissionPmid.class);


	public int doStartTag() throws JspException {
		try {
			SpaceflightMission theSpaceflightMission = (SpaceflightMission)findAncestorWithClass(this, SpaceflightMission.class);
			if (!theSpaceflightMission.commitNeeded) {
				pageContext.getOut().print(theSpaceflightMission.getPmid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SpaceflightMission for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SpaceflightMission for pmid tag ");
		}
		return SKIP_BODY;
	}

	public int getPmid() throws JspTagException {
		try {
			SpaceflightMission theSpaceflightMission = (SpaceflightMission)findAncestorWithClass(this, SpaceflightMission.class);
			return theSpaceflightMission.getPmid();
		} catch (Exception e) {
			log.error(" Can't find enclosing SpaceflightMission for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SpaceflightMission for pmid tag ");
		}
	}

	public void setPmid(int pmid) throws JspTagException {
		try {
			SpaceflightMission theSpaceflightMission = (SpaceflightMission)findAncestorWithClass(this, SpaceflightMission.class);
			theSpaceflightMission.setPmid(pmid);
		} catch (Exception e) {
			log.error("Can't find enclosing SpaceflightMission for pmid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SpaceflightMission for pmid tag ");
		}
	}

}
