package fileTransfer;

import javax.swing.text.StyledDocument;
import uMessenger.ChatClient;

public class FileSender {
    
    public FileSender(String dir, StyledDocument doc){
        ChatClient zz = new ChatClient(doc);
        FilesConnection con = new FilesConnection(dir, 5077, doc);
        FileSenderHandler file = new FileSenderHandler();
        
        String fileName = file.getFileName();
        con.sendString(fileName );
        
        if(fileName.equals("NULL")){
            zz.printOnScreen("   >File not selected. \n", "GREEN");
            zz.printOnScreen("   >Exiting... \n", "GREEN");
            
        } else{
            zz.printOnScreen("   >File to send: " + fileName, "GREEN");

            FilesArray fileDumpedInArray = file.dumpFileToArray();
            con.sendArray(fileDumpedInArray, 1);
            
            zz.printOnScreen("   >File " + fileName + " sent. \n", "GREEN");
        }
        con.closeAllSockets();
    }
}
