
/**
 *
 * @author Devasia
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MultiThreadedServer implements Runnable {

    private Socket clientSocket;
    private InputStream requestReader;
    private OutputStream responseWriter;

    MultiThreadedServer(Socket csocket) {
        this.clientSocket = csocket;

        try {
            /* setup streams */
            requestReader = clientSocket.getInputStream();
            responseWriter = clientSocket.getOutputStream();
        } catch (Exception e) {
            System.err.println("could not setup streams");
            System.exit(-1);
        }
    }

    public void run() {
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(requestReader));
            String host = null;

            String line = null, mess = "";
            while (!((line = rd.readLine()).equals(""))) {
                line = line.replace("\r", "");
                line = line.replace("\n", "");
                mess=mess+line+"\r\n";

                if (line.toLowerCase().contains("host")) {
                    String el[] = line.split(":");
                    host = el[1].trim();
                }
            }

            mess = mess + "\r\n";
            System.out.println("read request from client");

            if (host != null) {
                Socket s = new Socket(host, 80);
                BufferedWriter wt=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                wt.write(mess);
                wt.flush();
                System.out.println("wrote request to server");
                
                InputStream responseReader=s.getInputStream();
                byte[] b=new byte[200000];
                int bytesRead=responseReader.read(b);
                byte[] data=Arrays.copyOfRange(b, 0, bytesRead);
                System.out.println("read response from server\n"+new String(data));
                
                s.close();
                System.out.println("closed connection to server");
                
                if(!clientSocket.isClosed()){
                    responseWriter.write(data);
                    responseWriter.flush();
                    clientSocket.close();
                    System.out.println("wrote response to client");
                } else {
                    System.err.println("client socket is closed, ending thread");
                }
            } else {
                System.err.println("could not parse host, ending thread");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
