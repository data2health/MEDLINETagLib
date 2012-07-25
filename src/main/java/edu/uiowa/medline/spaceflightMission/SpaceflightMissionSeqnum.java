package edu.uiowa.medline.spaceflightMission;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")
public class SpaceflightMissionSeqnum extends MEDLINETagLibTagSupport {
	private static final Log log = LogFactory.getLog(SpaceflightMissionSeqnum.class);


	public int doStartTag() throws JspException {
		try {
			SpaceflightMission theSpaceflightMission = (SpaceflightMission)findAncestorWithClass(this, SpaceflightMission.class);
			if (!theSpaceflightMission.commitNeeded) {
				pageContext.getOut().print(theSpaceflightMission.getSeqnum());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SpaceflightMission for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing SpaceflightMission for seqnum tag ");
		}
		return SKIP_BODY;
	}

	public int getSeqnum() throws JspTagException {
		try {
			SpaceflightMission theSpaceflightMission = (SpaceflightMission)findAncestorWithClass(this, SpaceflightMission.class);
			return theSpaceflightMission.getSeqnum();
		} catch (Exception e) {
			log.error(" Can't find enclosing SpaceflightMission for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing SpaceflightMission for seqnum tag ");
		}
	}

	public void setSeqnum(int seqnum) throws JspTagException {
		try {
			SpaceflightMission theSpaceflightMission = (SpaceflightMission)findAncestorWithClass(this, SpaceflightMission.class);
			theSpaceflightMission.setSeqnum(seqnum);
		} catch (Exception e) {
			log.error("Can't find enclosing SpaceflightMission for seqnum tag ", e);
			throw new JspTagException("Error: Can't find enclosing SpaceflightMission for seqnum tag ");
		}
	}

}
