package ultimate.proxy.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ultimate.proxy.api.model.Connection;
import ultimate.proxy.server.Route;

@RestController
@RequestMapping(path = "/proxies")
public class ServerAPI {

    @Autowired
    private Route route;

    @PostMapping("/add/{serviceName}")
    public ResponseEntity addConnection(@RequestBody Connection connection, @PathVariable("serviceName") String serviceName) {
        route.create(serviceName, connection);
        return ResponseEntity.ok().build();
    }
}
