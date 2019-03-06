package ultimate.proxy.server;

import ultimate.proxy.server.connection.HttpConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ThreadProxy extends Thread {
    private Socket client;
    private final String SERVER_URL;
    private final int SERVER_PORT;

    ThreadProxy(Socket client, String ServerUrl, int ServerPort) {
        this.SERVER_URL = ServerUrl;
        this.SERVER_PORT = ServerPort;
        this.client = client;
        this.start();
    }

    @Override
    public void run() {
        try {
            final InputStream inFromClient = client.getInputStream();
            final OutputStream outToClient = client.getOutputStream();

            new HttpConnection(SERVER_PORT, SERVER_URL).serve(inFromClient, outToClient);

            inFromClient.close();
            outToClient.close();
            client.close();

        } catch (IOException e) {
            System.out.println("Request -> " + e.getMessage());
        }
    }
}
