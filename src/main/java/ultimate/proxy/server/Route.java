package ultimate.proxy.server;

import ultimate.proxy.api.model.Connection;

import java.net.ServerSocket;
import java.text.MessageFormat;

import static java.text.MessageFormat.*;

public class Route {

    public static void create(String serviceName, Connection connection) {
        new Thread(() -> {
            try {
                String pattern = "Starting %s proxy for %s [%s:%s] on port %s";
                System.out.println(String.format(pattern, connection.getProtocol(), serviceName, connection.getDestinationHost(), connection.getDestinationPort(), connection.getOpenPort()));
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
