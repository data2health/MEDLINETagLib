package edu.uiowa.medline.clusterDocument;


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
import edu.uiowa.medline.article.Article;
import edu.uiowa.medline.documentCluster.DocumentCluster;

@SuppressWarnings("serial")

public class ClusterDocumentIterator extends MEDLINETagLibBodyTagSupport {
    int cid = 0;
    int pmid = 0;
	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	private static final Log log =LogFactory.getLog(ClusterDocument.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

   boolean useArticle = false;
   boolean useDocumentCluster = false;

	public static String clusterDocumentCountByArticle(String pmid) throws JspTagException {
		int count = 0;
		ClusterDocumentIterator theIterator = new ClusterDocumentIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from medline_clustering.cluster_document where 1=1"
						+ " and pmid = ?"
						);

			stat.setInt(1,Integer.parseInt(pmid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating ClusterDocument iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean articleHasClusterDocument(String pmid) throws JspTagException {
		return ! clusterDocumentCountByArticle(pmid).equals("0");
	}

	public static String clusterDocumentCountByDocumentCluster(String cid) throws JspTagException {
		int count = 0;
		ClusterDocumentIterator theIterator = new ClusterDocumentIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from medline_clustering.cluster_document where 1=1"
						+ " and cid = ?"
						);

			stat.setInt(1,Integer.parseInt(cid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating ClusterDocument iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean documentClusterHasClusterDocument(String cid) throws JspTagException {
		return ! clusterDocumentCountByDocumentCluster(cid).equals("0");
	}

	public static Boolean clusterDocumentExists (String cid, String pmid) throws JspTagException {
		int count = 0;
		ClusterDocumentIterator theIterator = new ClusterDocumentIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from medline_clustering.cluster_document where 1=1"
						+ " and cid = ?"
						+ " and pmid = ?"
						);

			stat.setInt(1,Integer.parseInt(cid));
			stat.setInt(2,Integer.parseInt(pmid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating ClusterDocument iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

	public static Boolean articleDocumentClusterExists (String pmid, String cid) throws JspTagException {
		int count = 0;
		ClusterDocumentIterator theIterator = new ClusterDocumentIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from medline_clustering.cluster_document where 1=1"
						+ " and pmid = ?"
						+ " and cid = ?"
						);

			stat.setInt(1,Integer.parseInt(pmid));
			stat.setInt(2,Integer.parseInt(cid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating ClusterDocument iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {
		Article theArticle = (Article)findAncestorWithClass(this, Article.class);
		if (theArticle!= null)
			parentEntities.addElement(theArticle);
		DocumentCluster theDocumentCluster = (DocumentCluster)findAncestorWithClass(this, DocumentCluster.class);
		if (theDocumentCluster!= null)
			parentEntities.addElement(theDocumentCluster);

		if (theArticle == null) {
		} else {
			pmid = theArticle.getPmid();
		}
		if (theDocumentCluster == null) {
		} else {
			cid = theDocumentCluster.getCid();
		}


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (pmid == 0 ? "" : " and pmid = ?")
                                                        + (cid == 0 ? "" : " and cid = ?")
                                                        +  generateLimitCriteria());
            if (pmid != 0) stat.setInt(webapp_keySeq++, pmid);
            if (cid != 0) stat.setInt(webapp_keySeq++, cid);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT medline_clustering.cluster_document.cid, medline_clustering.cluster_document.pmid from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (pmid == 0 ? "" : " and pmid = ?")
                                                        + (cid == 0 ? "" : " and cid = ?")
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            if (pmid != 0) stat.setInt(webapp_keySeq++, pmid);
            if (cid != 0) stat.setInt(webapp_keySeq++, cid);
            rs = stat.executeQuery();

            if (rs.next()) {
                cid = rs.getInt(1);
                pmid = rs.getInt(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error generating ClusterDocument iterator: " + stat.toString());
        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("medline_clustering.cluster_document");
       if (useArticle)
          theBuffer.append(", medline_clustering.article");
       if (useDocumentCluster)
          theBuffer.append(", medline_clustering.document_cluster");

      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
       if (useArticle)
          theBuffer.append(" and article.pmid = cluster_document.null");
       if (useDocumentCluster)
          theBuffer.append(" and document_cluster.cid = cluster_document.null");

      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "cid,pmid";
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
                pmid = rs.getInt(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error iterating across ClusterDocument");
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new JspTagException("Error: JDBC error ending ClusterDocument iterator");
        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        cid = 0;
        pmid = 0;
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


   public boolean getUseArticle() {
        return useArticle;
    }

    public void setUseArticle(boolean useArticle) {
        this.useArticle = useArticle;
    }

   public boolean getUseDocumentCluster() {
        return useDocumentCluster;
    }

    public void setUseDocumentCluster(boolean useDocumentCluster) {
        this.useDocumentCluster = useDocumentCluster;
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

	public int getPmid () {
		return pmid;
	}

	public void setPmid (int pmid) {
		this.pmid = pmid;
	}

	public int getActualPmid () {
		return pmid;
	}
}
