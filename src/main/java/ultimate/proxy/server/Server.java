package ultimate.proxy.server;

import ultimate.proxy.api.model.Connection;

import java.net.ServerSocket;

public class Server {

    public static void start(Connection connection) {
        new Thread(() -> {
            try {

                System.out.println("Starting proxy for " + connection.getDestinationHost() + ":" + connection.getDestinationPort() + " on port " + connection.getOpenPort());
                ServerSocket server = new ServerSocket(connection.getOpenPort());
                while (true) {
                    new ThreadProxy(server.accept(), connection);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }
}
