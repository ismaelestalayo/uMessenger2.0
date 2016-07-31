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

import fileTransfer.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.*;  
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

public final class ChatClient implements Runnable{

    private Socket socket = null;
    private Thread thread = null;
    private ChatClientThread client = null;
    
    private DataOutputStream dos = null;
    private static final Scanner keyboard = new Scanner(System.in);
    
    private static String userName;
    private static String dir = null;
    
    private JTextField textBox;
    private JTextPane textPane = new JTextPane();
    private JScrollPane scroll;
    private Style CYAN = textPane.addStyle(null, null);
    private Style GREEN = textPane.addStyle(null, null);
    private Style YELLOW = textPane.addStyle(null, null);
    private Style MAGENTA = textPane.addStyle(null, null);
    private Style RED = textPane.addStyle(null, null);
    private Style BLUE = textPane.addStyle(null, null);
    private Style PINK = textPane.addStyle(null, null);
    private Style WHITE = textPane.addStyle(null, null);
    private Style GRAY = textPane.addStyle(null, null);
    private StyledDocument doc = textPane.getStyledDocument();
    
    private final SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
    //"[" +fmt.format(new Date())+"] "
    
    public static final String C_RST = "\u001B[0m";
    public static final String C_GREEN = "\u001B[32m";
    
//////CONSTRUCTOR///////////////////////////////////////////////////////////////
    public ChatClient(String dir, int port) {
                
        System.out.print(C_GREEN + "Connecting, please wait...");
        while(socket == null){
            try {
                socket = new Socket(dir, port);
                System.out.println("\nConnected to: " 
                        + socket.getInetAddress().getHostAddress() + C_RST );
                start();

            } catch (IOException ex) {
                System.out.print(".");
            }
        }
                
        /////// GUI ↓ //////////////////////////////////////
        StyleConstants.setForeground(CYAN, Color.cyan);
        StyleConstants.setForeground(GREEN, Color.green);
        StyleConstants.setForeground(YELLOW, Color.yellow);
        StyleConstants.setForeground(MAGENTA, Color.magenta);
        StyleConstants.setForeground(RED, Color.red);
        StyleConstants.setForeground(BLUE, Color.blue);
        StyleConstants.setForeground(PINK, Color.pink);
        StyleConstants.setForeground(WHITE, Color.WHITE);
        StyleConstants.setForeground(GRAY, Color.DARK_GRAY);
        
        printOnScreen(welcomeMessage(), "GREEN");
        
        textPane.setEditable(false);
        textPane.setBackground(Color.black);
        textPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11) );
        
        textBox = new JTextField();
        textBox.setBackground(Color.GRAY);
        textBox.setEditable(true);
        textBox.addActionListener((ActionEvent event) -> {
            sendString(event.getActionCommand() );
            textBox.setText("");
            scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
        });
        
        JFrame frame = new JFrame(userName);
        frame.getContentPane().add(textPane);
        frame.getContentPane().add(textBox, BorderLayout.SOUTH);
        frame.getContentPane().add(scroll = new JScrollPane(textPane));
        frame.setSize(480, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public ChatClient(StyledDocument doc){
        //This constructor is for the FileSender class
        //to be able to use printOnScreen from there
        this.doc = doc;
    }
    
    //MAIN//////////////////////////////////////////////////////////////////////
    public static void main(String args[]) {
        
        System.out.print(C_GREEN + "(If you are the server, put your IP, not 'localhost')\n");
        System.out.print("Insert the IP of the server: " + C_RST);
        dir = keyboard.nextLine();
        
        System.out.print(C_GREEN + "Choose an user name: " + C_RST);
        userName = keyboard.nextLine();
        
        ChatClient client = new ChatClient(dir, 5000);
    }
    
//////METHODS///////////////////////////////////////////////////////////////////
    private String welcomeMessage(){
        return ""
                + "Welcome to uMessenger 2.0 (by Ismael Estalayo).\n"
                + "       __  __                                          \n"
                + "      |  \\/  |                                         \n"
                + " _   _| \\  / | ___  ___ ___  ___ _ __   __ _  ___ _ __ \n"
                + "| | | | |\\/| |/ _ \\/ __/ __|/ _ \\ '_ \\ / _` |/ _ \\ '__|\n"
                + "| |_| | |  | |  __/\\__ \\__ \\  __/ | | | (_| |  __/ |   \n"
                + " \\__,_|_|  |_|\\___||___/___/\\___|_| |_|\\__, |\\___|_|   \n"
                + "                                        __/ |          \n"
                + "                                       |___/            \n"
                + "This is a small messenger-like app made with Sockets in Java \n"
                + "with a GUI to chat with your friends on the same LAN. You can\n"
                + "also use a RaspBerry Pi to host a server and chat among      \n"
                + "different LANs.                                              \n"
                + "Use /help for available commands.                            \n"
                + "\n"
                + "Full code and suggestions at www.github.com/IsmaelEstalayo   \n"
                + "───────────────────────────────────────\n";
    }
    
    public void start() throws IOException {
        dos = new DataOutputStream(socket.getOutputStream() );
        
        if (thread == null) {
            client = new ChatClientThread(this, socket);
            thread = new Thread(this);
            thread.start();
        }
    }
    
    @Override
    public void run() {
        while (thread != null){
            //keep opened
        }
    }
    
    private void sendString(String s){
        try {
            dos.writeUTF(s);
            dos.flush();
            
        } catch (IOException ex) {
            System.out.println("Sending error: " + ex);
        }
    }

    public void chatting(Object obj) {
        
        String type = obj.getType();
        String user = obj.getUser();
        String color = obj.getColor();
        String msg = obj.getMsg();
        
        switch (type) {
            
            case "CHAT":
                printOnScreen(user + ": ", color);
                printOnScreen(msg + "\n", "WHITE");
                break;
                
            case "INFO":
                //Only print it to the user that did the request
                if(user.equals(userName) )
                    printOnScreen(msg + "\n", "GRAY");
                
                break;
            
            case "NEW":
                //Print to all users
                printOnScreen("User " +user+ " connected!\n", "CYAN");
                break;
                
            case "FIN":
                //For the user that requested it, end it's session
                if(user.equals(userName) ){
                    printOnScreen("_________________________________________\n", "RED");
                    printOnScreen("Good bye. You can now close the window...\n", "RED");
                    closeAll();
                }
                //Rest of users, message of dissconect
                else{
                    printOnScreen("User " +user+ " dissconected.\n", "RED");
                }
                break;
            
            case "FILE":
                if(user.equals(userName) ){
                    printOnScreen("   Opening a subprocess for sending a file..\n", "GRAY");
                    FileSender sender = new FileSender(dir, doc);
                    
                } else{
                    printOnScreen("   Opening a subprocess for receiving a file..\n", "GRAY");
                    FileReceiver receiver = new FileReceiver(doc);
                }
                break;
        }
    }

    public void closeAll() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
        try {
            if (dos != null)
                dos.close();
            if (socket != null)
                socket.close();
            
        } catch (IOException ex) {
            System.out.println("Error closing: " + ex);
        }
        client.close();
        client.stop();
    }
    public void sendUserName(){
        try {
            dos.writeUTF(userName);
            
        } catch (IOException ex) {
            System.out.println("Error sending UTF:\n" + ex);
        }
    }

    public void printOnScreen(String msg, String color){
        try {
            switch (color) {
                case "CYAN":
                    doc.insertString(doc.getLength(), msg, CYAN);
                    break;
                case "GREEN":
                    doc.insertString(doc.getLength(), msg, GREEN);
                    break;
                case "YELLOW":
                    doc.insertString(doc.getLength(), msg, YELLOW);
                    break;
                case "MAGENTA":
                    doc.insertString(doc.getLength(), msg, MAGENTA);
                    break;
                case "RED":
                    doc.insertString(doc.getLength(), msg, RED);
                    break;
                case "BLUE":
                    doc.insertString(doc.getLength(), msg, BLUE);
                    break;
                case "PINK":
                    doc.insertString(doc.getLength(), msg, PINK);
                    break;
                case "GRAY":
                    doc.insertString(doc.getLength(), msg, GRAY);
                    break;
                default:
                    doc.insertString(doc.getLength(), msg, WHITE);
                    break;
            }

        } catch (BadLocationException ex){
            System.out.println("ERRRRROR: " + ex);
        }
    }
    
}