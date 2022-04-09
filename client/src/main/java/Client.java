import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private final static ExecutorService THREAD_POOL = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        try {
            new Client().start();
        } finally {
            THREAD_POOL.shutdown();
        }
    }

    public void start() {

            THREAD_POOL.execute(() -> {
                System.out.println("New client started on thread " + Thread.currentThread().getName());
                try {

                    SocketChannel channel = SocketChannel.open(new InetSocketAddress("localhost", 9000));

                    while (true) {

                        Scanner in2 = new Scanner(System.in);
                            String str = in2.nextLine();

                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String formatDateTime = now.format(format);

                        ByteBuffer byteBuffer = ByteBuffer.wrap(String.format(
                                "[%s]: " + str,
                                formatDateTime
                        ).getBytes());

                        channel.write(byteBuffer);

                        byteBuffer.flip();

                        channel.read(byteBuffer);
                        String message = new String(byteBuffer.array());
                        System.out.println("Echo message: " + message);

 //                       Thread.sleep(3000);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }
}
