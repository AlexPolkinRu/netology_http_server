/**
 * @author Aleksandr Polochkin
 * 11.09.2022
 */

public class Main {
    public static void main(String[] args) {

        final int PORT = 9999;
        final int NUMBER_OF_THREADPOOL = 64;

        new Server(PORT, NUMBER_OF_THREADPOOL).start();
    }
}
