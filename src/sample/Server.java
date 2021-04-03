package sample;

import java.io.*;
import java.net.*;
import java.util.Vector;

public class Server {
    protected Socket clientSocket = null;
    protected ServerSocket serverSocket = null;
    protected ServerThread[] ClientConnectionHandler = null;
    protected int numClients = 0;


    public static int SERVER_PORT = 8080;
    public static int MAX_CLIENTS = 50;

    public Server() {
        try{
            //creates server socket to listen for client
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server is running");
            System.out.println("Listening to port: "+SERVER_PORT);
            ClientConnectionHandler = new ServerThread[MAX_CLIENTS];
            //accepts multiple client socket and starts thread
            while(true){
                clientSocket = serverSocket.accept();
                if(numClients < MAX_CLIENTS){
                    ClientConnectionHandler[numClients] = new ServerThread(clientSocket);
                    ClientConnectionHandler[numClients].start();
                    System.out.println("Client "+numClients+" has connected to server");
                    numClients++;
                }
                else{
                    System.out.println("Server full");
                }
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
