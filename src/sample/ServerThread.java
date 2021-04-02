package sample;
import java.io.*;
import java.net.*;


public class ServerThread extends Thread {
    protected Socket socket;
    protected PrintWriter out = null;

    public ServerThread(Socket socket){
        super();
        this.socket = socket;
    }


    public void run() {
        String message;
        try {
            message = checkMessage();
            //Message sent from client to server
            System.out.println("Message = " + message);
            if(message.equals("CONNECT")) { folderFiles(); }
            if(message.equals("UPLOAD")) receiveFile();
            if(message.equals("DOWNLOAD")) sendFile();
            socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    //gets list of files in server folder and send it to client to for display
    public void folderFiles() throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        File remoteDirectory = new File("./RemoteFolder");
        File[] content = remoteDirectory.listFiles();
        String message = "";
        if (remoteDirectory.isDirectory()) {
            for (File current : content) {
                message += current.getName() + ",";
            }
        }
        out.println(message);
        out.flush();
    }

    //method to send requested file to client for download
    private void sendFile() throws IOException {
        try {
        String fileName = checkMessage();
        InputStream in = new FileInputStream("./RemoteFolder/" + fileName);
        OutputStream out = socket.getOutputStream();
        //byte used to send file
        byte[] buffer = new byte[8192];
        int len = 0;
        while((len = in.read(buffer)) != -1){
            out.write(buffer,0,len);
        }
        out.flush();

        System.out.println("file sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //method to receive file uploaded by client
    public void receiveFile() throws IOException {
        try {
            String message = checkMessage();
            socket.getOutputStream().flush();
            InputStream input = socket.getInputStream();
            OutputStream out = new FileOutputStream("./RemoteFolder/" + message);
            //byte used to receiver file on server
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
    }

    private String checkMessage() throws IOException {
        InputStream inputStream = socket.getInputStream();
        // create a DataInputStream so we can read data from it.
        DataInputStream dataInputStream = new DataInputStream(inputStream);

        // read the message from the socket
        String message = dataInputStream.readUTF();
        return message;
    }
    }

