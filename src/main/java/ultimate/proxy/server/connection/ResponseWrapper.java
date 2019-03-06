package ultimate.proxy.server.connection;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpHeaders;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.BodyReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ResponseWrapper {
    private InputStream inStream;
    private String startLine;
    private RawHttpHeaders headers;
    private String body;
    private final String SEPARATER = "\r\n";

    public ResponseWrapper(InputStream inStream) {
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

    public ResponseWrapper invoke() throws IOException {
        RawHttpResponse rawHttpResponse = new RawHttp().parseResponse(inStream);

        startLine = rawHttpResponse.getStartLine().toString();
        headers = rawHttpResponse.getHeaders();
        body = rawHttpResponse.getBody().map(this::getBodyString).orElse("<EMPTY>");
        return this;
    }

    private String getBodyString(BodyReader bodyReader) {
        try {
            return bodyReader.asRawString(Charset.defaultCharset());
        } catch (IOException e) {
            return "Could Not Parse";
        }
    }

    public String getString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\nResponse => ");
        stringBuilder.append(startLine);
        stringBuilder.append("\nHeaders => \n");
        stringBuilder.append(headers);
        stringBuilder.append("\nResponseBody => ");
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
