package ultimate.proxy.api.model;

public class Connection {
    int openPort;
    int destinationPort;
    String destinationHost;

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
