package Server;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class Server extends JFrame implements Runnable{

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
    
    public Server(Socket client){
        super("Firuz");
        connection = client;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent event){
                    SendMessage(event.getActionCommand());
                    userText.setText("");
                }
            }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(500,400);
        setVisible(true);
    }
    public void StartRunning(){
        try{
//            server = new ServerSocket(1994, 100);
            while(true){
                try{
                    //WaitForConnection();
                    SetupStreams();
                    WhileChatting();
                }catch(EOFException ex){
                    ShowMessage("\nFiruz ended a connection!\n");
                }finally{
                    Close();
                }
                
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
    private void WaitForConnection()throws IOException{
        ShowMessage("Waiting for someone to connect ... \n");
        //connection = server.accept();
        ShowMessage("Now connected to " + connection.getInetAddress().getHostName());
    }
    private void SetupStreams()throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        ShowMessage("\nStreams are setup!\n");
    }
    private void WhileChatting()throws IOException{
        String message = "You are now connected!";
        chatWindow.setEditable(false);
        SendMessage(message);
        AbleToType(true);
        do{
            try{
                message = (String) input.readObject();
                ShowMessage("\n[Taras]: "+ message);
            }catch(ClassNotFoundException ex){
                ShowMessage("\n idk wtf that user send!");
            }
        }while(!message.equals("END"));
    }
    private void Close(){
        ShowMessage("\n Closing connection");
        AbleToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private void SendMessage(String message){
        try{
            output.writeObject(message);
            output.flush();
            ShowMessage("\n[Firuz]: " + message);
        }catch(IOException e){
            chatWindow.append("\n ERROR: I CANT SEND THAT MESSAGE");
        }
    }
    private void ShowMessage(final String text){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    chatWindow.append(text);
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

    @Override
    public void run() {
        StartRunning();
    }
}
