package fileTransfer;

public class FileSender {
    
    public FileSender(String dir){
        FilesConnection con = new FilesConnection(dir, 5077);
        FileSenderHandler file = new FileSenderHandler();
        
        String fileName = file.getFileName();
        con.sendString(fileName );
        
        if(fileName.equals("NULL")){
            System.out.println("   >File not selected.");
            System.out.println("   >Exiting...");
            
        } else{
            System.out.println("   >File to send: " + fileName);

            FilesArray fileDumpedInArray = file.dumpFileToArray();
            con.sendArray(fileDumpedInArray, 1);

            System.out.println("   >File " + fileName + " sent");
        }
        con.closeAllSockets();
    }
}
