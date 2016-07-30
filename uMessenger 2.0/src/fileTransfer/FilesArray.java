package fileTransfer;

//SAME ON FILERECEIVER AND FILESENDER
public class FilesArray {
    
    private byte [] a;
    private long dim;
    
//////CONSTRUCTOR///////////////////////////////////////////////////////////////
    public FilesArray(){
        a = null;
    }
    
    public FilesArray(long dim){
        a = new byte[(int)dim];
        this.dim = dim;
    }
    
//////METHODS///////////////////////////////////////////////////////////////////
    public long getDim(){
        return dim;
    }
    
//    public void addElementAt(byte b, int pos){
//        a[pos] = b;
//    }
    
//    public byte getElementAt(int pos){
//        return a[pos];
//    }
    
    public byte [] getArray(){
        return a;
    }
    
}
