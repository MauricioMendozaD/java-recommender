package com.coursera.recommender.ratings;

import java.io.IOException;
import java.util.ArrayList;

import com.coursera.recommender.database.MovieDatabase;
import com.coursera.recommender.dto.EfficientRater;
import com.coursera.recommender.dto.Rating;
import com.coursera.recommender.filters.Filter;
import com.coursera.recommender.filters.TrueFilter;

public class ThirdRatings {

	private ArrayList<EfficientRater> myRaters;
    
    public ThirdRatings() {
        // default constructor
        this("ratings_short.csv");
    }
    
    public ThirdRatings(String ratingsfile) {
        
        FirstRatings ratings = new FirstRatings();
        
        try {
        	
			myRaters = ratings.loadRaters("data/" + ratingsfile);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    }
    
    /**
     * Returns the average movie rating that has a minimal raters.
     * @param id movie id.
     * @param minimalRaters minimal amount of raters.
     * @return 0.0 if the movie hasn't the minimum amount of ratings, otherwise, returns the average rating.
     */
    private double getAverageByID(String id, int minimalRaters) {
    	
    	double avgMovieRating = 0.0;
    	//ArrayList<Rating> movieRatings = new ArrayList<>();
    	ArrayList<Double> movieRatings = new ArrayList<>();
    	
    	for (EfficientRater rater : myRaters) {
    		if (rater.getItemsRated().contains(id)) {
                movieRatings.add(rater.getRating(id));
            }
    	}
    	
    	if (minimalRaters <= movieRatings.size()) {
    		for (Double rating : movieRatings) {
    			avgMovieRating += rating;
    		}
    		
    		avgMovieRating = avgMovieRating/movieRatings.size();
    	}
    	
    	return avgMovieRating;
    }
    
    public ArrayList<Rating> getAverageRatings(int minimalRaters) {
    	
    	ArrayList<Rating> avgRatings = new ArrayList<>();
    	ArrayList<String> myMovies = MovieDatabase.filterBy(new TrueFilter());
    	
    	for (String movie : myMovies) {
    		double avgRatingById = getAverageByID(movie, minimalRaters);
    		if (avgRatingById > 0.0) {
    			avgRatings.add(new Rating(movie, avgRatingById));
    		}
    	}
    	
    	return avgRatings;
    }
    
    public ArrayList<Rating> getAverageRatingsByFilter(int minimalRaters, Filter filterCriteria) {
    	
    	ArrayList<Rating> moviesWithMinimumRaters = getAverageRatings(minimalRaters);
    	ArrayList<String> moviesIds = MovieDatabase.filterBy(filterCriteria);
    	
    	ArrayList<Rating> response = new ArrayList<>();
    	
		for (Rating rating : moviesWithMinimumRaters) {
			if (moviesIds.contains(rating.getItem())) {
				response.add(rating);
			}
		}
	
    	return response;
    }
    
    public int getRaterSize() {
    	return myRaters.size();
    }
}
