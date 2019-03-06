package ultimate.proxy.server;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

import java.io.*;
import java.net.Socket;

public class ThreadProxy extends Thread {
    private Socket client;
    private final String SERVER_URL;
    private final int SERVER_PORT;

    ThreadProxy(Socket client, String ServerUrl, int ServerPort) {
        this.SERVER_URL = ServerUrl;
        this.SERVER_PORT = ServerPort;
        this.client = client;
        this.start();
    }

    @Override
    public void run() {
        try {
            final InputStream inFromClient = client.getInputStream();
            final OutputStream outToClient = client.getOutputStream();

            Socket server = establishConnection(outToClient);

            final InputStream inFromServer = server.getInputStream();
            final OutputStream outToServer = server.getOutputStream();

            sendRequestToServer(inFromClient, outToServer);

            sendResponseToClient(outToClient, inFromServer);

            server.close();
            outToClient.close();
            outToServer.close();
            client.close();
        } catch (IOException e) {
            System.out.println("Request -> " + e.getMessage());
        }
    }

    private Socket establishConnection(OutputStream outToClient) {
        try {
            return new Socket(SERVER_URL, SERVER_PORT);
        } catch (IOException e) {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(
                    outToClient));
            out.flush();
            throw new RuntimeException(e);
        }
    }

    private void sendRequestToServer(InputStream inFromClient, OutputStream outToServer) {
        new Thread(() -> {
            try {
                String request = sendRequest(inFromClient, outToServer);
                System.out.println("Request received : \n" + request);
            } catch (IOException e) {
                System.out.println("Request -> " + e.getMessage());
            }
        }).start();
    }

    private void sendResponseToClient(OutputStream outToClient, InputStream inFromServer) {
        try {
            String response = sendResponse(inFromServer, outToClient);
            System.out.println("Response received : \n" + response);
        } catch (IOException e) {
            System.out.println("Response -> " + e.getMessage());
        }
    }

    private String sendRequest(InputStream inStream, OutputStream outStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        RawHttpRequest rawHttpRequest = new RawHttp().parseRequest(inStream);

        stringBuilder.append(rawHttpRequest.getMethod());
        stringBuilder.append(rawHttpRequest.getUri().getPath());
        stringBuilder.append(rawHttpRequest.getUri().getPort());

        rawHttpRequest.writeTo(outStream);

        return stringBuilder.toString();
    }

    private String sendResponse(InputStream inStream, OutputStream outStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        RawHttpResponse rawHttpResponse = new RawHttp().parseResponse(inStream);

        stringBuilder.append(rawHttpResponse.getStatusCode());

        rawHttpResponse.writeTo(outStream);

        return stringBuilder.toString();
    }
}
