package edu.uiowa.medline;

import java.util.Vector;

public class MaterializationQueue {
    int capacity = Integer.MAX_VALUE;
    Vector<Vector<MaterializationRequest>> theQueue = new Vector<Vector<MaterializationRequest>>();
    boolean completed = false;
    
    public MaterializationQueue() {
	initialize();
    }
    
    public synchronized boolean atCapacity() {
	return theQueue.size() >= capacity;
    }
    
    public synchronized void queue(Vector<MaterializationRequest> queueEntry) {
	theQueue.add(queueEntry);
	completed = false;
    }
    
    public void completed() {
	completed = true;
    }
    
    public synchronized boolean isCompleted() {
	return theQueue.size() == 0 && completed;
    }
    
    public synchronized Vector<MaterializationRequest> dequeue() {
	if (theQueue.size() == 0)
	    return null;
	else
	    return theQueue.remove(0);
    }
    
    void initialize() {
	Vector<MaterializationRequest> theRequestList = null;
	
	// build chains of materialization dependencies
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("article_title", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("vernacular_title", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("e_location_id", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("abstract", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("author", "pmid,seqnum,equal_contrib,last_name,fore_name,initials,suffix,collective_name"));
	theRequestList.add(new MaterializationRequest("author_identifier", "*"));
	theRequestList.add(new MaterializationRequest("author_affiliation", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("language", "*"));
	queue(theRequestList);

	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("data_bank", "pmid,seqnum,data_bank_name"));
	theRequestList.add(new MaterializationRequest("accession_number", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("grant_info", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("publication_type", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("medline_journal_info", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("chemical", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("suppl_mesh_name", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("citation_subset", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("comments_corrections", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("gene_symbol", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("mesh_heading", "pmid,seqnum,major_topic,type,ui,descriptor_name"));
	theRequestList.add(new MaterializationRequest("mesh_qualifier", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("personal_name_subject", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("other_id", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("other_abstract", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("keyword", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("space_flight_mission", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("investigator", "pmid,seqnum,last_name,fore_name,initials,suffix"));
	theRequestList.add(new MaterializationRequest("investigator_identifier", "*"));
	theRequestList.add(new MaterializationRequest("investigator_affiliation", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("general_note", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("history", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("article_id", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("object", "*"));
	queue(theRequestList);
	
	theRequestList = new Vector<MaterializationRequest>();
	theRequestList.add(new MaterializationRequest("reference", "pmid,seqnum,title,unnest(xpath('.',citation)) as citation"));
	theRequestList.add(new MaterializationRequest("reference_article_id", "*"));
	queue(theRequestList);
	
    }

}
