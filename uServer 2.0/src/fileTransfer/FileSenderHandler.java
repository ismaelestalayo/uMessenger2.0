package fileTransfer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;

public class FileSenderHandler {
    
    File f;
    FileInputStream fis;
    BufferedInputStream bis;
    DataInputStream dis;
    
    FileOutputStream fos;
    BufferedOutputStream bos;
    DataOutputStream dos;
    
    private String fileName;
    private String filePath;
    private long dim;
    
//////CONSTRUCTOR///////////////////////////////////////////////////////////////
    public FileSenderHandler() throws NullPointerException{
                
        final JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null);
        
        f = fc.getSelectedFile();
        
        if(f != null){
            fileName = f.getName();
            filePath = f.getAbsolutePath();
        } else{
            fileName = "NULL";
            filePath = "NULL";
        }

    }
    
    public FileSenderHandler(String fileName){
        this.fileName = fileName;
        this.filePath = fileName;
    }
    
//////METHODS///////////////////////////////////////////////////////////////////
    private void openBinaryReading(){
        
        try {
            fis = new FileInputStream(filePath);
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
            fos = new FileOutputStream(filePath);
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
            System.out.println("   >ERROR CLOSING BINARY WRINTING MODE: " + ex);
        }
    }
    
////////////////////////////////////////////////////////////////////////////////
    public long getFileDim(){
        
        openBinaryReading();
        try {
            dim = dis.available();
            
        } catch (Exception ex) {
            System.out.println("   >ERROR GETTING FILE DIMENSION:" + ex);
        }
        
        closeBinaryReading();
        return dim;
    }
    public String getFileName(){
        return fileName;
    }
    
////////////////////////////////////////////////////////////////////////////////
    public FilesArray dumpFileToArray(){
        
        long dimArray = getFileDim();
        
        openBinaryReading();
        FilesArray a = new FilesArray(dimArray);
        
        try {
            dis.read( a.getArray() );
            
        } catch (IOException ex) {
            System.out.println("   >ERROR DUMPING THE FILE TO AN ARRAY: " + ex);
        }
        
        closeBinaryReading();
        return a;
    }

}
