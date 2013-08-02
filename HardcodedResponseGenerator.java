
/**
 *
 * @author devasia
 */
import java.util.Enumeration;

public class HardcodedResponseGenerator {

    public static String returnVideoplaybackResponse(String mime, int contentLength) {

        String resp = "HTTP/1.1 200 OK\r\n"
                + "Accept-Ranges: bytes\r\n"
                + "Access-Control-Allow-Credentials: true\r\n"
                + "Access-Control-Allow-Origin: http://www.youtube.com\r\n"
                + "Alternate-Protocol:80: quic\r\n"
                + "Cache-Control: private, max-age=20930\r\n"
                + "Connection: keep-alive\r\n"
                + "Content-Length: " + contentLength + "\r\n"
                + "Content-Type: " + mime + "\r\n"
                + "Date: Thu, 01 Aug 2013 20:51:45 GMT\r\n"
                + "Expires: Thu, 01 Aug 2013 20:51:45 GMT\r\n"
                + "Last-Modified: Fri, 28 Jun 2013 14:34:55 GMT\r\n"
                + "Server: gvs 1.0\r\n"
                + "Timing-Allow-Origin: http://www.youtube.com\r\n"
                + "X-Content-Type-Options: nosniff\r\n"
                + "\r\n";

        return resp;
    }

    public static HTTPResponse returnHardcodedResponse(String line) {
        HTTPResponse resp = null;
        String el[] = line.split("\\?");
        String url = el[0];

        Enumeration<String> keys = Main.returnHashTableKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String temp[] = key.split("\\?");
            String u = temp[0];

            if (u.equals(url)) {
                resp = Main.hashTableLookup(key);
            }
        }

        if (resp == null) {
            System.err.println("could not find key in hash table");
        }

        return resp;
    }
}