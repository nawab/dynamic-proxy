package ultimate.proxy.server;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

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
            e.printStackTrace();
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
                String request = sendData(inFromClient, outToServer);
                System.out.println("Request received : \n" + request);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendResponseToClient(OutputStream outToClient, InputStream inFromServer) {
        try {
            String response = sendData(inFromServer, outToClient);
            System.out.println("Response received : \n" + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String sendData(InputStream inStream, OutputStream outStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        final byte[] request = new byte[1024];

        int bytes_read;
        while ((bytes_read = inStream.read(request)) != -1) {
            stringBuilder.append(new String(Arrays.copyOf(request, bytes_read)));
            outStream.write(request, 0, bytes_read);
            outStream.flush();
        }

        return stringBuilder.toString();
    }
}
