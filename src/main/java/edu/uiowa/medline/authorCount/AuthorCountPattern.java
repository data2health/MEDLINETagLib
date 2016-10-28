package edu.uiowa.medline.authorCount;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.medline.MEDLINETagLibTagSupport;

@SuppressWarnings("serial")

public class AuthorCountPattern extends MEDLINETagLibTagSupport {

	static AuthorCountPattern currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(AuthorCountPattern.class);

	Vector<MEDLINETagLibTagSupport> parentEntities = new Vector<MEDLINETagLibTagSupport>();

	String lastName = null;
	String foreName = null;
	int count = 0;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			AuthorCountPatternIterator theAuthorCountPatternIterator = (AuthorCountPatternIterator)findAncestorWithClass(this, AuthorCountPatternIterator.class);

			if (theAuthorCountPatternIterator != null) {
				lastName = theAuthorCountPatternIterator.getLastName();
				foreName = theAuthorCountPatternIterator.getForeName();
			}

			if (theAuthorCountPatternIterator == null && lastName == null) {
				// no lastName was provided - the default is to assume that it is a new AuthorCount and to generate a new lastName
				lastName = null;
				log.debug("generating new AuthorCount " + lastName);
				insertEntity();
			} else {
				// an iterator or lastName was provided as an attribute - we need to load a AuthorCount from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select count(*) from medline16.author where last_name = ? and fore_name = ?");
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
			e.printStackTrace();
			freeConnection();
			throw new JspTagException("Error: JDBC error retrieving lastName " + lastName);
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update medline16.author_count set count = ? where last_name = ? and fore_name = ?");
				stmt.setInt(1,count);
				stmt.setString(2,lastName);
				stmt.setString(3,foreName);
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			freeConnection();
			throw new JspTagException("Error: IOException while writing to the user");
		}
		clearServiceState();
		return super.doEndTag();
	}

	public void insertEntity() throws JspException {
		try {
			PreparedStatement stmt = getConnection().prepareStatement("insert into medline16.author_count(last_name,fore_name,count) values (?,?,?)");
			stmt.setString(1,lastName);
			stmt.setString(2,foreName);
			stmt.setInt(3,count);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			freeConnection();
			throw new JspTagException("Error: IOException while writing to the user");
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

	public String getForeName () {
		if (commitNeeded)
			return "";
		else
			return foreName;
	}

	public void setForeName (String foreName) {
		this.foreName = foreName;
	}

	public int getCount () {
		return count;
	}

	public void setCount (int count) {
		this.count = count;
		commitNeeded = true;
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

	public static int countValue() throws JspException {
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

	}

	public static byte[] getBytes(Object obj) throws JspException {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			oos.close();
			bos.close();
			byte[] data = bos.toByteArray();
			return data;
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}
	}

	public static Object getObject(byte[] bytes) throws IOException, ClassNotFoundException {
		return (new ObjectInputStream(new ByteArrayInputStream(bytes))).readObject();
	}

}
