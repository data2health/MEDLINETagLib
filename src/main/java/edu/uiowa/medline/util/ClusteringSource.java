package edu.uiowa.medline.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.uiowa.loki.clustering.Instance;
import edu.uiowa.loki.clustering.Author;
import edu.uiowa.loki.clustering.Cluster;
import edu.uiowa.loki.clustering.ExternalSource;

public class ClusteringSource extends ExternalSource {

    Hashtable<String, Instance> pmidHash = new Hashtable<String, Instance>();

    public void generateClusters(Vector<Cluster> clusters, Author author) throws NamingException, SQLException {
		logger.debug(label + " clustering: " + author);

	    DataSource theDataSource = (DataSource)new InitialContext().lookup("java:/comp/env/jdbc/MEDLINETagLib");
	    Connection theConnection = theDataSource.getConnection();
		Pattern medDatePattern = Pattern.compile("^([0-9][0-9][0-9][0-9])(-[0-9][0-9][0-9][0-9])? ?.*");
        PreparedStatement stmt = theConnection.prepareStatement("select author.pmid,pub_year,article.title,medline_date from medline11.author,medline11.journal, medline11.article where journal.pmid=article.pmid and article.pmid=author.pmid and last_name = ? and fore_name = ? order by pmid desc");
        stmt.setString(1,author.getLastName());
        stmt.setString(2,author.getForeName());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int pmid = rs.getInt(1);
            int year = rs.getInt(2);
            String title = rs.getString(3);
            String medlineDate = rs.getString(4);
            
			if (year == 0) {
				Matcher medDateMatcher = medDatePattern.matcher(medlineDate);
				logger.info("medlineDate: " + medlineDate);
				if (medDateMatcher.find()) {
					year = Integer.parseInt(medDateMatcher.group(1));
					logger.info("\tyear: " + year);
				}
			}
            
            logger.info("\tpmid: " + pmid + "\tyear: " + year + "\ttitle: " + title);
            Instance theInstance = new Instance();
            theInstance.setPmid(pmid);
            theInstance.setYear(year);
            theInstance.setTitle(title);
            pmidHash.put(""+pmid, theInstance);
            
            PreparedStatement authStmt = theConnection.prepareStatement("select last_name, fore_name, initials from medline11.author where pmid = ? order by 1,2");
            authStmt.setInt(1, pmid);
            ResultSet ars = authStmt.executeQuery();
            while (ars.next()) {
                String lname = ars.getString(1);
                String fname = ars.getString(2);
                String initials = ars.getString(3);
                if (!(author.getLastName().equals(lname) && author.getForeName().equals(fname))) {
                	logger.info("\t\tauthor: " + lname + " " + fname);
                    theInstance.getAuthors().addElement(lname + " " + initials);
                }
            }
            authStmt.close();
            
            Cluster bestMatch = null;
            for (int i = 0; i<clusters.size() && bestMatch == null; i++) {
                if (authorMatch(theInstance, clusters.elementAt(i)))
            		bestMatch = clusters.elementAt(i);
            }
        	if (bestMatch == null) {
        		bestMatch = new Cluster();
        		clusters.addElement(bestMatch);
        	}
        	bestMatch.getInstances().addElement(theInstance);
        	for (int i = 0; i < theInstance.getAuthors().size(); i++) {
        		bestMatch.getAuthorHash().put(theInstance.getAuthors().elementAt(i), theInstance.getAuthors().elementAt(i));
        	}
        }
        stmt.close();
        theConnection.close();
    }

}
