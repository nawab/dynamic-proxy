package ultimate.proxy.server.connection;

import ultimate.proxy.api.model.Connection;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.cert.X509Certificate;

public class HttpConnection {

    private final int serverPort;
    private final String serverUrl;
    private final Socket server;

    public HttpConnection(Connection connection) {
        this.serverPort = connection.getDestinationPort();
        this.serverUrl = connection.getDestinationHost();
        server = connection.isSecure() ? establishSecureConnection() : establishConnection();
    }

    public void serve(InputStream inFromClient, OutputStream outToClient) {
        sendRequestToServer(inFromClient);
        sendResponseToClient(outToClient);
    }

    private Socket establishConnection() {
        try {
            Socket socket = new Socket(this.serverUrl, this.serverPort);
            System.out.println("Established HTTP connection for " + serverUrl + ":" + serverPort);
            return socket;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Socket establishSecureConnection() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            Socket socket = sslContext.getSocketFactory().createSocket(this.serverUrl, this.serverPort);
            System.out.println("Established HTTPS connection for " + serverUrl + ":" + serverPort);
            return socket;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sendRequestToServer(InputStream inFromClient) {
        new Thread(() -> {
            try {
                OutputStream outToServer = server.getOutputStream();
                RequestWrapper requestWrapper = new RequestWrapper(inFromClient).invoke();
                requestWrapper.setHost(serverUrl, serverPort);
                outToServer.write(requestWrapper.getBytes());
                System.out.println("******************************");
                System.out.println("\n*** Request received : ***\n" + requestWrapper.getString());
            } catch (IOException e) {
                System.out.println("Request -> " + e.getMessage());
            }
        }).start();
    }

    private void sendResponseToClient(OutputStream outToClient) {
        try {
            InputStream inFromServer = server.getInputStream();
            ResponseWrapper responseWrapper = new ResponseWrapper(inFromServer).invoke();
            outToClient.write(responseWrapper.getBytes());
            System.out.println("\n*** Response received : ***\n" + responseWrapper.getString());
            System.out.println("******************************");
        } catch (IOException e) {
            System.out.println("Response -> " + e.getMessage());
        }
    }
}
