package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
	
	private ServerSocket serverSocket = null;
	private int port;

    public HttpServer(int port) throws IOException {
	  serverSocket = new ServerSocket(port);
	  this.port = port;
    }
	
	public void handleRequests() throws IOException{
		System.out.println("Listening to port: "+port);
		
		// creating a thread to handle each of the clients
		while(true){
			Socket clientSocket = serverSocket.accept();
			HttpServerHandler handler = new HttpServerHandler(clientSocket);
			Thread handlerThread = new Thread(handler);
			handlerThread.start();
		}
		
	}	
	
	
	
	public static void main(String[] args) {

  }
}