package ultimate.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ultimate.proxy.config.RouteConfig;
import ultimate.proxy.server.Route;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class Application {
    public static void main(final String[] args) {
        run(Application.class, args);
    }

    @Autowired
    public void configureRoutes(RouteConfig routeConfig, Route route) {
        routeConfig
                .keySet()
                .forEach(key -> route.create(key, routeConfig.get(key)));
    }
}

