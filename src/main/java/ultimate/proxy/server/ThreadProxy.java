package ultimate.proxy.server;

import ultimate.proxy.api.model.Connection;
import ultimate.proxy.server.connection.HttpConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ThreadProxy extends Thread {
    private final Connection connection;
    private Socket client;


    ThreadProxy(Socket client, Connection connection) {
        this.connection = connection;
        this.client = client;
        this.start();
    }

    @Override
    public void run() {
        try {
            final InputStream inFromClient = client.getInputStream();
            final OutputStream outToClient = client.getOutputStream();

            new HttpConnection(connection).serve(inFromClient, outToClient);

            inFromClient.close();
            outToClient.close();
            client.close();

        } catch (IOException e) {
            System.out.println("Request -> " + e.getMessage());
        }
    }
}
