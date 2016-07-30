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
    
    private Style BLUE = textPane.addStyle(null, null);
    private Style PINK = textPane.addStyle(null, null);
    private Style GREEN = textPane.addStyle(null, null);
    private Style CYAN = textPane.addStyle(null, null);
    private Style RED = textPane.addStyle(null, null);
    private Style BLACK = textPane.addStyle(null, null);
    private Style YELLOW = textPane.addStyle(null, null);
    private Style WHITE = textPane.addStyle(null, null);
    private StyledDocument doc = textPane.getStyledDocument();
    
    private final SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
    //"[" +fmt.format(new Date())+"] "
    
    public static final String C_RST = "";
    public static final String C_GREEN = "";
    
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
        StyleConstants.setForeground(BLUE, Color.blue);
        StyleConstants.setForeground(RED, Color.RED);
        StyleConstants.setForeground(PINK, Color.PINK);
        StyleConstants.setForeground(GREEN, Color.GREEN);
        StyleConstants.setForeground(CYAN, Color.CYAN);
        StyleConstants.setForeground(BLACK, Color.BLACK);
        StyleConstants.setForeground(YELLOW, Color.yellow);
        StyleConstants.setForeground(WHITE, Color.WHITE);
        
        printOnScreenln(welcomeMessage(), GREEN);
        
        textPane.setEditable(false);
        textPane.setBackground(Color.black);
        
        textBox = new JTextField();
        textBox.setBackground(Color.GRAY);
        textBox.setEditable(true);
        textBox.addActionListener((ActionEvent event) -> {
            sendString(event.getActionCommand() );
            textBox.setText("");
        });
        
        JFrame frame = new JFrame(userName);
        frame.getContentPane().add(textPane);
        frame.getContentPane().add(textBox, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(textPane));
        frame.setSize(460, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
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
            + "         _______                                    \n"
            + " .--.--.|   |   |.-----.-----.-----.-----.-----.-----.-----.----.   \n"
            + " |  |  ||       ||  -__|__ --|__ --|  -__|     |  _  |  -__|   _|   \n"
            + " |_____||__|_|__||_____|_____|_____|_____|__|__|___  |_____|__|     \n"
            + "                                               |_____|              \n"
            + "Welcome to UMessenger 2.0 (by Ismael Estalayo).\n"
            + "\n"
            + "This is a small messenger-like app made with Sockets in Java with a GUI\n"
            + "to chat with your friends. \n"
            + "Use /help for available commands.\n"
            + "\n"
            + "Full info and suggestions at www.github.com/IsmaelEstalayo\n";
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
                switch (color){
                    case "BLUE":
                        printOnScreen(user + ": ", BLUE);
                        break;
                        
                    case "CYAN":
                        printOnScreen(user + ": ", CYAN);
                        break;
                    case "GREEN":
                        printOnScreen(user + ": ", GREEN);
                        break;
                    case "PURPLE":
                        printOnScreen(user + ": ", PINK);
                        break;
                    case "RED":
                        printOnScreen(user + ": ", RED);
                        break;
                    case "YELLOW":
                        printOnScreen(user + ": ", YELLOW);
                        break;
                    default:
                        printOnScreenln("ERROR GETTING YOUR COLOR!", WHITE);
                        printOnScreen(user + ": ", WHITE);
                        break;
                }
                
                printOnScreenln(msg, WHITE);
                
                break;
                
            case "INFO":
                //Only print it to the user that did the request
                if(user.equals(userName) )
                    printOnScreenln(msg, GREEN);
                
                break;
            
            case "NEW":
                //Print to all users
                printOnScreenln("User " +user+ " connected!", CYAN);
                break;
                
            case "FIN":
                //For the user that requested it, end it's session
                if(user.equals(userName) ){
                    printOnScreenln("_________________________________________", RED);
                    printOnScreenln("Good bye. You can now close the window...", RED);
                    closeAll();
                }
                //Rest of users, message of dissconect
                else{
                    printOnScreenln("User " +user+ " dissconected.", RED);
                }
                break;
            
            case "FILE":
                if(user.equals(userName) ){
                    printOnScreenln("Opening a subprocess for sending a file..", GREEN);
                    FileSender sender = new FileSender(dir);
                    
                } else{
                    printOnScreenln("Opening a subprocess for receiving a file..", GREEN);
                    FileReceiver receiver = new FileReceiver();
                    
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

    public void printOnScreen(String msg, Style style){
        try {
            doc.insertString(doc.getLength(), msg, style);
            
        } catch (BadLocationException ex) {
            System.out.println("ERRORRRRRRRRR: " + ex);
        }
    }
    public void printOnScreenln(String msg, Style style){
        try {
            doc.insertString(doc.getLength(), msg + "\n", style);
            
        } catch (BadLocationException ex) {
            System.out.println("ERRORRRRRRRRR: " + ex);
        }
    }
    
}