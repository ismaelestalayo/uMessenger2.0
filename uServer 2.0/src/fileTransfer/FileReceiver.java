package fileTransfer;

public class FileReceiver {
    
    private String fileName;
    
    public FileReceiver(){
        FilesConnection ms = new FilesConnection(5077);
        
        fileName = ms.receiveString();
        if(fileName.equals("NULL") ){
            System.out.println("   >Sender didnt select a file.");
            System.out.println("   >Exiting...");
            
        } else{
            System.out.println("   >File to receive: " + fileName);
            FilesArray receivedArray = ms.receiveArray();

            FileReceiverHandler binario = new FileReceiverHandler(fileName);
            binario.dumpArrayToFile(receivedArray);
        }
        ms.closeAllSockets();
    }
    
    public String getFileName(){
        return fileName;
    }
}
