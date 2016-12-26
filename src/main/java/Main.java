import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class Main {
    public static void main(String[] args) throws IOException {

        try {
            Connector connector = new Connector();
            MongoClient client = connector.createClient("localhost", 27017);
            DB db = connector.getDatabase(client, "LocalHost");
            DBCollection table = connector.getCollection(db, "movies");

            Searcher searcher = new Searcher(table);
            String s = searcher.findObjects("The Godfather: Part II").get(0).toString();
            System.out.println(s);

            Saver saver  = new Saver(table);
            saver.setType("Horror");
            saver.save("Zombie", "1979");


            BasicDBObject oldQ = new BasicDBObject();
            oldQ.put("Type", "2013");

            BasicDBObject newQ = new BasicDBObject();
            newQ.put("Type", "series");
            saver.update(oldQ,newQ );

            BasicDBObject del = new BasicDBObject();
            del.put("Title", "12");
            table.remove(del);

            System.out.println(searcher.findByActor("Travis Fimmel"));


            System.out.println( searcher.filter(searcher.findObjects("Godfat"), "Genre", "Crime"));

        } catch (MongoException e) {
            e.printStackTrace();
        }

    }
}