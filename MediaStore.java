
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;


/**
 *
 * @author devasia
 */


public class MediaStore {

    String clen;
    String type;
    byte[] media;
    
    public MediaStore(File f){
        try{
            
            String name=f.getName();
            String el[]=name.split("-");
            clen=el[0];
            type=el[1].replace(".mp4", "");
            
            if(!f.exists()){
                System.err.println("media file does not exist");
                System.exit(-1);
            }
            
            media = new byte[(int) f.length()];
            DataInputStream dis = new DataInputStream(new FileInputStream(f));
            dis.readFully(media);
            dis.close();
            
        } catch (Exception e){
            System.err.println("could not read media file properly");
            System.exit(-1);
        }
    }
    
    public String getClen(){
        return clen;
    }
    
    public String getType(){
        return type;
    }
    
    public byte[] processRangeRequest(int fin, int init){
        if(init>=fin){
            System.err.println("incorrect range request");
            System.exit(-1);
        }
        
        // Do NOT remove 'fin+1', this has been tested and it is correct! Look at 'TestingClass' in Github for details
        byte[] dat=Arrays.copyOfRange(media, init, fin+1);
        return dat;
    }
}
