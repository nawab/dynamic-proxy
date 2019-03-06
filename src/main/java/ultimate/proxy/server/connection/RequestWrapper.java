package ultimate.proxy.server.connection;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpHeaders;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.body.BodyReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class RequestWrapper {
    private InputStream inStream;
    private String startLine;
    private RawHttpHeaders headers;
    private String body;
    private final String SEPARATER = "\r\n";

    public RequestWrapper(InputStream inStream) {
        this.inStream = inStream;
    }

    public String getStartLine() {
        return startLine;
    }

    public RawHttpHeaders getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public RequestWrapper invoke() throws IOException {
        RawHttpRequest rawHttpRequest = new RawHttp().parseRequest(inStream);

        startLine = rawHttpRequest.getStartLine().toString();
        headers = rawHttpRequest.getHeaders();
        body = rawHttpRequest.getBody().map(this::getBodyString).orElse("<EMPTY>");

        return this;
    }

    private String getBodyString(BodyReader bodyReader) {
        try {
            return bodyReader.asRawString(Charset.defaultCharset());
        } catch (IOException e) {
            return "Could Not Parse";
        }
    }

    String getString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\nRequest => ");
        stringBuilder.append(startLine);
        stringBuilder.append("\nHeaders => \n");
        stringBuilder.append(headers);
        stringBuilder.append("\nRequestBody => ");
        stringBuilder.append(body);

        return stringBuilder.toString();
    }

    byte[] getBytes() {
        String startLine = getStartLine();
        RawHttpHeaders headers = getHeaders();
        String body = getBody();

        return (startLine + SEPARATER + headers + SEPARATER + body).getBytes();
    }
}
