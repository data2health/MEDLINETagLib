package edu.uiowa.medline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.Vector;

import org.apache.log4j.Logger;

public class XtableThread implements Runnable {
    static Logger logger = Logger.getLogger(XtableThread.class);
    public static enum Mode {LOAD, MATERIALIZE, REMATERIALIZE};
    static DecimalFormat formatter = new DecimalFormat("00");
    XtableLoader theLoader = null;
    DocumentQueue theQueue = null;
    Mode mode = Mode.LOAD;

    Connection conn = null;
    MaterializationQueue matQueue = null;
    int threadID = 0;
    
    public XtableThread(DocumentQueue theQueue, Mode mode) throws Exception {
	theLoader = new XtableLoader();
	this.theQueue = theQueue;
	this.mode = mode;
    }

    public XtableThread(int threadID, MaterializationQueue matQueue, Mode mode) throws Exception {
	this.matQueue = matQueue;
	this.threadID = threadID;
	this.mode = mode;
	conn = XtableLoader.getConnection();
    }

    @Override
    public void run() {
	switch (mode) {
	case LOAD:
	    runLoad();
	    break;
	case MATERIALIZE:
	case REMATERIALIZE:
	    runMaterialize();
	    break;
	}
    }

    public void runMaterialize() {
	while (!matQueue.isCompleted()) {
	    Vector<MaterializationRequest> theRequestVector = matQueue.dequeue();
	    if (theRequestVector == null) {
		return;
	    } else {
		for (MaterializationRequest theRequest : theRequestVector) {
		    try {
			PreparedStatement stmt = null;
			if (mode == Mode.MATERIALIZE) {
			    logger.info("[" + formatter.format(threadID) + "] materializing: " + theRequest.tableName + ": " + theRequest.attributeList);
			    stmt = conn.prepareStatement("insert into medline." + theRequest.tableName + " select " + theRequest.attributeList + " from medline21_staging." + theRequest.tableName);
			} else {
			    logger.info("[" + formatter.format(threadID) + "] rematerializing: " + theRequest.tableName + ": " + theRequest.attributeList);
			    stmt = conn.prepareStatement("insert into medline." + theRequest.tableName + " select " + theRequest.attributeList + " from medline21_staging." + theRequest.tableName + " where pmid in (select pmid from medline21_staging.queue)");
			}
			int count = stmt.executeUpdate();
			stmt.close();
			logger.info("[" + formatter.format(threadID) + "]\t" + theRequest.tableName + " count: " + count);
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
