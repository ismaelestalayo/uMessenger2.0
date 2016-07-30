package fileTransfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//SAME ON FILERECEIVER AND FILESENDER
public class FilesConnection {
    
    private int port;
    private String dir;
    public String fileName;
    
    Socket socketCliente;
    ServerSocket socketServer;
    
    DataInputStream dis;
    DataOutputStream dos;
    
//////CONSTRUCTOR///////////////////////////////////////////////////////////////
    public FilesConnection(String dir, int port){
        this.port = port;
        this.dir = dir;
        
        dis = null;
        dos = null;
        
        openClientSocket();
        openStreams();
    }
    public FilesConnection(int port){
        this.port = port;
        
        dis = null;
        dos = null;
        
        openServerSocket();
        openStreams();
    }
    
//////METHODS///////////////////////////////////////////////////////////////////
    private void openClientSocket(){
        
        System.out.print("   >Trying to connect to " + dir + "(" + port + ")..");
        do{
            try {
                socketCliente = new Socket(dir, port);
                System.out.println("\n   >Connected to the receiver!");

            } catch (IOException ex) {
                System.out.print(".");
            }
        } while(socketCliente == null);
    }
    private void openServerSocket(){
        
        System.out.println("   >Waiting for the sender...");
        try {
            socketServer = new ServerSocket(port);
            socketCliente = socketServer.accept();
            System.out.println("   >Connected to the sender!");
            
        } catch (Exception ex) {
            System.out.println("   >ERROR OPENING SERVER SOCKET: " + ex);
        }
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
        System.out.println("   >File size: " + dim/1000 + "kb");
        sendLong(dim);
        //System.out.println("  >Segment size: " + segmentSize);
        sendLong(segmentSize);
        System.out.print("    [");
        
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
                System.out.print("|");
                j++;
            }
            
            i++;
        }
        
        //Last segment 
        try {
            dos.write( a.getArray(), i*(int)segmentSize, (int)(dim%segmentSize) );
            System.out.println("|]");
            
        } catch (IOException ex) {
            System.out.println("   >ERROR SENDING LAST SEGMENT: " + ex);
        }
        System.out.println("   >File transfer complete.");
    }
    
    public FilesArray receiveArray(){
        
        long dim = receiveLong();
        System.out.println("   >File size: " + dim/1000 + "kb");
        long segmentSize = receiveLong();
        //System.out.println("  >Segment size: " + segmentSize);
        System.out.print("    [");
        
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
                System.out.print("|");
                j++;
            }
            i++;
        }
        
        //Last segment
        try {
            dis.read( b.getArray(), i*(int)segmentSize, (int)dim%(int)segmentSize);
            System.out.println("|]");
            
        } catch (IOException ex) {
            System.out.println("   >ERROR RECEIVING LAST SEGMENT: " + ex);
        }
        System.out.println("   >File transfer complete.");
        
        return b;
    }
    
}
