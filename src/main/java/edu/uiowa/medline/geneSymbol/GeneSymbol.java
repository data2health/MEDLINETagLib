package edu.uiowa.medline.geneSymbol;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.medline.article.Article;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.Sequence;

@SuppressWarnings("serial")

public class GeneSymbol extends MEDLINETagLibTagSupport {

	static GeneSymbol currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(GeneSymbol.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	int pmid = 0;
	int seqnum = 0;
	String symbol = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Article theArticle = (Article)findAncestorWithClass(this, Article.class);
			if (theArticle!= null)
				parentEntities.addElement(theArticle);

			if (theArticle == null) {
			} else {
				pmid = theArticle.getPmid();
			}

			GeneSymbolIterator theGeneSymbolIterator = (GeneSymbolIterator)findAncestorWithClass(this, GeneSymbolIterator.class);

			if (theGeneSymbolIterator != null) {
				pmid = theGeneSymbolIterator.getPmid();
				seqnum = theGeneSymbolIterator.getSeqnum();
			}

			if (theGeneSymbolIterator == null && theArticle == null && seqnum == 0) {
				// no seqnum was provided - the default is to assume that it is a new GeneSymbol and to generate a new seqnum
				seqnum = Sequence.generateID();
				log.debug("generating new GeneSymbol " + seqnum);
				insertEntity();
			} else {
				// an iterator or seqnum was provided as an attribute - we need to load a GeneSymbol from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select symbol from medline10.gene_symbol where pmid = ? and seqnum = ?");
				stmt.setInt(1,pmid);
				stmt.setInt(2,seqnum);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (symbol == null)
						symbol = rs.getString(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error retrieving seqnum " + seqnum);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update medline10.gene_symbol set symbol = ? where pmid = ? and seqnum = ?");
				stmt.setString(1,symbol);
				stmt.setInt(2,pmid);
				stmt.setInt(3,seqnum);
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			clearServiceState();
			freeConnection();
		}
		return super.doEndTag();
	}

	public void insertEntity() throws JspException {
		try {
			if (seqnum == 0) {
				seqnum = Sequence.generateID();
				log.debug("generating new GeneSymbol " + seqnum);
			}

			if (symbol == null)
				symbol = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline10.gene_symbol(pmid,seqnum,symbol) values (?,?,?)");
			stmt.setInt(1,pmid);
			stmt.setInt(2,seqnum);
			stmt.setString(3,symbol);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
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

	public int getSeqnum () {
		return seqnum;
	}

	public void setSeqnum (int seqnum) {
		this.seqnum = seqnum;
	}

	public int getActualSeqnum () {
		return seqnum;
	}

	public String getSymbol () {
		if (commitNeeded)
			return "";
		else
			return symbol;
	}

	public void setSymbol (String symbol) {
		this.symbol = symbol;
		commitNeeded = true;
	}

	public String getActualSymbol () {
		return symbol;
	}

	public static int pmidValue() throws JspException {
		try {
			return currentInstance.getPmid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pmidValue()");
		}
	}

	public static int seqnumValue() throws JspException {
		try {
			return currentInstance.getSeqnum();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function seqnumValue()");
		}
	}

	public static String symbolValue() throws JspException {
		try {
			return currentInstance.getSymbol();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function symbolValue()");
		}
	}

	private void clearServiceState () {
		pmid = 0;
		seqnum = 0;
		symbol = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
