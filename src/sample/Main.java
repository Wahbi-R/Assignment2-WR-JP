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
import java.util.Arrays;
import java.util.List;

public class Main extends Application {
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter networkOut = null;
    private BufferedReader networkIn = null;

    public static String SERVER_ADDRESS = "localhost";
    public static int SERVER_PORT = 8080;

    private TableView leftTable = new TableView();
    TableColumn<localData, String> leftTableColumnFileName = new TableColumn<>("Local Files");
    TableColumn<localData, String> leftTableColumnButton = new TableColumn<>("Button");

    private TableView rightTable = new TableView();
    TableColumn<localData, String> rightTableColumnFileName = new TableColumn<>("Remote Files");
    TableColumn<localData, String> rightTableColumnButton = new TableColumn<>("Button");

    String localPath;
    File[] content;

    public void selectLocalFolder(Stage stage){ //This is a function to select the local folder that you want to view
        leftTable.getItems().clear();
        DirectoryChooser localDirChooser = new DirectoryChooser();
        localDirChooser.setTitle("Open Local Folder");
        File localFolder = localDirChooser.showDialog(stage);
        localPath = localFolder.getPath();
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

    public void handle(GridPane bottomButtons) throws IOException {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            connectionStatus(bottomButtons, Color.LIMEGREEN);
            sendMessage("CONNECT");
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
        BufferedReader networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String message = networkIn.readLine();
        setRemoteTable(message);
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        //socket.getInputStream().reset();
    }

    private void setRemoteTable(String message) {
        System.out.println(message);
        rightTable.getItems().clear();
        List<String> fileNames = Arrays.asList(message.split(","));
        for(int i = 0; i<fileNames.size(); i++)
        rightTable.getItems().add(new localData(fileNames.get(i)));
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("File Sharer V1.0");

        //Setup buttons
        Button connectButton = new Button("Connect");
        connectButton.setMinWidth(246);
        connectButton.setEffect(new DropShadow(3, Color.BLACK));
        Button deleteButton = new Button("Delete File");
        deleteButton.setMinWidth(248);
        deleteButton.setEffect(new DropShadow(3, Color.BLACK));
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
        topButtons.add(connectButton,1,1);
        topButtons.add(deleteButton, 0, 1);

        //Set up BorderPane
        BorderPane root = new BorderPane();
        root.setTop(topButtons);

        //Set up left table
        leftTableColumnFileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        leftTable.setEffect(new DropShadow(3, 0, 1, Color.BLACK));
        root.setLeft(leftTable);
        leftTable.getColumns().add(leftTableColumnFileName);
        leftTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Set up right table
        root.setRight(rightTable);
        rightTable.setEffect(new DropShadow(3, 1, 1, Color.BLACK));
        rightTableColumnFileName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        rightTable.getColumns().add(rightTableColumnFileName);
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
        connectButton.setOnAction(e -> {
            try {
                handle(bottomButtons);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //Set up delete button to delete a file
        deleteButton.setOnAction(e -> delete());

        //Set up upload button
        uploadButton.setOnAction(e -> {
            try {
                uploadFile(bottomButtons);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //Set up download button
        downloadButton.setOnAction(e -> {
            try {
                downloadFile(bottomButtons);
            } catch (IOException ioException) {
                ioException.printStackTrace();
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

    private void delete() {
        localData name = (localData) leftTable.getSelectionModel().getSelectedItem();
        leftTable.getItems().remove(name);
        for (File current : content) {
            if (current.getName().equals(name.getFileName())) {
                current.delete();
            }
        }
        resetLeftTable();
    }

    private boolean nullSocket(){
        if(socket == null || socket.isClosed()) {
            Stage tempStage = new Stage();
            GridPane error = new GridPane();
            error.setAlignment(Pos.CENTER);
            Text errorMessage = new Text("You are not connected to the server. Press connect!");
            error.add(errorMessage, 0, 0);
            Scene tempScene = new Scene(error, 300, 100);
            tempStage.setScene(tempScene);
            tempStage.show();
            return true;
        }else{
            return false;
        }
    }

    private void downloadFile(GridPane buttons) throws IOException {
        if (nullSocket() == false) {
            sendMessage("DOWNLOAD");

            try {
                localData name = (localData) rightTable.getSelectionModel().getSelectedItem();
                String fileName = name.getFileName();
                sendMessage(fileName);
                InputStream input = socket.getInputStream();
                OutputStream out = new FileOutputStream("./LocalFolder/" + fileName);

                byte[] buffer = new byte[8192];
                int len = 0;
                while ((len = input.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
                System.out.println("Received file on server");
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket.close();
            connectionStatus(buttons, Color.DARKRED);
        }
        resetLeftTable();
    }

    private void resetLeftTable() {
        leftTable.getItems().clear();
        System.out.println(localPath);
        File localFolder = new File(localPath);
        content = localFolder.listFiles();
        if(localFolder.isDirectory()){
            for(File current : content){
                leftTable.getItems().add(new localData(current.getName()));
            }
        }
    }

    private void sendMessage(String message) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        System.out.println("Sending message to the server");

        // write the message we want to send
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush(); // send the message
        //dataOutputStream.close();
        //outputStream.flush();
    }

    private void uploadFile(GridPane buttons) throws IOException {
        if(nullSocket() == false){
            sendMessage("UPLOAD");
            localData name = (localData) leftTable.getSelectionModel().getSelectedItem();
            System.out.println(name.getFileName());
            String path = "NO PATH";
            for (File current : content) {
                if (current.getName().equals(name.getFileName())) {
                    path = current.getPath();
                }
                System.out.println("The current file is: " + current.getName());
            }
            System.out.println(name.getFileName());
            System.out.println(path);
            sendMessage(name.getFileName());
            InputStream in = new FileInputStream(path);
            OutputStream out = socket.getOutputStream();
            byte[] buffer = new byte[8192];
            int len = 0;
            while((len = in.read(buffer)) != -1){
                out.write(buffer,0,len);
            }
            out.flush();
            System.out.println("file sent");

            socket.close();
            connectionStatus(buttons, Color.DARKRED);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
