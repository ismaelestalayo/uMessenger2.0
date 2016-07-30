package uMessenger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class UserListHandler {    
    
    private File f = null;
    private FileReader fr;
    private BufferedReader br;
    
    private FileWriter fw;
    private BufferedWriter bw;
    private PrintWriter pw;
    
    private String fileName;
    
    //CONSTRUCTOR///////////////////////////////////////////////////////////////
    public UserListHandler(String fileName){
        
        this.fileName = fileName;
        
        f = new File(fileName);
    }
    
    //METHODS///////////////////////////////////////////////////////////////////
    public void openReadMode(){
        
        try {
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);
            
        } catch (IOException ex) {
            System.out.println("ERROR OPENING FILE IN READING MODE: " + ex);
        }
    }
    public void closeReadMode(){
        
        try {
            if(br != null)
                br.close();
            if(fr != null)
                fr.close();
            
        } catch (IOException ex) {
            System.out.println("ERROR CLOSING FILE IN READING MODE: " + ex);
        }
    }
    private void openWriteMode(){
        
        try {
            fw = new FileWriter(fileName, true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);
            
        } catch (IOException ex) {
            System.out.println("ERROR OPENING FILE IN WRITE MODE: " + ex);
        }
    }
    private void closeWriteMode(){
        
        try {
            if(pw != null)
                pw.close();
            if(bw != null)
                bw.close();
            if(fw != null)
                fw.close();
            
        } catch (IOException ex) {
            System.out.println("ERROR CLOSING FILE IN WRITE MODE: " + ex);
        }
    }
    
//******************************************************************************
    public void addToList(String name){
        openWriteMode();
        pw.write(name);
        closeWriteMode();
    }
    
    public String readLine(){
        
        String s;
        
        try {
            s = br.readLine();
            
        } catch (IOException ex) {
            System.out.println("ERROR READING LINE: " + ex);
            s = null;
        }
        
        return s;
    }
}

