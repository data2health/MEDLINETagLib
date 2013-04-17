package edu.uiowa.medline.authorCount;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.medline.MEDLINETagLibTagSupport;
import edu.uiowa.medline.Sequence;

@SuppressWarnings("serial")
public class AuthorCount extends MEDLINETagLibTagSupport {

	static AuthorCount currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(AuthorCount.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	String lastName = null;
	String foreName = null;
	int count = 0;

	private String var = null;

	private AuthorCount cachedAuthorCount = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			AuthorCountIterator theAuthorCountIterator = (AuthorCountIterator)findAncestorWithClass(this, AuthorCountIterator.class);

			if (theAuthorCountIterator != null) {
				lastName = theAuthorCountIterator.getLastName();
				foreName = theAuthorCountIterator.getForeName();
			}

			if (theAuthorCountIterator == null && lastName == null) {
				// no lastName was provided - the default is to assume that it is a new AuthorCount and to generate a new lastName
				lastName = null;
				insertEntity();
			} else {
				// an iterator or lastName was provided as an attribute - we need to load a AuthorCount from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select count from medline13.author_count where last_name = ? and fore_name = ?");
				stmt.setString(1,lastName);
				stmt.setString(2,foreName);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (count == 0)
						count = rs.getInt(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving lastName " + lastName, e);
			throw new JspTagException("Error: JDBC error retrieving lastName " + lastName);
		} finally {
			freeConnection();
		}

		AuthorCount currentAuthorCount = (AuthorCount) pageContext.getAttribute("tag_authorCount");
		if(currentAuthorCount != null){
			cachedAuthorCount = currentAuthorCount;
		}
		currentAuthorCount = this;
		pageContext.setAttribute((var == null ? "tag_authorCount" : var), currentAuthorCount);

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(this.cachedAuthorCount != null){
			pageContext.setAttribute((var == null ? "tag_authorCount" : var), this.cachedAuthorCount);
		}else{
			pageContext.removeAttribute((var == null ? "tag_authorCount" : var));
			this.cachedAuthorCount = null;
		}

		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update medline13.author_count set count = ? where last_name = ? and fore_name = ?");
				stmt.setInt(1,count);
				stmt.setString(2,lastName);
				stmt.setString(3,foreName);
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			clearServiceState();
			freeConnection();
		}
		return super.doEndTag();
	}

	public void insertEntity() throws JspException {
		try {
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline13.author_count(last_name,fore_name,count) values (?,?,?)");
			stmt.setString(1,lastName);
			stmt.setString(2,foreName);
			stmt.setInt(3,count);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
	}

	public String getLastName () {
		if (commitNeeded)
			return "";
		else
			return lastName;
	}

	public void setLastName (String lastName) {
		this.lastName = lastName;
	}

	public String getActualLastName () {
		return lastName;
	}

	public String getForeName () {
		if (commitNeeded)
			return "";
		else
			return foreName;
	}

	public void setForeName (String foreName) {
		this.foreName = foreName;
	}

	public String getActualForeName () {
		return foreName;
	}

	public int getCount () {
		return count;
	}

	public void setCount (int count) {
		this.count = count;
		commitNeeded = true;
	}

	public int getActualCount () {
		return count;
	}

	public String getVar () {
		return var;
	}

	public void setVar (String var) {
		this.var = var;
	}

	public String getActualVar () {
		return var;
	}

	public static String lastNameValue() throws JspException {
		try {
			return currentInstance.getLastName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function lastNameValue()");
		}
	}

	public static String foreNameValue() throws JspException {
		try {
			return currentInstance.getForeName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function foreNameValue()");
		}
	}

	public static Integer countValue() throws JspException {
		try {
			return currentInstance.getCount();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function countValue()");
		}
	}

	private void clearServiceState () {
		lastName = null;
		foreName = null;
		count = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();
		this.var = null;

	}

}
