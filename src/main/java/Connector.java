import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;


public class Connector {

    MongoClient createClient(String host, int port){//server address
        return new MongoClient(host, port);//27017
    }

    DB getDatabase(MongoClient client, String dbHost){//db address
        return client.getDB(dbHost);
    }

    DBCollection getCollection(DB database, String collectionName){
        return database.getCollection(collectionName);
    }
}

