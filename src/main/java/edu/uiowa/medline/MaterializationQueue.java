package edu.uiowa.medline;

import java.util.Vector;

public class MaterializationQueue {
    int capacity = Integer.MAX_VALUE;
    Vector<Vector<MaterializationRequest>> matQueue = new Vector<Vector<MaterializationRequest>>();
    boolean completed = false;
    
    public synchronized boolean atCapacity() {
	return matQueue.size() >= capacity;
    }
    
    public synchronized void queue(Vector<MaterializationRequest> queueEntry) {
	matQueue.add(queueEntry);
	completed = false;
    }
    
    public void completed() {
	completed = true;
    }
    
    public synchronized boolean isCompleted() {
	return matQueue.size() == 0 && completed;
    }
    
    public synchronized Vector<MaterializationRequest> dequeue() {
	if (matQueue.size() == 0)
	    return null;
	else
	    return matQueue.remove(0);
    }

}
