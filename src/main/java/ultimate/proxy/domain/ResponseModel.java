package ultimate.proxy.domain;

import rawhttp.core.RawHttpHeaders;
import rawhttp.core.StatusLine;
import ultimate.proxy.utils.HttpMessageUtils;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseModel {
    private StatusLine startLine;
    private RawHttpHeaders headers;
    private byte[] body;
    private String bodyString;

    public ResponseModel(StatusLine startLine, RawHttpHeaders headers, byte[] body) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
        this.bodyString = HttpMessageUtils.convert(body, headers);
    }

    public int getStatus() {
        return startLine.getStatusCode();
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

        stringBuilder.append("\nStatus => ");
        stringBuilder.append(getStatus());
        stringBuilder.append("\nHeaders => \n");
        stringBuilder.append(headers);
        stringBuilder.append("\nResponseBody => ");
        stringBuilder.append(bodyString);

        return stringBuilder.toString();
    }
}
