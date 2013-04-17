/*
 * Created on Feb 26, 2009
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.uiowa.medline;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xml.sax.helpers.*;
import java.sql.*;
import java.util.*;

public class CitationDump extends DefaultHandler {

	static Logger logger = Logger.getLogger(Loader.class);

	static Connection conn = null;
	static Connection medConn = null;


	public static void main(String args[]) throws Exception {
		logger.setLevel(Level.DEBUG);
		
		Class.forName("org.postgresql.Driver");

		Properties props = new Properties();
		props.setProperty("user", "");
		props.setProperty("password", "");
		props.setProperty("sslfactory",	"org.postgresql.ssl.NonValidatingFactory");
		props.setProperty("ssl", "true");
		conn = DriverManager.getConnection("jdbc:postgresql://database-dev/apr", props);

		Properties props2 = new Properties();
		props2.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
		props2.setProperty("ssl", "true");
		props2.setProperty("user", "");
		props2.setProperty("password", "");
		medConn = DriverManager.getConnection("jdbc:postgresql://neuromancer/bioinformatics", props2);

		PreparedStatement cntStmt = conn.prepareStatement("select pmid from apr.publication");
		ResultSet rs = cntStmt.executeQuery();
		while (rs.next()) {
			int pmid = rs.getInt(1);
//			System.out.println("pmid: " + pmid);
			emit(pmid);
		}
		cntStmt.close();
	}
	
	static void emit(int pmid) throws Exception {
		PreparedStatement artStmt = medConn.prepareStatement("select title,medline_pgn from medline13.article where pmid=?");
		artStmt.setInt(1, pmid);
		ResultSet ars = artStmt.executeQuery();
		while (ars.next()) {
			String title = ars.getString(1);
			String pages = ars.getString(2);
			System.out.println("%T " + title);
			System.out.println("%P " + pages);
		}
		artStmt.close();
		
		PreparedStatement jourStmt = medConn.prepareStatement("select iso_abbreviation,volume,issue,pub_year,medline_date from medline13.journal where pmid=?");
		jourStmt.setInt(1, pmid);
		ResultSet jrs = jourStmt.executeQuery();
		while (jrs.next()) {
			String title = jrs.getString(1);
			String volume = jrs.getString(2);
			String issue = jrs.getString(3);
			int year = jrs.getInt(4);
			String medDate = jrs.getString(5);
			System.out.println("%J " + title);
			System.out.println("%V " + volume);
			System.out.println("%I " + issue);
			if (year > 0)
				System.out.println("%D " + year);
			else
				System.out.println("%D " + medDate);
		}
		jourStmt.close();

		PreparedStatement authStmt = medConn.prepareStatement("select last_name,fore_name,initials,suffix,collective_name from medline13.author where pmid=? order by seqnum");
		authStmt.setInt(1, pmid);
		ResultSet aurs = authStmt.executeQuery();
		while (aurs.next()) {
			String lastName = aurs.getString(1);
			String foreName = aurs.getString(2);
			String initials = aurs.getString(3);
			String suffix = aurs.getString(4);
			String collectiveName = aurs.getString(5);
			if (lastName != null)
				System.out.println("%A " + initials + " " + lastName + (suffix == null ? "" : (", " + suffix)));
			else
				System.out.println("%A " + collectiveName);
		}
		authStmt.close();

		System.out.println("");
	}

}
