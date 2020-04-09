package edu.uiowa.medline;

import org.apache.log4j.Logger;

public class XtableThread implements Runnable {
    static Logger logger = Logger.getLogger(XtableThread.class);
    XtableLoader theLoader = null;
    DocumentQueue theQueue = null;
    
    public XtableThread(DocumentQueue theQueue) throws Exception {
	theLoader = new XtableLoader();
	this.theQueue = theQueue;
    }

    @Override
    public void run() {
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
