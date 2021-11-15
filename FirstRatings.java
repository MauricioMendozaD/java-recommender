import edu.duke.*;
import java.util.*;
import org.apache.commons.csv.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Write a description of class FirstRatings here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FirstRatings {

    public static void main(String[] args) {
        //testLoadMovies();
        testLoadRaters();
    }

    public static ArrayList<Movie> loadMovies(String filename) throws FileNotFoundException, IOException {

        ArrayList<Movie> movieList = new ArrayList<Movie>();
        String[] HEADERS = {"id", "title", "year", "genre", "director", "country", "minutes", "poster"};

        Reader in = new FileReader("./data/" + filename);

        CSVParser csvParser = new CSVParser(in, CSVFormat.DEFAULT
                .withHeader(HEADERS)
                .withIgnoreHeaderCase()
                .withFirstRecordAsHeader()
                .withTrim());

        Movie movie = null;

        for (CSVRecord record : csvParser) {
            movie = new Movie(record.get("id"), 
                record.get("title"), 
                record.get("year"), 
                record.get("genre"),
                record.get("director"),
                record.get("country"),
                record.get("poster"),
                Integer.parseInt(record.get("minutes")));

            movieList.add(movie);
        }

        csvParser.close();

        return movieList;
    }

    public static ArrayList<Rater> loadRaters(String filename) throws IOException {
        ArrayList<Rater> raterList = new ArrayList<Rater>();
        String[] HEADERS = {"rater_id", "movie_id", "rating", "time"};

        Reader in = new FileReader("./data/" + filename);
        CSVParser csvParser = new CSVParser(in, CSVFormat.DEFAULT
                .withHeader(HEADERS)
                .withIgnoreHeaderCase()
                .withFirstRecordAsHeader()
                .withTrim());

        Rater rater = null;

        // Obtiene todos los raters
        for (CSVRecord record : csvParser) {

            rater = new Rater(record.get("rater_id"));
            boolean raterFound = false;

            // Busca si en la lista de raters ya existe.
            if (!raterList.isEmpty()) {
                for (int i = 0; i < raterList.size() ; i++) {
                    if (raterList.get(i).getID().equals(rater.getID())) {
                        raterFound = true;
                        raterList.get(i).addRating(record.get("movie_id"), Double.valueOf(record.get("rating")));
                        break;
                    }
                }
            }

            // Si no existia en la lista, lo agrega
            if (!raterFound) {
                rater.addRating(record.get("movie_id"), Double.valueOf(record.get("rating")));
                raterList.add(rater);
            }

        }

        csvParser.close();

        return raterList;

    }

    public static void testLoadMovies() {

        byte comedyMovies = 0;
        byte longMovies = 0;

        try {
            ArrayList<Movie> movieList = loadMovies("ratedmovies_short.csv");

            System.out.println("Total Movies: " + movieList.size());

            for (Movie movie : movieList) {

                System.out.println(movie.toString());

                if (movie.getGenres().toLowerCase().contains("comedy")) {
                    comedyMovies++;
                }

                if (movie.getMinutes() > 150) {
                    longMovies++;
                }
            }

            System.out.println("Comedy movies: " + comedyMovies);
            System.out.println("Long Movies (>150m): " + longMovies);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void testLoadRaters() {

        String raterId = "2";
        int rates = 0;
        ArrayList<Rater> topRaters = new ArrayList<>();
        Rater newTopRater = null;
        boolean newTopRaterFound = false;
        String movieRatedId = "1798709";
        int movieRates = 0;
        ArrayList<String> moviesRated = new ArrayList<>();
        boolean movieRatedFound = false;
        String movieNotFound = "";

        try {
            ArrayList<Rater> raterList = loadRaters("ratings_short.csv");

            System.out.println("Total raters: " + raterList.size());

            for (Rater rater : raterList) {
                newTopRaterFound = false;

                System.out.println("Rater ID: " + rater.getID() + " Ratings made: " + rater.numRatings());

                if (rater.getID().equals(raterId)) {
                    rates = rater.numRatings();
                }

                if (topRaters.isEmpty()) {
                    topRaters.add(rater);
                } else {
                    for (Rater topRater : topRaters) {
                        if (rater.numRatings() > topRater.numRatings()) {
                            topRaters.clear();
                            newTopRater = rater;
                            newTopRaterFound = true;
                            break;

                        } else if (rater.numRatings() == topRater.numRatings()) {
                            newTopRater = rater;
                            newTopRaterFound = true;
                        }
                    }

                    if(newTopRaterFound)
                        topRaters.add(newTopRater);

                }

                for (String movieRated : rater.getItemsRated()) {
                    if (movieRated.equals(movieRatedId)) {
                        movieRates++;
                    }

                    movieRatedFound = false;

                    if (moviesRated.isEmpty()) {
                        moviesRated.addAll(rater.getItemsRated());
                    } else {
                        for (String ratedMovie : moviesRated) {
                            if (movieRated.equals(ratedMovie)) {
                                movieRatedFound = true;
                                break;
                            } else {
                                movieNotFound = movieRated;
                            }
                        }

                        if (!movieRatedFound) {
                            moviesRated.add(movieNotFound);
                        }
                    }
                }
            }

            System.out.println("Total rates by selected rater " + raterId + " = " + rates);

            int i = 1;
            for (Rater topRater : topRaters) {
                System.out.println(i + " Top Rater: " + topRater.getID() + ", Total Movies: " + topRater.numRatings());
                i++;
            }

            System.out.println("Movie '" + movieRatedId + "' was rated " + movieRates + " times.");

            System.out.println("Total Movies Rated: " + moviesRated.size());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
