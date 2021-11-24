package com.coursera.recommender.ratings;

import java.io.IOException;
import java.util.*;

import com.coursera.recommender.dto.EfficientRater;
import com.coursera.recommender.dto.Movie;
import com.coursera.recommender.dto.Rating;

public class SecondRatings {
    
    private ArrayList<Movie> myMovies;
    private ArrayList<EfficientRater> myRaters;
    
    public SecondRatings() {
        // default constructor
        this("ratedmoviesfull.csv", "ratings.csv");
    }
    
    public SecondRatings(String moviefile, String ratingsfile) {
        
        FirstRatings ratings = new FirstRatings();
        
        try {
        	
			myMovies = ratings.loadMovies(moviefile);
			myRaters = ratings.loadRaters(ratingsfile);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
    	
    	for (Movie movie : myMovies) {
    		double avgRatingById = getAverageByID(movie.getID(), minimalRaters);
    		if (avgRatingById > 0.0) {
    			avgRatings.add(new Rating(movie.getID(), avgRatingById));
    		}
    	}
    	
    	return avgRatings;
    }
    
    public String getTitle(String id) {
    	
    	String movieTitle = "";
    	boolean isMovieFound = false;
    	
    	for (Movie movie : myMovies) {
    		if (movie.getID().equals(id)) {
    			movieTitle = movie.getTitle();
    			isMovieFound = true;
    			break;
    		}
    	}
    	
    	if (!isMovieFound) {
    		movieTitle = "ID was not found";
    	}
    	
    	return movieTitle;
    }
    
    public String getID(String title) {
    	String id = "";
    	
    	for (Movie movie : myMovies) {
    		if (movie.getTitle().equalsIgnoreCase(title)) {
    			id = movie.getID();
    			break;
    		}
    	}
    	
    	if (id.isEmpty()) {
    		id = "NO SUCH TITLE.";
    	}
    	
    	return id;
    }
    
    public int getMovieSize() {
    	return myMovies.size();
    }
    
    public int getRaterSize() {
    	return myRaters.size();
    }
    
}
