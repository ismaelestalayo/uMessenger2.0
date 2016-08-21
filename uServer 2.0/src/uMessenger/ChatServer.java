/* 
 * Copyright (C) 2016 Ismael Estalayo Mena
                        http://github.com/ismaelestalayo

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uMessenger;

import fileTransfer.FileReceiver;
import fileTransfer.FileSender;
import fileTransfer.FileSenderHandler;
import java.net.*;
import java.io.*;

public class ChatServer implements Runnable {
    

    private ChatServerThread clients[] = new ChatServerThread[20];
    private ServerSocket server = null;
    private Thread thread = null;
    
    private int clientCount = 0;
    private int c = 0;              //For the array of colors
    private String[] userList = new String[20];
    
    private final String C_RST = "\u001B[0m";
    private final String C_RED = "\u001B[31m";
    
    private String[] colors = {"CYAN", "GREEN", "YELLOW", "MAGENTA", "RED", "BLUE", "PINK"};
    
    //CONSTRUCTOR///////////////////////////////////////////////////////////////
    public ChatServer(int port) {
        try {
            System.out.println("Binding to port " + port + ", please wait...");
            server = new ServerSocket(port);
            System.out.println("Server started successfully. ");
            start();
            
        } catch (IOException ex) {
            System.out.println("Can not bind to port " + port + ": " + ex);
        }
    }
    
    //MAIN//////////////////////////////////////////////////////////////////////
    public static void main(String args[]) {
        ChatServer server = null;
        server = new ChatServer(5000);
    }
    
    //METHODS///////////////////////////////////////////////////////////////////
    @Override
    public void run() {
        while (thread != null) {
            try {
                System.out.println("Waiting for a new client..." + C_RST);
                addThread(server.accept() );
                
            } catch (IOException ex) {
                System.out.println("Server accept error: " + ex);
                closeAll();
            }
        }
    }
    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }
    public void closeAll() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
   }
    private int findClient(int ID) {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i].getID() == ID)
                return i;
        }
        
        return -1;
    }

    public synchronized void broadcast(String userName, String color, int ID, String msg){
        
        if(msg.equals("/forcedFin")){
            for (int i = 0; i < clientCount; i++)
                //don't send it to the user who closed the window
                if(!clients[i].getUserName().equals(userName))
                    clients[i].sendMsg("FIN", userName, color, "doesn't mind");
            
            removeClient(ID);
        }
        
        else if(msg.equals("/newUser")){
            for (int i = 0; i < clientCount; i++)
                clients[i].sendMsg("NEW", userName, color, "doesn't mind");
            
        }
        
        /////// my commands   ↑ //////////////////////////////////////
        /////// chat commands ↓ //////////////////////////////////////
        
        else if(msg.equals("/fin") ){
            for (int i = 0; i < clientCount; i++)
                clients[i].sendMsg("FIN", userName, color, "doesn't mind");
            
            removeClient(ID);
        } 
        
        else if(msg.equals("/help") ){
            for (int i = 0; i < clientCount; i++)
                if(clients[i].getUserName().equals(userName) )
                    clients[i].sendMsg("INFO", userName, color, helpMessage() );
            
        }
        
        else if(msg.equals("/IPs")){
            String userList = "   Users IPs: \n";
            
            for (int i = 0; i < clientCount; i++){
                if(i != clientCount -1){
                    userList += "    • " + clients[i].getUserName() + " (" +clients[i].getUserIP()+ ")\n";
                } else{
                    userList += "    • " + clients[i].getUserName() + " (" +clients[i].getUserIP()+ ")";
                }
            }
            
            for (int i = 0; i < clientCount; i++)
                if(clients[i].getUserName().equals(userName) )
                    clients[i].sendMsg("INFO", userName, color, userList );
        }
        
        else if(msg.startsWith("/send") ){
            //If someone puts /send but no username, try-catch:
            try{
                String target = msg.split("/send")[1];
                String targetIP = "";
                String fileName = "";
                
                boolean sent = false;
                for (int i = 0; i < clientCount; i++) {
                    if (clients[i].getUserName().equals(target)) {
                        targetIP = clients[i].getUserIP();
                        sent = true;
                    }
                }
                //If there was an user with that name:
                if (sent) {
                    for (int i = 0; i < clientCount; i++) {
                        if (clients[i].getUserName().equals(userName)) {
                            clients[i].sendMsg("FILE", userName, color, "doesnt mind");
                            FileReceiver receiver = new FileReceiver();
                            fileName = receiver.getFileName();
                        }
                    }
                    for(int i = 0; i < clientCount; i++) {
                        if (clients[i].getUserName().equals(target)) {
                            clients[i].sendMsg("FILE", userName, color, "doesn't mind");
                            FileSender sender = new FileSender(targetIP, fileName );
                            
                        }
                    }
                } else {
                    for (int i = 0; i < clientCount; i++) {
                        if (clients[i].getUserName().equals(userName)) {
                            clients[i].sendMsg("INFO", userName, color, "   User "
                                    + target + " was not found.");
                        }
                    }
                }
                
            } catch(ArrayIndexOutOfBoundsException ex){
                for(int i = 0; i < clientCount; i++){
                    if(clients[i].getUserName().equals(userName))
                        clients[i].sendMsg("INFO", userName, color, "   You must write"
                                + " who you want to send it to (/sendAnon)");
                    
                }
            }
        }
        
        else if(msg.equals("/trivial")){
            fileTransfer.FileSenderHandler f = new FileSenderHandler("trivial.txt");
        }
        
        else if(msg.equals("/users") ){
            String userList = "   Online users: \n";
            
            for (int i = 0; i < clientCount; i++){
                if(i != clientCount-1){
                    userList += "    • " + clients[i].getUserName() + "\n";
                } else{
                    userList += "    • " + clients[i].getUserName();
                }
            }
            
            for (int i = 0; i < clientCount; i++)
                if(clients[i].getUserName().equals(userName) )
                    clients[i].sendMsg("INFO", userName, color, userList );
            
        }
        
        else if(msg.startsWith("/GODkick") ){
            String userToKick = msg.split("/GODkick")[1];
            Boolean kicked = false;
            for(int i = 0; i < clientCount; i++){
                if(clients[i].getUserName().equals(userToKick)){
                    removeClient(clients[i].getID() );
                    kicked = true;
                }
            }
            if(kicked){
                for (int i = 0; i < clientCount; i++)
                    clients[i].sendMsg("FIN", userToKick, color, "doesn't mind");
            }
            else{
                for (int i = 0; i < clientCount; i++){
                    if(clients[i].getUserName().equals(userName) )
                        clients[i].sendMsg("INFO", userName, color, "User "
                                +userToKick+ " wasn't found." );
                }
            }
        }
        
        //Normal messages:
        else {
            for (int i = 0; i < clientCount; i++) {
                clients[i].sendMsg("CHAT", userName, color, msg);
            }
        }
    }

    public synchronized void removeClient(int ID) {
        int pos = findClient(ID);
        if (pos >= 0) {
            ChatServerThread threadToClose = clients[pos];
            System.out.println(C_RED
                    + "Removing " + clients[pos].getUserName()
                    + " at thread " + ID + C_RST);
            if (pos < clientCount - 1) {
                for (int i = pos + 1; i < clientCount; i++) {
                    clients[i - 1] = clients[i];
                    userList[i - 1] = userList[i];
                }
            }
            
            clientCount--;
            
            try {
                threadToClose.close();
            } catch (IOException ioe) {
                System.out.println("Error closing thread: " + ioe);
            }
            threadToClose.stop();
        }
    }
    private void addThread(Socket socket) {
        if(c > 6)
            c = 0;
        clients[clientCount] = new ChatServerThread(this, socket, colors[c]);
        c++;

        try {
            clients[clientCount].openStreams();
            clients[clientCount].start();

            clientCount++;

        } catch (IOException ioe) {
            System.out.println("Error opening thread: " + ioe);
        }
    }
    
    public void addClientToList(String name){
        userList[clientCount - 1] = name;
    }
    //**************************************************************************
    //**************************************************************************
    //**************************************************************************
    //**************************************************************************
    //**************************************************************************
    
    private String helpMessage(){
        return "   Available commands: \n"
                + "    /help - This one\n"
                + "    /IPs - Prints a list of users and their IPs\n"
                + "    /sendUser - Send a file to the specified user.\n"
                + "    /users - Prints a list of online users";
    }
}


