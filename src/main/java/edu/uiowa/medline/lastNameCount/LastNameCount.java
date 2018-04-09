package edu.uiowa.medline.lastNameCount;

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
public class LastNameCount extends MEDLINETagLibTagSupport {

	static LastNameCount currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(LastNameCount.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	String lastName = null;
	int count = 0;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			LastNameCountIterator theLastNameCountIterator = (LastNameCountIterator)findAncestorWithClass(this, LastNameCountIterator.class);

			if (theLastNameCountIterator != null) {
				lastName = theLastNameCountIterator.getLastName();
			}

			if (theLastNameCountIterator == null && lastName == null) {
				// no lastName was provided - the default is to assume that it is a new LastNameCount and to generate a new lastName
				lastName = null;
				insertEntity();
			} else {
				// an iterator or lastName was provided as an attribute - we need to load a LastNameCount from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select count from medline18.last_name_count where last_name = ?");
				stmt.setString(1,lastName);
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
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update medline18.last_name_count set count = ? where last_name = ?");
				stmt.setInt(1,count);
				stmt.setString(2,lastName);
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
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline18.last_name_count(last_name,count) values (?,?)");
			stmt.setString(1,lastName);
			stmt.setInt(2,count);
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

	public static String lastNameValue() throws JspException {
		try {
			return currentInstance.getLastName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function lastNameValue()");
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
		count = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<MEDLINETagLibTagSupport>();

	}

}
