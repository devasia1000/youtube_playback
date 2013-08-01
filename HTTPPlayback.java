
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
            String line = rd.readLine();

            if (line != null && (line.contains("GET") || line.contains("POST"))) {
                line = line.replace("\r", "");
                line = line.replace("\n", "");
                //System.out.println(line);
                HTTPResponse resp = null;
                if (line.contains("stream_204")) {
                    resp = new HTTPResponse(HardcodedResponses.returnStream204Response().getBytes());
                } else if (line.contains("videoplayback")) {
                    resp = MediaManager.handle(line);
                } else {
                    resp = Main.returnResponse(line);
                }
                
                
                if (resp == null) {
                    System.err.println(line + "\n");
                    clientSocket.close();
                }
                
                if (!clientSocket.isClosed()) {
                    responseWriter.write(resp.returnTotalData());
                    //System.out.print(new String(resp.returnTotalData()));
                    responseWriter.flush();
                    responseWriter.close();
                } 

                clientSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
