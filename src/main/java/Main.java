import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

/**
 * @author Aleksandr Polochkin
 * 11.09.2022
 */

public class Main {
    public static void main(String[] args) {

        final int PORT = 9999;
        final int NUMBER_OF_THREADS = 64;

        final var server = new Server(NUMBER_OF_THREADS);

        // добавление handler'ов (обработчиков)
        server.addHandler("GET", "/index.html", (request, out) -> {
            final var FILE_PATH = Path.of(".", "public", request.getPath());
            final var MIME_TYPE = Files.probeContentType(FILE_PATH);
            final var LENGTH = Files.size(FILE_PATH);
            out.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + MIME_TYPE + "\r\n" +
                            "Content-Length: " + LENGTH + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            Files.copy(FILE_PATH, out);
            out.flush();
        });

        server.addHandler("POST", "/index.html", (request, out) -> {
            final var FILE_PATH = Path.of(".", "public", "events.html");
            final var MIME_TYPE = Files.probeContentType(FILE_PATH);
            final var LENGTH = Files.size(FILE_PATH);
            out.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + MIME_TYPE + "\r\n" +
                            "Content-Length: " + LENGTH + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            Files.copy(FILE_PATH, out);
            out.flush();
        });

        server.addHandler("GET", "/classic.html", (request, out) -> {
            final var FILE_PATH = Path.of(".", "public", request.getPath());
            final var MIME_TYPE = Files.probeContentType(FILE_PATH);
            final var TEMPLATE = Files.readString(FILE_PATH);
            final var CONTENT = TEMPLATE.replace(
                    "{time}",
                    LocalDateTime.now().toString()
            ).getBytes();
            out.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + MIME_TYPE + "\r\n" +
                            "Content-Length: " + CONTENT.length + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            out.write(CONTENT);
            out.flush();
        });

        server.listen(PORT);
    }
}
