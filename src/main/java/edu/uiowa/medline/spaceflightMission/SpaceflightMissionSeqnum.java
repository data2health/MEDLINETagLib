package edu.uiowa.medline.spaceflightMission;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class SpaceflightMissionSeqnum extends MEDLINETagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			SpaceflightMission theSpaceflightMission = (SpaceflightMission)findAncestorWithClass(this, SpaceflightMission.class);
			if (!theSpaceflightMission.commitNeeded) {
				pageContext.getOut().print(theSpaceflightMission.getSeqnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing SpaceflightMission for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			SpaceflightMission theSpaceflightMission = (SpaceflightMission)findAncestorWithClass(this, SpaceflightMission.class);
			return theSpaceflightMission.getSeqnum();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing SpaceflightMission for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			SpaceflightMission theSpaceflightMission = (SpaceflightMission)findAncestorWithClass(this, SpaceflightMission.class);
			theSpaceflightMission.setSeqnum(seqnum);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing SpaceflightMission for seqnum tag ");
		}
	}

}
