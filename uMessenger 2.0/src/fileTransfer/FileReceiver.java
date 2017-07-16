package fileTransfer;

import javax.swing.text.StyledDocument;
import uMessenger.ChatClient;

public class FileReceiver {
    
    private String fileName;
    
    public FileReceiver(StyledDocument doc){
        FilesConnection ms = new FilesConnection(5077, doc);
        ChatClient zz = new ChatClient(doc);
        
        fileName = ms.receiveString();
        if(fileName.equals("NULL") ){
            zz.printOnScreen("   >Sender didnt select a file.\n", "GRAY");
            zz.printOnScreen("   >Exiting...\n", "GRAY");
            
        } else{
            zz.printOnScreen("   >File to receive: " + fileName, "GRAY");
            FilesArray receivedArray = ms.receiveArray();

            FileReceiverHandler binario = new FileReceiverHandler(fileName);
            binario.dumpArrayToFile(receivedArray);
            zz.printOnScreen("   >File " +fileName+ " saved.\n", "GRAY");
        }
        ms.closeAllSockets();
    }
    
    public String getFileName(){
        return fileName;
    }
}
