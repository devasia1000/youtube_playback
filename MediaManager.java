/**
 *
 * @author devasia
 */

import java.util.ArrayList;

public class MediaManager {

    private static ArrayList<MediaStore> mediaList=new ArrayList<MediaStore>();
    
    public static void handle(String req, HTTPResponse resp) throws Exception{
        for(MediaStore store: mediaList){
            String clen=parseClen(req);
            String mime=parseMime(req);
            if(store.getCLEN().equals(clen) && store.getMime().equals(mime)){
                store.storeMedia(resp);
            } else {
                mediaList.add(new MediaStore(clen, mime));
            }
        }
    }
    
    private static String parseClen(String req){
        String clen="";
        
        String el[]=req.split("&");
        for(String s:el){
            if(s.toLowerCase().contains("clen")){
                String el2[]=s.split("=");
                clen=el2[1].trim();
            }
        }
        
        if(clen.equals("")){
            System.err.println("could not parse clen");
            System.exit(-1);
        } 
        
        return clen;
    }
    
    private static String parseMime(String req){
        String mime="";
        
        String el[]=req.split("&");
        for(String s:el){
            if(s.toLowerCase().contains("mime")){
                String el2[]=s.split("=");
                if(el2[1].trim().contains("audio")){
                    mime="audio";
                } else if (el2[1].trim().contains("video")){
                    mime="video";
                }
            }
        }
        
        if(mime.equals("")){
            System.err.println("could not parse clen");
            System.exit(-1);
        } 
        
        return mime;
    }
}
