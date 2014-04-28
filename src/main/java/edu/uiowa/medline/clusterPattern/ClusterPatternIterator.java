package edu.uiowa.medline.clusterPattern;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.MEDLINETagLibBodyTagSupport;
import edu.uiowa.medline.documentCluster.DocumentCluster;

@SuppressWarnings("serial")
public class ClusterPatternIterator extends MEDLINETagLibBodyTagSupport {
    int cid = 0;
    int seqnum = 0;
    String lastName = null;
    String foreName = null;
    int occurrences = 0;
	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	private static final Log log = LogFactory.getLog(ClusterPatternIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

	public static String clusterPatternCountByDocumentCluster(String cid) throws JspTagException {
		int count = 0;
		ClusterPatternIterator theIterator = new ClusterPatternIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from medline_clustering.cluster_pattern where 1=1"
						+ " and cid = ?"
						);

			stat.setInt(1,Integer.parseInt(cid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating ClusterPattern iterator", e);
			throw new JspTagException("Error: JDBC error generating ClusterPattern iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean documentClusterHasClusterPattern(String cid) throws JspTagException {
		return ! clusterPatternCountByDocumentCluster(cid).equals("0");
	}

	public static Boolean clusterPatternExists (String cid, String seqnum) throws JspTagException {
		int count = 0;
		ClusterPatternIterator theIterator = new ClusterPatternIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from medline_clustering.cluster_pattern where 1=1"
						+ " and cid = ?"
						+ " and seqnum = ?"
						);

			stat.setInt(1,Integer.parseInt(cid));
			stat.setInt(2,Integer.parseInt(seqnum));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating ClusterPattern iterator", e);
			throw new JspTagException("Error: JDBC error generating ClusterPattern iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {
		DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
		if (theDocumentCluster!= null)
			parentEntities.addElement(theDocumentCluster);

		if (theDocumentCluster == null) {
		} else {
			cid = theDocumentCluster.getCid();
		}


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (cid == 0 ? "" : " and cid = ?")
                                                        +  generateLimitCriteria());
            if (cid != 0) stat.setInt(webapp_keySeq++, cid);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT medline_clustering.cluster_pattern.cid, medline_clustering.cluster_pattern.seqnum from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (cid == 0 ? "" : " and cid = ?")
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            if (cid != 0) stat.setInt(webapp_keySeq++, cid);
            rs = stat.executeQuery();

            if (rs.next()) {
                cid = rs.getInt(1);
                seqnum = rs.getInt(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating ClusterPattern iterator: " + stat.toString(), e);
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error generating ClusterPattern iterator: " + stat.toString());
        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("medline_clustering.cluster_pattern");
      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "cid,seqnum";
        }
    }

    private String generateLimitCriteria() {
        if (limitCriteria > 0) {
            return " limit " + limitCriteria;
        } else {
            return "";
        }
    }

    public int doAfterBody() throws JspTagException {
        try {
            if (rs.next()) {
                cid = rs.getInt(1);
                seqnum = rs.getInt(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across ClusterPattern", e);
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error iterating across ClusterPattern");
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
            rs.close();
            stat.close();
        } catch (SQLException e) {
            log.error("JDBC error ending ClusterPattern iterator",e);
            throw new JspTagException("Error: JDBC error ending ClusterPattern iterator");
        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        cid = 0;
        seqnum = 0;
        parentEntities = new Vector<MEDLINETagLibTagSupport>();

        this.rs = null;
        this.stat = null;
        this.sortCriteria = null;
        this.var = null;
        this.rsCount = 0;
    }

    public String getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(String sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public int getLimitCriteria() {
        return limitCriteria;
    }

    public void setLimitCriteria(int limitCriteria) {
        this.limitCriteria = limitCriteria;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }



	public int getCid () {
		return cid;
	}

	public void setCid (int cid) {
		this.cid = cid;
	}

	public int getActualCid () {
		return cid;
	}

	public int getSeqnum () {
		return seqnum;
	}

	public void setSeqnum (int seqnum) {
		this.seqnum = seqnum;
	}

	public int getActualSeqnum () {
		return seqnum;
	}
}
