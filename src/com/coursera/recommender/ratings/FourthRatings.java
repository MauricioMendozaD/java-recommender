package com.coursera.recommender.ratings;


import java.util.ArrayList;
import java.util.Collections;

import com.coursera.recommender.database.MovieDatabase;
import com.coursera.recommender.database.RaterDatabase;
import com.coursera.recommender.dto.Rater;
import com.coursera.recommender.dto.Rating;
import com.coursera.recommender.filters.Filter;
import com.coursera.recommender.filters.TrueFilter;

public class FourthRatings {
	
	public FourthRatings() {
		RaterDatabase.initialize("ratings.csv");
		MovieDatabase.initialize("ratedmoviesfull.csv");
	}

	/**
     * Returns the average movie rating that has a minimal raters.
     * @param id movie id.
     * @param minimalRaters minimal amount of raters.
     * @return 0.0 if the movie hasn't the minimum amount of ratings, otherwise, returns the average rating.
     */
    private double getAverageByID(String id, int minimalRaters) {
    	
    	double avgMovieRating = 0.0;
    	ArrayList<Double> movieRatings = new ArrayList<>();
    	ArrayList<Rater> myRaters = RaterDatabase.getRaters();
    	
    	for (Rater rater : myRaters) {
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
    
    private double dotProduct(Rater me, Rater r) {
    	
    	double scaleTranslated = 0.0;
    	double[] similarities = new double[me.getItemsRated().size()];
    	
    	int i = 0;
    	for(String myRatedMovie : me.getItemsRated()) {
    		if (r.getItemsRated().contains(myRatedMovie)) {
    			similarities[i] = (me.getRating(myRatedMovie) - 5) * (r.getRating(myRatedMovie) - 5);
    			i++;
    		}
    	}
    	
    	for (int j = 0; j < similarities.length ; j++) {
    		scaleTranslated += similarities[j];
    	}
    	
    	return scaleTranslated;
    }
    
    private ArrayList<Rating> getSimilarities(String id) {
    	
    	ArrayList<Rating> ratings = new ArrayList<>();
    	ArrayList<Rater> raters = RaterDatabase.getRaters();
    	Rater me = RaterDatabase.getRater(id);
    	
    	for (Rater rater : raters) {
    		if (!rater.getID().equals(id) ) {
    			if (dotProduct(me, rater) > 0) {
        			ratings.add(new Rating(rater.getID(), dotProduct(me, rater)));
        		}
    		}
    	}
    	
    	Collections.sort(ratings, Collections.reverseOrder());
    	
    	return ratings;
    }
    
    public ArrayList<Rating> getSimilarRatings(String id, int numSimilarRaters, int minimalRaters) {
    	
    	ArrayList<Rating> similarRatings = new ArrayList<>();
    	ArrayList<Rating> avgRatings = getAverageRatings(minimalRaters);
    	ArrayList<Rating> avgRatingsTemp = new ArrayList<>();
    	ArrayList<Rating> ratingsWeights = new ArrayList<>();
    	
    	// Obtiene un maximo de evaluadores similares
    	ArrayList<Rating> similarRaters = getSimilarities(id);
    	similarRaters = new ArrayList<Rating>(similarRaters.subList(0, (similarRaters.size() >= numSimilarRaters) ? numSimilarRaters - 1 : similarRaters.size()));
    	
    	// Filtra las peliculas que tienen un minimo de evaluadores 
    	// que estén dentro de la lista de los evaluadores con mayor similitud con el del "id". 
    	for (Rating avgRating : avgRatings) {
    		int movieCounter = 0;
    		for (Rating similarRater : similarRaters) {
    			if (RaterDatabase.getRater(similarRater.getItem()).getItemsRated().contains(avgRating.getItem())) {
    				movieCounter++;
    			}
    			
    			if (movieCounter == minimalRaters) {
    				avgRatingsTemp.add(avgRating);
    				break;
    			}
    		}
    	}
    	
    	for (Rating avgRating : avgRatingsTemp) {
    		for (Rating similarRater : similarRaters) {
    			Rater rater = RaterDatabase.getRater(similarRater.getItem());
    			if (rater.getItemsRated().contains(avgRating.getItem())) {
    				double avgWeight = rater.getRating(avgRating.getItem()) * similarRater.getValue();
    				ratingsWeights.add(new Rating(avgRating.getItem(), avgWeight));
    			}
    			
    		}
    	}
    	
    	ArrayList<String> moviesCalculated = new ArrayList<>();
    	for (Rating ratingWeight : ratingsWeights) {
    		double avgRatingWeight = 0.0;
    		int count = 0;
    		if (!moviesCalculated.contains(ratingWeight.getItem())) {
    			for (Rating movieWeight : ratingsWeights) {
    				if (ratingWeight.getItem().equals(movieWeight.getItem())) {
    					avgRatingWeight += movieWeight.getValue();
    					count++;
    				}
    			}
    			
    			moviesCalculated.add(ratingWeight.getItem());
    			avgRatingWeight = avgRatingWeight/count;
    			similarRatings.add(new Rating(ratingWeight.getItem(), avgRatingWeight));
    		}
    	}
    	
    	Collections.sort(similarRatings, Collections.reverseOrder());
    	
    	return similarRatings;
    }
    
    public ArrayList<Rating> getSimilarRatingsByFilter(String id, int numSimilarRaters, int minimalRaters, Filter filterCriteria) {
    	
    	ArrayList<Rating> similarRatings = new ArrayList<>();
    	//ArrayList<Rating> similarRaters = new ArrayList<Rating>(getSimilarities(id).subList(0, numSimilarRaters - 1));
    	ArrayList<Rating> avgRatings = getAverageRatingsByFilter(minimalRaters, filterCriteria);
    	ArrayList<Rating> avgRatingsTemp = new ArrayList<>();
    	ArrayList<Rating> ratingsWeights = new ArrayList<>();
    	
    	// Obtiene un maximo de evaluadores similares
    	ArrayList<Rating> similarRaters = getSimilarities(id);
    	similarRaters = new ArrayList<Rating>(similarRaters.subList(0, (similarRaters.size() >= numSimilarRaters) ? numSimilarRaters - 1 : similarRaters.size()));
    	
    	
    	for (Rating avgRating : avgRatings) {
    		int movieCounter = 0;
    		for (Rating similarRater : similarRaters) {
    			if (RaterDatabase.getRater(similarRater.getItem()).getItemsRated().contains(avgRating.getItem())) {
    				movieCounter++;
    			}
    			
    			if (movieCounter == minimalRaters) {
    				avgRatingsTemp.add(avgRating);
    				break;
    			}
    		}
    	}
    	
    	for (Rating avgRating : avgRatingsTemp) {
    		for (Rating similarRater : similarRaters) {
    			Rater rater = RaterDatabase.getRater(similarRater.getItem());
    			if (rater.getItemsRated().contains(avgRating.getItem())) {
    				double avgWeight = rater.getRating(avgRating.getItem()) * similarRater.getValue();
    				ratingsWeights.add(new Rating(avgRating.getItem(), avgWeight));
    			}
    			
    		}
    	}
    	
    	ArrayList<String> moviesCalculated = new ArrayList<>();
    	for (Rating ratingWeight : ratingsWeights) {
    		double avgRatingWeight = 0.0;
    		int count = 0;
    		if (!moviesCalculated.contains(ratingWeight.getItem())) {
    			for (Rating movieWeight : ratingsWeights) {
    				if (ratingWeight.getItem().equals(movieWeight.getItem())) {
    					avgRatingWeight += movieWeight.getValue();
    					count++;
    				}
    			}
    			
    			moviesCalculated.add(ratingWeight.getItem());
    			avgRatingWeight = avgRatingWeight/count;
    			similarRatings.add(new Rating(ratingWeight.getItem(), avgRatingWeight));
    		}
    	}
    	
    	Collections.sort(similarRatings, Collections.reverseOrder());
    	
    	return similarRatings;
    }
}
