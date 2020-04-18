package edu.uiowa.medline;

public class MaterializationRequest {
    String tableName = null;
    String attributeList = null;
    
    public MaterializationRequest(String tableName, String attributeList) {
	this.tableName = tableName;
	this.attributeList = attributeList;
    }
}
