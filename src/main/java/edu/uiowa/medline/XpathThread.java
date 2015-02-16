package edu.uiowa.medline;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dom4j.Element;

public class XpathThread implements Runnable {
    static Logger logger = Logger.getLogger(XpathThread.class);
    XpathLoader theLoader = new XpathLoader();
    DocumentQueue theQueue = null;
    
    public XpathThread(DocumentQueue theQueue) {
	this.theQueue = theQueue;
    }

    @Override
    public void run() {
	while (!theQueue.isCompleted()) {
	    DocumentRequest theRequest = theQueue.dequeue();
	    if (theRequest == null) {
		try {
		    logger.info("loader thread sleeping...");
		    Thread.sleep(5000);
		} catch (InterruptedException e) {
		    logger.error("loader sleep error: ", e);
		}
	    } else {
		try {
		    logger.info("processing document: " + theRequest.fileName);
		    theLoader.processDocument(theRequest.document);
		} catch (SQLException e) {
		    logger.error("loader processing error: ", e);
		}
	    }
	}
    }

}
