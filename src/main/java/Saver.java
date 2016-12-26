import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;


public class Saver {

    private DBCollection table;

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    private String genre = "N/A";
    private String type = "N/A";
    private String rating = "N/A";
    private String actors = "N/A";
    private String released = "N/A";
    private String plot = "N/A";

    Saver(DBCollection table){
        this.table = table;
    }

    boolean save(String title, String year){
        BasicDBObject document = new BasicDBObject();
        document.put("Title", title);
        document.put("Genre", genre);
        document.put("Type", type);
        document.put("Year", year);
        document.put("Rating", rating);
        document.put("Actors", actors);
        document.put("Released", released);
        document.put("Plot", plot);


        return tableHasLike(table, document);

    }


    private boolean tableHasLike(DBCollection table, BasicDBObject document) {

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("Title", document.get("Title"));
        searchQuery.put("Year", document.get("Year"));
        DBCursor cursor = table.find(searchQuery);
        if (cursor.hasNext()){
            while (cursor.hasNext()) {
                update((BasicDBObject)cursor.next(), document);
            }
            return true;
        }
        return false;
    }

    void update(BasicDBObject from, BasicDBObject to){
        BasicDBObject updateObj = new BasicDBObject();
        updateObj.put("$set", to);
        table.update(from, updateObj);
    }
}
