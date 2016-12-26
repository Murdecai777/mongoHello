import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 24.12.2016.
 */
public class MovieInfo {
    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getReleased() {
        return released;
    }

    public String getGenre() {
        return genre;
    }

    public String getActors() {
        return actors;
    }

    public String getPlot() {
        return plot;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getType() {
        return type;
    }

    private String title ;
    private String year;
    private String released;
    private String genre;
    private String actors;
    private String plot;
    private String imdbRating;
    private String type;

    MovieInfo (JSONObject object) {
        try {

            title = object.get("Title").toString();
            year = object.get("Year").toString();
            released = object.get("Released").toString();
            genre = object.get("Genre").toString();
            actors = object.get("Actors").toString();
            plot = object.get("Plot").toString();
            imdbRating = object.get("imdbRating").toString();
            type = object.get("Type").toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
