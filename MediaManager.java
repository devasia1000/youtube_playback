
/**
 *
 * @author devasia
 */
import java.util.ArrayList;

public class MediaManager {

    private static ArrayList<MediaStore> mediaList = new ArrayList<MediaStore>();

    public static void handle(String req, HTTPResponse resp) throws Exception {
        boolean exists = false;

        String clen = parseClen(req);
        String mime = parseMime(req);

        for (MediaStore store : mediaList) {
            if (store.getCLEN().equals(clen) && store.getMime().equals(mime)) {
                exists = true;
                store.storeMedia(resp);
                System.out.println("stored " + parseRange(req) + " bytes of " + mime + " data");
            }
        }

        if (!exists) {
            mediaList.add(new MediaStore(clen, mime));
            System.out.println("stored " + parseRange(req) + " bytes of " + mime + " data");
        }

    }

    private static String parseClen(String req) {
        String clen = "";

        String el[] = req.split("&");

        for (String s : el) {
            if (s.toLowerCase().contains("clen")) {
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
