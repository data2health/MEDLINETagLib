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

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		PropertyConfigurator.configure(args[0]);

        Class.forName("org.postgresql.Driver");
		Properties props = new Properties();
		props.setProperty("user", "eichmann");
		props.setProperty("password", "translational");
//		props.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
//		props.setProperty("ssl", "true");
		theConnection = DriverManager.getConnection("jdbc:postgresql://localhost/loki", props);

        try {
            ts = new TupleSpace("MEDLINE", "localhost");
        } catch (TupleSpaceException tse) {
            logger.error("TSpace error: " + tse);
        }

		Statement stmt = theConnection.createStatement();
		ResultSet rs = stmt.executeQuery("select last_name,fore_name from medline_clustering.author_count where not completed order by 1,2 limit 1000000");

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