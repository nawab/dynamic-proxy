package ultimate.proxy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ultimate.proxy.api.model.Connection;

import java.util.HashMap;

@Configuration
@ConfigurationProperties(prefix = "routes")
public class RouteConfig extends HashMap<String, Connection> {
}
