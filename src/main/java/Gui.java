import com.mongodb.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.json.*;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.json.JSONObject;


public class Gui extends Application {

    private DBCollection table = null, usrTable = null;
    private List<BasicDBObject> response;
    private Boolean updated = false;
    private byte owner = 0;

    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setScene(new Scene(root));

        final TextField movie = new TextField();
        movie.setPromptText("Title");
        movie.setMaxHeight(TextField.USE_PREF_SIZE);
        movie.setMaxWidth(TextField.USE_PREF_SIZE);

        final TextField genre = new TextField();
        genre.setPromptText("Genre");
        genre.setMaxHeight(TextField.USE_PREF_SIZE);
        genre.setMaxWidth(TextField.USE_PREF_SIZE);


        final TextField type = new TextField();
        type.setPromptText("Type");
        type.setMaxHeight(TextField.USE_PREF_SIZE);
        type.setMaxWidth(TextField.USE_PREF_SIZE);

        final TextField year = new TextField();
        year.setPromptText("Year");
        year.setMaxHeight(TextField.USE_PREF_SIZE);
        year.setMaxWidth(TextField.USE_PREF_SIZE);

        final TextField actors = new TextField();
        actors.setPromptText("Actors");
        actors.setMaxHeight(TextField.USE_PREF_SIZE);
        actors.setMaxWidth(TextField.USE_PREF_SIZE);

        final TextField plot = new TextField();
        plot.setPromptText("Plot");
        plot.setMaxHeight(TextField.USE_PREF_SIZE);
        plot.setMaxWidth(TextField.USE_PREF_SIZE);

        final TextField rating = new TextField();
        rating.setPromptText("Rating");
        rating.setMaxHeight(TextField.USE_PREF_SIZE);
        rating.setMaxWidth(TextField.USE_PREF_SIZE);

        final TextField released = new TextField();
        released.setPromptText("Released");
        released.setMaxHeight(TextField.USE_PREF_SIZE);
        released.setMaxWidth(TextField.USE_PREF_SIZE);

        final TextField filter = new TextField();
        filter.setPromptText("filter");
        filter.setMaxHeight(TextField.USE_PREF_SIZE);
        filter.setMaxWidth(TextField.USE_PREF_SIZE);

        filter.setTranslateY(50);

        Label label = new Label();


        TextField[] request = new TextField[9];
        request[0] = movie;
        request[1] = genre;
        request[2] = type;
        request[3] = year;
        request[4] = actors;
        request[5] = plot;
        request[6] = rating;
        request[7] = released;
        request[8] = filter;


        //INIT TEXT
        Label info = new Label();
        info.setTranslateX(320);
        info.setTranslateY(60);

        //

        List<Button> btnList = getButtons(request, info);

