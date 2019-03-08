package ultimate.proxy.server.connection;

import rawhttp.core.*;
import rawhttp.core.body.BodyReader;
import ultimate.proxy.domain.RequestModel;
import ultimate.proxy.domain.ResponseModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class HttpMessageParser {

    public static RequestModel createRequestModel(InputStream inStream) throws IOException {
        RawHttpRequest rawHttpRequest = new RawHttp().parseRequest(inStream);

        RequestLine startLine = rawHttpRequest.getStartLine();
        RawHttpHeaders headers = rawHttpRequest.getHeaders();
        byte[] body = getBodyBytes(rawHttpRequest.getBody());
        return new RequestModel(startLine, headers, body);
    }

    public static ResponseModel createResponseModel(InputStream inStream) throws IOException {
        RawHttpResponse rawHttpResponse = new RawHttp().parseResponse(inStream);

        StatusLine startLine = rawHttpResponse.getStartLine();
        RawHttpHeaders headers = rawHttpResponse.getHeaders();
        byte[] body = getBodyBytes(rawHttpResponse.getBody());
        return new ResponseModel(startLine, headers, body);
    }

    private static byte[] getBodyBytes(Optional<? extends BodyReader> body) {
        return body.map(bodyReader -> {
            try {
                return bodyReader.asRawBytes();
            } catch (IOException e) {
                return new byte[]{};
            }
        }).orElse(new byte[]{});
    }
}
