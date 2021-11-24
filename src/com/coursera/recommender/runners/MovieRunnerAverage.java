package com.coursera.recommender.runners;

import java.util.ArrayList;
import java.util.Collections;

import com.coursera.recommender.dto.Rating;
import com.coursera.recommender.ratings.SecondRatings;

public class MovieRunnerAverage {
	
	public static void main(String[] args) {
		
		MovieRunnerAverage mra = new MovieRunnerAverage();
		
		mra.printAverageRatings();
		mra.getAverageRatingOneMovie();
	}

	public void printAverageRatings() {
		
		SecondRatings movieRatings = new SecondRatings();
		
		System.out.println("Total movies loaded: " + movieRatings.getMovieSize());
		System.out.println("Total raters loaded: " + movieRatings.getRaterSize());
		
		int minimumRaters = 3;
		
		ArrayList<Rating> avgRatings = movieRatings.getAverageRatings(minimumRaters);
		
		Collections.sort(avgRatings);
		
		for (Rating rating : avgRatings) {
			System.out.println(rating.getValue() + " " + movieRatings.getTitle(rating.getItem()));
		}
	}
	
	public void getAverageRatingOneMovie() {
		
		SecondRatings movieRatings = new SecondRatings();
		String movieTitle = "The Godfather";
		boolean hasMovieRating = false;
		double movieRating = 0.0;
		
		String movieId = movieRatings.getID(movieTitle);
		
		if (!movieId.equalsIgnoreCase("no such title.") ) {
			for (Rating rating : movieRatings.getAverageRatings(1)) {
				
				if (rating.getItem().equals(movieId)) {
					hasMovieRating = true;
					movieRating = rating.getValue();
					break;
				}
			}
			
			if (hasMovieRating) {
				System.out.println("AVG Rating for " + movieTitle + " is: " + movieRating);
			} else {
				System.out.println("AVG Rating for " + movieTitle + " was not found.");
			}
			
		} else {
			System.out.println(movieId);
		}
		
	}
}
