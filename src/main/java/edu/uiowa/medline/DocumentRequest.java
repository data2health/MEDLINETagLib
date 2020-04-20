package edu.uiowa.medline;

import org.dom4j.Element;

public class DocumentRequest {
    int pmid = 0;
    String fileName = null;
    Element document = null;
    
    public DocumentRequest(int pmid, String title) {
	this.pmid = pmid;
	this.fileName = title;
    }
    
    public DocumentRequest(String fileName, Element document) {
	this.fileName = fileName;
	this.document = document;
    }
    
    public int getPMID() {
	return pmid;
    }
    
    public String getTitle() {
	return fileName;
    }
}
