package Server;

import java.io.*;
import java.net.*;
import javax.swing.JFrame;

public class ServerTest {
    public static void main(String args[]) throws IOException{
//        Server sally = new Server();
//        sally.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        sally.StartRunning();
        ServerSocket server = new ServerSocket(1994, 100);
        Socket client = null;
        
        while(true){
            try {
                client = server.accept();
            } 
            catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            System.out.println("Client accepted " + client.getInetAddress() + ":" + client.getPort());
            (new Thread(new Server(client))).start();
        }
    }
}
