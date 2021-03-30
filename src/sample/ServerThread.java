package sample;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread extends Thread {
    protected Socket socket = null;
    protected PrintWriter out = null;

    public void run() {
        out.println("Connected to server");
    }
}
