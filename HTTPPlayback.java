
/**
 *
 * @author devasia
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HTTPPlayback implements Runnable {

    private Socket clientSocket;
    private InputStream requestReader;
    private OutputStream responseWriter;
    int numberOfRequests=0;

    HTTPPlayback(Socket csocket) {
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

    @Override
    public void run() {
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(requestReader));

            while (true) {

                String line = rd.readLine();
                //System.out.print(line);

                if (line != null && (line.contains("GET") || line.contains("POST"))) {
                    numberOfRequests++;
                    //System.out.println("handled "+numberOfRequests+" request with same socket");
                    line = line.replace("\r", "");
                    line = line.replace("\n", "");
                    //System.out.println(line);
                    HTTPResponse resp = null;
                    if (line.contains("videoplayback")) {
                        resp = MediaManager.handle(line);
                    } else {
                        resp = Main.returnResponse(line);
                    }


                    if(resp==null){
                        resp=HardcodedResponses.returnHardcodedResponse(line);
                    }
                    
                    if (resp != null) {
                        if (!clientSocket.isClosed()) {
                            responseWriter.write(resp.returnTotalData());
                            //System.out.print(new String(resp.returnTotalData()));
                            responseWriter.flush();
                        } 
                    } else {
                        /* failsafe dummy response if we can't match any prefixes in the has table */
                        String dummyResponse = new String("HTTP 200 OK\r\n"
                                + "Content-Length: 0\r\n"
                                + "\r\n");
                        responseWriter.write(dummyResponse.getBytes());
                        responseWriter.flush();
                    }
                } 

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