        VBox vBox = new VBox(7);
        vBox.setPadding(new Insets(12));
        vBox.getChildren().addAll(label, movie, type, genre, year, actors, plot, rating, released, filter);
        root.getChildren().add(vBox);
        root.getChildren().addAll(btnList);
        root.getChildren().add(info);

        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(650);
    }

    private List<Button> getButtons(TextField[] request, Label info) {

        List<Button> btnList = new ArrayList<>();

        Button saveBtn = new Button();
        saveBtn.setDisable(true);
        saveBtn.setText("Save/Update");
        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Saver saver = new Saver(table);
                saver.setType(request[2].getText());
                saver.setActors(request[4].getText());
                saver.setGenre(request[1].getText());
                saver.setPlot(request[5].getText());
                saver.setRating(request[6].getText());
                saver.setReleased(request[7].getText());
                updated = saver.save(request[0].getText(), request[3].getText());
                if(updated){
                    info.setText("Updated");
                }else
                    info.setText("Saved");
            }
        });




        Button deleteBtn = new Button();
        deleteBtn.setDisable(true);
        deleteBtn.setText("Delete");
        deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BasicDBObject del = new BasicDBObject();
                for (TextField aRequest : request) {
                    if (aRequest.getText().length() > 0) {
                        del.put(aRequest.getPromptText(), aRequest.getText());
                    }
                }
                table.remove(del);
                info.setText("Deleted");
            }
        });

        Button filterBtn = new Button();
        filterBtn.setText("Filter");
        filterBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Searcher searcher;
                if (owner == 1){
                    searcher = new Searcher(usrTable);
                }else {
                    searcher = new Searcher(table);
                }

                String req = request[8].getText();
                String val = req.substring(req.indexOf(":") + 1);
                String field = req.substring(0, req.indexOf(":"));

                response = searcher.filter(response,field, val);
                info.setText(informer(response));
            }
        });
        filterBtn.setDisable(true);

        Button favoriteBtn = new Button();
        favoriteBtn.setDisable(true);
        favoriteBtn.setText("To favorites ★\n");
        favoriteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for(BasicDBObject dbObject : response) {
                    if(usrTable.find(dbObject).hasNext()){
                        info.setText("Already in favorites ");
                    }else {
                        usrTable.insert(dbObject);
                        info.setText("Added to favorites");
                    }
                }
            }
        });

        Button delFavoriteBtn = new Button();
        delFavoriteBtn.setDisable(true);
        delFavoriteBtn.setText("Delete from favorites ★\n");
        delFavoriteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BasicDBObject del = new BasicDBObject();
                for (TextField aRequest : request) {
                    if (aRequest.getText().length() > 0) {
                        del.put(aRequest.getPromptText(), aRequest.getText());
                    }
                }
                usrTable.remove(del);
                info.setText("Deleted");
            }
        });


        Button findFavoriteBtn = new Button();
        findFavoriteBtn.setDisable(true);
        findFavoriteBtn.setText("Find in favorites ★\n");
        findFavoriteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                owner = 1;
                Searcher searcher = new Searcher(usrTable);
                filterBtn.setDisable(false);
                if(request[0].getText().length() <= 0){
                    response = searcher.findByActor(request[4].getText());
                }else if(request[0].getText().length() > 0 && request[3].getText().length() > 0){
                    response = searcher.findObjects(request[0].getText(), request[3].getText());
                }
                else if(request[0].getText().length() > 0 && request[3].getText().length() <= 0){
                    response = searcher.findObjects(request[0].getText());
                }
                info.setText(informer(response));
            }
        });


        Button findBtn = new Button();
        findBtn.setDisable(true);
        findBtn.setText("Find");
        findBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                owner = 0;
                Searcher searcher = new Searcher(table);
                filterBtn.setDisable(false);
                favoriteBtn.setDisable(false);
                if(request[0].getText().length() <= 0){
                    response = searcher.findByActor(request[4].getText());
                }else if(request[0].getText().length() > 0 && request[3].getText().length() > 0){
                    response = searcher.findObjects(request[0].getText(), request[3].getText());
                }
                else if(request[0].getText().length() > 0 && request[3].getText().length() <= 0){
                    response = searcher.findObjects(request[0].getText());
                }
                info.setText(informer(response));
            }
        });

        Button connectBtn = new Button();
        connectBtn.setText("Connect");
        connectBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Connector connector = new Connector();
                    MongoClient client = connector.createClient("localhost", 27017);
                    DB db = connector.getDatabase(client, "LocalHost");
                    table = connector.getCollection(db, "movies");
                    usrTable = connector.getCollection(db, "user");
                }catch (MongoSocketOpenException e){
                    info.setText("Connection error!");
                };

                saveBtn.setDisable(false);
                findBtn.setDisable(false);
                deleteBtn.setDisable(false);
                findFavoriteBtn.setDisable(false);
                delFavoriteBtn.setDisable(false);

                info.setText("Successfully connected...");

            }
        });


        connectBtn.setTranslateX(220);
        connectBtn.setTranslateY(20);

        filterBtn.setTranslateX(220);
        filterBtn.setTranslateY(325);

        saveBtn.setTranslateX(10.0);
        saveBtn.setTranslateY(300);

        findBtn.setTranslateX(100.0);
        findBtn.setTranslateY(300);


        deleteBtn.setTranslateX(150.0);
        deleteBtn.setTranslateY(300);

        favoriteBtn.setTranslateX(220);
        favoriteBtn.setTranslateY(170);

        findFavoriteBtn.setTranslateX(220);
        findFavoriteBtn.setTranslateY(220);

        delFavoriteBtn.setTranslateX(220);
        delFavoriteBtn.setTranslateY(275);

        btnList.add(saveBtn);
        btnList.add(findBtn);
        btnList.add(deleteBtn);
        btnList.add(connectBtn);
        btnList.add(filterBtn);
        btnList.add(favoriteBtn);
        btnList.add(delFavoriteBtn);
        btnList.add(findFavoriteBtn);

        return btnList;
    }

    private String informer(List<BasicDBObject> infos){
        String str = "";
        String sObj = "";
        for(BasicDBObject object : infos){
            try {
                JSONObject obj = new JSONObject(object.toJson());
                sObj = "Title: " + obj.getString("Title") + "\n" +
                "Genre: " + obj.getString("Genre")  + "\n" +
                "Type: " + obj.getString("Type")  + "\n" +
                "Year: " + obj.getString("Year")  + "\n" +
                "Rating: " + obj.getString("Rating") + "\n" +
                "Actors: " + obj.getString("Actors") + "\n" +
                "Released: " + obj.getString("Year") + "\n" +
                "Plot: " + obj.getString("Plot");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            str = str + sObj + "\n\n";
        }
        return  str;
    }


    @Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }
    public static void main(String[] args) { launch(args); }
}