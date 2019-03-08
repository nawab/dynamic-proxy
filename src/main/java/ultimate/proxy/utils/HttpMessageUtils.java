package ultimate.proxy.utils;

import rawhttp.core.RawHttpHeaders;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

public class HttpMessageUtils {

    public static String convert(byte[] content, RawHttpHeaders headers) {
        try {
            StringBuilder stringBuilder = new StringBuilder();

            InputStream bodyStream = isGZipped(headers)
                    ? new GZIPInputStream(new ByteArrayInputStream(content))
                    : new ByteArrayInputStream(content);

            String line;

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bodyStream, Charset.defaultCharset()))) {
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
            return stringBuilder.toString();
        } catch (Exception ex) {
            return "<Unable to parse>";
        }
    }

    private static boolean isGZipped(RawHttpHeaders headers) {
        return headers.getFirst("Content-Encoding")
                .map("gzip"::equalsIgnoreCase)
                .orElse(false);
    }
}
