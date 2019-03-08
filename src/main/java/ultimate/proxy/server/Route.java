package ultimate.proxy.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ultimate.proxy.api.model.Connection;
import ultimate.proxy.service.CacheManager;

import java.net.ServerSocket;
import java.text.MessageFormat;

import static java.text.MessageFormat.*;

@Component
public class Route {

    @Autowired
    private CacheManager cacheManager;

    public void create(String serviceName, Connection connection) {
        new Thread(() -> {
            try {
                String pattern = "Starting %s proxy for %s [%s:%s] on port %s";
                System.out.println(String.format(pattern, connection.getProtocol(), serviceName, connection.getDestinationHost(), connection.getDestinationPort(), connection.getOpenPort()));
                ServerSocket server = new ServerSocket(connection.getOpenPort());
                while (true) {
                    new ThreadProxy(serviceName, server.accept(), connection, cacheManager).start();
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }
}
