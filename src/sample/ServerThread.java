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
        File remoteFolder = new File("./RemoteFolder");
        File[] content = remoteFolder.listFiles();
        if(remoteFolder.isDirectory()){
            for(File current : content){
                out.print(current.getName() + ",");
            }
        }
        try {
            InputStreamReader input = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(input);

            PrintWriter printwriter = new PrintWriter(socket.getOutputStream(), true);
            String line = "";
            boolean done = false;
            while(((line = reader.readLine()) != null)){
                System.out.println("Received on Server: " + line);
            }

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write("YO THIS A TEST WORK MAN");
            writer.flush();

//            OutputStream output = socket.getOutputStream();
//            byte[] byteArray = new byte[1024];
//            int currentByte = input.read(byteArray, 0, byteArray.length);
//
//            FileOutputStream tempFileOut = new FileOutputStream("temp.pdf");
//            BufferedOutputStream temp = new BufferedOutputStream(tempFileOut);
//            temp.write(byteArray, 0, currentByte);
//            temp.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("Connected to server");
    }
}
