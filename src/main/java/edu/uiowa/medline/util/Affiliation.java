package edu.uiowa.medline.util;

public class Affiliation {
	String name = null;
	String ror = null;
	int count = 0;
	
	public Affiliation(String name, String ror, int count) {
		this.name = name;
		this.ror = ror;
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRor() {
		return ror;
	}

	public void setRor(String ror) {
		this.ror = ror;
	}
}
