package edu.uiowa.medline.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import edu.uiowa.loki.clustering.Instance;
import edu.uiowa.loki.clustering.Author;
import edu.uiowa.loki.clustering.Cluster;
import edu.uiowa.loki.clustering.ExternalSource;
import edu.uiowa.loki.clustering.Linkage;
import edu.uiowa.medline.article.Article;
import edu.uiowa.medline.journal.Journal;

public class ClusteringSource extends ExternalSource {
	static Logger logger = Logger.getLogger(ClusteringSource.class);
	private boolean useCollectiveName = true;
	private boolean useFirstInitial = false;

	Pattern medDatePattern = Pattern.compile("^([0-9][0-9][0-9][0-9])(-[0-9][0-9][0-9][0-9])? ?.*");

	private Connection getConnection() throws NamingException, SQLException, ClassNotFoundException {
		Connection theConnection = null;

		if (tomcat) {
			DataSource theDataSource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/MEDLINETagLib");
			theConnection = theDataSource.getConnection();
		} else {
			Class.forName("org.postgresql.Driver");
			LocalProperties prop_file = PropertyLoader.loadProperties("medline_clustering");
			Properties props = new Properties();
			props.setProperty("user", prop_file.getProperty("jdbc.user"));
			props.setProperty("password", prop_file.getProperty("jdbc.password"));
			// props.setProperty("sslfactory",
			// "org.postgresql.ssl.NonValidatingFactory");
			// props.setProperty("ssl", "true");
			theConnection = DriverManager.getConnection(prop_file.getProperty("jdbc.url"), props);

		}

		return theConnection;
	}

	public void generateClusters(Vector<Cluster> clusters, Author author) {
		try {
			cluster(clusters, author);
		} catch (Exception e) {
			logger.error("Exception raised: ", e);
		}
	}

