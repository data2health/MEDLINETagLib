package edu.uiowa.medline.util;

public class Grant {
	String grant_id = null;
	int count = 0;

	public Grant(String grant_id, int count) {
		this.grant_id = grant_id;
		this.count = count;
	}

	public String getGrant_id() {
		return grant_id;
	}

	public void setGrant_id(String grant_id) {
		this.grant_id = grant_id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
