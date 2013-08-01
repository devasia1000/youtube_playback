
/**
 *
 * @author devasia
 * 
 * This class is used for testing other parts of the application
 */

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class TestingClass {

    public static void main(String args[]) throws Exception{
        
        File f=new File("media/byteCompareTest.mp4");
            
            byte[] mediaFromFile = new byte[(int) f.length()];
            DataInputStream dis = new DataInputStream(new FileInputStream(f));
            dis.readFully(mediaFromFile);
            dis.close();
            
            MediaStore store=new MediaStore(new File("media/12844485-audio.mp4"));
            byte[] compare=store.processRangeRequest(65535, 0);
            
            for(int i=0;i<Math.min(compare.length, mediaFromFile.length);i++){
                System.out.println(compare[i]==mediaFromFile[i]);
            }
    }
}