	void cluster(Vector<Cluster> clusters, Author author) throws NamingException, SQLException, ClassNotFoundException {
		logger.debug(label + " clustering: " + author);

		Connection theConnection = getConnection();
		PreparedStatement stmt = null;
		if (author.getForeName() == null) {
			stmt = theConnection.prepareStatement("select pmid from medline.author where last_name = ? and fore_name is null order by pmid desc");
			stmt.setString(1, author.getLastName());
		} else if (useFirstInitial) {
			if (author.getForeName() != null)
				author.setForeName("" + author.getForeName().charAt(0));
			stmt = theConnection.prepareStatement("select pmid from medline.author where last_name = ? and fore_name ~ ? order by pmid desc");
			stmt.setString(1, author.getLastName());
			stmt.setString(2, "^" + author.getForeName());
		} else {
			stmt = theConnection.prepareStatement("select pmid from medline.author where last_name = ? and fore_name = ? order by pmid desc");
			stmt.setString(1, author.getLastName());
			stmt.setString(2, author.getForeName());
		}
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			int pmid = rs.getInt(1);

			if (parentClusterer.publicationAlreadyClustered("MEDLINE", pmid)) {
				logger.info("MEDLINE publication already clustered: " + pmid);
				continue;
			}

			Instance theInstance = generateInstance(theConnection, pmid, author);

			Cluster bestMatch = null;
			for (int i = 0; i < clusters.size() && bestMatch == null; i++) {
				if (authorMatch(theInstance, clusters.elementAt(i)))
					bestMatch = clusters.elementAt(i);
			}
			if (bestMatch == null) {
				bestMatch = new Cluster();
				bestMatch.setRecent(true);
				clusters.addElement(bestMatch);
			}
			bestMatch.getInstances().addElement(theInstance);
			for (int i = 0; i < theInstance.getAuthors().size(); i++) {
				bestMatch.getAuthorHash().put(theInstance.getAuthors().elementAt(i),theInstance.getAuthors().elementAt(i));
			}
			for (Author theAuthor : theInstance.authorVector) {
				bestMatch.addAuthor(theAuthor);
			}

			parentClusterer.publicationClustered("MEDLINE", pmid);
		}
		stmt.close();
		theConnection.close();
	}

	public Instance generateInstance(Connection theConnection, int pmid, Author author) throws SQLException {
		Instance theInstance = new Instance();
		PreparedStatement stmt = theConnection.prepareStatement("select pub_date_year,article_title,pub_date_medline from medline.article natural join medline.article_title where article.pmid=?");
		stmt.setInt(1, pmid);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			int year = rs.getInt(1);
			String title = rs.getString(2);
			String medlineDate = rs.getString(3);

			if (year == 0) {
				Matcher medDateMatcher = medDatePattern.matcher(medlineDate);
				logger.debug("medlineDate: " + medlineDate);
				if (medDateMatcher.find()) {
					year = Integer.parseInt(medDateMatcher.group(1));
					logger.debug("\tyear: " + year);
				}
			}

			if (parentClusterer.publicationAlreadyClustered("MEDLINE", pmid)) {
				logger.debug("\tskipping an already present pmid: " + pmid + "\tyear: " + year + "\ttitle: " + title);
				continue;
			}

			logger.debug("\tpmid: " + pmid + "\tyear: " + year + "\ttitle: " + title);
			theInstance.setPmid(pmid);
			theInstance.setYear(year);
			theInstance.setTitle(title);
			theInstance.setValid(true);
			Linkage newLink = new Linkage(sid, pmid);
			newLink.setRecent(true);
			theInstance.getLinkages().add(newLink);
			parentClusterer.publicationClustered("MEDLINE", pmid);

			PreparedStatement authStmt = theConnection.prepareStatement("select last_name, fore_name, initials, collective_name from medline.author where pmid = ? order by 1,2");
			authStmt.setInt(1, pmid);
			ResultSet ars = authStmt.executeQuery();
			while (ars.next()) {
				String lname = ars.getString(1);
				String fname = (useFirstInitial && ars.getString(2) != null)
								? "" + ars.getString(2).charAt(0)
								: ars.getString(2);
				String initials = ars.getString(3);
				String collective = ars.getString(4);
				if (lname == null && collective != null && useCollectiveName) {
					logger.debug("\t\tauthor: " + collective);
					theInstance.getAuthors().addElement(collective);
					theInstance.addAuthor(new Author(collective, null));
				} else if (!(author.getLastName().equals(lname) && ((author.getForeName() == null && fname == null)
						|| (author.getForeName() != null && author.getForeName().equals(fname))))) {
					logger.debug("\t\tauthor: " + lname + " " + fname);
					theInstance.getAuthors().addElement(lname + " " + initials);
					theInstance.addAuthor(new Author(lname, fname));
				}
			}
			authStmt.close();
		}

		return theInstance;
	}

	public int getAuthorCountCount() {
		int count = 0;

		try {
			Connection theConnection = getConnection();
			PreparedStatement stat = theConnection.prepareStatement("SELECT count(*) from medline.author_count");

			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
			theConnection.close();
		} catch (Exception e) {
			logger.error("Exception raised: ", e);
		}

		return count;
	}

	public boolean authorCountExists(String lastName, String foreName) {
		int count = 0;

		try {
			Connection theConnection = getConnection();
			PreparedStatement stat = theConnection.prepareStatement("SELECT count(*) from medline.author_count where" + " last_name = ?" + " and fore_name = ?");

			stat.setString(1, lastName);
			stat.setString(2, foreName);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
			theConnection.close();
		} catch (Exception e) {
			logger.error("Exception raised: ", e);
		}

		return count > 0;
	}

	public void getAuthorNames(String lastName, String foreNamePrefix, Vector<Author> authors) {
		try {
			logger.debug(label + " scanning for author : " + lastName + " " + foreNamePrefix);
			Connection theConnection = getConnection();
			PreparedStatement stat = theConnection.prepareStatement("select distinct last_name, fore_name from medline.author where last_name = ? and fore_name ~ ? order by last_name,fore_name");
			stat.setString(1, lastName.substring(0, 1).toUpperCase() + lastName.substring(1));
			stat.setString(2, "^" + foreNamePrefix.substring(0, 1).toUpperCase() + foreNamePrefix.substring(1));

			ResultSet rs = stat.executeQuery();
			while (rs.next()) {
				String lName = rs.getString(1);
				String fName = rs.getString(2);
				logger.debug(label + " matching for author : " + lName + " " + fName);
				addAuthor(authors, lName, fName);
			}
			stat.close();
			theConnection.close();
		} catch (Exception e) {
			logger.error("Exception raised: ", e);
		}

	}

	public void getIDs(String lastName, String foreName, Set<Integer> ids) {
		try {
			Connection theConnection = getConnection();
			PreparedStatement stat = theConnection.prepareStatement("select pmid from medline.author where last_name = ? and fore_name = ?");
			stat.setString(1, lastName);
			stat.setString(2, foreName);

			ResultSet rs = stat.executeQuery();
			while (rs.next()) {
				int ID = rs.getInt(1);
				ids.add(ID);
			}
			stat.close();
			theConnection.close();
		} catch (Exception e) {
			logger.error("Exception raised: ", e);
		}

	}

	public String authorString(int pmid) {
		StringBuffer authorBuffer = new StringBuffer();

		try {
			Connection theConnection = getConnection();
			int count = 0;
			PreparedStatement loadStmt = getConnection().prepareStatement("select last_name,fore_name,collective_name from medline.author where pmid = ? order by seqnum");
			loadStmt.setInt(1, pmid);
			ResultSet lrs = loadStmt.executeQuery();
			while (lrs.next()) {
				String lastName = lrs.getString(1);
				String foreName = lrs.getString(2);
				String collective = lrs.getString(3);
				if (lastName == null)
					authorBuffer.append((count++ > 0 ? ", " : "") + collective);
				else
					authorBuffer.append((count++ > 0 ? ", " : "") + lastName + ", " + foreName);
			}
			loadStmt.close();
			authorBuffer.append(".");

			theConnection.close();
		} catch (Exception e) {
			logger.error("Exception raised: ", e);
		}

		return authorBuffer.toString();
	}

	public String citationString(int pmid) {
		return null;
	}

	public String hyperlinkedCitationString(int pmid, String hrefPrefix) {
		String citationString = "";
		int year = 0;

		try {
			Article theArticle = new Article();
			theArticle.setPmid(pmid);
			theArticle.doStartTag();
			Journal theJournal = new Journal();
			theJournal.setParent(theArticle);
			theJournal.doStartTag();
			year = theJournal.getPubYear();

			if (year == 0) {
				Matcher medDateMatcher = medDatePattern.matcher(theJournal.getMedlineDate());
				logger.info("medlineDate: " + theJournal.getMedlineDate());
				if (medDateMatcher.find()) {
					year = Integer.parseInt(medDateMatcher.group(1));
					logger.info("\tyear: " + year);
				}
			}

			citationString = authorString(pmid) + " <a href=\"" + hrefPrefix + "?id=" + theArticle.getPmid() + "\">"
							+ theArticle.getTitle() + "</a> <i>" + theArticle.getTa() + "</i> " + theJournal.getVolume() + "("
							+ theJournal.getIssue() + "):" + theArticle.getMedlinePgn() + ", " + year + ".";
			theJournal.freeConnection();
			theArticle.freeConnection();
		} catch (Exception e) {
			logger.error("Exception raised: ", e);
		}

		return citationString;
	}

	public int publicationYear(int pmid) {
		int year = 0;

		try {
			Article theArticle = new Article();
			theArticle.setPmid(pmid);
			theArticle.doStartTag();
			Journal theJournal = new Journal();
			theJournal.setParent(theArticle);
			theJournal.doStartTag();
			year = theJournal.getPubYear();

			if (year == 0) {
				Matcher medDateMatcher = medDatePattern.matcher(theJournal.getMedlineDate());
				logger.info("medlineDate: " + theJournal.getMedlineDate());
				if (medDateMatcher.find()) {
					year = Integer.parseInt(medDateMatcher.group(1));
					logger.info("\tyear: " + year);
				}
			}

			theJournal.freeConnection();
			theArticle.freeConnection();
		} catch (Exception e) {
			logger.error("Exception raised: ", e);
		}

		return year;
	}

	public void getInstanceInformation(Instance theInstance, Vector<Author> authorsOfInterest) {
		if (theInstance.getYear() > 0 && theInstance.getTitle() != null && theInstance.getAuthors().size() > 0) {
			logger.info("skipping instance as already populated: " + theInstance);
			return;
		}
		logger.debug("populating instance " + theInstance);

		int pmid = getIDbyInstance(theInstance);
//	Hashtable<String, Instance> pmidHash = parentClusterer.getSourceIDHashByLabel("MEDLINE");

		try {
			Connection theConnection = getConnection();
			Pattern medDatePattern = Pattern.compile("^([0-9][0-9][0-9][0-9])(-[0-9][0-9][0-9][0-9])? ?.*");
			PreparedStatement stmt = theConnection.prepareStatement("select pub_year,article.title,medline_date from medline.journal, medline.article where journal.pmid=article.pmid and article.pmid = ?");
			stmt.setInt(1, pmid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int year = rs.getInt(1);
				String title = rs.getString(2);
				String medlineDate = rs.getString(3);

				if (year == 0) {
					Matcher medDateMatcher = medDatePattern.matcher(medlineDate);
					logger.debug("medlineDate: " + medlineDate);
					if (medDateMatcher.find()) {
						year = Integer.parseInt(medDateMatcher.group(1));
						logger.debug("\tyear: " + year);
					}
				}

				logger.debug("\tpmid: " + pmid + "\tyear: " + year + "\ttitle: " + title);
				if (theInstance.getYear() == 0)
					theInstance.setYear(year);
				if (theInstance.getTitle() == null)
					theInstance.setTitle(title);
//		pmidHash.put("" + pmid, theInstance);

				if (theInstance.getAuthors().size() == 0) {
					PreparedStatement authStmt = theConnection.prepareStatement("select last_name, fore_name, initials from medline.author where pmid = ? order by 1,2");
					authStmt.setInt(1, pmid);
					ResultSet ars = authStmt.executeQuery();
					while (ars.next()) {
						String lname = ars.getString(1);
						String fname = ars.getString(2);
						String initials = ars.getString(3);
						if (!authorMatch(authorsOfInterest, lname, fname)) {
							logger.debug("\t\tauthor: " + lname + " " + fname);
							theInstance.getAuthors().addElement(lname + " " + initials);
						}
					}
					authStmt.close();
				}
			}
			stmt.close();
			theConnection.close();
		} catch (Exception e) {
		}
	}

	@Override
	public void generateInstance(int pid, Author author) {
		// TODO Auto-generated method stub

	}

}
