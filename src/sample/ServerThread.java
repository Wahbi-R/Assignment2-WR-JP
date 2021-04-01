package sample;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread {
    protected Socket socket = null;
    protected PrintWriter out = null;
    protected BufferedReader in = null;

    public ServerThread(Socket socket){
        super();
        this.socket = socket;
        try{
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch(IOException e){
            System.err.println("IOEXception while opening a read/write connection");
        }
    }

    public void run() {
        out.println("Connected to server");
    }
}
