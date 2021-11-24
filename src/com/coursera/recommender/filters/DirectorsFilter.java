package com.coursera.recommender.filters;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.coursera.recommender.database.MovieDatabase;

public class DirectorsFilter implements Filter {
	
	private ArrayList<String> myDirectors;
	
	public DirectorsFilter(String directors) {
		StringTokenizer tokens = new StringTokenizer(directors, ",");
		myDirectors = new ArrayList<>();
		
		while (tokens.hasMoreElements()) {
			myDirectors.add(tokens.nextToken().trim());
		}
	}

	@Override
	public boolean satisfies(String id) {
		String movieDirectors = MovieDatabase.getDirector(id).toLowerCase(); 
		for (String director : myDirectors) {
			if (movieDirectors.contains(director.toLowerCase())) {
				return true;
			}
		}
		
		return false;
	}

}
