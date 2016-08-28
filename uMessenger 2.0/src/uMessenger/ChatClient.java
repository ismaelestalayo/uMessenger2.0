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
import javax.swing.*;
import javax.swing.text.*;

public final class ChatClient extends JFrame implements Runnable{

    private Socket socket = null;
    private Thread thread = null;
    private ChatClientThread client = null;
    
    private DataOutputStream dos = null;
    
    private static String userName;
    private static String dir = null;
    
    private JScrollPane jScrollPane1;
    private JTextField jTextField1;
    private JTextPane textPane = new JTextPane();
    private JTextPane userList;
    private JButton jButton1;
    private DefaultCaret caret;
    private JMenu jMenu1, jMenu2;
    private JMenuBar jMenuBar1;
    private JMenuItem jMenuItem1, jMenuItem2, jMenuItem3, jMenuItem4;
    
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
    
//////CONSTRUCTOR///////////////////////////////////////////////////////////////
    public ChatClient(String dir, int port) {
                
        System.out.print("Connecting, please wait...");
        while(socket == null){
            try {
                socket = new Socket(dir, port);
                System.out.println("\nConnected to: " 
                        + socket.getInetAddress().getHostAddress());
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
        
        initComponents();
    }
    public ChatClient(StyledDocument doc){
        //This constructor is for the FileSender class
        //to be able to use printOnScreen from there
        this.doc = doc;
    }
    
    //MAIN//////////////////////////////////////////////////////////////////////
    public static void main(String args[]) {
        //Give a Windows look to the whole UI
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        }
        
        JPanel p = new JPanel(new BorderLayout(5, 5));
        
        JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
        labels.add(new JLabel("Server's IP", SwingConstants.RIGHT));
        labels.add(new JLabel("Username", SwingConstants.RIGHT));
        p.add(labels, BorderLayout.WEST);

        JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField field1 = new JTextField();
        controls.add(field1);
        JTextField field2 = new JTextField();
        controls.add(field2);
        p.add(controls, BorderLayout.CENTER);
        
        do{
            int n = JOptionPane.showConfirmDialog(null, p, "Connect to uMessenger", JOptionPane.CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            
            dir = field1.getText();
            userName = field2.getText();
            
            if(n == JOptionPane.CANCEL_OPTION || n == JOptionPane.CLOSED_OPTION)
                System.exit(0);
            
        }while(dir.equals(""));
        
        ChatClient client = new ChatClient(dir, 5000);
    }
    
//////METHODS///////////////////////////////////////////////////////////////////
    private String welcomeMessage(){
        return ""
                //+ "Welcome to uMessenger 2.0 (by Ismael Estalayo).\n"
                + "         __  __                                             \n"
                + "        |  \\/  |    Use /help for available commands    \n"
                + "   _   _| \\  / | ___  ___ ___  ___ _ __   __ _  ___ _ __   \n"
                + "  | | | | |\\/| |/ _ \\/ __/ __|/ _ \\ '_ \\ / _` |/ _ \\ '__|\n"
                + "  | |_| | |  | |  __/\\__ \\__ \\  __/ | | | (_| |  __/ |   \n"
                + "   \\__,_|_|  |_|\\___||___/___/\\___|_| |_|\\__, |\\___|_| \n"
                + "                                          __/ |             \n"
                + "         www.github.com/IsmaelEstalayo   |___/              \n"
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
            //keep open
        }
    }
    
    public void sendString(String s){
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
                printOnScreen("   >User " +user+ " connected!\n", "CYAN");
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
                    printOnScreen("   >User " +user+ " dissconected.\n", "RED");
                }
                break;
            
            case "FILE":
                if(user.equals(userName) ){
                    printOnScreen("   >Opening a subprocess for sending a file..\n", "GRAY");
                    FileSender sender = new FileSender(dir, doc);
                    
                } else{
                    printOnScreen("   >Opening a subprocess for receiving a file..\n", "GRAY");
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
            //Autoscroll
            textPane.setCaretPosition(textPane.getDocument().getLength() );
            
        } catch (BadLocationException ex){
            System.out.println("ERRRRROR: " + ex);
        }
    }
    
    //**************************************************************************
    //**************************************************************************
    //**************************************************************************
    //**************************************************************************
    private void initComponents() {
        
        jScrollPane1 = new JScrollPane();
            jScrollPane1.setViewportView(textPane);
        textPane.setBackground(Color.BLACK);
            textPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11) );
            textPane.setEditable(false);
        userList = new javax.swing.JTextPane();
            userList.setBackground(Color.BLACK);
            userList.setEditable(false);
        jTextField1 = new JTextField();
            jTextField1.setEditable(true);
            jTextField1.setBackground(Color.GRAY);
            jTextField1.addActionListener((ActionEvent evt) -> {
                sendString(evt.getActionCommand() );
                jTextField1.setText("");
            });
        jButton1 = new JButton();
            jButton1.setText("Attach");
            jButton1.addActionListener((ActionEvent evt) -> {
                sendString("/attach");
            });
        caret = (DefaultCaret)textPane.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            
        jMenuBar1 = new JMenuBar();
            jMenu1 = new JMenu();
                jMenu1.setText("Options");
                    jMenuItem1 = new JMenuItem();
                    jMenuItem1.setText("Open downloads folder");
                    jMenuItem1.addActionListener((ActionEvent evt) -> {
                        try {
                            Desktop.getDesktop().open( new File(System.getProperty("user.dir") ));

                        } catch (IOException ex) {
                            printOnScreen("   >Couldnt open downloads folder.\n", "RED");
                        }
                    });
                jMenu1.add(jMenuItem1);
            jMenu2 = new javax.swing.JMenu();
                jMenu2.setText("Help");
                jMenuItem2 = new javax.swing.JMenuItem();
                    jMenuItem2.setText("About");
                    jMenuItem2.addActionListener((ActionEvent evt) -> {
                        JOptionPane.showMessageDialog(textPane,
                                    "This is a small Messenger-like app made with sockets in Java\n"
                                    + "with a GUI to chat with your friends on the same LAN. You can\n"
                                    + "also use a Raspberry Pi to host a server and chat with among\n"
                                    + "different LANs.\n"
                                    + "\n"
                                    + "Full code and suggestions at github.com/IsmaelEstalayo",
                                "uMessenger 2.0 - Ismael Estalayo", JOptionPane.INFORMATION_MESSAGE);
                    });
                jMenuItem3 = new javax.swing.JMenuItem();
                    jMenuItem3.setText("Github");
                    jMenuItem3.addActionListener((ActionEvent evt) -> {
                        try {
                            Desktop.getDesktop().browse( new URL("https://github.com/ismaelestalayo/uMessenger2.0").toURI() );

                        } catch (IOException | URISyntaxException ex) {
                        }
                    });
                jMenu2.add(jMenuItem2);
                jMenu2.add(jMenuItem3);
            jMenuBar1.add(jMenu1);
            jMenuBar1.add(jMenu2);
            setJMenuBar(jMenuBar1);
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
        setVisible(true);
        setTitle("uMessenger");
    }
    
}