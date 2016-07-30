package uMessenger;

import java.net.*;
import java.io.*;

public final class ChatClientThread extends Thread{
    private Socket socket = null;
    private ChatClient client = null;
    
    private ObjectInputStream ois = null;
    
    public static final String C_RST = "";
    public static final String C_BLACK = "";
    public static final String C_RED = "";
    
    //CONSTRUCTOR///////////////////////////////////////////////////////////////
    public ChatClientThread(ChatClient cc, Socket ss) {
        client = cc;
        socket = ss;
        
        openStream();
        start();
    }
    
    //METHODS///////////////////////////////////////////////////////////////////
    public void openStream() {
        try {
            ois = new ObjectInputStream(socket.getInputStream() );
            
        } catch (IOException ioe) {
            System.out.println("Error getting input stream: " + ioe);
            client.closeAll();
        }
    }
    public void close() {
        try {
            if (ois != null)
                ois.close();
            
        } catch (IOException ioe) {
            System.out.println("Error closing input stream: " + ioe);
        }
    }

    @Override
    public void run() {
        client.sendUserName();  //Self explanatory
        
        while (true) {
            try {
                Object obj = (Object)ois.readObject();
                client.chatting(obj);
                
            } catch (IOException ex) {
                System.out.println(C_RED + "_________________________________________");
                System.out.println("Lost connection with the server: " + ex + C_RST);
                System.out.println("Press enter to close.");
                client.closeAll();
            } catch (ClassNotFoundException ex) {
                System.out.println(C_RED + "_________________________________________");
                System.out.println("Lost connection with the server: " + ex + C_RST);
                System.out.println("Press enter to close.");
                client.closeAll();
            }
        }
    } 
}

