package edu.uiowa.medline.spaceflightMission;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class SpaceflightMissionMission extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(SpaceflightMissionMission.class);


	public int doStartTag() throws JspException {
		try {
			SpaceflightMission theSpaceflightMission = (SpaceflightMission)findAncestorWithClass(this, SpaceflightMission.class);
			if (!theSpaceflightMission.commitNeeded) {
				pageContext.getOut().print(theSpaceflightMission.getMission());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SpaceflightMission for mission tag ", e);
			throw new JspTagException("Error: Can't find enclosing SpaceflightMission for mission tag ");
		}
		return SKIP_BODY;
	}

	public String getMission() throws JspTagException {
		try {
			SpaceflightMission theSpaceflightMission = (SpaceflightMission)findAncestorWithClass(this, SpaceflightMission.class);
			return theSpaceflightMission.getMission();
		} catch (Exception e) {
			log.error(" Can't find enclosing SpaceflightMission for mission tag ", e);
			throw new JspTagException("Error: Can't find enclosing SpaceflightMission for mission tag ");
		}
	}

	public void setMission(String mission) throws JspTagException {
		try {
			SpaceflightMission theSpaceflightMission = (SpaceflightMission)findAncestorWithClass(this, SpaceflightMission.class);
			theSpaceflightMission.setMission(mission);
		} catch (Exception e) {
			log.error("Can't find enclosing SpaceflightMission for mission tag ", e);
			throw new JspTagException("Error: Can't find enclosing SpaceflightMission for mission tag ");
		}
	}

}
