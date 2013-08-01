


/**
 *
 * @author devasia
 */

import java.io.*;

public class MediaManager {

    FileOutputStream wt;
    FileInputStream in;

    MediaManager(String clen, String type) {

        try {
            if(type.equals("audio") || type.equals("video")){
                File f=new File(clen+"."+type);
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
    
    public static String getCLEN(String req){
        String ret="";
        
        String el[]=req.split("&");
        for(String s:el){
            if(s.contains("clen")){
                String el2[]=s.split("=");
                ret=el2[1].trim();
            }
        }
        
        if(ret.equals("")){
            System.err.println("could not find clen in videoplayback request");
        }
        
        return ret;
    }
    
    public static String getMime(String req){
        String ret="";
        
        String el[]=req.split("&");
        for(String s:el){
            if(s.contains("mime")){
                String el2[]=s.split("=");
                if(el2[1].trim().contains("audio")){
                    ret="audio";
                } else if (el2[1].trim().contains("video")){
                    ret="video";
                }
            }
        }
        
        if(ret.equals("")){
            System.err.println("could not find mime in videoplayback request");
        }
        
        return ret;
    }
    
    
}

