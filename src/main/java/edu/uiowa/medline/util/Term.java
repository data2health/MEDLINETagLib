package edu.uiowa.medline.util;

public class Term {
	String term = null;
	double frequency = 0.0;
	
	public Term (String term, double frequency) {
		this.term = term;
		this.frequency = frequency;
	}
	
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public double getFrequency() {
		return frequency;
	}
	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

}
