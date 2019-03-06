package ultimate.proxy.server.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnection {

    private final int serverPort;
    private final String serverUrl;
    private final Socket server;

    public HttpConnection(int serverPort, String serverUrl) {
        this.serverPort = serverPort;
        this.serverUrl = serverUrl;
        server = establishConnection();
    }

    public void serve(InputStream inFromClient, OutputStream outToClient) {
        sendRequestToServer(inFromClient);
        sendResponseToClient(outToClient);
    }

    private Socket establishConnection() {
        try {
            return new Socket(this.serverUrl, this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendRequestToServer(InputStream inFromClient) {
        new Thread(() -> {
            try {
                OutputStream outToServer = server.getOutputStream();
                RequestWrapper requestWrapper = new RequestWrapper(inFromClient).invoke();
                outToServer.write(requestWrapper.getBytes());
                System.out.println("******************************");
                System.out.println("\n*** Request received : ***\n" + requestWrapper.getString());
            } catch (IOException e) {
                System.out.println("Request -> " + e.getMessage());
            }
        }).start();
    }

    private void sendResponseToClient(OutputStream outToClient) {
        try {
            InputStream inFromServer = server.getInputStream();
            ResponseWrapper responseWrapper = new ResponseWrapper(inFromServer).invoke();
            outToClient.write(responseWrapper.getBytes());
            System.out.println("\n*** Response received : ***\n" + responseWrapper.getString());
            System.out.println("******************************");
        } catch (IOException e) {
            System.out.println("Response -> " + e.getMessage());
        }
    }
}
