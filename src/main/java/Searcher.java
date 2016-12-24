import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import java.util.ArrayList;
import java.util.List;


public class Searcher {

    private DBCollection table;

    Searcher(DBCollection table){
        this.table = table;
    }

    List<BasicDBObject> findObjects(String title, String year){
        List<BasicDBObject> objects = new ArrayList<BasicDBObject>();

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("Title", title);
        searchQuery.put("Year", year);

        DBCursor cursor = table.find(searchQuery);

        if (!cursor.hasNext()) {
            tryImport(title, year);
            cursor = table.find(searchQuery);
        }

        if (!cursor.hasNext()) {
            searchQuery.put("Title", new BasicDBObject("$regex", title));
            cursor = table.find(searchQuery);
        }

        while (cursor.hasNext()) {
            objects.add((BasicDBObject) cursor.next());
        }
        return objects;
    }

    List<BasicDBObject> findObjects(String title){
        List<BasicDBObject> objects = new ArrayList<BasicDBObject>();

        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("Title", title);

        DBCursor cursor = table.find(searchQuery);

        if (!cursor.hasNext()) {
            tryImport(title);
            cursor = table.find(searchQuery);
        }

        if (!cursor.hasNext()) {
            searchQuery.put("Title", new BasicDBObject("$regex", title));
            cursor = table.find(searchQuery);
        }

        while (cursor.hasNext()) {
            objects.add((BasicDBObject) cursor.next());
        }
        return objects;
    }

    private void tryImport(String title, String year){
        Importer importer = new Importer();
        String parameters = importer.getParameters(title, year);
        List<MovieInfo> movies = importer.getMovieInfoList(importer.getURL(), parameters);
        importer.addMovieInfos(movies, table);
    }

    private void tryImport(String title){
        Importer importer = new Importer();
        String parameters = importer.getParameters(title, "");
        List<MovieInfo> movies = importer.getMovieInfoList(importer.getURL(), parameters);
        importer.addMovieInfos(movies, table);
    }

}
