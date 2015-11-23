package predictions3;

import java.net.InetSocketAddress;

import javax.ws.rs.ext.RuntimeDelegate;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class PredictionsPublisher {

    private static final int port = 9877;
    private static final String uri = "/resourcesP/";
    private static final String url = "http://localhost:" + port + uri;

    public static void main(String[] args) {
        new PredictionsPublisher().publish();
    }

    private void publish() {
        try {
            int backlog = 8;
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", port), backlog);
            HttpHandler requestHandler = RuntimeDelegate.getInstance().createEndpoint(new RestfulPrediction(),
                    HttpHandler.class);
            server.createContext(uri, requestHandler);

            server.start();

            System.out.println("Publishing RestfulAdage on " + url + ". Hit any key to stop.");
            System.in.read();

            server.stop(0); // normal termination
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
