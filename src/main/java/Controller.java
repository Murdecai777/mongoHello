import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;



public class Controller {

   /* private DB db;

    Controller(DB db){
        this.db = db;
    }

    void readStream(String[] keys){
        for (String key : keys){

        }
    }

    DBCollection table = connector.getCollection(db, keys[1]);



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


    while (true) {

                if(keys.length > 1) {
                    switch (keys[0]) {
                        case "-c":
                            table = connector.getCollection(db, keys[1]);

                        case "-s":
                            Searcher searcher = new Searcher(table);
                            if(keys.length == 2){
                                if(!keys[2].equals("-f"))
                                    searcher.findObjects(keys[1], keys[2]).forEach(System.out::println);
                                else
                            }
                            searcher.findObjects(keys[1]).forEach(System.out::println);
                            if(keys.length > 3){

                            }

                    }
                }  else
                    System.out.println("Wrong command!");
            }*/
}
