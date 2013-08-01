
/**
 *
 * @author devasia
 */
import java.io.File;
import java.util.ArrayList;

public class MediaManager {

    private static ArrayList<MediaStore> mediaList = new ArrayList<MediaStore>();

    public void init() {
    }

    public static HTTPResponse handle(String req) {
        HTTPResponse resp = null;

        String clen = parseClen(req);
        String type = parseMime(req);
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
                String respHeaders = HardcodedResponses.returnVideoplaybackResponse(mime, b.length);
                byte[] data = HTTPResponse.concat(respHeaders.getBytes(), b);
                resp = new HTTPResponse(data);
            }
        }
        
        if (!exists) {
            MediaStore store = new MediaStore(new File("media/" + clen + "-" + type + ".mp4"));
            mediaList.add(store);
            byte[] b = store.processRangeRequest(finRange, initRange);
            String respHeaders = HardcodedResponses.returnVideoplaybackResponse(mime, b.length);
            byte[] data = HTTPResponse.concat(respHeaders.getBytes(), b);
            resp = new HTTPResponse(data);
        }

        if (resp == null) {
            System.err.println("could not create HTTPResponse");
            System.exit(-1);
        }

        return resp;
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

    private static String parseMime(String req) {
        String mime = "";

        String el[] = req.split("&");
        for (String s : el) {
            if (s.toLowerCase().contains("mime")) {
                String el2[] = s.split("=");
                if (el2[1].trim().contains("audio")) {
                    mime = "audio";
                } else if (el2[1].trim().contains("video")) {
                    mime = "video";
                }
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
