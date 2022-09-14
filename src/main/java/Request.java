import java.util.Map;

/**
 * @author Aleksandr Polochkin
 * 11.09.2022
 */

public class Request {
    private final String METHOD;
    private final String PATH;
    private final String PROTOCOL;
    private final Map<String, String> HEADERS;
    private String body;

    public Request(String method, String path, String protocol, Map<String, String> headers) {
        METHOD = method;
        PATH = path;
        PROTOCOL = protocol;
        HEADERS = headers;
    }

    public String getMethod() {
        return METHOD;
    }

    public String getPath() {
        return PATH;
    }

    public String getProtocol() {
        return PROTOCOL;
    }

    public Map<String, String> getHeaders() {
        return HEADERS;
    }

    public String getBody() {
        return body;
    }
}
