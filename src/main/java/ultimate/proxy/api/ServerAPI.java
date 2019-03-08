package ultimate.proxy.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ultimate.proxy.api.model.Connection;
import ultimate.proxy.server.Route;

@RestController
@RequestMapping(path = "/proxies")
public class ServerAPI {

    @PostMapping("/add/{serviceName}")
    public ResponseEntity addConnection(@RequestBody Connection connection, @PathVariable("serviceName") String serviceName) {
        Route.create(serviceName, connection);
        return ResponseEntity.ok().build();
    }
}
