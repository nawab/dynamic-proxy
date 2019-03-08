package ultimate.proxy.utils;

import rawhttp.core.RawHttpHeaders;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

public class HttpMessageUtils {

    public static String convert(byte[] content, RawHttpHeaders headers) {
        try {
            StringBuilder stringBuilder = new StringBuilder();

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getInputStream(content, headers), Charset.defaultCharset()))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
            return stringBuilder.toString();
        } catch (Exception ex) {
            return "<Unable to parse>";
        }
    }

    private static InputStream getInputStream(byte[] content, RawHttpHeaders headers) throws IOException {
        try {
            return isGZipped(headers)
                    ? new GZIPInputStream(new ByteArrayInputStream(content))
                    : new ByteArrayInputStream(content);
        } catch (ZipException e) {
            return new ByteArrayInputStream(content);
        }
    }

    private static boolean isGZipped(RawHttpHeaders headers) {
        return headers.getFirst("Content-Encoding")
                .map("gzip"::equalsIgnoreCase)
                .orElse(false);
    }
}
