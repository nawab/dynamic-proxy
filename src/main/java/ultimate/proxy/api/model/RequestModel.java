package ultimate.proxy.api.model;

import rawhttp.core.RawHttpHeaders;
import rawhttp.core.RequestLine;
import ultimate.proxy.utils.HttpMessageUtils;

import java.io.IOException;
import java.io.OutputStream;

public class RequestModel {
    private RawHttpHeaders headers;
    private byte[] body;
    private String bodyString;
    private RequestLine startLine;

    public RequestModel(RequestLine startLine, RawHttpHeaders headers, byte[] body) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
        this.bodyString = HttpMessageUtils.convert(body, headers);
    }

    public void setHost(String serverUrl, int serverPort) {
        headers = headers.and(RawHttpHeaders.newBuilder().with("host", serverUrl + ":" + serverPort).build());
    }

    public String getMethod() {
        return startLine.getMethod();
    }

    public String getUrl() {
        return startLine.getUri().toString();
    }

    public void writeTo(OutputStream outToServer) {
        try {
            startLine.writeTo(outToServer);
            headers.writeTo(outToServer);
            outToServer.write(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\nMethod => ");
        stringBuilder.append(getMethod());
        stringBuilder.append("\nPath => ");
        stringBuilder.append(getUrl());
        stringBuilder.append("\nHeaders => \n");
        stringBuilder.append(headers);
        stringBuilder.append("\nRequestBody => ");
        stringBuilder.append(bodyString);

        return stringBuilder.toString();
    }
}
