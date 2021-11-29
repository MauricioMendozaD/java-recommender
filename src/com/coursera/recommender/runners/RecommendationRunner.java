package com.coursera.recommender.runners;

import java.util.ArrayList;

import com.coursera.recommender.database.MovieDatabase;
import com.coursera.recommender.database.RaterDatabase;
import com.coursera.recommender.dto.Rating;
import com.coursera.recommender.filters.AllFilters;
import com.coursera.recommender.filters.MinutesFilter;
import com.coursera.recommender.filters.YearAfterFilter;
import com.coursera.recommender.ratings.FourthRatings;

public class RecommendationRunner implements Recommender {

	@Override
	public ArrayList<String> getItemsToRate() {
		
		ArrayList<String> moviesToRate = new ArrayList<>();
		FourthRatings fr = new FourthRatings();
		
		int yearFilter = 1975;
		int minRaters = 5;
		int minMinutes = 70;
		int maxMinutes = 200;
		
		AllFilters af = new AllFilters();
		af.addFilter(new YearAfterFilter(yearFilter));
		af.addFilter(new MinutesFilter(minMinutes, maxMinutes));
		
		ArrayList<Rating> similarRatings = fr.getSimilarRatingsByFilter("314", 10, minRaters, af);
		
		int limit = 0;
		if (similarRatings.size() > 20) {
			limit = 19;
		} else if (similarRatings.size() > 0) {
			limit = similarRatings.size() - 2;
		}
		
		similarRatings = new ArrayList<Rating>(similarRatings.subList(0, limit));
		
		for (Rating similarRating : similarRatings) {
			moviesToRate.add(similarRating.getItem());
		}
		
		return moviesToRate;
	}

	@Override
	public void printRecommendationsFor(String webRaterID) {
		
		FourthRatings fr = new FourthRatings();
		
		int yearFilter = 1975;
		int minRaters = 5;
		int minMinutes = 90;
		int maxMinutes = 130;
		
		AllFilters af = new AllFilters();
		af.addFilter(new YearAfterFilter(yearFilter));
		af.addFilter(new MinutesFilter(minMinutes, maxMinutes));
		
		if (RaterDatabase.getRater(webRaterID) != null) {
			
			ArrayList<Rating> similarRatings = new ArrayList<Rating>(fr.getSimilarRatingsByFilter(webRaterID, 10, minRaters, af));
			
			if (similarRatings.size() > 0) {
				
				int limit = 0;
				if (similarRatings.size() > 20) {
					limit = 19;
				} else if (similarRatings.size() > 0) {
					limit = similarRatings.size() - 1;
				}
				
				similarRatings = new ArrayList<Rating>(similarRatings.subList(0, limit));
				
				System.out.println("<link href=\"https://fonts.googleapis.com/css?family=Syncopate\" rel=\"stylesheet\">"
						         + "<link href=\"https://fonts.googleapis.com/css?family=Roboto|Syncopate\" rel=\"stylesheet\">"
						             + "<div id=\"header\"><h2>Recommended Movies:</h2></div>"
						             + "<table class=\"outside_table\">"
						                 + "<tr class=\"table-header\">"
						                 	+ "<th>Poster</th>"
						                 	+ "<th class=\"movie_title\">Title</th>"
						                 	+ "<th class=\"movie_title\">Genre</th>"
						                 	+ "<th class=\"movie_title\">Year</th>"
						                 	+ "<th class=\"movie_title\">Country</th>"
						                 	+ "<th class=\"movie_title\">Minutes</th>"
						                 	+ "<th class=\"movie_title\">Directors</th>"
						                 + "</tr>");
				
				for (Rating similarRating : similarRatings) {
	
					System.out.println("<tr>");
					//System.out.println("<td class = \"pic\">");
					
					//if(poster.length() > 3){
	                System.out.println(	"<td class=\"pic\"><img src=\"" + MovieDatabase.getPoster(similarRating.getItem()) + "\" target=_blank></td>");
	                 //}
	                  
	                System.out.println(	"<td><h3>"+ MovieDatabase.getTitle(similarRating.getItem())    + "</h3></td>");
	                System.out.println(	"<td>"    + MovieDatabase.getGenres(similarRating.getItem())   + "</td>");
	                System.out.println(	"<td>"    + MovieDatabase.getYear(similarRating.getItem())     + "</td>");
	                System.out.println(	"<td>"    + MovieDatabase.getCountry(similarRating.getItem())  + "</td>");
	                System.out.println(	"<td>"    + MovieDatabase.getMinutes(similarRating.getItem())  + "</td>");
	                System.out.println(	"<td>"    + MovieDatabase.getDirector(similarRating.getItem()) + "</td>");
	                System.out.println("</tr>");
				}
	
				System.out.println("</table>");
				
			} else {
				System.out.println("<h2>Recommendations for rater " + webRaterID + " wasn't found.</h2>");
			}
		} else {
			System.out.println("<h2>Rater " + webRaterID + " doesn't exist.</h2>");
		}
		
	}

}
