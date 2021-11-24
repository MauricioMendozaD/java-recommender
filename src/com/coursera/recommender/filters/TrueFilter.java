package com.coursera.recommender.filters;

public class TrueFilter implements Filter {
	
	@Override
	public boolean satisfies(String id) {
		return true;
	}

}
