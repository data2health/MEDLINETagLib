package edu.uiowa.medline;

import java.util.Vector;

import org.dom4j.Element;

public class DocumentQueue {
    int capacity = 5;
    Vector<DocumentRequest> documentQueue = new Vector<DocumentRequest>();
    boolean completed = false;
    
    public synchronized boolean atCapacity() {
	return documentQueue.size() >= capacity;
    }
    
    public synchronized void queue(String fileName, Element theElement) {
	documentQueue.add(new DocumentRequest(fileName, theElement));
	completed = false;
    }
    
    public void completed() {
	completed = true;
    }
    
    public synchronized boolean isCompleted() {
	return documentQueue.size() == 0 && completed;
    }
    
    public synchronized DocumentRequest dequeue() {
	if (documentQueue.size() == 0)
	    return null;
	else
	    return documentQueue.remove(0);
    }

}
