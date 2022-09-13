import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * @author Aleksandr Polochkin
 * 11.09.2022
 */

@FunctionalInterface
public interface Handler {
    void handle(Request request, BufferedOutputStream responseStream) throws IOException;
}
