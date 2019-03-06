package ultimate.proxy.server.connection;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpHeaders;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.BodyReader;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

public class ResponseWrapper {
    private InputStream inStream;
    private String startLine;
    private RawHttpHeaders headers;
    private byte[] body;
    private final String SEPARATER = "\r\n";

    public ResponseWrapper(InputStream inStream) {
        this.inStream = inStream;
    }

    public ResponseWrapper invoke() throws IOException {
        RawHttpResponse rawHttpResponse = new RawHttp().parseResponse(inStream);

        startLine = rawHttpResponse.getStartLine().toString();
        headers = rawHttpResponse.getHeaders();
        body = rawHttpResponse.getBody().map(this::getBodyString).orElse(new byte[]{});
        return this;
    }

    private byte[] getBodyString(BodyReader bodyReader) {
        try {
            return bodyReader.asRawBytes();
        } catch (IOException e) {
            return new byte[]{};
        }
    }

    private boolean isGZipped() {
        return "gzip".equals(headers.getFirst("Content-Encoding").get());
    }

    public String getString() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\nResponse => ");
        stringBuilder.append(startLine);
        stringBuilder.append("\nHeaders => \n");
        stringBuilder.append(headers);
        stringBuilder.append("\nResponseBody => ");
        stringBuilder.append(convert(body));

        return stringBuilder.toString();
    }

    private String convert(byte[] content) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        InputStream bodyStream = isGZipped()
                ? new GZIPInputStream(new ByteArrayInputStream(body))
                : new ByteArrayInputStream(body);

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bodyStream, Charset.defaultCharset()))) {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }

        return stringBuilder.toString();
    }

    byte[] getBytes() {
        byte[] bytes = (this.startLine + SEPARATER + this.headers + SEPARATER).getBytes();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(bytes);
            outputStream.write(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}
