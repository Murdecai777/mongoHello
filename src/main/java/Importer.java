import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.json.*;

public class Importer {

    public List<MovieInfo> getMovieInfoList(String targetURL, String urlParameters) {
        List<MovieInfo> movies = new ArrayList<MovieInfo>();
        URL OMDB = null;
        try {
            OMDB = new URL(targetURL+urlParameters);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection OMDBConnection = null;
        try {
            OMDBConnection = OMDB != null ? OMDB.openConnection() : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;
        try {
            assert (OMDBConnection != null ? OMDBConnection.getInputStream() : null) != null;
            in = new BufferedReader(
                    new InputStreamReader(
                            OMDBConnection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String inputLine;
        JSONObject obj;

        try {
            assert in != null;
            while ((inputLine = in.readLine()) != null) {
                obj = new JSONObject(inputLine);
                if(!obj.toString().contains("Error")) {
                    movies.add(new MovieInfo(obj));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }

    void addMovieInfo(MovieInfo movieInfo ,DBCollection table){
        BasicDBObject document = new BasicDBObject();
        document.put("Title", movieInfo.getTitle());
        document.put("Genre", movieInfo.getGenre());
        document.put("Type", movieInfo.getType());
        document.put("Year", movieInfo.getYear());
        document.put("Rating", movieInfo.getImdbRating());
        document.put("Actors", movieInfo.getActors());
        document.put("Released", movieInfo.getReleased());
        document.put("Plot", movieInfo.getPlot());

        if(!isTableContains(table, document)) {
            table.insert(document);
        }
    }

    void addMovieInfos(List<MovieInfo> movieInfoList , DBCollection table){
        BasicDBObject document = new BasicDBObject();

        for(MovieInfo movieInfo : movieInfoList) {
            document.put("Title", movieInfo.getTitle());
            document.put("Genre", movieInfo.getGenre());
            document.put("Type", movieInfo.getType());
            document.put("Year", movieInfo.getYear());
            document.put("Rating", movieInfo.getImdbRating());
            document.put("Actors", movieInfo.getActors());
            document.put("Released", movieInfo.getReleased());
            document.put("Plot", movieInfo.getPlot());

            if(!isTableContains(table, document)) {
                table.insert(document);
            }
        }
    }

    private boolean isTableContains(DBCollection table, BasicDBObject document) {
        DBCursor cursor = table.find(document);
        if(cursor.hasNext()){
            return true;
        }
        return false;
    }

    String getURL(){
        return "https://www.omdbapi.com";
    }

    String getParameters(String title, String year){
        if(title.contains(" ")){
            title = title.replace(" ", "%20");
        }
        return "/?t=" + title + "&y=" + year +"&plot=short&r=json";
    }
}
