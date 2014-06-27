package Client;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;
    
    public Client(String host){
        super("Taras");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent event){
                    SendMessage(event.getActionCommand()); 
                    userText.setText("");
                }
            }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(300,150);
        setVisible(true);
    }
    public void StartRunning(){
        try{
            ConnectToServer();
            SetupStreams();
            WhileChatting();
        }
        catch(EOFException e){
            ShowMessage("\n Client terminated connection");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally{
            Close();
        }
    }
    private void ConnectToServer() throws IOException{
        ShowMessage("Attempting connection...");
        connection = new Socket(InetAddress.getByName(serverIP), 1994);
        ShowMessage("Connected to:" + connection.getInetAddress().getHostName());
    }
    private void SetupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        ShowMessage("Your streams now are good to go!");
    }
    private void WhileChatting()throws IOException{
        AbleToType(true);
        do{
            try{
                message = (String) input.readObject();
                ShowMessage("\n[Firuz]: " + message);
            }
            catch(ClassNotFoundException e){
                ShowMessage("\n idk that object type!");
            }
        }while(!message.equals("END"));
    }
    private void Close(){
        ShowMessage("\n closig app down...");
        AbleToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    private void SendMessage(String message){
        try{
            output.writeObject(message);
            output.flush();
            ShowMessage("\n[Taras]: " + message);
        }
        catch(IOException e){
            chatWindow.append("\n something messed up sending a message");
        }
    }
    private void ShowMessage(final String m){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    chatWindow.append(m);
                }
            }
        );
    }
    private void AbleToType(final boolean tof){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    userText.setEditable(tof);
                }
            }
        );
    }
}
