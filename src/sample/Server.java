package sample;

import java.io.*;
import java.net.*;
import java.util.Vector;

public class Server {
    protected Socket clientSocket = null;
    protected ServerSocket serverSocket = null;
    protected ServerThread ClientConnectionHandler = null;

    public static int SERVER_PORT = 8080;

    public Server() {
        try{
            //creates server socket to listen for client
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server is running");
            System.out.println("Listening to port: "+SERVER_PORT);

            //accepts client socket and starts thread
            while(true){
                clientSocket = serverSocket.accept();
                ClientConnectionHandler = new ServerThread(clientSocket);
                ClientConnectionHandler.start();
                System.out.println("Client has connected to server");
            }
        }
        catch(IOException e){
            System.err.println("IOException while creating server connection");
        }
    }

    public static void main(String[] args) {
        Server app = new Server();
    }
}
