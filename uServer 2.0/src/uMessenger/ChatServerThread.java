package uMessenger;

import java.net.*;
import java.io.*;

public class ChatServerThread extends Thread {

    private ChatServer server = null;
    private Socket socket = null;
    private DataInputStream dis = null;
    private ObjectOutputStream oos = null;
        
    private int ID = -1;
    private String userName = "";
    private String color = "";
    
    private final String C_RST = "\u001B[0m";
    private final String C_CYAN = "\u001B[36m";
    private final String C_RED = "\u001B[31m";
    
//////CONSTRUCTOR///////////////////////////////////////////////////////////////
    public ChatServerThread(ChatServer cs, Socket socket, String myColor) {
        super();
        this.server = cs;
        this.socket = socket;
        this.color = myColor;
        ID = socket.getPort();
    }
    
//////METHODS///////////////////////////////////////////////////////////////////
    @Override
    public void run() {
        checkNewUser();
        
        System.out.println(C_CYAN + "Added client " + userName + " (" + getUserIP()
                + ") on thread " + ID + C_RST);
        server.broadcast(userName, color, ID, "/newUser");
        server.addClientToList(userName);
        
        while (true) {
            try {
                server.broadcast(userName, color, ID, dis.readUTF() );
                
            } catch (IOException ex) {
                System.out.println(C_RED + "Client " + getUserName() + " closed the window." + C_RST);
                server.broadcast(userName, color, ID, "/forcedFin");
            }
        }
    }
    
    public void sendMsg(String type, String user, String color, String msg) {
        try {
            Object obj = new Object(type, user, color, msg);
            oos.writeObject(obj);
            oos.flush();
            
        } catch (IOException ex) {
            server.broadcast(userName, color, ID, "/forcedFin");
        }
    }
    
    private void checkNewUser(){
        
        try {
            userName = dis.readUTF();
            
        } catch (IOException ex) {
            System.out.println("ERROR reading userName: " + ex);
        }
        
    }
    public void openStreams() throws IOException {
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream() ) );
        oos = new ObjectOutputStream(socket.getOutputStream() );
    }
    public void close() throws IOException {
        if (dis != null)
            dis.close();
        if (oos != null)
            oos.close();
        if (socket != null)
            socket.close();
    }
    
    //**************************************************************************
    public int getID() {
        return ID;
    }
    public String getUserName(){
        return userName;
    }
    public String getUserIP(){
        return socket.getInetAddress().getHostAddress();
    }
    public String getUserColor(){
        return color;
    }

}
