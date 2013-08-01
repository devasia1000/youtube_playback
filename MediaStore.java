


/**
 *
 * @author devasia
 */

import java.io.*;

public class MediaStore {

    private FileOutputStream wt;
    private FileInputStream in;
    
    private String clen;
    private String mime;

    MediaStore(String cl, String type) {

        clen=cl;
        mime=type;
        
        try {
            if(mime.equals("audio") || mime.equals("video")){
                File f=new File(clen+"."+mime);
                f.createNewFile();
                
                wt=new FileOutputStream(f);
            } else {
                System.err.println("unknown media type");
                System.exit(-1);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void storeMedia(HTTPResponse resp) throws Exception{
        wt.write(resp.returnContent());
        wt.flush();
    }
    
    public void getMedia(String req) throws Exception{
        
    }
    
    public String getCLEN(){
        return clen;
    }
    
    public String getMime(){
        return mime;
    }
}

