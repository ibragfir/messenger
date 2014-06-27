package Client;

import javax.swing.JFrame;

public class ClientTest {
    public static void main(String args[]){
        Client cl;
        cl = new Client("localhost");
        cl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cl.StartRunning();
    }
}
