package edu.uiowa.medline.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import com.ibm.tspaces.TupleSpace;
import com.ibm.tspaces.TupleSpaceException;

public class ClusterMEDLINEAggregatorhub {
	protected static final Log logger = LogFactory.getLog(ClusterMEDLINEAggregatorhub.class);
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
        
        aggregate();
	}
	
	static void aggregate() throws SQLException {
		PreparedStatement stmt = theConnection.prepareStatement("select distinct last_name,substring(fore_name from 1 for 1) "
																+ "from medline_clustering.document_cluster "
																+ "where (last_name,substring(fore_name from 1 for 1)) not in (select distinct last_name,substring(fore_name from 1 for 1) "
																															+ "from medline_clustering.document_cluster "
																															+ "where cid in (select super from medline_clustering.supercluster) "
																															+ "   or cid in (select sub from medline_clustering.supercluster)) "
																+ "order by 1,2");
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			String lastName = rs.getString(1);
			String foreName = rs.getString(2);
			logger.info("requesting " + lastName + ", " + foreName);
			try {
				ts.write("aggregate_request", lastName, foreName);
			} catch (Exception e) {
				logger.error("tspace exception raised: " + e);
				e.printStackTrace();
			}
		}
		stmt.close();
	}
	
}