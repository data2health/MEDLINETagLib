package edu.uiowa.medline.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import com.ibm.tspaces.TupleSpace;
import com.ibm.tspaces.TupleSpaceException;

public class ClusterMEDLINEhub {
	protected static final Log logger = LogFactory.getLog(ClusterMEDLINEhub.class);
	static Connection theConnection = null;
	static TupleSpace ts = null;
	static LocalProperties prop_file = null;
	
	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		PropertyConfigurator.configure(args[0]);
		prop_file = PropertyLoader.loadProperties("medline_clustering");

        Class.forName("org.postgresql.Driver");
		Properties props = new Properties();
		props.setProperty("user", prop_file.getProperty("jdbc.user"));
		props.setProperty("password", prop_file.getProperty("jdbc.password"));
//		props.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
//		props.setProperty("ssl", "true");
		theConnection = DriverManager.getConnection(prop_file.getProperty("jdbc.url"), props);

        try {
            ts = new TupleSpace(prop_file.getProperty("tspace.space"), prop_file.getProperty("tspace.server"));
        } catch (TupleSpaceException tse) {
            logger.error("TSpace error: " + tse);
        }

		Statement stmt = theConnection.createStatement();
		ResultSet rs = ClusterMEDLINE.useFirstInitial ? stmt.executeQuery("select last_name,initial from medline_clustering.author_prefix where not completed order by 1,2")
													  : stmt.executeQuery("select distinct author_count.last_name,author_count.fore_name "
													  						+ "from medline_clustering.author_count,neuromancer_n3c_admin.registration "
													  						+ "where not completed and author_count.last_name=registration.last_name "
													  						+ "and author_count.fore_name ~ ('^'||substring(first_name from 1 for 1))::text order by 1,2");
//		ResultSet rs = stmt.executeQuery("select last_name,fore_name,count(*) from medline_clustering.document_cluster_prefix_2 group by 1,2 having count(*) > 1 order by 1,2");

		while (rs.next()) {
			String lastName = rs.getString(1);
			String foreName = rs.getString(2);
			logger.info("requesting " + lastName + ", " + foreName);
			try {
				ts.write("cluster_request", lastName, foreName);
			} catch (Exception e) {
				logger.error("tspace exception raised: " + e);
				e.printStackTrace();
			}
		}
		stmt.close();
	}
	
}