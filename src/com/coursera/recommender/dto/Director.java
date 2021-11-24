package com.coursera.recommender.dto;

public class Director {
	
	String name;
	int movieCount;
	
	public Director(String name) {
		super();
		this.name = name;
	}

	public Director(String name, int movieCount) {
		super();
		this.name = name;
		this.movieCount = movieCount;
	}

	public Director() {
		super();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getMovieCount() {
		return movieCount;
	}
	
	public void setMovieCount(int movieCount) {
		this.movieCount = movieCount;
	}
	
}
