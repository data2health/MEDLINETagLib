package edu.uiowa.medline;

import org.dom4j.Element;

public class DocumentRequest {
    String fileName = null;
    Element document = null;
    
    public DocumentRequest(String fileName, Element document) {
	this.fileName = fileName;
	this.document = document;
    }
}
