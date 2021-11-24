package com.coursera.recommender.filters;

import com.coursera.recommender.database.MovieDatabase;

public class YearAfterFilter implements Filter {
	
	private int myYear;
	
	public YearAfterFilter(int year) {
		myYear = year;
	}
	
	@Override
	public boolean satisfies(String id) {
		return MovieDatabase.getYear(id) >= myYear;
	}

}