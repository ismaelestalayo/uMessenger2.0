package fileTransfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.text.StyledDocument;
import uMessenger.ChatClient;

//SAME ON FILERECEIVER AND FILESENDER
public class FilesConnection {
    
    private int port;
    private String dir;
    public String fileName;
    
    ChatClient zz;
    StyledDocument doc;
    
    Socket socketCliente;
    ServerSocket socketServer;
    
    DataInputStream dis;
    DataOutputStream dos;
    
//////CONSTRUCTOR///////////////////////////////////////////////////////////////
    public FilesConnection(String dir, int port, StyledDocument doc){
        this.port = port;
        this.dir = dir;
        this.doc = doc;
        
        zz = new ChatClient(doc);
        
        dis = null;
        dos = null;
        
        openClientSocket();
        openStreams();
    }
    public FilesConnection(int port, StyledDocument doc){
        this.port = port;
        this.doc = doc;
        
        zz = new ChatClient(doc);
        
        dis = null;
        dos = null;
        
        openServerSocket();
        openStreams();
    }
    
//////METHODS///////////////////////////////////////////////////////////////////
    private void openClientSocket(){
        
        zz.printOnScreen("   >Trying to connect to " + dir + "(" + port + ")..", "GRAY");
        do{
            try {
                socketCliente = new Socket(dir, port);
                zz.printOnScreen("\n   >Connected to the receiver!\n", "GRAY");

            } catch (IOException ex) {
            }
        } while(socketCliente == null);
    }
    private void openServerSocket(){
        
        zz.printOnScreen("   >Waiting for the sender...", "GRAY");
        do{
            try {
                socketServer = new ServerSocket(port);
                socketCliente = socketServer.accept();
                zz.printOnScreen("\n   >Connected to the sender!\n", "GRAY");

            } catch (Exception ex) {
                zz.printOnScreen(".", "GREEN");
            } 
        } while(socketServer == null);
    }
    public void closeAllSockets(){
        
        try {
            if(socketServer != null)
                socketServer.close();
            if(socketCliente != null)
                socketCliente.close();
            
        } catch (IOException ex) {
            System.out.println("   >ERROR CLOSING ALL SOCKETS: " + ex);
        }
    }
    
    private void openStreams(){
        
        try {
            dis = new DataInputStream( socketCliente.getInputStream() );
            dos = new DataOutputStream( socketCliente.getOutputStream() );
            
        } catch (IOException ex) {
            System.out.println("   >ERROR OPENING STREAMS: " + ex);
        }
    }
    
////////////////////////////////////////////////////////////////////////////////    
    public void sendLong(long x){ //Only needed for the FileSender
        
        try {
            dos.writeLong(x);
        } catch (IOException ex) {
            System.out.println("   >ERROR SENDING LONG ON FILESCONNECTION: " + ex);
        }
    }
    public long receiveLong(){ //Only needed for the FileReceiver
        
        long x = 0;
        
        try {
            x = dis.readLong();
        } catch (Exception ex) {
            System.out.println("   >ERROR RECEIVING LONG ON FILESCONNECTION: " + ex);
        }
        
        return x;
    }
    public void sendString(String s){ //Only needed for the FileSender
        
        try {
            dos.writeUTF(s);
        } catch (IOException ex) {
            System.out.println("   >ERROR SENDING STRING ON FILESCONNECTION: " + ex);
        }
    }
    public String receiveString(){ //Only needed for the FileReceiver
        String x = "";
        try {
            x = dis.readUTF();
        } catch (IOException ex) {
            System.out.println("   >ERROR RECIEVING STRING ON FILESCONNECTION: " + ex);
        }
        return x;
    }
    
    //*******************************************************
    public void sendArray(FilesArray a, long segmentSize){
        
        long dim = a.getDim();
        if((float)dim/1000000 < 1)
            zz.printOnScreen(" [" + (float)dim/1000 + "kb]\n", "GRAY");
        if((float)dim/1000000 > 1)
            zz.printOnScreen(" [" + (float)dim/1000000 + "Mb]\n", "GRAY");
        
        sendLong(dim);
        sendLong(segmentSize);
        zz.printOnScreen("    [", "GRAY");
        
        //All segments except the last one
        //Because last segment may be smaller than segment size
        int i = 0, j = 0;
        while(i < (dim/segmentSize)){
            
            try {
                dos.write( a.getArray(), i*(int)segmentSize, (int)segmentSize);
                
            } catch (IOException ex) {
                System.out.println("   >ERROR SENDING A SEGMENT: " + ex);
            }
            
            //NEW WAY OF DISPLAYING PROGRESS:
            if(i > j*(dim/segmentSize)/34){
                zz.printOnScreen("|", "GRAY");
                j++;
            }
            
            i++;
        }
        
        //Last segment 
        try {
            dos.write( a.getArray(), i*(int)segmentSize, (int)(dim%segmentSize) );
            zz.printOnScreen("|]\n", "GRAY");
            
        } catch (IOException ex) {
            System.out.println("   >ERROR SENDING LAST SEGMENT: " + ex);
        }
    }
    
    public FilesArray receiveArray(){
        
        long dim = receiveLong();
        if((float)dim/1000000 < 1)
            zz.printOnScreen(" [" + (float)dim/1000 + "kb]\n", "GRAY");
        if((float)dim/1000000 > 1)
            zz.printOnScreen(" [" + (float)dim/1000000 + "Mb]\n", "GRAY");
        long segmentSize = receiveLong();
        zz.printOnScreen("    [", "GRAY");
        
        //All segments except the last one
        //Because last segment may be smaller than segment size
        int i = 0, j = 0;
        FilesArray b = new FilesArray(dim);
        while( i < (dim/segmentSize)){
            
            try {
                dis.read( b.getArray(), i*(int)segmentSize, (int)segmentSize);
                
            } catch (IOException ex) {
                System.out.println("   >ERROR RECEIVING A SEGMENT: " + ex);
            }
            
            //NEW WAY OF DISPLAYING PROGRESS:
            if(i > j*(dim/segmentSize)/32){
                zz.printOnScreen("|", "GRAY");
                j++;
            }
            i++;
        }
        
        //Last segment
        try {
            dis.read( b.getArray(), i*(int)segmentSize, (int)dim%(int)segmentSize);
            zz.printOnScreen("|]\n", "GREEN");
            
        } catch (IOException ex) {
            System.out.println("   >ERROR RECEIVING LAST SEGMENT: " + ex);
        }
        
        return b;
    }
    
}
