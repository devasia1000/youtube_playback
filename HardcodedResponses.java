

/**
 *
 * @author devasia
 */
public class HardcodedResponses {

    public static String returnStream204Response(){
        String resp="Cache-Control:no-cache, must-revalidate\r\n"
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
}