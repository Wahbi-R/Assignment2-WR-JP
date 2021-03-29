package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import java.io.*;
import java.net.*;

public class Main extends Application {

    private TableView leftTable = new TableView();
    TableColumn<localData, String> leftTableColumn = new TableColumn<>("File Names");

    private TableView rightTable = new TableView();
    TableColumn<localData, String> rightTableColumn = new TableColumn<>("File Names");


    File[] content;
    public void selectLocalFolder(Stage stage){
        DirectoryChooser localDirChooser = new DirectoryChooser();
        localDirChooser.setTitle("Open Local Folder");
        File localFolder = localDirChooser.showDialog(stage);
        content = localFolder.listFiles();
        if(localFolder.isDirectory()){
            for(File current : content){
                leftTable.getItems().add(new localData(current.getName()));
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("File Sharer V1.0");

        //Setup buttons
        Button downloadButton = new Button("Download");
        downloadButton.setMinWidth(246);
        Button uploadButton = new Button("Upload");
        uploadButton.setMinWidth(248);

        //Set up top buttons via GridPane
        GridPane topButtons = new GridPane();
        topButtons.setAlignment(Pos.TOP_LEFT);
        topButtons.setHgap(10);
        topButtons.setVgap(10);
        topButtons.setPadding(new Insets(0, 0, 0, 1));
        topButtons.add(downloadButton, 0, 0);
        topButtons.add(uploadButton, 1, 0);

        //Set up BorderPane
        BorderPane root = new BorderPane();
        root.setTop(topButtons);

        //Set up left table
        leftTableColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        root.setLeft(leftTable);
        leftTable.getColumns().add(leftTableColumn);
        leftTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Set up right table
        root.setRight(rightTable);
        rightTableColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        rightTable.getColumns().add(rightTableColumn);
        rightTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Set up local directory button
        Button chooseFolder = new Button("Choose Local Folder");
        GridPane bottomButtons = new GridPane();
        bottomButtons.setHgap(10);
        bottomButtons.setVgap(10);
        bottomButtons.setPadding(new Insets(0, 0, 0, 1));
        bottomButtons.setAlignment(Pos.BOTTOM_LEFT);
        bottomButtons.add(chooseFolder, 0, 0);
        root.setBottom(bottomButtons);
        chooseFolder.setOnAction(e -> selectLocalFolder(primaryStage));

        //Setting up connection tab on bottom right
        DropShadow dropShadow = new DropShadow(); //Drop shadow for circle
        dropShadow.setRadius(3.0);
        dropShadow.setOffsetX(1.0);
        dropShadow.setOffsetY(1.0);
        DropShadow dropShadow2 = new DropShadow();
        dropShadow2.setOffsetX(6.0);
        dropShadow2.setOffsetY(4.0);
        Circle circle = new Circle(6.0f);
        Color tempColour = Color.DARKRED;
        circle.setEffect(dropShadow);
        circle.setFill(tempColour);
        Text connection = new Text("Not connected");
        int tempLoc = 27;
        bottomButtons.add(circle, tempLoc, 0);
        bottomButtons.add(connection, tempLoc+1, 0);

        //Set up scene
        primaryStage.setScene(new Scene(root, 505, 600));
        primaryStage.show();
        System.out.println("Hello, World!");
        System.out.println("Test");
    }

    public static void main(String[] args) {
        /*int port = 8080;
        // port to listen default 8080, or the port from the argument
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        try {
            //Instantiating the HttpServer Class
            HttpServer server = new HttpServer(port);
            // handle any requests from the port
            server.handleRequests();

        } catch (IOException e) {
            e.printStackTrace();
        }*/
        launch(args);
    }
}
