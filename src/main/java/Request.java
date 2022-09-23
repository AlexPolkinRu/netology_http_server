import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aleksandr Polochkin
 * 11.09.2022
 */

public class Request {
    private final String method;
    private final String requestTarget;
    private final URI uri;
    private final String path;
    private final String protocol;
    private final List<String> headers;
    private final String body;

    public Request(String method, String requestTarget, String protocol, List<String> headers, String body) {
        this.method = method;
        this.requestTarget = requestTarget;

        uri = URI.create(this.requestTarget);

        path = uri.getPath();

        this.protocol = protocol;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public String getQuery() {
        return uri.getQuery();
    }

    public List<String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public List<NameValuePair> getQueryParam(String name) {
        var parsedQuery = URLEncodedUtils.parse(uri, Charset.defaultCharset());
        return parsedQuery.stream()
                .filter(o -> o.getName().equals(name))
                .collect(Collectors.toList());
    }

    public List<NameValuePair> getQueryParams() {
        return URLEncodedUtils.parse(uri, Charset.defaultCharset());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Method: ").append(method).append("\n")
                .append("Request-target: ").append(requestTarget).append("\n")
                .append("Path: ").append(path).append("\n")
                .append("Query: ").append(getQuery()).append("\n")
                .append("HTTP-version: ").append(protocol).append("\n");
        sb.append("FIELD-LINES:\n");

        headers.forEach(k -> sb.append("\t")
                .append(k)
                .append("\n")
        );

        sb.append("===== START BODY ======\n")
                .append(body)
                .append("\n")
                .append("===== END BODY =====");

        return sb.toString();
    }
}
