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
        testLoadMovies();
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
    
    public static void testLoadMovies() {
        
    try {
        ArrayList<Movie> movieList = loadMovies("ratedmovies_short.csv");
        
        System.out.println("Total Movies: " + movieList.size());
        
        for (Movie movie : movieList) {
            System.out.println(movie.toString());
        }
        
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    
    }

}
