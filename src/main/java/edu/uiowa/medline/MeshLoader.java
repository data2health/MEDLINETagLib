package edu.uiowa.medline;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class MeshLoader {
	static Logger logger = Logger.getLogger(MeshLoader.class);
	static Properties prop_file = PropertyLoader.loadProperties("cd2h");

	static Connection conn = null;
	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure(args[0]);
		conn = getConnection();
		
//		Element desc = parseDocument("/Users/eichmann/downloads/desc2024.xml");
//		processDesc(desc);
//		Element pa = parseDocument("/Users/eichmann/downloads/pa2024.xml");
//		processPa(pa);
//		Element qual = parseDocument("/Users/eichmann/downloads/qual2024.xml");
//		processQual(qual);
		Element supp = parseDocument("/Users/eichmann/downloads/supp2024.xml");
		processSupp(supp);
	}

	static Element parseDocument(String fileName) throws Exception {
		logger.info("scanning " + fileName + "...");

		File input = new File(fileName);
		InputStream is = new FileInputStream(input);
		SAXReader reader = new SAXReader(false);

		// <!ELEMENT MedlineCitationSet (MedlineCitation*, DeleteCitation?)>

		Document document = reader.read(is);
		Element root = document.getRootElement();
		logger.debug("document root: " + root.getName());
		is.close();

		return root;
	}
	
	@SuppressWarnings("unchecked")
	static void processDesc(Element root) throws SQLException {
		for (Element citation : (List<Element>) root.selectNodes("DescriptorRecord")) {
			logger.debug(citation.toString());

			PreparedStatement insStmt = conn.prepareStatement("insert into mesh_staging.raw_desc values(?::xml)");
			insStmt.setString(1, citation.asXML());
			insStmt.execute();
			insStmt.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	static void processPa(Element root) throws SQLException {
		for (Element action : (List<Element>) root.selectNodes("PharmacologicalAction")) {
			logger.debug(action.toString());

			PreparedStatement insStmt = conn.prepareStatement("insert into mesh_staging.raw_pa values(?::xml)");
			insStmt.setString(1, action.asXML());
			insStmt.execute();
			insStmt.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	static void processQual(Element root) throws SQLException {
		for (Element qual : (List<Element>) root.selectNodes("QualifierRecord")) {
			logger.debug(qual.toString());

			PreparedStatement insStmt = conn.prepareStatement("insert into mesh_staging.raw_qual values(?::xml)");
			insStmt.setString(1, qual.asXML());
			insStmt.execute();
			insStmt.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	static void processSupp(Element root) throws SQLException {
		for (Element supp : (List<Element>) root.selectNodes("SupplementalRecord")) {
			logger.debug(supp.toString());

			PreparedStatement insStmt = conn.prepareStatement("insert into mesh_staging.raw_supp values(?::xml)");
			insStmt.setString(1, supp.asXML());
			insStmt.execute();
			insStmt.close();
		}
	}
	
	static Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection local = null;
		Properties props = new Properties();
		props.setProperty("user", prop_file.getProperty("jdbc.user"));
		props.setProperty("password", prop_file.getProperty("jdbc.password"));

		Class.forName("org.postgresql.Driver");
		local = DriverManager.getConnection(prop_file.getProperty("jdbc.url"), props);
		// local.setAutoCommit(false);
		return local;
	}
}
