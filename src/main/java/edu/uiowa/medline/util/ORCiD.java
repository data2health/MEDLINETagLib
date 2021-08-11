package edu.uiowa.medline.util;

public class ORCiD {
	String orcid_id = null;
	int count = 0;

	public ORCiD(String orcid_id, int count) {
		this.orcid_id = orcid_id;
		this.count = count;
	}

	public String getOrcid_id() {
		return orcid_id;
	}

	public void setOrcid_id(String orcid_id) {
		this.orcid_id = orcid_id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
