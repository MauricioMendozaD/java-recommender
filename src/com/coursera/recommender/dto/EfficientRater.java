package com.coursera.recommender.dto;

import java.util.ArrayList;
import java.util.HashMap;

public class EfficientRater implements Rater {

	private String myID;
    //private ArrayList<Rating> myRatings;
    private HashMap<String,Rating> myRatings;

    public EfficientRater(String id) {
        myID = id;
        myRatings = new HashMap<>();
    }

    public void addRating(String item, double rating) {
        myRatings.put(item, new Rating(item, rating));
    }

    /*public boolean hasRating(String item) {
        for(int k=0; k < myRatings.size(); k++){
            if (myRatings.get(k).getItem().equals(item)){
                return true;
            }
        }
        
        return false;
    }*/
    
    public boolean hasRating(String item) {
    	if (myRatings.get(item) != null)
    		return true;
    	
    	return false;
    }

    public String getID() {
        return myID;
    }

    /*public double getRating(String item) {
        for(int k=0; k < myRatings.size(); k++){
            if (myRatings.get(k).getItem().equals(item)){
                return myRatings.get(k).getValue();
            }
        }
        
        return -1;
    }*/
    
    public double getRating(String item) {
    	if (myRatings.containsKey(item)) {
    		return myRatings.get(item).getValue();
    	}
    	
    	return -1;
    }

    public int numRatings() {
        return myRatings.size();
    }

    /*public ArrayList<String> getItemsRated() {
        ArrayList<String> list = new ArrayList<String>();
        for(int k=0; k < myRatings.size(); k++){
            list.add(myRatings.get(k).getItem());
        }
        
        return list;
    }*/
    
    public ArrayList<String> getItemsRated() {
    	ArrayList<String> list = new ArrayList<>();
    	myRatings.forEach((String key, Rating rating) -> {
    		list.add(key);
    	});
    	
    	return list;
    }
}
