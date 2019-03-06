package ultimate.proxy.api.model;

public class Connection {
    int openPort;
    int destinationPort;
    String destinationHost;

    String protocol;

    public String getProtocol() {
        return protocol;
    }

    public boolean isSecure() {
        return "https".equalsIgnoreCase(protocol);
    }

    public int getOpenPort() {
        return openPort;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public String getDestinationHost() {
        return destinationHost;
    }
}
