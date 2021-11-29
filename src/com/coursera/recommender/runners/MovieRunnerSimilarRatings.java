package com.coursera.recommender.runners;

import java.util.ArrayList;
import java.util.Collections;

import com.coursera.recommender.database.MovieDatabase;
import com.coursera.recommender.database.RaterDatabase;
import com.coursera.recommender.dto.Rating;
import com.coursera.recommender.filters.AllFilters;
import com.coursera.recommender.filters.DirectorsFilter;
import com.coursera.recommender.filters.GenreFilter;
import com.coursera.recommender.filters.MinutesFilter;
import com.coursera.recommender.filters.YearAfterFilter;
import com.coursera.recommender.ratings.FourthRatings;

public class MovieRunnerSimilarRatings {
	
	public MovieRunnerSimilarRatings() {
		
	}
	
	public static void main(String[] args) {
		
		MovieRunnerSimilarRatings mrsr = new MovieRunnerSimilarRatings();
		
		//mrsr.printAverageRatings();
		//mrsr.printAverageRatingsByYearAfterAndGenre();
		//mrsr.printSimilarRatings();
		//mrsr.printSimilarRatingsByGenre();
		//mrsr.printSimilarRatingsByDirector();
		//mrsr.printSimilarRatingsByGenreAndMinutes();
		mrsr.printSimilarRatingsByYearAfterAndMinutes();
	}

	public void printAverageRatings() {
		
		FourthRatings fr = new FourthRatings();
		
		System.out.println("Total raters loaded: " + RaterDatabase.getRaters().size());
		System.out.println("Total movies loaded: " + MovieDatabase.size());
		
		int minimumRaters = 35;
		
		ArrayList<Rating> avgRatings = fr.getAverageRatings(minimumRaters);
		
		System.out.println("Total movies with " + minimumRaters + " raters: " + avgRatings.size());
		Collections.sort(avgRatings);
		
		for (Rating rating : avgRatings) {
			System.out.println(rating.getValue() + " " + MovieDatabase.getMovie(rating.getItem()).getTitle());
		}
	}
	
	public void printAverageRatingsByYearAfterAndGenre() {
		
		AllFilters af = new AllFilters();
		af.addFilter(new YearAfterFilter(1990));
		af.addFilter(new GenreFilter("Drama"));
		
		int minRaters = 8;
		FourthRatings fr = new FourthRatings();
		
		ArrayList<Rating> movies = fr.getAverageRatingsByFilter(minRaters, af);
		
		System.out.println(movies.size() + " movie matched.");
		
		for (Rating movie : movies) {
			System.out.println(movie.getValue() + " " 
					+ MovieDatabase.getYear(movie.getItem()) + " " 
							+ MovieDatabase.getTitle(movie.getItem()));
			System.out.println("\t" + MovieDatabase.getGenres(movie.getItem()));
		}
	}
	
	public void printSimilarRatings() {
		
		FourthRatings fr = new FourthRatings();
		
		ArrayList<Rating> similarRatings = fr.getSimilarRatings("71", 20, 5);
		
		System.out.println(similarRatings.size() + " movie matched.");
		
		for (Rating rating : similarRatings) {
			System.out.println(rating.getValue() + " Title: " + MovieDatabase.getTitle(rating.getItem()));
		}
	}
	
	public void printSimilarRatingsByGenre() {
		
		FourthRatings fr = new FourthRatings();
		
		ArrayList<Rating> similarRatings = fr.getSimilarRatingsByFilter("964", 20, 5, new GenreFilter("Mystery"));
		
		System.out.println(similarRatings.size() + " movie matched.");
		
		for (Rating rating : similarRatings) {
			System.out.println(rating.getValue() + " Title: " + MovieDatabase.getTitle(rating.getItem()));
			System.out.println("\t Genres: " + MovieDatabase.getGenres(rating.getItem()));
		}
	}
	
	public void printSimilarRatingsByDirector() {
		
		FourthRatings fr = new FourthRatings();
		
		String directors = "Clint Eastwood,J.J. Abrams,Alfred Hitchcock,Sydney Pollack,David Cronenberg,Oliver Stone,Mike Leigh";
		ArrayList<Rating> similarRatings = fr.getSimilarRatingsByFilter("120", 10, 2, new DirectorsFilter(directors));
		
		System.out.println(similarRatings.size() + " movie matched.");
		
		for (Rating rating : similarRatings) {
			System.out.println(rating.getValue() + " Title: " + MovieDatabase.getTitle(rating.getItem()));
			System.out.println("\t Directors: " + MovieDatabase.getDirector(rating.getItem()));
		}
	}
	
	public void printSimilarRatingsByGenreAndMinutes() {
		
		FourthRatings fr = new FourthRatings();
		
		String genreFilter = "Drama";
		int minRaters = 3;
		int minMinutes = 80;
		int maxMinutes = 160;
		
		AllFilters af = new AllFilters();
		af.addFilter(new GenreFilter(genreFilter));
		af.addFilter(new MinutesFilter(minMinutes, maxMinutes));
		
		ArrayList<Rating> similarRatings = fr.getSimilarRatingsByFilter("168", 10, minRaters, af);
		
		System.out.println(similarRatings.size() + " movie matched.");
		
		for (Rating rating : similarRatings) {
			System.out.println(rating.getValue() + 
					" Title: " + MovieDatabase.getTitle(rating.getItem()) + 
					" Minutes: " + MovieDatabase.getMinutes(rating.getItem()));
			System.out.println("\t Genres: " + MovieDatabase.getGenres(rating.getItem()));
		}
	}
	
	public void printSimilarRatingsByYearAfterAndMinutes() {
		
		FourthRatings fr = new FourthRatings();
		
		int yearFilter = 1975;
		int minRaters = 5;
		int minMinutes = 70;
		int maxMinutes = 200;
		
		AllFilters af = new AllFilters();
		af.addFilter(new YearAfterFilter(yearFilter));
		af.addFilter(new MinutesFilter(minMinutes, maxMinutes));
		
		ArrayList<Rating> similarRatings = fr.getSimilarRatingsByFilter("314", 10, minRaters, af);
		
		System.out.println(similarRatings.size() + " movie matched.");
		
		for (Rating rating : similarRatings) {
			System.out.println(rating.getValue() + 
					" Title: " + MovieDatabase.getTitle(rating.getItem()) + 
					" Year: "  + MovieDatabase.getYear(rating.getItem()) +
					" Minutes: " + MovieDatabase.getMinutes(rating.getItem()));
		}
	}
}
