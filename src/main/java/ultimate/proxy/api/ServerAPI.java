package ultimate.proxy.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ultimate.proxy.api.model.Connection;
import ultimate.proxy.server.Server;

@RestController
@RequestMapping(path = "/proxies")
public class ServerAPI {

    @PostMapping("/add")
    public ResponseEntity addConnection(@RequestBody Connection connection) {
        Server.start(connection);
        return ResponseEntity.ok().build();
    }
}
