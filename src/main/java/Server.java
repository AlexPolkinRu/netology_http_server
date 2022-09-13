import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Aleksandr Polochkin
 * 11.09.2022
 */

public class Server {

    private final Map<String, Map<String, Handler>> HANDLERS = new ConcurrentHashMap<>();
    final ExecutorService THREAD_POOL;

    public Server(int threadPoolSize) {
        THREAD_POOL = Executors.newFixedThreadPool(threadPoolSize);
    }

    public void listen(int port) {
        try (final var serverSocket = new ServerSocket(port)) {
            while (true) {
                final var socket = serverSocket.accept();
                THREAD_POOL.submit(() -> processingRequest(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processingRequest(Socket socket) {
        try (
                final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final var out = new BufferedOutputStream(socket.getOutputStream())
        ) {

            Request request = parseRequest(in);

            if (request == null) {
                sendResponseBadRequest(out);
            } else {
                if ((!HANDLERS.containsKey(request.getMethod())) ||
                        (!HANDLERS.get(request.getMethod()).containsKey(request.getPath()))
                ) {
                    sendResponseNotFound(out);
                } else {
                    HANDLERS.get(request.getMethod()).get(request.getPath()).handle(request, out);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addHandler(String method, String path, Handler handler) {
        if (!HANDLERS.containsKey(method)) {
            HANDLERS.put(method, new ConcurrentHashMap<>());
        }
        HANDLERS.get(method).put(path, handler);
    }

    private Request parseRequest(BufferedReader in) throws IOException {
        String requestLine;

        try {
            requestLine = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(requestLine);

        String[] parts = requestLine.split(" ");

        if (parts.length != 3) {
            return null;
        }

        String method = parts[0];
        String path = parts[1];
        String protocol = parts[2];

        System.out.println("========================");

        final Map<String, String> HEADERS = new HashMap<>();
        while (true) {
            String headerLine = in.readLine();
            System.out.println(headerLine);
            String[] partsHeader = headerLine.split(": ");
            if (partsHeader.length != 2) {
                break;
            }
            HEADERS.put(partsHeader[0], partsHeader[1]);
        }

        System.out.println("========================");

        return new Request(method, path, protocol, HEADERS);


    }

    private void sendResponseBadRequest(BufferedOutputStream out) throws IOException {
        out.write((
                "HTTP/1.1 400 Bad Request\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.flush();
    }

    private void sendResponseNotFound(BufferedOutputStream out) throws IOException {
        out.write((
                "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.flush();
    }

}
