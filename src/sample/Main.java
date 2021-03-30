package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private Socket socket = null;

    public static String SERVER_ADDRESS = "localhost";
    public static int SERVER_PORT = 8080;

    private TableView leftTable = new TableView();
    TableColumn<localData, String> leftTableColumn = new TableColumn<>("File Names");

    private TableView rightTable = new TableView();
    TableColumn<localData, String> rightTableColumn = new TableColumn<>("File Names");


    File[] content;

    public void selectLocalFolder(Stage stage){ //This is a function to select the local folder that you want to view
        leftTable.getItems().clear();
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

    public void connectionStatus(GridPane grid, Color colour){ //This is a function to change the bottom right of the
                                                               //screens connection status
        Text connection = new Text();
        int tempLoc = 27;
        DropShadow dropShadow = new DropShadow(); //Drop shadow for circle
        dropShadow.setRadius(3.0);
        dropShadow.setOffsetX(1.0);
        dropShadow.setOffsetY(1.0);
        DropShadow dropShadow2 = new DropShadow();
        dropShadow2.setOffsetX(6.0);
        dropShadow2.setOffsetY(4.0);
        Circle circle = new Circle(6.0f);
        circle.setEffect(dropShadow);
        circle.setFill(colour);
        if(colour == Color.DARKRED) {
            if(grid.getChildren().size() == 3){ grid.getChildren().remove(1, 3); }
            connection = new Text("Not connected");
            tempLoc = 8;
        }else{
            if(grid.getChildren().size() == 3){ grid.getChildren().remove(1, 3); }
            connection = new Text("Connected");
            tempLoc = 9;
        }
        grid.add(circle, tempLoc, 0);
        grid.add(connection, tempLoc+1, 0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("File Sharer V1.0");

        //Setup buttons
        Button connectButton = new Button("Connect");
        Button downloadButton = new Button("Download");
        downloadButton.setMinWidth(246);
        downloadButton.setEffect(new DropShadow(3, Color.BLACK));
        Button uploadButton = new Button("Upload");
        uploadButton.setMinWidth(248);
        uploadButton.setEffect(new DropShadow(3, Color.BLACK));

        //Set up top buttons via GridPane
        GridPane topButtons = new GridPane();
        topButtons.setAlignment(Pos.TOP_LEFT);
        topButtons.setHgap(10);
        topButtons.setVgap(10);
        topButtons.setPadding(new Insets(2, 0, 2, 1));
        topButtons.add(downloadButton, 0, 0);
        topButtons.add(uploadButton, 1, 0);
        topButtons.add(connectButton,0,1);

        //Set up BorderPane
        BorderPane root = new BorderPane();
        root.setTop(topButtons);

        //Set up left table
        leftTableColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        leftTable.setEffect(new DropShadow(3, 0, 1, Color.BLACK));
        root.setLeft(leftTable);
        leftTable.getColumns().add(leftTableColumn);
        leftTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Set up right table
        root.setRight(rightTable);
        rightTable.setEffect(new DropShadow(3, 1, 1, Color.BLACK));
        rightTableColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        rightTable.getColumns().add(rightTableColumn);
        rightTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Set up local directory button
        Button chooseFolder = new Button("Choose Local Folder");
        chooseFolder.setMinWidth(246);
        chooseFolder.setEffect(new DropShadow(3, 1, 1, Color.BLACK));
        GridPane bottomButtons = new GridPane();
        bottomButtons.setHgap(10);
        bottomButtons.setVgap(10);
        bottomButtons.setPadding(new Insets(2, 0, 2, 1));
        bottomButtons.setAlignment(Pos.BOTTOM_LEFT);
        bottomButtons.add(chooseFolder, 0, 0);
        root.setBottom(bottomButtons);
        chooseFolder.setOnAction(e -> selectLocalFolder(primaryStage));

        //Setting up connection tab on bottom right
        connectionStatus(bottomButtons, Color.DARKRED);

        //Set up connection button to connect to server
        connectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                    connectionStatus(bottomButtons, Color.LIMEGREEN);
                }
                catch (UnknownHostException e){
                    System.err.println("Unknown host: "+SERVER_ADDRESS);
                }
                catch (IOException e){
                    System.err.println("IOException while connecting to server: "+SERVER_ADDRESS);
                }

                if (socket == null) {
                    System.err.println("Socket is null");
                }
            }
        });

        //double click on a row to open file
        leftTable.setRowFactory(e -> {
            TableRow<localData> row = new TableRow<>();
            row.setOnMouseClicked(e2 -> {
                if(e2.getClickCount() == 2 && (!row.isEmpty())) {
                    localData rowData = row.getItem();
                    if (rowData.getFileName().contains(".txt")) { //Checking if the file is a text file
                        try {
                            String path = "";
                            for (File current : content) {
                                if (current.getName().equals(rowData.getFileName())) {
                                    path = current.getAbsolutePath();
                                }
                                System.out.println(current.getName());
                                System.out.println(rowData.getFileName());
                                System.out.println(path);
                            }
                            Runtime.getRuntime().exec(new String[]{"notepad", path});
                        } catch (IOException e3) {
                            System.out.println(e3);
                        }
                    }else{ //Open a window saying "That's not a text file"
                        Stage tempStage = new Stage();
                        GridPane error = new GridPane();
                        error.setAlignment(Pos.CENTER);
                        Text errorMessage = new Text("You can only open .txt files!");
                        error.add(errorMessage, 0, 0);
                        Scene tempScene = new Scene(error, 200, 200);
                        tempStage.setScene(tempScene);
                        tempStage.show();
                    }
                }
            });
            return row;
        });

        //Set up scene
        Scene scene = new Scene(root, 505, 600);
        scene.getStylesheets().add("Colors.css");
        primaryStage.setScene(scene);
        primaryStage.show();
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
        //TODO DO THE WHOLE CONNECTION THING

        Main client = new Main();
        launch(args);
    }
}
