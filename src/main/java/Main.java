/**
 * @author Aleksandr Polochkin
 * 11.09.2022
 */

public class Main {
    public static void main(String[] args) {

        final int port = 9999;
        final int threadPoolSize = 64;

        new Server(port, threadPoolSize).start();
    }
}
