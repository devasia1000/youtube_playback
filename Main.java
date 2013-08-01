
/**
 *
 * @author devasia
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {

    private static String logDirectory = "/home/devasia/ourtube/mapping/";
    private static Hashtable<String, HTTPResponse> table = new Hashtable<>();
    private static int port = 80;

    public static void main(String args[]) throws Exception {
        generateHashTable();
        //printHashTableKeys();

        ServerSocket ssock = new ServerSocket(port);
        System.out.println("Listening on port " + port);
        int i = 0;
        while (true) {
            Socket sock = ssock.accept();
            new Thread(new HTTPPlayback(sock)).start();
            i++;
            //System.out.println("number of connections: "+i);
        }
    }

    public static void generateHashTable() throws Exception {
        File logDir = new File(logDirectory);
        if (!logDir.isDirectory()) {
            System.err.println(logDir.getPath() + " is not a dir");
            System.exit(-1);
        }

        File[] files = logDir.listFiles();
        for (File file : files) {

            byte[] d = new byte[(int) file.length()];
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            dis.readFully(d);
            dis.close();

            /* find header line */
            String sat[] = new String(d).split("\r\n");
            String headerLine = sat[0];
            /* find the end of the request */
            int pos = -1;
            for (int i = 0; i < d.length; i++) {
                if (d[i] == 13 && d[i + 1] == 10 && d[i + 2] == 13 && d[i + 3] == 10) {
                    pos = i + 4;
                    break;
                }
            }

            if (pos == -1) {
                System.err.println("could not find end of request headers");
                System.exit(-1);
            }

            byte[] response = Arrays.copyOfRange(d, pos, d.length);
            table.put(headerLine, new HTTPResponse(response));
        }
    }

    private static void printHashTable() {
        Enumeration<String> keys = table.keys();
        Enumeration<HTTPResponse> elements = table.elements();

        while (keys.hasMoreElements()) {
            System.out.print(keys.nextElement() + "\n\n");
            System.out.print(elements.nextElement());
        }
    }

    public static void printHashTableKeys() {
        Enumeration<String> keys = table.keys();
        while (keys.hasMoreElements()) {
            System.out.println(keys.nextElement());
        }
    }

    public static HTTPResponse returnResponse(String requestLine) {
        return table.get(requestLine);
    }
}
