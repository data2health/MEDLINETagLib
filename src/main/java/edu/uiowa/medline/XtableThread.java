package edu.uiowa.medline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.Vector;

import org.apache.log4j.Logger;

public class XtableThread implements Runnable {
    static Logger logger = Logger.getLogger(XtableThread.class);
    static DecimalFormat formatter = new DecimalFormat("00");
    XtableLoader theLoader = null;
    DocumentQueue theQueue = null;

    Connection conn = null;
    MaterializationQueue matQueue = null;
    boolean materialize = false;
    int threadID = 0;
    
    public XtableThread(DocumentQueue theQueue) throws Exception {
	theLoader = new XtableLoader();
	this.theQueue = theQueue;
    }

    public XtableThread(int threadID, MaterializationQueue matQueue) throws Exception {
	this.matQueue = matQueue;
	this.threadID = threadID;
	materialize = true;
	conn = XtableLoader.getConnection();
    }

    @Override
    public void run() {
	if (materialize)
	    runMaterialize();
	else
	    runLoad();
    }

    public void runMaterialize() {
	while (!matQueue.isCompleted()) {
	    Vector<MaterializationRequest> theRequestVector = matQueue.dequeue();
	    if (theRequestVector == null) {
		return;
	    } else {
		for (MaterializationRequest theRequest : theRequestVector) {
		    logger.info("[" + formatter.format(threadID) + "] materializing: " + theRequest.tableName + ": " + theRequest.attributeList);
		    try {
			PreparedStatement stmt = conn.prepareStatement("insert into medline." + theRequest.tableName + " select " + theRequest.attributeList + " from medline20_staging." + theRequest.tableName);
			int count = stmt.executeUpdate();
			stmt.close();
			logger.info("[" + formatter.format(threadID) + "]\tcount: " + count);
		    } catch (Exception e) {
			logger.error("[" + formatter.format(threadID) + "] exception raised materializing " + theRequest.tableName + ": ",e);
		    }
		}
	    }
	}
    }

    public void runLoad() {
	while (!theQueue.isCompleted()) {
	    DocumentRequest theRequest = theQueue.dequeue();
	    if (theRequest == null) {
		return;
	    } else {
		try {
		    logger.info("processing document: " + theRequest.fileName);
		    if (theRequest.document == null)
			theLoader.processDocument(theLoader.parseDocument(theRequest.fileName));
		    else
			theLoader.processDocument(theRequest.document);
		} catch (Exception e) {
		    logger.error("loader processing error: " + theRequest.fileName, e);
		}
	    }
	}
    }

}
