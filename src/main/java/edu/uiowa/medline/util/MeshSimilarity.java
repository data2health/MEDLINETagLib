package edu.uiowa.medline.util;

import edu.uiowa.similarity.cosineVectorSimilarity;

public class MeshSimilarity extends cosineVectorSimilarity {

	@Override
	protected int compare(Object arg0, Object arg1) {
		Term term1 = (Term)arg0;
		Term term2 = (Term)arg1;
		return term1.getTerm().compareTo(term2.getTerm());
	}

	@Override
	protected double elementFrequency(Object arg0) {
		return ((Term) arg0).frequency;
	}

}
