import java.util.Date;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class Main {
    public static void main(String[] args) {

        try {
            MongoClient mongo = new MongoClient("localhost", 27017);

            DB db = mongo.getDB("LocalHost");
            DBCollection table = db.getCollection("user");

            BasicDBObject document = new BasicDBObject();
            document.put("name", "Hello");
            document.put("created date", new Date());
            table.insert(document);

            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("name", "Hello");

            DBCursor cursor = table.find(searchQuery);

            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }

            BasicDBObject query = new BasicDBObject();
            query.put("name", "Hello");

            BasicDBObject newDocument = new BasicDBObject();
            newDocument.put("name", "World");

            BasicDBObject updateObj = new BasicDBObject();
            updateObj.put("$set", newDocument);

            table.update(query, updateObj);

            BasicDBObject searchQuery2
                    = new BasicDBObject().append("name", "World");

            DBCursor cursor2 = table.find(searchQuery2);

            while (cursor2.hasNext()) {
                System.out.println(cursor2.next());
            }

            System.out.println("Programm over!");

        } catch (MongoException e) {
            e.printStackTrace();
        }

    }
}