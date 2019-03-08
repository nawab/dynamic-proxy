package ultimate.proxy.api.model;

import lombok.Data;

@Data
public class Connection {
    int openPort;
    int destinationPort;
    String destinationHost;
    String protocol;

    public boolean isSecure() {
        return "https".equalsIgnoreCase(protocol);
    }
}
