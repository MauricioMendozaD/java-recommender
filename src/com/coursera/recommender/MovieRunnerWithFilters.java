package com.coursera.recommender;

import java.util.ArrayList;
import java.util.Collections;

public class MovieRunnerWithFilters {
	
	private static String ratings = "ratings.csv";
	
	public static void main(String[] args) {
		MovieRunnerWithFilters mrwf = new MovieRunnerWithFilters();
		
		//mrwf.printAverageRatings();
		//mrwf.printAverageRatingsByYear();
		//mrwf.printAverageRatingsByGenre();
		//mrwf.printAverageRatingsByMinutes();
		//mrwf.printAverageRatingsByDirectors();
		//mrwf.printAverageRatingsByYearAfterAndGenre();
		mrwf.printAverageRatingsByDirectorsAndMinutes();
	}

	public void printAverageRatings() {
		
		ThirdRatings movieRatings = new ThirdRatings(ratings);
		
		System.out.println("Total raters loaded: " + movieRatings.getRaterSize());
		
		MovieDatabase.initialize("ratedmoviesfull.csv");
		System.out.println("Total movies loaded: " + MovieDatabase.size());
		
		int minimumRaters = 35;
		
		ArrayList<Rating> avgRatings = movieRatings.getAverageRatings(minimumRaters);
		
		System.out.println("Total movies with " + minimumRaters + " raters: " + avgRatings.size());
		Collections.sort(avgRatings);
		
		for (Rating rating : avgRatings) {
			System.out.println(rating.getValue() + " " + MovieDatabase.getMovie(rating.getItem()).getTitle());
		}
	}
	
	public void printAverageRatingsByYear() {
    	
    	YearAfterFilter yaf = new YearAfterFilter(2000);
    	int minRaters = 20;
    	ThirdRatings tr = new ThirdRatings(ratings);
    	
    	ArrayList<Rating> movies = tr.getAverageRatingsByFilter(minRaters, yaf);
    	System.out.println("Total movies filtered by year: " + movies.size());
    	
    	for (Rating movie : movies) {
    		System.out.println(movie.getValue() + " " + 
    				MovieDatabase.getYear(movie.getItem()) + " " + 
    				MovieDatabase.getTitle(movie.getItem()));
    	}
    }
	
	public void printAverageRatingsByGenre() {
		
		String genre = "Comedy";
		GenreFilter gf = new GenreFilter(genre);
		ThirdRatings tr = new ThirdRatings(ratings);
		int minimalRaters = 20;
		
		ArrayList<Rating> movies = tr.getAverageRatingsByFilter(minimalRaters, gf);
		
		System.out.println("Movies found for genre " + genre + ": " + movies.size());
		
		for (Rating movie : movies) {
			System.out.println(movie.getValue() + " " + MovieDatabase.getTitle(movie.getItem()));
			System.out.println("\t" + MovieDatabase.getGenres(movie.getItem()));
		}
	}
	
	public void printAverageRatingsByMinutes() {
		
		int minMinutes = 105;
		int maxMinutes = 135;
		int minRaters = 5;
		ThirdRatings tr = new ThirdRatings(ratings);
		
		MinutesFilter mf = new MinutesFilter(minMinutes, maxMinutes);
		
		ArrayList<Rating> movies = tr.getAverageRatingsByFilter(minRaters, mf);
		Collections.sort(movies);
		
		System.out.println("Movies found around " + minMinutes + " and " + maxMinutes + ": " + movies.size());
		
		for (Rating movie : movies ) {
			System.out.println(movie.getValue() + 
					" Time: " + MovieDatabase.getMinutes(movie.getItem()) + 
					" " + MovieDatabase.getTitle(movie.getItem()));
		}
		
	}
	
	public void printAverageRatingsByDirectors() {
		
		String directorFilter = "Clint Eastwood,Joel Coen,Martin Scorsese,Roman Polanski,Nora Ephron,Ridley Scott,Sydney Pollack";
		int minRaters = 4;
		ThirdRatings tr = new ThirdRatings(ratings);
		
		DirectorsFilter df = new DirectorsFilter(directorFilter);
		
		ArrayList<Rating> movies = tr.getAverageRatingsByFilter(minRaters, df);
		Collections.sort(movies);
		
		System.out.println("Movies found directed by " + directorFilter + ": " + movies.size());
		
		for (Rating movie : movies) {
			System.out.println(movie.getValue() + " " + MovieDatabase.getTitle(movie.getItem()));
			System.out.println("\t" + MovieDatabase.getDirector(movie.getItem()));
		}
		
	}
	
	public void printAverageRatingsByYearAfterAndGenre() {
		
		AllFilters af = new AllFilters();
		af.addFilter(new YearAfterFilter(1990));
		af.addFilter(new GenreFilter("Drama"));
		
		int minRaters = 8;
		ThirdRatings tr = new ThirdRatings(ratings);
		
		ArrayList<Rating> movies = tr.getAverageRatingsByFilter(minRaters, af);
		
		System.out.println(movies.size() + " movie matched.");
		
		for (Rating movie : movies) {
			System.out.println(movie.getValue() + " " 
					+ MovieDatabase.getYear(movie.getItem()) + " " 
							+ MovieDatabase.getTitle(movie.getItem()));
			System.out.println("\t" + MovieDatabase.getGenres(movie.getItem()));
		}
	}
	
	public void printAverageRatingsByDirectorsAndMinutes() {
		
		String directorFilter = "Clint Eastwood,Joel Coen,Tim Burton,Ron Howard,Nora Ephron,Sydney Pollack";
		int minRaters = 3;
		int minMinutes = 90;
		int maxMinutes = 180;
		
		AllFilters af = new AllFilters();
		af.addFilter(new DirectorsFilter(directorFilter));
		af.addFilter(new MinutesFilter(minMinutes, maxMinutes));
		
		ThirdRatings tr = new ThirdRatings(ratings);
		
		ArrayList<Rating> movies = tr.getAverageRatingsByFilter(minRaters, af);
		Collections.sort(movies);
		
		System.out.println(movies.size() + " movie matched.");
		
		for (Rating movie : movies) {
			System.out.println(movie.getValue() + " Time: " 
					+ MovieDatabase.getMinutes(movie.getItem()) + " " 
							+ MovieDatabase.getTitle(movie.getItem()));
			System.out.println("\t" + MovieDatabase.getDirector(movie.getItem()));
		}
	}
}
