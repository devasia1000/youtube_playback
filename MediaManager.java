
/**
 *
 * @author devasia
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;

public class MediaManager {

    private static ArrayList<MediaStore> mediaList = new ArrayList<MediaStore>();
    /* WARNING: this below variable could change */
    private static String mediaDomain = "r12---sn-p5q7yney.c.youtube.com";

    public static HTTPResponse handle(String req) {
        HTTPResponse resp = null;

        String clen = parseClen(req);
        String type = parseType(req);
        String mime = type + "/mp4";

        String range = parseRange(req);
        range = range.replace("range=", "");
        String el[] = range.split("-");
        int initRange = Integer.parseInt(el[0]);
        int finRange = Integer.parseInt(el[1]);


        boolean exists = false;
        for (MediaStore store : mediaList) {
            if (store.getClen().equals(clen) && store.getType().equals(type)) {
                exists = true;
                byte[] b = store.processRangeRequest(finRange, initRange);
                String respHeaders = HardcodedResponseGenerator.returnVideoplaybackResponse(mime, b.length);
                byte[] data = HTTPResponse.concat(respHeaders.getBytes(), b);
                resp = new HTTPResponse(data);
            }
        }

        if (!exists) {
            MediaStore store = new MediaStore(new File("media/" + clen + "-" + type));
            mediaList.add(store);
            byte[] b = store.processRangeRequest(finRange, initRange);
            String respHeaders = HardcodedResponseGenerator.returnVideoplaybackResponse(mime, b.length);
            byte[] data = HTTPResponse.concat(respHeaders.getBytes(), b);
            resp = new HTTPResponse(data);
        }

        if (resp == null) {
            System.err.println("could not create HTTPResponse");
            System.exit(-1);
        }

        return resp;
    }

    public static void downloadMedia() {
        Enumeration<String> keys = Main.returnHashTableKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key.contains("videoplayback")) {
                /* create media directory if it does not exist */
                File dir=new File("media");
                if(!dir.exists()){
                    dir.mkdir();
                }
                
                //System.out.println(key);
                String clen = parseClen(key);
                String type = parseType(key);
                
                String url = key.replace("GET ", "");
                url = url.replace("POST ", "");
                url=url.replace(" HTTP/1.1", "");
                url="http://"+mediaDomain+url;
                
                String range = parseRange(key);
                url = url.replace(range, "range=0-" + Long.MAX_VALUE);
                File f = null;

                try {

                    f = new File("media/" + clen + "-" + type);
                    if (!f.exists()) {
                        f.createNewFile();

                        System.out.println("downloading media stream to "+f.getAbsolutePath()+", please be patient...");
                        Process p = Runtime.getRuntime().exec("wget -O media/" + clen + "-" + type + " " + url);
                        p.waitFor();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        String line=null;
                        while((line=reader.readLine())!=null){
                            System.out.println(line);
                        }
                        reader.close();
                    }

                } catch (Exception e) {
                    f.delete();
                    e.printStackTrace();
                    System.err.println("could not download media");
                    System.exit(-1);
                }


            }
        }
    }

    private static String parseClen(String req) {
        String clen = "";

        String el[] = req.split("&");

        for (String s : el) {
            if (s.startsWith("clen")) {
                String el2[] = s.split("=");
                clen = el2[1].trim();
                break;
            }
        }

        if (clen.equals("")) {
            System.err.println("could not parse clen");
            System.exit(-1);
        }

        return clen;
    }

    private static String parseType(String req) {
        String type = "";

        String el[] = req.split("&");
        for (String s : el) {
            if (s.toLowerCase().contains("mime")) {
                String el2[] = s.split("=");
                if (el2[1].trim().contains("audio")) {
                    type = "audio";
                } else if (el2[1].trim().contains("video")) {
                    type = "video";
                }
            }
        }

        if (type.equals("")) {
            System.err.println("could not parse clen");
            System.exit(-1);
        }

        return type;
    }

    private static String parseMime(String req) {
        String mime = "";

        String el[] = req.split("&");
        for (String s : el) {
            if (s.toLowerCase().contains("mime")) {
                String el2[] = s.split("=");
                mime = el2[1].replace("%2F", "/");
            }
        }

        if (mime.equals("")) {
            System.err.println("could not parse clen");
            System.exit(-1);
        }

        return mime;
    }

    private static String parseRange(String resp) {
        String range = "";
        String el[] = resp.split("&");
        for (String s : el) {
            if (s.toLowerCase().contains("range")) {
                range = s;
                break;
            }
        }

        if (range.equals("")) {
            System.err.println("could not parse clen");
            System.exit(-1);
        }

        return range;
    }
}
