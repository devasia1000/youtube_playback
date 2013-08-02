
import java.util.Enumeration;


/**
 *
 * @author devasia
 */
public class HardcodedResponseGenerator {

    public static String returnStream204Response() {
        String resp = "HTTP/1.1 200 OK\r\n"
                + "Cache-Control:no-cache, must-revalidate\r\n"
                + "Content-Length:0\r\n"
                + "Content-Type:text/html; charset=UTF-8\r\n"
                + "Date:Thu, 01 Aug 2013 14:48:43 GMT\r\n"
                + "Expires:Fri, 01 Jan 1990 00:00:00 GMT\r\n"
                + "Pragma:no-cache\r\n"
                + "Server:Video Stats Server\r\n"
                + "X-Content-Type-Options:nosniff\r\n"
                + "X-Frame-Options:SAMEORIGIN\r\n"
                + "X-XSS-Protection:1; mode=block\r\n"
                + "\r\n";

        return resp;
    }

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
    
    public static HTTPResponse returnHardcodedResponse(String line){
        HTTPResponse resp=null;
        String el[]=line.split("\\?");
        String url=el[0];
        
        Enumeration <String> keys=Main.returnHashTableKeys();
        while(keys.hasMoreElements()){
            String key=keys.nextElement();
            String temp[]=key.split("\\?");
            String u=temp[0];
            
            if(u.equals(url)){
                resp=Main.hashTableLookup(key);
            }
        }
        
        if(resp==null){
            System.err.println("could not find key in hash table");
        }
        
        return resp;
    }
}