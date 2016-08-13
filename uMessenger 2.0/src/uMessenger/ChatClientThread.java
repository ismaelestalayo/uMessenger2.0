package uMessenger;

import java.net.*;
import java.io.*;

public final class ChatClientThread extends Thread{
    
    private Socket socket = null;
    private ChatClient client = null;
    
    private ObjectInputStream ois = null;
    
//////CONSTRUCTOR///////////////////////////////////////////////////////////////
    public ChatClientThread(ChatClient cc, Socket ss) {
        client = cc;
        socket = ss;
        
        try {
            ois = new ObjectInputStream(socket.getInputStream() );
            
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex);
            client.closeAll();
        }
        start();
    }
    
//////METHODS///////////////////////////////////////////////////////////////////
    public void close() {
        try {
            if (ois != null)
                ois.close();
            
        } catch (IOException ex) {
            System.out.println("Error closing input stream: " + ex);
        }
    }

    @Override
    public void run() {
        client.sendUserName();  //Self explanatory
        
        while (true) {
            try {
                Object obj = (Object)ois.readObject();
                client.chatting(obj);
                
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("_________________________________________");
                System.out.println("Lost connection with the server: " + ex);
                client.closeAll();
            }
        }
    }
}

