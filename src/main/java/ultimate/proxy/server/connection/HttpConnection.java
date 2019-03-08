package ultimate.proxy.server.connection;

import ultimate.proxy.api.model.Connection;
import ultimate.proxy.api.model.RequestModel;
import ultimate.proxy.api.model.ResponseModel;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
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
                RequestModel requestModel = HttpMessageParser.createRequestModel(inFromClient);
                requestModel.setHost(serverUrl, serverPort);
                requestModel.writeTo(outToServer);
                System.out.println("******************************");
                System.out.println("\n*** Request received : ***\n" + requestModel.toString());
            } catch (IOException e) {
                System.out.println("Request -> " + e.getMessage());
            }
        }).start();
    }

    private void sendResponseToClient(OutputStream outToClient) {
        try {
            InputStream inFromServer = server.getInputStream();
            ResponseModel responseModel = HttpMessageParser.createResponseModel(inFromServer);
            responseModel.writeTo(outToClient);
            System.out.println("\n*** Response received : ***\n" + responseModel.toString());
            System.out.println("******************************");
        } catch (IOException e) {
            System.out.println("Response -> " + e.getMessage());
        }
    }
}
