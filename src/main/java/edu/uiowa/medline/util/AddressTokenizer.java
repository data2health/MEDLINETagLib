package edu.uiowa.medline.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class AddressTokenizer implements Runnable {
    static Logger logger = Logger.getLogger(AddressTokenizer.class);
    static Connection theConnection = null;
    
    static Hashtable<String, Integer> countryHash = new Hashtable<String, Integer>();
    static Hashtable<String, Integer> stateHash = new Hashtable<String, Integer>();
    static Hashtable<String, Integer> cityHash = new Hashtable<String, Integer>();
    static Hashtable<String, Integer> universityHash = new Hashtable<String, Integer>();
    static Hashtable<String, Integer> collegeHash = new Hashtable<String, Integer>();
    static Hashtable<String, Integer> hospitalHash = new Hashtable<String, Integer>();
    
    static Vector<Address> addressVector = new Vector<Address>();

    public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException  {
	PropertyConfigurator.configure(args[0]);
	int maxCrawlerThreads = Runtime.getRuntime().availableProcessors();
	Thread[] matcherThreads = new Thread[maxCrawlerThreads];

	theConnection = getConnection();
	initializeCaches();
	
	logger.info("loading addresses...");
	PreparedStatement stmt = theConnection.prepareStatement("select label from medline_affiliation.address");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    String address = rs.getString(1);
	    addressVector.add(new Address(address));
	}

	for (int i = 0; i < maxCrawlerThreads; i++) {
		Thread theThread = new Thread(new AddressTokenizer(i));
		theThread.setPriority(Math.max(theThread.getPriority() - 2, Thread.MIN_PRIORITY));
		theThread.start();
		matcherThreads[i] = theThread;
	}
	for (int i = 0; i < maxCrawlerThreads; i++) {
		matcherThreads[i].join();
	}
    }
    
    static Connection getConnection() throws ClassNotFoundException, SQLException {
	Connection newConnection = null;
	
	Class.forName("org.postgresql.Driver");
	Properties props = new Properties();
	props.setProperty("user", "eichmann");
	props.setProperty("password", "translational");
//	props.setProperty("sslfactory","org.postgresql.ssl.NonValidatingFactory");
//	props.setProperty("ssl", "true");
	newConnection = DriverManager.getConnection("jdbc:postgresql://localhost/loki", props);
	newConnection.setAutoCommit(false);
	
	return newConnection;
	
    }
    
    static void initializeCaches() throws SQLException {
	initializeCountryHash();
	initializeCityHash();
	initializeCodeHash(stateHash, "ADM1");
	initializeCodeHash(stateHash, "ADM2");
	initializeCodeHash(universityHash, "UNIV");
	initializeCodeHash(collegeHash, "SCH");
	initializeCodeHash(collegeHash, "SCHC");
    }
    
    static void initializeCountryHash() throws SQLException {
	logger.info("loading countries:");
	PreparedStatement stmt = theConnection.prepareStatement("select geonameid,name,asciiname,alternatenames from geonames.geoname where geonameid in (select geonameid from geonames.country)");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    int id = rs.getInt(1);
	    String name = rs.getString(2);
	    String ascii = rs.getString(3);
	    String alternates = rs.getString(4);
	    
	    logger.debug("\tid: " + id + "\tname:" + name);
	    logger.debug("\t\tascii: " + ascii);
	    logger.debug("\t\talternates: " + alternates);
	    
	    if (name != null)
		countryHash.put(name, id);
	    if (ascii != null)
		countryHash.put(ascii, id);
	    
	    if (alternates == null)
		continue;
	    
	    StringTokenizer tokenizer = new StringTokenizer(alternates,",");
	    while (tokenizer.hasMoreTokens()) {
		String token = tokenizer.nextToken();
		countryHash.put(token, id);
	    }
	}
	stmt.close();
	
	stmt = theConnection.prepareStatement("select iso,geonameid from geonames.country");
	rs = stmt.executeQuery();
	while (rs.next()) {
	    String code = rs.getString(1);
	    int id = rs.getInt(2);
	    
	    logger.debug("\tid: " + id);
	    logger.debug("\t\tiso: " + code);
	    
	    countryHash.put(code, id);
	}
    }
    
    static void initializeCityHash() throws SQLException {
	logger.info("loading countries:");
	PreparedStatement stmt = theConnection.prepareStatement("select geonameid,name,asciiname,alternatenames from geonames.city");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    int id = rs.getInt(1);
	    String name = rs.getString(2);
	    String ascii = rs.getString(3);
	    String alternates = rs.getString(4);
	    
	    logger.debug("\tid: " + id + "\tname:" + name);
	    logger.debug("\t\tascii: " + ascii);
	    logger.debug("\t\talternates: " + alternates);
	    
	    if (name != null)
		cityHash.put(name, id);
	    if (ascii != null)
		cityHash.put(ascii, id);
	    
	    if (alternates == null)
		continue;
	    
	    StringTokenizer tokenizer = new StringTokenizer(alternates,",");
	    while (tokenizer.hasMoreTokens()) {
		String token = tokenizer.nextToken();
		cityHash.put(token, id);
	    }
	}
    }
    
    static void initializeCodeHash(Hashtable<String,Integer> codeHash, String pattern) throws SQLException {
	logger.info("loading " + pattern + ":");
	PreparedStatement stmt = theConnection.prepareStatement("select geonameid,name,asciiname,alternatenames from geonames.geoname where feature_code = ?");
	stmt.setString(1, pattern);
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    int id = rs.getInt(1);
	    String name = rs.getString(2);
	    String ascii = rs.getString(3);
	    String alternates = rs.getString(4);
	    
	    logger.debug("\tid: " + id + "\tname:" + name);
	    logger.debug("\t\tascii: " + ascii);
	    logger.debug("\t\talternates: " + alternates);
	    
	    if (name != null)
		codeHash.put(name, id);
	    if (ascii != null)
		codeHash.put(ascii, id);

	    if (alternates == null)
		continue;
	    
	    StringTokenizer tokenizer = new StringTokenizer(alternates,",");
	    while (tokenizer.hasMoreTokens()) {
		String token = tokenizer.nextToken();
		codeHash.put(token, id);
	    }
	}
    }
    
    int threadID = 0;
    Connection conn = null;
    
    public AddressTokenizer(int threadID) throws ClassNotFoundException, SQLException {
	this.threadID = threadID;
	conn = getConnection();
    }

    public void run() {
	int count = 0;
	while (addressVector.size() > 0) {
	    try {
		Address theAddress = addressVector.remove(0);
		theAddress.setThreadID(threadID);
		theAddress.decompose();

		PreparedStatement insStmt = conn.prepareStatement("insert into medline_affiliation.component values(?,?,?,?)");
		for (int i = 0; i < theAddress.componentVector.size(); i++) {
		    logger.debug("[" + threadID + "]\tleft: " + i + "\tright: " + (theAddress.componentVector.size() - i - 1) + "\ttoken: "+ theAddress.componentVector.elementAt(i));
		    insStmt.setString(1, theAddress.componentVector.elementAt(i).label);
		    insStmt.setString(2, theAddress.componentVector.elementAt(i).category);
		    insStmt.setInt(3, i);
		    insStmt.setInt(4, theAddress.componentVector.size() - i - 1);
		    insStmt.addBatch();
		}
		insStmt.executeBatch();
		insStmt.close();
		
		if (count++ % 100 == 0)
		    conn.commit();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	try {
	    conn.commit();
	} catch (SQLException e) { }
    }
}
