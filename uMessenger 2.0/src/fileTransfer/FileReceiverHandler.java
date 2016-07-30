package fileTransfer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileReceiverHandler {
    
    FileInputStream fis;
    BufferedInputStream bis;
    DataInputStream dis;
    
    FileOutputStream fos;
    BufferedOutputStream bos;
    DataOutputStream dos;
    
    private String fileName;
    private int dim;
    
//////CONSTRUCTOR///////////////////////////////////////////////////////////////
    public FileReceiverHandler(String fileName){
        this.fileName = fileName;
    }
    
//////METHODS///////////////////////////////////////////////////////////////////
    private void openBinaryReading(){
        
        try {
            /*Here I use fileName instead of filePath, to save the file on
            the same directory the server is on.*/
            fis = new FileInputStream(fileName);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);
            
        } catch (FileNotFoundException ex) {
            System.out.println("   >ERROR OPENING BINARY READING MODE: " + ex);
        }
    }
    private void closeBinaryReading(){
        
        try {
            if(dis != null)
                dis.close();
            if(bis != null)
                bis.close();
            if(fis != null)
                fis.close();
            
        } catch (Exception ex) {
            System.out.println("   >ERROR CLOSING BINARY READING MODE: " + ex);
        }
    }
    
    private void openBinaryWriting(){
        
        try {
            /*Same as before on the fileName or filePath stuff*/
            fos = new FileOutputStream(fileName);
            bos = new BufferedOutputStream(fos);
            dos = new DataOutputStream(bos);
            
        } catch (FileNotFoundException ex) {
            System.out.println("   >ERROR OPENING BINARY WRITING MODE: " + ex);
        }
    }
    private void closeBinaryWriting(){
        
        try {
            
            if(dos != null)
                dos.close();
            if(bos != null)
                bos.close();
            if(fos != null)
                fos.close();
            
        } catch (Exception ex) {
            System.out.println("   >ERROR CLOSINGBINARY WRITING MODE: " + ex);
        }
    }
    
////////////////////////////////////////////////////////////////////////////////
    public int getFileDim(){
        
        openBinaryReading();
        try {
            dim = dis.available();
            
        } catch (Exception ex) {
            System.out.println("   >ERROR OBTENIENDO DIMENSION\n" + ex);
        }
        
        closeBinaryReading();
        return dim;
    }
    public String getFileName(){
        return fileName;
    }
    
////////////////////////////////////////////////////////////////////////////////
    public void dumpArrayToFile(FilesArray a){
        
        openBinaryWriting();
        System.out.println("   >File " + fileName + " saved.");
        
        try {
            dos.write( a.getArray() );
            
        } catch (IOException ex) {
            System.out.println("   >ERROR DUMPING THE ARRAY TO A FILE: " + ex);
        }
        closeBinaryWriting();
    }
    
}
