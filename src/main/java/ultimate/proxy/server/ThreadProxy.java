package ultimate.proxy.server;

import ultimate.proxy.api.model.Connection;
import ultimate.proxy.domain.Transaction;
import ultimate.proxy.server.connection.HttpConnection;
import ultimate.proxy.service.CacheManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ThreadProxy extends Thread {
    private String serviceName;
    private final Connection connection;
    private Socket client;
    private CacheManager cacheManager;

    public ThreadProxy(String serviceName, Socket client, Connection connection, CacheManager cacheManager) {
        this.serviceName = serviceName;
        this.connection = connection;
        this.client = client;
        this.cacheManager = cacheManager;
    }

    @Override
    public void run() {
        try {
            final InputStream inFromClient = client.getInputStream();
            final OutputStream outToClient = client.getOutputStream();

            Transaction transaction = new HttpConnection(connection).serve(inFromClient, outToClient);

            cacheManager.save(serviceName, transaction);

            inFromClient.close();
            outToClient.close();
            client.close();

        } catch (IOException e) {
            System.out.println("Request -> " + e.getMessage());
        }
    }
}
