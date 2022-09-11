import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Aleksandr Polochkin
 * 11.09.2022
 */

public class Server {
    final List<String> validPaths = List.of(
            "/index.html",
            "/spring.svg",
            "/spring.png",
            "/resources.html",
            "/styles.css",
            "/app.js",
            "/links.html",
            "/forms.html",
            "/classic.html",
            "/events.html",
            "/events.js"
    );

    final int port;

    final ExecutorService threadPool;

    public Server(int port, int threadPoolSize) {
        this.port = port;
        threadPool = Executors.newFixedThreadPool(threadPoolSize);
    }

    public void start() {
        try (final var serverSocket = new ServerSocket(port)) {
            while (true) {
                final var socket = serverSocket.accept();
                threadPool.submit(new RequestHandler(socket, validPaths));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
